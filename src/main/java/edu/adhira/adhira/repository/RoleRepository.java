package edu.adhira.adhira.repository;

import edu.adhira.adhira.authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRole(String role);
}
