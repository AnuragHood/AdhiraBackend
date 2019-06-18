package edu.adhira.adhira.repository;

import edu.adhira.adhira.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User,Long>,CrudRepository<User,Long> {
    User findByEmail(String email);
    @Query(value = "SELECT password FROM user where email=?", nativeQuery = true)
    String compPassword(String email);
}
