package com.dh.dhOdonto.config.security;
import com.dh.dhOdonto.entity.Usuario;
import com.dh.dhOdonto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {
//Classe responsavel por filtrar as chamadas que precisaram ser validadas via token
    @Autowired
    TokenService tokenService;
    @Autowired
    UsuarioRepository usuarioRepository;

    //Metodo que valida se o token é valido
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Chama o metodo que retorna o token enviado pela requisição
        String token = recuperarToken(request);
        //Chama o metodo la da service token, que valida se o token retornado e valido
        boolean valido = tokenService.verificaToken(token);


        //Se for valido, a gente chama o metodo responsavel por autenticar o usuario na aplicação
        if(valido){
            autenticarUsuario(token);
        }

        //Aqui mandamos a aplicação seguir o fluxo, com o usuario autenticado, ou não
        filterChain.doFilter(request,response);
    }

    //Esse metodo recebe um token e autentica o usuario na aplicação
    private void autenticarUsuario(String token) {
        //chamamos o metodo da token service que recebe um token e retorna o username que esta dentro desse token
        String username = tokenService.getUsernameUsuario(token);
        //com o username, nos vamos ao banco para verificar se existe usuario com esse username
        Usuario usuario = usuarioRepository.findByUsername(username);
        //Pegamos esse usuario e passamos para o UsernamePassword para ele gerar um autentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
        //Agora passamos o authentication para o spring validar e autenticar o usuario
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    //Metodo que recebe os dados da requisição e retorna o token
    private String recuperarToken(HttpServletRequest request) {
        //Aqui pegamos o authorization que enviamos dentro do header da requisicao
        String getToken = request.getHeader("Authorization");
        //validamos se o token de fato veio no header
        if(getToken == null || getToken.isEmpty() || !getToken.startsWith("Bearer ")){
            return null;
        }
        //Retornamos o token, fazemos um substring para remover o bearer que e enviado junto com o token
        return getToken.substring(7,getToken.length());
    }
}
