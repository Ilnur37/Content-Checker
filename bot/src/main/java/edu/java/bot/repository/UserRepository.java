package edu.java.bot.repository;

import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Map<User, Set<Link>> registeredUsers = new HashMap<>();

    private boolean containsUser(User user) {
        return registeredUsers.containsKey(user);
    }

    private Optional<Map.Entry<User, Set<Link>>> getEntryById(Long id) {
        for (Map.Entry<User, Set<Link>> entry : registeredUsers.entrySet()) {
            if (Objects.equals(entry.getKey().getId(), id)) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    public void saveUser(User user) {
        if (containsUser(user)) {
            return;
        }
        registeredUsers.put(user, new HashSet<>());
    }

    public void saveLink(Long id, Link link) {
        var entry = getEntryById(id);
        if (entry.isEmpty()) {
            return;
        }
        Set<Link> newListOfLinks = entry.get().getValue();
        newListOfLinks.add(link);
        registeredUsers.put(entry.get().getKey(), newListOfLinks);
    }

    public void deleteLink(Long id, Link link) {
        var entry = getEntryById(id);
        if (entry.isEmpty()) {
            return;
        }
        Set<Link> newListOfLinks = entry.get().getValue();
        newListOfLinks.remove(link);
        registeredUsers.put(entry.get().getKey(), newListOfLinks);
    }

    public Optional<User> findUserById(Long id) {
        return getEntryById(id)
            .map(Map.Entry::getKey);
    }

    public Optional<Set<Link>> findLinksById(Long id) {
        return getEntryById(id)
            .map(Map.Entry::getValue);
    }
}
