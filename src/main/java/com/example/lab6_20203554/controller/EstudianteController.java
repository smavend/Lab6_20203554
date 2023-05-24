package com.example.lab6_20203554.controller;

import com.example.lab6_20203554.entity.Usuario;
import com.example.lab6_20203554.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired
    UsuarioRepository usuarioRepository;


    @GetMapping("/lista")
    public String listaUsuarios(Model model){
        List<Usuario> estudiantes = usuarioRepository.findByRolid(5);
        model.addAttribute("estudiantes", estudiantes);
        return "lista_usuarios";
    }
    @GetMapping("/nuevo")
    public String nuevoEstudiante(@ModelAttribute("estudiante") Usuario estudiante){
        return "form_estudiante";
    }
    @PostMapping("/save")
    public String saveEstudiante(@ModelAttribute("estudiante") @Valid Usuario estudiante, BindingResult bindingResult,
                                 RedirectAttributes attr, Model model){
        if (bindingResult.hasErrors()) {
            return "form_estudiante";
        } else {

            if (estudiante.getId() == 0) {
                attr.addFlashAttribute("msg", "Estudiante creado exitosamente");
            } else {
                attr.addFlashAttribute("msg", "Estudiante actualizado exitosamente");
            }
            estudiante.setActivo(true);
            estudiante.setRolid(5);
            usuarioRepository.save(estudiante);
            return "redirect:/product";
        }
    }
}
