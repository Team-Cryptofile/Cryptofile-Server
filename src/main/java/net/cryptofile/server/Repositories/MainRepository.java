package net.cryptofile.server.Repositories;

import net.cryptofile.server.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.security.core.context.SecurityContextHolder;

import net.cryptofile.server.Objects.*;

@Repository
public class MainRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public MainRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFileToDb(String title, byte[] file){
        String query = "SET @generatedID = UUID_TO_BIN(UUID());" +
                "INSERT INTO cryptofiles (file_info_idfile_info, cryptofile) VALUES (@generatedID, ?);" +
                "INSERT INTO file_info (idfile_info, file_name, time_added, time_deletes) VALUES (@generatedID, ?, NOW(), NOW() + INTERVAL 3 MONTH);" +
                "INSERT INTO users_has_file_info (users_idusers, file_info_idfile_info) VALUES (?, @generatedID)";

        try {
            long userId = getCurrentUser().getId();
            jdbcTemplate.update(query, file, title, userId);
        }catch (Exception e){
            System.out.println(e);
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
