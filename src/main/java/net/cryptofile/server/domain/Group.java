package net.cryptofile.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mikael
 */
@Entity @Table(name = "AGROUP")
@Data @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(exclude="users")
public class Group implements Serializable {
    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String[] GROUPS = {USER, ADMIN};

    @Id
    String name;

    String project;

    @JsonbTransient
    @ManyToMany
    @JoinTable(name="AUSERGROUP",
            joinColumns = @JoinColumn(name="name", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name="userid",referencedColumnName = "userid"))
    List<User> users;

    public Group(String name) {
        this.name = name;
    }
}
