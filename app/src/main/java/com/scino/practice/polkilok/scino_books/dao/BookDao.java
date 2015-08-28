package com.scino.practice.polkilok.scino_books.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.scino.practice.polkilok.scino_books.db.BookTable;
import com.scino.practice.polkilok.scino_books.db.SCSQLiteHelper;
import com.scino.practice.polkilok.scino_books.model.Book;
import com.scino.practice.polkilok.scino_books.model.Category;

import java.util.ArrayList;
import java.util.List;

public class BookDao {

	public static final short FALSE = 0;
	public static final short TRUE = 1;
	private SQLiteDatabase mDatabase;
	private SCSQLiteHelper mHelper;

	public BookDao(Context context) {
		mHelper = new SCSQLiteHelper(context);
	}

	public void open() throws SQLException {
		mDatabase = mHelper.getWritableDatabase();
	}

	public void close() {
		mHelper.close();
	}

	public List<Book> find_book(String author) {
		List<Book> answer = new ArrayList<>();// = new Book();
//		Cursor cursor = mDatabase.query(BookTable.TABLE_BOOK, null,
//		BookTable.COLUMN_TITLE + " like '?' AND " + BookTable.COLUMN_AUTHOR + " like '?'",
//		new String[]{title + "%", author + "%"},
//		null, null, null);
		Cursor cursor = mDatabase.query(BookTable.TABLE_BOOK, null,
		BookTable.COLUMN_AUTHOR + "=?",
		new String[]{author},
		null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			answer.add(cursorToBook(cursor));
			cursor.moveToNext();
		}
		return answer;
	}

	public void createBook(Book obj, long category_id) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(BookTable.COLUMN_TITLE, obj.getTitle());
		contentValues.put(BookTable.COLUMN_AUTHOR, obj.getAuthor());
		//if (category_id != Category.AUTO_ID)
		contentValues.put(BookTable.COLUMN_CATEGORY_PTR, category_id);
		if (obj.isRead())
			contentValues.put(BookTable.COLUMN_WAS_READ, TRUE);
		else
			contentValues.put(BookTable.COLUMN_WAS_READ, FALSE);
		mDatabase.insert(BookTable.TABLE_BOOK, null, contentValues);
	}

	public void createCategory(String name) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(BookTable.COLUMN_CATEGORY, name);
		mDatabase.insert(BookTable.TABLE_CATEGORY, null, contentValues);
	}

	public void deleteBookById(long id) {
		mDatabase.delete(BookTable.TABLE_BOOK, BookTable.COLUMN_BOOK_ID + " = " + id, null);
	}

	public void deleteCategory(Category name) {
		List<Book> books = getCategoryList(name);
		for (Book book : books) {
			update_book(book, Category.AUTO_ID);
		}
		mDatabase.delete(BookTable.TABLE_CATEGORY, BookTable.COLUMN_CATEGORY_ID + "=" + name.getId(), null);
	}

	public void change_read(Book obj) {
		ContentValues updatedValues = new ContentValues();
		if (obj.isRead())
			updatedValues.put(BookTable.COLUMN_WAS_READ, TRUE);
		else
			updatedValues.put(BookTable.COLUMN_WAS_READ, FALSE);
		mDatabase.update(BookTable.TABLE_BOOK, updatedValues, BookTable.COLUMN_BOOK_ID + "=" + obj.getId(), null);
	}

	public void update_book(Book obj, long category_id) {
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(BookTable.COLUMN_TITLE, obj.getTitle());
		//updatedValues.put(BookTable.COLUMN_BOOK_ID, obj.getId());
		updatedValues.put(BookTable.COLUMN_AUTHOR, obj.getAuthor());
		//updatedValues.put(BookTable.COLUMN_CATEGORY_PTR, obj.getCategory());
		//if (category_id != Category.AUTO_ID)
		updatedValues.put(BookTable.COLUMN_CATEGORY_PTR, category_id);
		//else
		//	updatedValues.put(BookTable.COLUMN_CATEGORY_PTR, "null");
		if (obj.isRead())
			updatedValues.put(BookTable.COLUMN_WAS_READ, TRUE);
		else
			updatedValues.put(BookTable.COLUMN_WAS_READ, FALSE);
		mDatabase.update(BookTable.TABLE_BOOK, updatedValues, BookTable.COLUMN_BOOK_ID + "=" + obj.getId(), null);
	}

	public void update_category(Category upd) {
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(BookTable.COLUMN_CATEGORY, upd.toString());
		mDatabase.update(BookTable.TABLE_CATEGORY, updatedValues, BookTable.COLUMN_CATEGORY_ID + "=" + upd.getId(), null);
	}

	public List<Book> getAllBooks() {
		List<Book> books = new ArrayList<>();
		Cursor cursor = mDatabase.rawQuery("select * from " + BookTable.TABLE_BOOK, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Book book = cursorToBook(cursor);
			books.add(book);
			cursor.moveToNext();
		}
		cursor.close();
		return books;
	}

	public List<Book> getCategoryList(Category category) {
		final String query = "select * from " + BookTable.TABLE_BOOK + " where " + BookTable.COLUMN_CATEGORY_PTR + " like '" + category.getId() + "';";
		List<Book> ListBooks = new ArrayList<>();
		Cursor cursor = mDatabase.rawQuery(query, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ListBooks.add(cursorToBook(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return ListBooks;
	}

	public List<Book> getUncategoryList() {
		//final String query = "select * from " + BookTable.TABLE_BOOK + " where " + BookTable.COLUMN_CATEGORY_PTR + " is null;";
		final String query = "select * from " + BookTable.TABLE_BOOK + " where " + BookTable.COLUMN_CATEGORY_PTR + "=" + Category.AUTO_ID + ";";
		List<Book> ListBooks = new ArrayList<>();
		Cursor cursor = mDatabase.rawQuery(query, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ListBooks.add(cursorToBook(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return ListBooks;
	}

	public List<Category> getAllCategory() {
		List<Category> ListsNames = new ArrayList<>();
		Cursor cursor = mDatabase.query(BookTable.TABLE_CATEGORY,
		new String[]{BookTable.COLUMN_CATEGORY_ID, BookTable.COLUMN_CATEGORY},
		null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = new Category();
			category.setName(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_CATEGORY)));
			category.setId(cursor.getLong(cursor.getColumnIndex(BookTable.COLUMN_CATEGORY_ID)));
			ListsNames.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		return ListsNames;
	}

	public boolean contain(String table_name, String column_name, String value) {
		final String QUERY = "select * from " + table_name + " where "
		+ column_name + " like '" + value + "';";
		//Cursor cursor = mDatabase.rawQuery(QUERY, null);
		Cursor cursor = mDatabase.query(table_name, null, column_name + " like '" + value + "'",
		null, null, null, null);
		cursor.moveToFirst();
		boolean answer = !cursor.isAfterLast();
		cursor.close();
		return answer;
	}

	public Category getCategory_by_id(long id) {
		Cursor cursor = mDatabase.query(BookTable.TABLE_CATEGORY, null,
		BookTable.COLUMN_CATEGORY_ID + "=" + id,
		null, null, null, null);

		//final String QUERY = "select * from " + BookTable.TABLE_CATEGORY + " where "
		//+ BookTable.COLUMN_CATEGORY_ID + "=" + id + ";";
		//Cursor cursor = mDatabase.rawQuery(QUERY, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast())
			return null;
		Category answer = new Category(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_CATEGORY)));
		cursor.getColumnNames();
		answer.setId(id);
		return answer;
	}

	private Book cursorToBook(Cursor cursor) {
		Book book = new Book();
		book.setId(cursor.getLong(cursor.getColumnIndex(BookTable.COLUMN_BOOK_ID)));
		book.setTitle(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_TITLE)));
		book.setAuthor(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_AUTHOR)));
		//book.setCategory(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_CATEGORY_PTR)));
		book.setCategory_id(cursor.getLong(cursor.getColumnIndex(BookTable.COLUMN_CATEGORY_PTR)));
		book.setRead(cursor.getShort(cursor.getColumnIndex(BookTable.COLUMN_WAS_READ)) == TRUE);
		return book;
	}
}
