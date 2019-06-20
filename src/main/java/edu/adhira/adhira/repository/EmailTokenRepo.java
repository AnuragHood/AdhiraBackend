package edu.adhira.adhira.repository;

import org.springframework.data.repository.CrudRepository;
import edu.adhira.adhira.authentication.EmailToken;
import org.springframework.stereotype.Repository;

@Repository("emailTokenRepo")
public interface EmailTokenRepo extends CrudRepository<EmailToken,Long> {
    EmailToken findByConfirmationToken(String confirmationToken);
}
