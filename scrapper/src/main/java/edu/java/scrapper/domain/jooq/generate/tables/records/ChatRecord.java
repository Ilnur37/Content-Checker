/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq.generate.tables.records;


import edu.java.scrapper.domain.jooq.generate.tables.Chat;

import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


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
public class ChatRecord extends UpdatableRecordImpl<ChatRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>CHAT.ID</code>.
     */
    public void setId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>CHAT.ID</code>.
     */
    @Nullable
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>CHAT.TG_CHAT_ID</code>.
     */
    public void setTgChatId(@NotNull Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>CHAT.TG_CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getTgChatId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>CHAT.CREATED_AT</code>.
     */
    public void setCreatedAt(@NotNull OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>CHAT.CREATED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatRecord
     */
    public ChatRecord() {
        super(Chat.CHAT);
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    @ConstructorProperties({ "id", "tgChatId", "createdAt" })
    public ChatRecord(@Nullable Long id, @NotNull Long tgChatId, @NotNull OffsetDateTime createdAt) {
        super(Chat.CHAT);

        setId(id);
        setTgChatId(tgChatId);
        setCreatedAt(createdAt);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    public ChatRecord(edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat value) {
        super(Chat.CHAT);

        if (value != null) {
            setId(value.getId());
            setTgChatId(value.getTgChatId());
            setCreatedAt(value.getCreatedAt());
            resetChangedOnNotNull();
        }
    }
}
