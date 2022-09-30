package com.dh.dhOdonto.service;

import com.dh.dhOdonto.entity.Usuario;
import com.dh.dhOdonto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    //Metodo responsavel por receber um username e retornar o usuario que sera autenticado
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            //Buscamos o usuario pelo username para saber se o mesmo existe
            Usuario usuario =  repository.findByUsername(username);
            return usuario;
        }catch (UsernameNotFoundException exception){
            //Caso não exista lançamos uma exception
            throw new UsernameNotFoundException("Usuario não existe");
        }
    }
}