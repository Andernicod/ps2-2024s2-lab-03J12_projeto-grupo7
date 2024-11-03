package br.mackenzie.wsrestdb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Imovel {
    @Id @GeneratedValue
    private long id;
    private String titulo; // Título do imóvel

    @Lob
    private String descricao; // Descrição do imóvel

    private double preco; // Preço do imóvel

    @Lob
    private String foto; // URL da foto do imóvel

    private String tipo; // Tipo (venda ou aluguel)

    public Imovel() {}

    // Getters e Setters
    public long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public double getPreco() { return preco; }
    public String getFoto() { return foto; }
    public String getTipo() { return tipo; }

    public void setId(long id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}