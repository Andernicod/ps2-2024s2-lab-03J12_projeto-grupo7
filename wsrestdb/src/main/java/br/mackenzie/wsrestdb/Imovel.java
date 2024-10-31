package br.mackenzie.wsrestdb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Imovel {
    @Id @GeneratedValue
    private long id;
    private String endereco;
    private double preco;

    public Imovel() {}

    public Imovel(long id, String endereco, double preco) {
        this.id = id;
        this.endereco = endereco;
        this.preco = preco;
    }

    public long getId() { return id; }
    public String getEndereco() { return endereco; }
    public double getPreco() { return preco; }

    public void setId(long id) { this.id = id; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setPreco(double preco) { this.preco = preco; }
}