package org.tutgi.student_registration.data.models;

import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "profiles")
public class Profile extends MasterData{
    @Column(nullable = false, name="name_mm")
    private String mmName;

    @Column(nullable = false, name="name_eng")
    private String engName;
    
    @Column(nullable = false)
    private String nrc;
    
    @Column(name="photo_url")
    private String photoUrl;
    
    @Column(name="signature_url")
    private String signatureUrl;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    public Profile(String mmName, String engName, String nrc,String photoUrl) {
        this.mmName = mmName;
        this.engName = engName;
        this.nrc = nrc;
        this.photoUrl = photoUrl;
    }
    
    public void assignUser(User user) {
        this.user = user;
        user.setProfile(this);
    }
}
