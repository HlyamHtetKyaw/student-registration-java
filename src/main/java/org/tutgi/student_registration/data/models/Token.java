package org.tutgi.student_registration.data.models;

import java.time.Instant;

import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class Token extends MasterData{
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String refreshtoken;

    @Column(nullable = false)
    private Instant expiredAt;
    
    public Token(String refreshtoken,Instant expiredAt) {
    	this.refreshtoken = refreshtoken;
    	this.expiredAt = expiredAt;
    }
    
    public void assignUser(User user) {
        this.user = user;
        user.setToken(this);
    }
}
