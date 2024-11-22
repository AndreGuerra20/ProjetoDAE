package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
