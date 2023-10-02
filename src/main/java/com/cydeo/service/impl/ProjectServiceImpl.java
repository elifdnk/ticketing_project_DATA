package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
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

        //save the obj in DB
        projectRepository.save(project);
    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
    }


}