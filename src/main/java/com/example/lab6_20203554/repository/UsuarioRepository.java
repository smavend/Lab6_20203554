package com.example.lab6_20203554.repository;

import com.example.lab6_20203554.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByRolid(int rol);
    Usuario findByCorreo(String correo);
}
