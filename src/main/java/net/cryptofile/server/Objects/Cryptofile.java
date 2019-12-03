package net.cryptofile.server.Objects;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cryptofiles")
public class Cryptofile {

    @Id
    @Column(name = "file_id",
            columnDefinition = "BINARY(16)"
    )
    private UUID id;

    @Column(name = "file_name")
    private String title;

    @CreationTimestamp
    @Column(name = "time_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeAdded;

    @CreationTimestamp
    @Column(name = "time_deletes")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeDeletes;

    @Column(name = "file_type")
    private String filetype;

    @ManyToMany(mappedBy = "fileInfos")
    private Set<User> users = new HashSet<>();

    /*
    @Column(name = "file_owner")
    private long owner;

     */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Date timeAdded) {
        this.timeAdded = timeAdded;
    }

    public Date getTimeDeletes() {
        return timeDeletes;
    }

    public void setTimeDeletes(Date timeDeletes) {
        this.timeDeletes = timeDeletes;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
