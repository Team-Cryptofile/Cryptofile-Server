package net.cryptofile.server.Repositories;

import net.cryptofile.server.Objects.Cryptofile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<Cryptofile, Long> {
    List<Cryptofile> findById(UUID id);
}
