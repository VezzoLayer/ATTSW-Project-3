package com.tasks.manager.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tasks.manager.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

	Task findByTitle(String title);

	Task findByDescription(String description);

	List<Task> findByPriority(int priority);

	List<Task> findByDone(boolean done);

	List<Task> findByPriorityAndDone(int priority, boolean done);

}
