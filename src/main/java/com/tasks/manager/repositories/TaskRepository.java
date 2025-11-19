package com.tasks.manager.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tasks.manager.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

	Task findByTitle(String string);

}
