package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService,@Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @Override
    public List<UserDTO> listAllUsers() {
        //we remove Where condition because we will change find all method to  : findAllByIsDeletedOrderByFirstNameDesc

        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {

        return userMapper.convertToDto(userRepository.findByUserNameAndIsDeleted(username,false));
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));

    }

//    @Override
//    public void deleteByUserName(String username) {
//        userRepository.deleteByUserName(username);  // this method how we can do hard delete , we dont use this method anywhere
//    }


    @Override
    public UserDTO update(UserDTO user) {
        //find current user
        User user1 = userRepository.findByUserNameAndIsDeleted(user.getUserName(),false); //this one has id

        //Map update user dto to entity object
        User convertedUser = userMapper.convertToEntity(user); // this one doesnt have id

        //set id to the converted object
        convertedUser.setId(user1.getId());

        //save the updated user in the DB
        userRepository.save(convertedUser);
        return findByUserName(user.getUserName());
    }

    @Override
    public void delete(String username) {
        //go to DB and get that user with username;
        User user = userRepository.findByUserNameAndIsDeleted(username,false);

        //control
        if(checkIfUserCanBeDeleted(user)){
            //change the isdeleted field to true
            user.setIsDeleted(true);

            user.setUserName(user.getUserName()+"-"+user.getId());  // if I delete someone, his/hers username will be change ex:if ı delete harold, deleted harold username going to be like this :   harold@manager.com-2

            //save the obj in DB
            userRepository.save(user);
        }

    }

    @Override
    public List<UserDTO> listAllByRole(String role) {

        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role,false);

        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user) {

        //learn user is employee or manager?
        switch (user.getRole().getDescription()) {
            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                return projectDTOList.size()==0;

            case "Employee":
                List<TaskDTO> taskDTOList =taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
                return taskDTOList.size()==0;
            default:
                return true;
        }
    }


}
