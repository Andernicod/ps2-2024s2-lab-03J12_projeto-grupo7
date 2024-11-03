package br.mackenzie.wsrestdb;

import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepo extends CrudRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}