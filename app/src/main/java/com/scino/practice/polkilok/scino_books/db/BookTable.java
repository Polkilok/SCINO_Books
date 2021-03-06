package com.scino.practice.polkilok.scino_books.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BookTable {

	public static final String TABLE_BOOK = "books";
	public static final String COLUMN_BOOK_ID = "_id";
	public static final String COLUMN_TITLE = "_title";
	public static final String COLUMN_AUTHOR = "_author";
	public static final String COLUMN_CATEGORY_PTR = "_category_ptr";
	public static final String COLUMN_WAS_READ = "_is_read";
	public static final String TABLE_CATEGORY = "categories";
	public static final String COLUMN_CATEGORY_ID = "_cid";
	public static final String COLUMN_CATEGORY = "_category_name";
	private static final String TAG = BookTable.class.toString();
	private static final String DATABASE_CREATE_CATEGORY_TABLE = "create table "
	+ TABLE_CATEGORY
	+ "("
	+ COLUMN_CATEGORY_ID + " integer primary key autoincrement, "
	+ COLUMN_CATEGORY + " text not null"
	+ ");";

	private static final String DATABASE_CREATE_BOOK_TABLE = "create table "
	+ TABLE_BOOK
	+ "("
	+ COLUMN_BOOK_ID + " integer primary key autoincrement, "
	+ COLUMN_TITLE + " text not null, "
	+ COLUMN_AUTHOR + " text not null, "
	+ COLUMN_WAS_READ + " tinyint not null, "
	+ COLUMN_CATEGORY_PTR + " int null"
	//+ "foreign key (" + COLUMN_CATEGORY_PTR + ") references " + TABLE_CATEGORY + " (" + COLUMN_CATEGORY_ID + ")"
	+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_CATEGORY_TABLE);
		database.execSQL(DATABASE_CREATE_BOOK_TABLE);

	}

	public static void onUpgrate(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade database to newVersion =" + newVersion + " from oldVersion =" + oldVersion);

		database.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
		onCreate(database);
	}
}
