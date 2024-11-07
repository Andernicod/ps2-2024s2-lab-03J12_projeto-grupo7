package br.mackenzie.wsrestdb;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepo usuarioRepo;

    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody Usuario novoUsuario) {
        if (usuarioRepo.findByUsername(novoUsuario.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Usuário já existe. Tente outro nome de usuário.");
        }
        
        usuarioRepo.save(novoUsuario);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario, HttpSession session) {
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Nome de usuário não pode estar vazio.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Senha não pode estar vazia.");
        }

        if (session.getAttribute("usuarioLogado") != null) {
            return ResponseEntity.ok("Você já está logado como " + session.getAttribute("usuarioLogado") + ".");
        }

        Usuario foundUser = usuarioRepo.findByUsername(usuario.getUsername());
        if (foundUser == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }
        if (!foundUser.getPassword().equals(usuario.getPassword())) {
            return ResponseEntity.badRequest().body("Senha incorreta.");
        }

        session.setAttribute("usuarioLogado", foundUser.getUsername());
        return ResponseEntity.ok("Bem-vindo, " + foundUser.getUsername() + "!");
    }

    @GetMapping("/bemvindo")
    public ResponseEntity<String> bemVindo(HttpSession session) {
        String usuarioLogado = (String) session.getAttribute("usuarioLogado");
        if (usuarioLogado != null) {
            return ResponseEntity.ok("Bem-vindo, " + usuarioLogado + "!");
        }
        return ResponseEntity.status(401).body("Usuário não logado.");
    }
}