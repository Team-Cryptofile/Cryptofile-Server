package net.cryptofile.server.Objects;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusers", unique = true, nullable = false)
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    //@Transient
    //private String passwordConfirm;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "users_has_file_info",
            joinColumns = {@JoinColumn(name = "users_idusers")},
            inverseJoinColumns = {@JoinColumn(name = "file_info_idfile_info")}
    )
    private Set<FileInfo> fileInfos = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(Set<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }
}
