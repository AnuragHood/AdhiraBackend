package edu.adhira.adhira.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import edu.adhira.adhira.authentication.EmailToken;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("emailTokenRepo")
public interface EmailTokenRepo extends CrudRepository<EmailToken, Long> {
    EmailToken findByConfirmationToken(String confirmationToken);
   

    @Query(value = "SELECT created_date FROM email_token where user_id=?", nativeQuery = true)
    Date getTokenCreationDate(int userId);
    @Modifying
    @Query(value = "Delete FROM email_token where user_id=?", nativeQuery = true)
    void deleteToken(int userId);
}
