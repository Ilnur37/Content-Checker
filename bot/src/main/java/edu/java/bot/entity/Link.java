package edu.java.bot.entity;

import lombok.Data;

@Data
public class Link {
    //private Long id;
    private final Domain domain;
    private final String link;

    @Override
    public String toString() {
        return domain.toString() + link;
    }
}
