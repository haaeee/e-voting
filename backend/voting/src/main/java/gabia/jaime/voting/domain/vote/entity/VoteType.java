package gabia.jaime.voting.domain.vote.entity;

import lombok.Getter;

@Getter
public enum VoteType {
    YES("찬성"), NO("반대"), GIVE_UP("기권");

    private final String description;

    VoteType(String description) {
        this.description = description;
    }
}
