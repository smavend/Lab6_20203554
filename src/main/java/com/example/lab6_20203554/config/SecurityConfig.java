package com.example.lab6_20203554.config;

import com.example.lab6_20203554.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration


public class SecurityConfig{
    final DataSource dataSource;
    final
    UsuarioRepository usuarioRepository;

    public SecurityConfig(DataSource dataSource, UsuarioRepository usuarioRepository) {
        this.dataSource = dataSource;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginProcessingUrl("/processLogin")
                .usernameParameter("correo")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {

                    HttpSession session = request.getSession();
                    session.setAttribute("usuario",usuarioRepository.findByCorreo(authentication.getName()));

                    String rol = "";
                    for(GrantedAuthority role : authentication.getAuthorities()){
                        rol = role.getAuthority();
                        break;
                    }

                    if(rol.equals("Administrador")){
                        response.sendRedirect("/cursos");
                    } else if (rol.equals("Estudiante")) {
                        response.sendRedirect("/cursos");
                    }

                });

        http.authorizeHttpRequests()
                .requestMatchers("/cursos/lista").permitAll()
                .requestMatchers("/estudiante/lista").hasAuthority("Administrador")
                .requestMatchers("/estudiante/cursos","/cursos/lista/**").hasAuthority("Estudiante")
                .anyRequest().permitAll();

        http.logout().logoutSuccessUrl("/").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager jdbc = new JdbcUserDetailsManager(dataSource);

        String sql1 = "SELECT email,pwd,activo FROM usuario where email = ?";
        String sql2 = "SELECT u.email, r.nombre FROM usuario u INNER JOIN rol r ON (u.idrol = r.idrol) " +
                "WHERE u.email = ?";

        jdbc.setUsersByUsernameQuery(sql1);
        jdbc.setAuthoritiesByUsernameQuery(sql2);
        return jdbc;
    }

}
