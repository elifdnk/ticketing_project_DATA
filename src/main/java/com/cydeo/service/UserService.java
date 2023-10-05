package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    void save(UserDTO user);
//    void deleteByUserName(String user);   // this is hard delete method. we dont use this method anywhere
    UserDTO update(UserDTO user);
    void delete(String username);

    List<UserDTO> listAllByRole(String role);


}
