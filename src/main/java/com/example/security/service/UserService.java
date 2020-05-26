package com.example.security.service;

import com.example.security.dto.UserDTO;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.model.Usuarios;

import java.util.List;

public interface UserService {

    List<Usuarios> getAllActive();
    List<Usuarios> getAll();
    void switchActive(String nameUsuario) throws EntityNotFoundException;
    Usuarios findByName(String nameUsuario) throws EntityNotFoundException;
    Usuarios create(Usuarios usuarios);
    void update(String nameUsuario, UserDTO usuarioDTO);
    void delete(String nameUsuario) throws EntityNotFoundException;

}
