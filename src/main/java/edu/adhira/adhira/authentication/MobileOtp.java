package edu.adhira.adhira.authentication;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@EnableAutoConfiguration
public class MobileOtp {
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name="otp_token_id")
	    private long otpTokenId;

	    @Column(name="otp_token")
	    private String otpToken;

	    @Temporal(TemporalType.TIMESTAMP)
	    private Date createdDate;

	    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	    @JoinColumn(nullable = false, name = "user_id")
	    private User user;

	    public MobileOtp(User user,String otpToken) {
	        this.user = user;
	        createdDate = new Date();
	        this.otpToken = otpToken;
	        
	    }
}
