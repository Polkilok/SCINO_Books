package com.scino.practice.polkilok.scino_books;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.scino.practice.polkilok.scino_books.adapters.BookAdapter;
import com.scino.practice.polkilok.scino_books.dao.BookDao;
import com.scino.practice.polkilok.scino_books.model.Book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by эмсиай on 11.08.2015.
 */
public class BookViewerActivity extends AppCompatActivity{

	public static final int BOOK_LOADER_ID = 1;
	private ListView mListView;
	private ArrayAdapter<Book> mAdapter;

	private BookDao mBookDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_viewer);

		mBookDao = new BookDao(this);
		mBookDao.open();

		mListView = (ListView) findViewById(R.id.book_list);
		mAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, mBookDao.getAllBooks());
		mListView.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
