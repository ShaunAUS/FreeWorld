package server.test.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "member_no")
    private Long no;

    private String password;
    private String name;
    private String email;
    private Integer age;
    private String role;
    private String createDate;
    private String updateDate;
}
