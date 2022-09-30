package com.dh.dhOdonto.config.security;

import com.dh.dhOdonto.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {


    //Recuperamos variaveis do application properties
    @Value("${ecommerce.jwt.expiration}")
    private String expiration;
    @Value("${ecommerce.jwt.secret}")
    private  String secret;


    //Metodo responsavel por gerar os tokens JWT
    public String gerarToken(Authentication authentication){
        //Recuperamos o usuario que é enviado dentro do authentication
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        //Criamos a data atual e fazemos a data de expiração
        Date dataHoje = new Date();
        Date dataExpiracao = new Date(dataHoje.getTime() + Long.parseLong(expiration));
        //Aqui passamos todos os parametros para criar o token
        String token = Jwts.builder()
                .setIssuer("Api DH Ecommerce")
                .setSubject(usuarioLogado.getUsername())
                .setIssuedAt(dataHoje)
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();

        return token;
    }

    //Metodo que verifica se o token é valido
    public boolean verificaToken(String token) {
        try{
            //O metodo do JWT que faz o parse de uma string, se a string for um token valido, ele converte se não ela da erro
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch(Exception exception){
                return false;
        }
    }

    //Metodo responsavel por pegar uma string e devolver o username do usuario
    public String getUsernameUsuario(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        String username =claims.getSubject();

        return username;
    }
}
