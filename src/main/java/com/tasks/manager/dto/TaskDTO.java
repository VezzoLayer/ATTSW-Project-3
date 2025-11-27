package com.tasks.manager.dto;

import java.util.Objects;

public class TaskDTO {

	private String id;
	private String title;
	private String description;
	private int priority;
	private boolean done;

	public TaskDTO(String id, String title, String description, int priority, boolean done) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.done = done;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, done, id, priority, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskDTO other = (TaskDTO) obj;
		return Objects.equals(description, other.description) && done == other.done && Objects.equals(id, other.id)
				&& priority == other.priority && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "TaskDTO [id=" + id + ", title=" + title + ", description=" + description + ", priority=" + priority
				+ ", done=" + done + "]";
	}
}
