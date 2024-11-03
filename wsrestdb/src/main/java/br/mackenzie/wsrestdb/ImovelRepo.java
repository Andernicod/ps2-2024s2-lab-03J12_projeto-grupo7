package br.mackenzie.wsrestdb;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ImovelRepo extends CrudRepository<Imovel, Long> {
    List<Imovel> findByTipo(String tipo);
    List<Imovel> findByPrecoLessThan(double preco);
}