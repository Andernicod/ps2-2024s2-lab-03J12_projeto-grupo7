package br.mackenzie.wsrestdb;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imoveis")
public class ImovelController {

    @Autowired
    private ImovelRepo imovelRepository;

    @Autowired
    private UsuarioRepo usuarioRepository;

    @PostMapping
    public ResponseEntity<String> adicionarImovel(@RequestBody Imovel imovel, HttpSession session) {
        String usernameLogado = (String) session.getAttribute("usuarioLogado");
        if (usernameLogado == null) {
            return ResponseEntity.status(401).body("Usuário não está logado.");
        }

        Usuario usuario = usuarioRepository.findByUsername(usernameLogado);
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não encontrado.");
        }

        imovel.setUsuario(usuario);
        imovelRepository.save(imovel);
        return ResponseEntity.ok("Imóvel adicionado com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<Imovel>> consultarTodosImoveis(@RequestParam(required = false) String search) {
        List<Imovel> imoveis = (search != null && !search.isEmpty()) 
            ? imovelRepository.findByRuaContainingIgnoreCase(search) 
            : (List<Imovel>) imovelRepository.findAll();
        return ResponseEntity.ok(imoveis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imovel> consultarImovel(@PathVariable Long id) {
        return imovelRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editarImovel(@PathVariable Long id, @RequestBody Imovel imovel, HttpSession session) {
        // Verifica se o imóvel existe
        Imovel imovelExistente = imovelRepository.findById(id).orElse(null);
        if (imovelExistente == null) {
            return ResponseEntity.notFound().build();
        }

        String usernameLogado = (String) session.getAttribute("usuarioLogado");
        if (usernameLogado == null || !imovelExistente.getUsuario().getUsername().equals(usernameLogado)) {
            return ResponseEntity.status(403).body("Você não tem permissão para editar este imóvel.");
        }

        imovelExistente.setTitulo(imovel.getTitulo());
        imovelExistente.setDescricao(imovel.getDescricao());
        imovelExistente.setPreco(imovel.getPreco());
        imovelExistente.setFoto(imovel.getFoto());
        imovelExistente.setTipo(imovel.getTipo());
        imovelExistente.setRua(imovel.getRua());
        imovelExistente.setNumero(imovel.getNumero());
        imovelExistente.setCidade(imovel.getCidade());
        imovelExistente.setEstado(imovel.getEstado());
        imovelExistente.setCep(imovel.getCep());
        imovelRepository.save(imovelExistente);

        return ResponseEntity.ok("Imóvel editado com sucesso!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirImovel(@PathVariable Long id, HttpSession session) {

        Imovel imovelExistente = imovelRepository.findById(id).orElse(null);
        if (imovelExistente == null) {
            return ResponseEntity.notFound().build();
        }

        String usernameLogado = (String) session.getAttribute("usuarioLogado");
        if (usernameLogado == null || !imovelExistente.getUsuario().getUsername().equals(usernameLogado)) {
            return ResponseEntity.status(403).body("Você não tem permissão para excluir este imóvel.");
        }

        imovelRepository.deleteById(id);
        return ResponseEntity.ok("Imóvel excluído com sucesso!");
    }
}