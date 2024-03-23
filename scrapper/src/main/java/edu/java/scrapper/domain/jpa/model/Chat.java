package edu.java.scrapper.domain.jpa.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tg_chat_id", nullable = false)
    private Long tgChatId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(cascade = {
        CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "chat_link",
               joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id")
    )
    private Set<Link> links = new HashSet<>();

    public void addLink(Link link) {
        this.links.add(link);
        link.getChats().add(this);
    }

    public void removeLink(Link link) {
        this.links.remove(link);
        link.getChats().remove(this);
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) && Objects.equals(tgChatId, chat.tgChatId)
            && Objects.equals(createdAt, chat.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tgChatId, createdAt);
    }
}
