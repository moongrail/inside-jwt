package ru.inside.jwt.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "access_tokens")
public class AccessTokenAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    @OneToOne()
    @JoinColumn(name = "id")
    private Account accountId;

    @Column(name = "access_token")
    private String accessToken;
}
