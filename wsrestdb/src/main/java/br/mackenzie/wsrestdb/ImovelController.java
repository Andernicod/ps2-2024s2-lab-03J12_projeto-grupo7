package br.mackenzie.wsrestdb;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImovelController {
    @Autowired
    private ImovelRepo imovelRepo;

    public ImovelController() {
    }  

    @GetMapping("/api/imoveis")
    public Iterable<Imovel> consultarTodosImoveis() {
        return imovelRepo.findAll();
    }

    @GetMapping("/api/imoveis/{id}")
    public Optional<Imovel> consultarImovel(@PathVariable long id) {
        return imovelRepo.findById(id);
    }

    @PostMapping("/api/imoveis")
    public Imovel adicionar(@RequestBody Imovel novoImovel) {
        return imovelRepo.save(novoImovel);
    }
}