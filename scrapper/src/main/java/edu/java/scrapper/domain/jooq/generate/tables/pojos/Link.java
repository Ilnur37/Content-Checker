/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq.generate.tables.pojos;


import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.19.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String url;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdateAt;
    private String name;
    private String author;
    private OffsetDateTime lastCheckAt;

    public Link() {}

    public Link(Link value) {
        this.id = value.id;
        this.url = value.url;
        this.createdAt = value.createdAt;
        this.lastUpdateAt = value.lastUpdateAt;
        this.name = value.name;
        this.author = value.author;
        this.lastCheckAt = value.lastCheckAt;
    }

    @ConstructorProperties({ "id", "url", "createdAt", "lastUpdateAt", "name", "author", "lastCheckAt" })
    public Link(
        @Nullable Long id,
        @NotNull String url,
        @NotNull OffsetDateTime createdAt,
        @NotNull OffsetDateTime lastUpdateAt,
        @Nullable String name,
        @Nullable String author,
        @NotNull OffsetDateTime lastCheckAt
    ) {
        this.id = id;
        this.url = url;
        this.createdAt = createdAt;
        this.lastUpdateAt = lastUpdateAt;
        this.name = name;
        this.author = author;
        this.lastCheckAt = lastCheckAt;
    }

    /**
     * Getter for <code>LINK.ID</code>.
     */
    @Nullable
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>LINK.ID</code>.
     */
    public void setId(@Nullable Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String url) {
        this.url = url;
    }

    /**
     * Getter for <code>LINK.CREATED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>LINK.CREATED_AT</code>.
     */
    public void setCreatedAt(@NotNull OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for <code>LINK.LAST_UPDATE_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastUpdateAt() {
        return this.lastUpdateAt;
    }

    /**
     * Setter for <code>LINK.LAST_UPDATE_AT</code>.
     */
    public void setLastUpdateAt(@NotNull OffsetDateTime lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    /**
     * Getter for <code>LINK.NAME</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>LINK.NAME</code>.
     */
    public void setName(@Nullable String name) {
        this.name = name;
    }

    /**
     * Getter for <code>LINK.AUTHOR</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getAuthor() {
        return this.author;
    }

    /**
     * Setter for <code>LINK.AUTHOR</code>.
     */
    public void setAuthor(@Nullable String author) {
        this.author = author;
    }

    /**
     * Getter for <code>LINK.LAST_CHECK_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastCheckAt() {
        return this.lastCheckAt;
    }

    /**
     * Setter for <code>LINK.LAST_CHECK_AT</code>.
     */
    public void setLastCheckAt(@NotNull OffsetDateTime lastCheckAt) {
        this.lastCheckAt = lastCheckAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Link other = (Link) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.url == null) {
            if (other.url != null)
                return false;
        }
        else if (!this.url.equals(other.url))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.lastUpdateAt == null) {
            if (other.lastUpdateAt != null)
                return false;
        }
        else if (!this.lastUpdateAt.equals(other.lastUpdateAt))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.author == null) {
            if (other.author != null)
                return false;
        }
        else if (!this.author.equals(other.author))
            return false;
        if (this.lastCheckAt == null) {
            if (other.lastCheckAt != null)
                return false;
        }
        else if (!this.lastCheckAt.equals(other.lastCheckAt))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.lastUpdateAt == null) ? 0 : this.lastUpdateAt.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.author == null) ? 0 : this.author.hashCode());
        result = prime * result + ((this.lastCheckAt == null) ? 0 : this.lastCheckAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Link (");

        sb.append(id);
        sb.append(", ").append(url);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(lastUpdateAt);
        sb.append(", ").append(name);
        sb.append(", ").append(author);
        sb.append(", ").append(lastCheckAt);

        sb.append(")");
        return sb.toString();
    }
}