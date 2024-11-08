package br.mackenzie.wsrestdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/imoveis")
public class ImovelController {

    @Autowired
    private ImovelRepo imovelRepository;

    @PostMapping
    public ResponseEntity<String> adicionarImovel(@RequestBody Imovel imovel) {
        try {
            imovelRepository.save(imovel);
            return ResponseEntity.ok("Imóvel adicionado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar imóvel: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Imovel>> consultarTodosImoveis(@RequestParam(required = false) String search) {
        try {
            List<Imovel> imoveis = search != null && !search.isEmpty() 
                ? imovelRepository.findByRuaContainingIgnoreCase(search)
                : (List<Imovel>) imovelRepository.findAll();
            return ResponseEntity.ok(imoveis);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imovel> consultarImovel(@PathVariable Long id) {
        return imovelRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editarImovel(@PathVariable Long id, @RequestBody Imovel imovel) {
        if (!imovelRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        imovel.setId(id);
        imovelRepository.save(imovel);
        return ResponseEntity.ok("Imóvel editado com sucesso!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirImovel(@PathVariable Long id) {
        if (!imovelRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        imovelRepository.deleteById(id);
        return ResponseEntity.ok("Imóvel excluído com sucesso!");
    }
}