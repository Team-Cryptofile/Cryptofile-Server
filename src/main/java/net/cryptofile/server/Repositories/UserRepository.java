package net.cryptofile.server.Repositories;

import net.cryptofile.server.Objects.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
