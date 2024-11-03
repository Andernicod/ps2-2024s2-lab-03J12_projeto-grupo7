package br.mackenzie.wsrestdb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id @GeneratedValue
    private long id;
    private String username;
    private String password;

    public Usuario() {}

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setId(long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}