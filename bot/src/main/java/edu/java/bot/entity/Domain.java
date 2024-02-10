package edu.java.bot.entity;

import lombok.Data;

@Data
public class Domain {
    //private Long id;
    private final String domain;

    @Override
    public String toString() {
        return domain;
    }
}
