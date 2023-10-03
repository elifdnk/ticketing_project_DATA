package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectService{

  ProjectDTO getByProjectCode(String code);
  List<ProjectDTO> listAllProjects();
  void save(ProjectDTO dto);
  void update(ProjectDTO dto);
  void delete(String code);
  void complete(String projectCode);
  List<ProjectDTO> listAllProjectDetails(); // Who manager in the system, his/her project we see in the UI

  List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager);



}
