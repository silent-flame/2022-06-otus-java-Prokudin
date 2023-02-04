package ru.otus.db;

import org.springframework.data.repository.CrudRepository;
import ru.otus.db.entity.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {}