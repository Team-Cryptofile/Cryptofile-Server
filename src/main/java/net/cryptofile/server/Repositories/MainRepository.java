package net.cryptofile.server.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MainRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public MainRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public byte[] getCryptofile(String fileId){
        String query = "SELECT cryptofile FROM cryptofiles WHERE file_info_idfile_info=?";
        byte[] file;
        file = jdbcTemplate.queryForObject(query, (rs, rowNum) -> rs.getBytes(1));

        return file;
    }

    /*
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

     */
}
