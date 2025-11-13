package com.tasks.manager.model;

import java.util.Objects;

public class Task {

	private Long Id;
	private String Title;
	private String Description;
	private int Priority;
	private boolean Done;

	public Task() {
		// Required for serialization/deserialization
	}

	public Task(Long id, String title, String description, int priority, boolean done) {
		Id = id;
		Title = title;
		Description = description;
		Priority = priority;
		Done = done;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getPriority() {
		return Priority;
	}

	public void setPriority(int priority) {
		Priority = priority;
	}

	public boolean isDone() {
		return Done;
	}

	public void setDone(boolean done) {
		Done = done;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Description, Done, Id, Priority, Title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(Description, other.Description) && Done == other.Done && Objects.equals(Id, other.Id)
				&& Priority == other.Priority && Objects.equals(Title, other.Title);
	}

	@Override
	public String toString() {
		return "Task [Id=" + Id + ", Title=" + Title + ", Description=" + Description + ", Priority=" + Priority
				+ ", Done=" + Done + "]";
	}
}
