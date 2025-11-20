package ru.sergeyshokhin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table (name = "users")
@NoArgsConstructor
@ToString
@Getter

// Create table user (created_at Date Default Now())
public class User {


    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Setter
    @Column(nullable = false, length = 25)
    private String name;


    @Setter
    @Column(nullable = false, unique = true,length = 50)
    private String email;


    @Setter
    @Column(nullable = false)
    private int age;


    @Column (nullable = false)
    @ColumnDefault("current_date")
    private LocalDate created_at;

    @PrePersist
    protected void createDate (){
        created_at = LocalDate.now();
    }

    public User(String name, String email, int age) {
        this.name = name;
        if (email != null && !email.isEmpty()) email=email.toLowerCase();
        this.email = email;
        this.age = age;
    }

    public User(Integer id, String name, String email, int age) {
        this(name, email, age);
        this.id = id;
    }

}
