package net.cryptofile.server.Repositories;

import net.cryptofile.server.Objects.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findById(UUID id);
}
