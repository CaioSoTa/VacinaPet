package vacina.pet.api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import vacina.pet.api.model.Usuario;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Chave secreta para assinar o token (em produção, deve vir de uma variavel de ambiente)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // Tempo de expiração do token: 8 horas (em milissegundos)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 8;

    // Gera o token JWT para o usuário logado
    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("perfil", usuario.getPerfil())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extrai o e-mail (subject) de dentro do token
    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    // Valida se o token pertence ao usuário e se ainda não expirou
    public boolean isTokenValido(String token, String emailUsuario) {
        String email = extrairEmail(token);
        return (email.equals(emailUsuario) && !isTokenExpirado(token));
    }

    private boolean isTokenExpirado(String token) {
        return extrairClaims(token).getExpiration().before(new Date());
    }

    private Claims extrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
