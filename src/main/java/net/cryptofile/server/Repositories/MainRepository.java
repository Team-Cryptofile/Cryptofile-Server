package net.cryptofile.server.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.security.core.context.SecurityContextHolder;

import net.cryptofile.server.Objects.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Repository
public class MainRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public MainRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String addCryptofile(MultipartFile multipartFile, String title) throws IOException {
        byte[] file = multipartFile.getBytes();
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        if(title.isEmpty()){
            addFileToDb(uuidString, file);
        }else{
            addFileToDb(uuidString, file, title);
        }

        return uuidString;
    }

    public byte[] getCryptofile(String fileId){
        String query = "SELECT cryptofile FROM cryptofiles WHERE file_info_idfile_info=?";
        byte[] file;
        file = jdbcTemplate.queryForObject(query, (rs, rowNum) -> rs.getBytes(1));

        return file;
    }

    public void addFileToDb(String uuid, byte[] file, String title){
        String query = "SET @generatedID = UUID_TO_BIN(?);" +
                "INSERT INTO cryptofiles (file_info_idfile_info, cryptofile) VALUES (@generatedID, ?);" +
                "INSERT INTO file_info (idfile_info, file_name, time_added, time_deletes) VALUES (@generatedID, ?, NOW(), NOW() + INTERVAL 3 MONTH);" +
                "INSERT INTO users_has_file_info (users_idusers, file_info_idfile_info) VALUES (?, @generatedID)";

        try {
            //long userId = getCurrentUser().getId();
            long userId = 1;
            jdbcTemplate.update(query, uuid, file, title, userId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addFileToDb(String uuid, byte[] file){
        String query = "SET @generatedID = UUID_TO_BIN(?);" +
                "INSERT INTO cryptofiles (file_info_idfile_info, cryptofile) VALUES (@generatedID, ?);" +
                "INSERT INTO file_info (idfile_info, file_name, time_added, time_deletes) VALUES (@generatedID, @generatedID, NOW(), NOW() + INTERVAL 3 MONTH);" +
                "INSERT INTO users_has_file_info (users_idusers, file_info_idfile_info) VALUES (?, @generatedID)";

        try {
            //long userId = getCurrentUser().getId();
            long userId = 1;
            jdbcTemplate.update(query, uuid, file, userId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUsername;
        if(principal instanceof UserDetails){
            loggedInUsername = ((UserDetails)principal).getUsername();
        }else{
            loggedInUsername = principal.toString();
        }
        return userRepository.findByUsername(loggedInUsername);
    }
}
