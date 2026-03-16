package ru.otus.hw.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.document.Author;

public interface AuthorRepository extends MongoRepository<Author, ObjectId> { }
