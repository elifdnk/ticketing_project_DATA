package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDTO> listAllRoles() {
        //Controller called this method and requesting all RoleDTOs so ,t can show in the drop-down in the UI
        //This method need to make a call to DB and get all the roles from table
        //Go to repository and find a service(method) which gives me the roles from DB


        //we have Role entities from DB
        //we need to convert those Role entities to Dtos
        //we need to use Modelmapper
        //We already created a class called RoleMApper and there are methods for me that will make this conversion

      return   roleRepository.findAll().stream().map(roleMapper::convertToDto).collect(Collectors.toList());



    }

    @Override
    public RoleDTO findById(Long id) {
        return roleMapper.convertToDto(roleRepository.findById(id).get());
    }
}
