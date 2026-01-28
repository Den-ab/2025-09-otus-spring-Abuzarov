package ru.otus.hw.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Genre;

public interface GenreRepository extends MongoRepository<Genre, ObjectId> { }
