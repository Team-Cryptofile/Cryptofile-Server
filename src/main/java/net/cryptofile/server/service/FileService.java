package net.cryptofile.server.service;

import net.cryptofile.server.Objects.Cryptofile;
import net.cryptofile.server.Objects.FileInfo;
import net.cryptofile.server.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * The service for processing files.
 * This is mainly used for adding and getting files.
 */
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    /**
     * Gets file as object from the database by giving id as a UUID string.
     * @param uuidString id as string.
     * @return file as object.
     */
    public Cryptofile getCryptofileObject(String uuidString){
        UUID uuid = UUID.fromString(uuidString);
        return fileRepository.findById(uuid).get(0);
    }

    /**
     * Gets file as object from the database by giving id as a UUID datatype.
     * @param uuid id as UUID datatype.
     * @return file as object.
     */
    public Cryptofile getCryptofileObject(UUID uuid){
        return fileRepository.findById(uuid).get(0);
    }

    /**
     * Gets file as bytes from the database by giving id as a UUID datatype.
     * @param uuid id as UUID datatype.
     * @return file as bytes.
     */
    public byte[] getCryptofileBytes(UUID uuid){
        return fileRepository.findById(uuid).get(0).getCryptofile();
    }

    /**
     * Gets file as bytes from the database by giving id as a UUID string.
     * @param uuidString id as string.
     * @return file as bytes.
     */
    public byte[] getCryptofileBytes(String uuidString){
        UUID uuid = UUID.fromString(uuidString);
        return fileRepository.findById(uuid).get(0).getCryptofile();
    }

    /**
     * Adds a file to the database and returns an UUID string.
     * @param fileBytes file as a byte array.
     * @param title title of the file.
     * @return UUID as string.
     */
    public String addCryptofile(byte[] fileBytes, String title){
        // Generate UUID
        UUID uuid = UUID.randomUUID();

        // Create and build file object
        Cryptofile cryptofile = new Cryptofile();
        cryptofile.setId(uuid);
        cryptofile.setCryptofile(fileBytes);

        // Create and build file info
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(uuid);
        fileInfo.setTitle(title);
        Date deleteDate = Date.from(LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        fileInfo.setTimeDeletes(deleteDate);

        // Combines file and file info
        cryptofile.setFileInfo(fileInfo);

        // Stores the file in database
        fileRepository.save(cryptofile);

        // Returns id as string
        return uuid.toString();
    }

}
