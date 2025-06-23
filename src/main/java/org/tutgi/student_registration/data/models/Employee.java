package org.tutgi.student_registration.data.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.tutgi.student_registration.data.enums.RoleName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = true)
    private String password;
    
    @Column(nullable = true)
    private String sign;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName role;
    
    @Builder.Default
    @Column(nullable = false)
    private boolean loginFirstTime = true;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
