package edu.adhira.adhira.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.Set;

@Entity
@EnableAutoConfiguration
@SequenceGenerator(name = "seq", initialValue = 357948, allocationSize = 100)
public class User {

	@Id
	@JsonInclude
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")

	private int id;

	private String email;

	private String password;

	private String name;

	private String lastName;

	private String gender;

	private String phone;
	
    @Transient
	private String registrationMode;

	private char active;
	
	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRegistrationMode() {
		return registrationMode;
	}

	public void setRegistrationMode(String registrationMode) {
		this.registrationMode = registrationMode;
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public char getActive() {
		return active;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setActive(char active) {
		this.active = active;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", lastName="
				+ lastName + ", gender=" + gender + ", phone=" + phone + ", registrationMode=" + registrationMode
				+ ", active=" + active + ", image=" + image + ", roles=" + roles + "]";
	}
}