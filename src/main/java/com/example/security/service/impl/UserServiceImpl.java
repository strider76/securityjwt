package com.example.security.service.impl;


import com.example.security.dao.UserRepository;
import com.example.security.dto.UserDTO;
import com.example.security.exception.EntityNotFoundException;
import com.example.security.model.Usuarios;
import com.example.security.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final String USER_NOT_EXIST = "Usuario con nombre '%s' no existe";

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<Usuarios> getAllActive() {
        return userRepository.findAllByActiveTrue();
    }

    @Override
    public List<Usuarios> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void switchActive(String nameUsuario) throws EntityNotFoundException {
        if (userRepository.switchUsuario(nameUsuario)!=1) {
            throw new EntityNotFoundException(String.format(USER_NOT_EXIST, nameUsuario));
        }
    }

    @Override
    public Usuarios findByName(String nameUsuario) throws EntityNotFoundException {

        return userRepository.findByName(nameUsuario).orElseThrow(
                ()-> new EntityNotFoundException(String.format(USER_NOT_EXIST, nameUsuario))
        );
    }

    @Override
    public Usuarios create(Usuarios usuario) {

        return userRepository.save(usuario);

    }

    @Override
    public void update(String nameUsuario, UserDTO usuarioDTO) {
        //TODO crear Update de usuarios cuando tengamos UsuariosMapper
    }

    @Override
    public void delete(String nameUsuario) throws EntityNotFoundException {
        Usuarios usuario = findByName(nameUsuario);
        if (usuario != null)
            userRepository.delete(usuario);
        else
            throw new EntityNotFoundException(String.format(USER_NOT_EXIST, nameUsuario));
    }
}
