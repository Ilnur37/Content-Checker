package edu.java.scrapper.domain.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_update_at", nullable = false)
    private OffsetDateTime lastUpdateAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(name = "last_check_at", nullable = false)
    private OffsetDateTime lastCheckAt;

    @ManyToMany(mappedBy = "links", fetch = FetchType.LAZY)
    private Set<Chat> chats = new HashSet<>();

}
