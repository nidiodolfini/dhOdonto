package com.dh.dhOdonto.config.createAdmin;

import com.dh.dhOdonto.entity.Dentista;
import com.dh.dhOdonto.entity.Perfil;
import com.dh.dhOdonto.entity.Usuario;
import com.dh.dhOdonto.repository.DentistaRepository;
import com.dh.dhOdonto.repository.PerfilRepository;
import com.dh.dhOdonto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@Configuration
public class CreateAdmin implements ApplicationRunner {


    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DentistaRepository dentistaRepository;

    @Autowired
    PerfilRepository perfilRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //verificando se existe perfil no banco
        if(perfilRepository.findAll().isEmpty()){
            //Criando nosso perfis
            Perfil perfilUser = new Perfil();
            Perfil perfilAdmin = new Perfil();
            perfilUser.setDescricao("USER");
            perfilAdmin.setDescricao("ADMIN");
            //salvando perfil
            perfilRepository.save(perfilAdmin);
            perfilRepository.save(perfilUser);
        }

        if(usuarioRepository.findAll().isEmpty()) {
            //Criando nosso usuarios
            Usuario usuario = new Usuario();

            //Populando usuario Dentista
            usuario.setPassword(encoder.encode("admin123"));
            usuario.setUsername("dentistaAdmin");
            usuario.setPerfis(perfilRepository.findByDescricao("ADMIN"));
            Dentista dentista = new Dentista();
            dentista.setMatricula();
            dentista.setNome("Admin");
            dentista.setSobrenome("Admin");
            dentista.setUsuario(usuario);
            dentistaRepository.save(dentista);
        }
    }
}