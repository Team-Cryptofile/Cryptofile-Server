package net.cryptofile.server.service;

import net.cryptofile.server.Objects.Cryptofile;
import net.cryptofile.server.Objects.FileInfo;
import net.cryptofile.server.Repositories.FileInfoRepository;
import net.cryptofile.server.Repositories.FileRepository;
import net.cryptofile.server.Repositories.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileInfoRepository fileInfoRepository;

    public byte[] getCryptoFile(UUID uuid){
        return fileRepository.findById(uuid).get(0).getCryptofile();
    }

    public byte[] getCryptoFile(String uuidString){
        UUID uuid = UUID.fromString(uuidString);
        return fileRepository.findById(uuid).get(0).getCryptofile();
    }

    public String addCryptoFile(byte[] fileBytes, String title){
        UUID uuid = UUID.randomUUID();

        Cryptofile cryptofile = new Cryptofile();
        cryptofile.setId(uuid);
        cryptofile.setCryptofile(fileBytes);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(uuid);
        fileInfo.setTitle(title);

        fileRepository.save(cryptofile);
        fileInfoRepository.save(fileInfo);

        return uuid.toString();
    }

}
