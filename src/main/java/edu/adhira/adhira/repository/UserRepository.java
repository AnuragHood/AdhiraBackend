package edu.adhira.adhira.repository;

import edu.adhira.adhira.authentication.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
	User findByEmail(String email);

	User findByPhone(String phone);

	@Query(value = "SELECT password FROM user where email=?", nativeQuery = true)
	String compPassword(String email);

	@Modifying
	@Query(value = "Update user set active =? where id=?", nativeQuery = true)
	void setActiveInactiveUser(char activeInactive, int userId);

	@Query(value = "SELECT * FROM user where email=? OR name =? OR phone =?", nativeQuery = true)
	User searchUser(String email,String name,String phone);
	

}
