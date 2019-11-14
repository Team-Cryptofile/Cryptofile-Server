package net.cryptofile.server.Objects;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Table(name = "cryptofiles")
public class Cryptofile {

    @Id
    @Column(name = "file_info_idfile_info",
            //updatable = false,
            columnDefinition = "BINARY(16)"
    )
    //@Type(type = "uuid-char")
    private UUID id;

    @PrimaryKeyJoinColumn
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FileInfo fileInfo;

    @Column(name = "cryptofile")
    private byte[] cryptofile;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public byte[] getCryptofile() {
        return cryptofile;
    }

    public void setCryptofile(byte[] cryptofile) {
        this.cryptofile = cryptofile;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
