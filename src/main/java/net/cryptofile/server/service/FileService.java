package net.cryptofile.server.service;

import net.cryptofile.server.Objects.Cryptofile;
import net.cryptofile.server.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    private String fileDestination = System.getProperty("user.home") + "/cryptofiles/";

        
    /**
     * Gets file as object from the filesystem by giving id as a UUID string.
     * @param uuidString id as string.
     * @return file info object.
     */
    public Cryptofile getCryptofileObject(String uuidString){
        UUID uuid = UUID.fromString(uuidString);
        return fileRepository.findById(uuid).get(0);
    }

    /**
     * Gets file as object from the filesystem by giving id as a UUID datatype.
     * @param uuid id as UUID datatype.
     * @return file info object.
     */
    public Cryptofile getCryptofileObject(UUID uuid){
        return fileRepository.findById(uuid).get(0);
    }

    /**
     * Gets file as bytes from the filesystem by giving id as a UUID datatype.
     * @param uuid id as UUID datatype.
     * @return file as bytes.
     */
    public byte[] getCryptofileBytes(UUID uuid) throws IOException {
        return getFile(uuid.toString());
    }

    //public byte[] getCryptoFile(String uuidString) throws IOException {
     //   return getFile(uuidString);
    /**
     * Gets file as bytes from the filesystem by giving id as a UUID string.
     * @param uuidString id as string.
     * @return file as bytes.
     */
    public byte[] getCryptofileBytes(String uuidString) throws IOException {
        UUID uuid = UUID.fromString(uuidString);
        return getFile(uuidString);
    }

    //public String addCryptoFile(byte[] fileBytes, String title) throws IOException {
    /**
     * Adds a file to the filesystem and returns an UUID string.
     * @param fileBytes file as a byte array.
     * @param title title of the file.
     * @return UUID as string.
     */
    public String addCryptofile(MultipartFile fileBytes, String title) throws IOException {
        // Generate UUID
        UUID uuid = UUID.randomUUID();

        // Create and build file object
        Cryptofile cryptofile = new Cryptofile();
        cryptofile.setId(uuid);
        cryptofile.setTitle(title);
        cryptofile.setTitle(title);
        Date deleteDate = Date.from(LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        cryptofile.setTimeDeletes(deleteDate);

        // Stores file in filesystem
        storeFile(fileBytes, uuid.toString());

        // Stores the file in filesystem
        fileRepository.save(cryptofile);

        // Returns id as string
        return uuid.toString();
    }

    private void storeFile(MultipartFile file, String uuid) throws IOException {
        Files.copy(file.getInputStream(), Paths.get(fileDestination + uuid), StandardCopyOption.REPLACE_EXISTING);
    }

    private byte[] getFile(String uuid) throws IOException {
        File file = new File(fileDestination + uuid);
        return Files.readAllBytes(file.toPath());
    }

    public void deleteFile(String uuidString){
        UUID uuid = UUID.fromString(uuidString);
        File file = new File(fileDestination + uuid);
        if (file.delete()) {
            fileRepository.delete(fileRepository.findById(uuid).get(0));
            System.out.println(uuid + " was successfully deleted");
        } else {
            System.out.println("File failed to be deleted");
        }

    }

}
