package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@NamedQueries({@NamedQuery(name = "getAllUsers", query = "SELECT u FROM User u ORDER BY u.username")})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String nome;
    private String email;

    public User() {
    }

    public User(String username, String password, String nome, String email) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
