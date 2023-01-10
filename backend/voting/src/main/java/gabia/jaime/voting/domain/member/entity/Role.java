package gabia.jaime.voting.domain.member.entity;

import lombok.Getter;
@Getter
public enum Role {

    SHAREHOLDER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
