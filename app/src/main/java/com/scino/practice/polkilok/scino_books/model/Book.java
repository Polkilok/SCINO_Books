package com.scino.practice.polkilok.scino_books.model;
public class Book {

	static private final char space = '\n';
	private long id;
	private String title;
	private String author;
	private String category;
	private long category_id;
	private boolean read;

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

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}
}
