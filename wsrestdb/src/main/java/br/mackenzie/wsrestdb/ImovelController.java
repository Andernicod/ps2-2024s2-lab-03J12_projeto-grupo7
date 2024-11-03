package br.mackenzie.wsrestdb;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            // Log do erro para investigação (pode usar um logger em vez de print)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar imóvel: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Imovel>> consultarTodosImoveis() {
        try {
            List<Imovel> imoveis = (List<Imovel>) imovelRepository.findAll();
            return ResponseEntity.ok(imoveis);
        } catch (Exception e) {
            // Log do erro para investigação
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Retorna null em caso de erro
        }
    }
}