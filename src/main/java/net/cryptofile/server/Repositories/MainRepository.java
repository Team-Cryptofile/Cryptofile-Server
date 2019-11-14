package net.cryptofile.server.Repositories;

import org.hibernate.type.UUIDBinaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.UUID;

@Repository
public class MainRepository {
    private JdbcTemplate jdbcTemplate;

    //@Autowired
    //UserRepository userRepository;

    @Autowired
    public MainRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String addCryptofile(byte[] file, String title) throws Exception {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        System.out.println("UUID as string: " + uuidString);
        byte[] uuidBytes = getBytesFromUUID(uuid);

        Blob fileBlob = new SerialBlob(file);

        System.out.println("File: " + fileBlob);

        //byte[] uuidBytes = ByteBuffer.wrap(new byte[16]).order(ByteOrder.BIG_ENDIAN)
        //                .putLong(uuid.getMostSignificantBits())
        //                .putLong(uuid.getLeastSignificantBits()).array();

        System.out.println("UUID as bytes: " + Arrays.toString(uuidBytes));

        if(title.isEmpty()){
            addFileToDb(uuidBytes, file);
        }else{
            addFileToDb(uuidBytes, file, title);
        }

        return uuidString;
    }

    public byte[] getCryptofile(String fileId){
        String query = "SELECT cryptofile FROM cryptofiles WHERE file_info_idfile_info=?";
        byte[] file;
        file = jdbcTemplate.queryForObject(query, (rs, rowNum) -> rs.getBytes(1));

        return file;
    }

    public void addFileToDb(byte[] uuid, byte[] file, String title) throws Exception{
        String query = "BEGIN;" +
                "INSERT INTO cryptofiles (file_info_idfile_info, cryptofile) VALUES (:id, :cryptofile);" +
                "INSERT INTO file_info (idfile_info, file_name, time_added, time_deletes) VALUES (:id, :title, now(), now() + INTERVAL 3 MONTH);" +
                "INSERT INTO users_has_file_info (users_idusers, file_info_idfile_info) VALUES (:userid, :id);" +
                "COMMIT;";

        //long userId = getCurrentUser().getId();
        long userId = 1;
        SqlLobValue fileToDB = new SqlLobValue(file);
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", uuid, Types.BINARY);
        paramSource.addValue("cryptofile", fileToDB, Types.BLOB);
        paramSource.addValue("title", title, Types.VARCHAR);
        paramSource.addValue("userid", userId, Types.NUMERIC);

        //jdbcTemplate.update(query, uuid, file, uuid, title, userId, uuid);
        jdbcTemplate.update(query, paramSource);

    }

    public void addFileToDb(byte[] uuid, byte[] file){
        String query = "SET @generatedID = ?;" +
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

    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }

    public static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Long high = byteBuffer.getLong();
        Long low = byteBuffer.getLong();

        return new UUID(high, low);
    }
}
