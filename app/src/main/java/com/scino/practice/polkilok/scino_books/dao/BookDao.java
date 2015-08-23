package com.scino.practice.polkilok.scino_books.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.scino.practice.polkilok.scino_books.db.BookTable;
import com.scino.practice.polkilok.scino_books.db.SCSQLiteHelper;
import com.scino.practice.polkilok.scino_books.model.Book;

/**
 * Created by Name on 10/08/15.
 */
public class BookDao {

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

	public void createBook(Book obj) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(BookTable.COLUMN_TITLE, obj.getTitle());
		contentValues.put(BookTable.COLUMN_AUTHOR, obj.getAuthor());
		contentValues.put(BookTable.COLUMN_CATEGORY_PTR, obj.getCategory());

		long bookId = mDatabase.insert(BookTable.TABLE_BOOK, null, contentValues);

		//Cursor cursor = mDatabase.query(BookTable.TABLE_BOOK,
		//new String[]{BookTable.COLUMN_BOOK_ID, BookTable.COLUMN_TITLE},
		//BookTable.COLUMN_BOOK_ID + " = ?",
		//new String[]{String.valueOf(bookId)}, null, null, null);
		//cursor.moveToFirst();
		//Book book = cursorToBook(cursor);
		//cursor.close();

		//return book;
		//return obj;
	}

	public void createCategory(String name) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(BookTable.COLUMN_CATEGORY, name);
		long bookId = mDatabase.insert(BookTable.TABLE_CATEGORY, null, contentValues);
	}

	public void deleteBookById(long id) {
		mDatabase.delete(BookTable.TABLE_BOOK, BookTable.COLUMN_BOOK_ID + " = " + id, null);
	}

	public List<Book> getAllBooks() {
		List<Book> books = new ArrayList<>();

		Cursor cursor = mDatabase.query(BookTable.TABLE_BOOK,
		new String[]{BookTable.COLUMN_BOOK_ID, BookTable.COLUMN_TITLE, BookTable.COLUMN_AUTHOR, BookTable.COLUMN_CATEGORY_PTR},
		null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Book book = cursorToBook(cursor);
			books.add(book);
			cursor.moveToNext();
		}
		cursor.close();
		return books;
	}

	public List<String> getNamesLists() {
		List<String> ListsNames = new ArrayList<>();

		Cursor cursor = mDatabase.query(BookTable.TABLE_CATEGORY,
		new String[]{BookTable.COLUMN_CATEGORY_ID, BookTable.COLUMN_CATEGORY},
		null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ListsNames.add(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_CATEGORY)));
			cursor.moveToNext();
		}
		cursor.close();
		return ListsNames;
	}

	public Cursor getAllBooksCursor() {
		return mDatabase.query(BookTable.TABLE_BOOK,
		new String[]{BookTable.COLUMN_BOOK_ID, BookTable.COLUMN_TITLE}, null, null, null, null, null);

	}

	public Cursor getCursorOnCategory(String Category_name) {
		final String query = "select * from " + BookTable.TABLE_CATEGORY + " where " + BookTable.COLUMN_CATEGORY + " like '" + Category_name + "';";
		return mDatabase.rawQuery(query, null);
	}

	private Book cursorToBook(Cursor cursor) {
		Book book = new Book();
		book.setId(cursor.getLong(cursor.getColumnIndex(BookTable.COLUMN_BOOK_ID)));
		book.setTitle(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_TITLE)));
		book.setAuthor(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_AUTHOR)));
		book.setCategory(cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_CATEGORY_PTR)));

		return book;
	}
}
