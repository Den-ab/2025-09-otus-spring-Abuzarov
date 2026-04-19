package ru.otus.hw.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdMappingCache {
    private final Map<Long, ObjectId> authorIds = new ConcurrentHashMap<>();

    private final Map<Long, ObjectId> genreIds = new ConcurrentHashMap<>();

    private final Map<Long, ObjectId> bookIds = new ConcurrentHashMap<>();

    public void putAuthor(Long sourceId, ObjectId targetId) {
        authorIds.put(sourceId, targetId);
    }

    public void putGenre(Long sourceId, ObjectId targetId) {
        genreIds.put(sourceId, targetId);
    }

    public void putBook(Long sourceId, ObjectId targetId) {
        bookIds.put(sourceId, targetId);
    }

    public ObjectId getAuthor(Long sourceId) {
        return authorIds.get(sourceId);
    }

    public ObjectId getGenre(Long sourceId) {
        return genreIds.get(sourceId);
    }

    public void clearAll() {

        this.authorIds.clear();
        this.genreIds.clear();
        this.bookIds.clear();
    }
}
