package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return projectMapper.convertToDto(project);

    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));
        return list.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(dto);
        projectRepository.save(project);
    }

    @Override
    public void update(ProjectDTO dto) {
        //go to DB and grab to dto ,why we try to get DB? This project has id now.
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());

        //get dto and convert to entity.why we do that? because that is the obj we try to save in DB
        Project convertedProject = projectMapper.convertToEntity(dto);

        //before save, I geting the id project and I set this id to convertedProject
        convertedProject.setId(project.getId());

        //Project status is not in the form
        convertedProject.setProjectStatus(project.getProjectStatus());

        //save project
        projectRepository.save(convertedProject);
    }

    @Override
    public void delete(String code) {
        // go to DB get that project with project code
        Project project = projectRepository.findByProjectCode(code);

        //change is deleted field to true
        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode() + "-" + project.getId()); //when we delete one project, this line change the id because we can use later same project code. //new code will be SP00-1  like this.

        //save the obj in DB
        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDto(project));
    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        taskService.completeByProject(projectMapper.convertToDto(project));

    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        //DB, give me all the projects assigned to manager login in the system.
        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com"); // this is will come with security in the future :)
        User user = userMapper.convertToEntity(currentUserDTO);

        List<Project> list = projectRepository.findAllByAssignedManager(user);  //give me all the projects assigned to this user

        return list.stream().map(project -> {
                    //convert dto first
                    ProjectDTO obj = projectMapper.convertToDto(project);
                    obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
                    obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
                    return obj;
                }
        ).collect(Collectors.toList());
    }


}
