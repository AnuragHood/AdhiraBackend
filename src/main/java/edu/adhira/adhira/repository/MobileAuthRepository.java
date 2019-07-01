package edu.adhira.adhira.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.adhira.adhira.authentication.MobileOtp;

@Repository
public interface MobileAuthRepository extends CrudRepository<MobileOtp, Long> {
	@Query(value = "SELECT otp_token FROM mobile_otp where user_id=?", nativeQuery = true)
	String verifyOtp(Long id);

	@Query(value = "SELECT created_date FROM mobile_otp where user_id=?", nativeQuery = true)
	Date getTokenCreationDate(int userId);

	@Modifying
	@Query(value = "Delete FROM mobile_otp where user_id=?", nativeQuery = true)
	void deleteToken(int userId);
	
}
