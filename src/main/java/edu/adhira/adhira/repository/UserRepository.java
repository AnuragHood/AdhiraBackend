package edu.adhira.adhira.repository;

import edu.adhira.adhira.authentication.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends  CrudRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "SELECT password FROM user where email=?", nativeQuery = true)
    String compPassword(String email);


}
