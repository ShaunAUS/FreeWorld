package server.test.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String no;
    private String password;
    private String name;
    private String email;
    private Integer age;
    private String role;
    private String createDate;
    private String updateDate;
}
