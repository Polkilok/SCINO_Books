package com.scino.practice.polkilok.scino_books.model;

public class Category {
	static public final long AUTO_ID = -1;
	private String _name;
	private long _id;

	public Category(String name) {
		_name = name;
		_id = AUTO_ID;
	}

	public Category() {
		_id = AUTO_ID;
	}

	@Override
	public String toString() {
		return _name;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public long getId() {
		return _id;
	}

	public void setId(long id) {
		this._id = id;
	}
}
