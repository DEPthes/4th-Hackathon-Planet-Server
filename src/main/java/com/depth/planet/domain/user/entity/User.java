package com.depth.planet.domain.user.entity;

import com.depth.planet.domain.user.entity.enums.GenderType;
import com.depth.planet.domain.user.entity.enums.MBTI;
import com.depth.planet.domain.user.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "USER_ACCOUNT")
public class User {
    //====== 유저 기본정보 ========
    @Id @Setter(AccessLevel.NONE)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.User;

    //======= 유저 성향 ==========
    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> hobbies = new ArrayList<>();
}
