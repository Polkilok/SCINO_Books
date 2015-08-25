package com.scino.practice.polkilok.scino_books.model;

/**
 * Created by Name on 10/08/15.
 */
public class Book {

	private long id;
	private String title;
	private String author;
	private String category;
	private boolean read;
	private final char space = '\n';

	public Book()
	{
		read = false;
	}

	@Override
	public String toString() {
		String answer = title + space + author;
		if (category == null)
			return answer;
		else
			return answer + space + category;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
