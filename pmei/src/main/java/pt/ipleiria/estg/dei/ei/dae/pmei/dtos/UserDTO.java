package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.User;

public class UserDTO {
    private long id;
    private String username;
    private String role;

    public UserDTO() {

    }

    public UserDTO(String username, String role, long id) {
        this.username = username;
        this.role = role;
        this.id = id;
    }

    public static UserDTO from(User user) {
        return new UserDTO(
                user.getUsername(),
                Hibernate.getClass(user).getSimpleName(),
                user.getId()
        );
    }

    public static List<UserDTO> from(List<User> users) {
        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

