package com.dh.dhOdonto.controller;


import com.dh.dhOdonto.config.security.TokenService;
import com.dh.dhOdonto.entity.dto.TokenDTO;
import com.dh.dhOdonto.entity.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticar(@RequestBody @Valid UsuarioDTO usuarioDTO){

        try {
            //COnvertemos um usuarioDTO em um UserNamePassword
            UsernamePasswordAuthenticationToken loginUsuario = usuarioDTO.converter();

            //chamamos o authManager para validar o username e senha do usuario
            Authentication authentication = authManager.authenticate(loginUsuario);

            //com o retorno chamamos o metodo do token service para gerar o token
            String token = tokenService.gerarToken(authentication);

            //montamos o objeto do tokenDTO
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setToken(token);
            tokenDTO.setTipo("Bearer");

            //Retornamos
            return new ResponseEntity(tokenDTO,HttpStatus.OK);
        }catch (AuthenticationException exception){
            return new ResponseEntity("Erro ao autenticar",HttpStatus.BAD_REQUEST);
        }



    }

}
