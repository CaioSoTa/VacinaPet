package vacina.pet.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vacina.pet.api.config.JwtService;
import vacina.pet.api.dto.LoginRequest;
import vacina.pet.api.dto.LoginResponse;
import vacina.pet.api.model.Usuario;
import vacina.pet.api.repository.UsuarioRepository;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeta o BCrypt

    // Cadastrar novo cliente/usuário
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado!");
        }

        // Criptografa a senha antes de salvar no banco!
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        if (usuario.getPerfil() == null) {
            usuario.setPerfil("CLIENTE");
        }

        Usuario novoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @Autowired
    private JwtService jwtService;

    // Fazer Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginDto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginDto.email());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(loginDto.senha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        }

        // Gera o token JWT
        String token = jwtService.gerarToken(usuario);

        return ResponseEntity.ok(new LoginResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                token // Retorna o token para o frontend
        ));
    }
}