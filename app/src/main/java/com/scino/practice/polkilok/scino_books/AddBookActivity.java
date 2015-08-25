package com.scino.practice.polkilok.scino_books;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.scino.practice.polkilok.scino_books.dao.BookDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by эмсиай on 11.08.2015.
 */
public class AddBookActivity extends AppCompatActivity {

	//private Button mSave;
	private EditText mAuthor;
	private EditText mTitle;
	private Spinner mSpinner;
	private BookDao mBookDao;
	private ArrayAdapter<String> mAdapter;
	public static final String AUTHOR = "com.scino.practice.polkilok.scino_books.Author";
	public static final String TITLE = "com.scino.practice.polkilok.scino_books.Title";
	public static final String CATEGORY = "com.scino.practice.polkilok.scino_books.Category";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_book);

		mBookDao = new BookDao(this);
		mBookDao.open();
		//mSave = (Button) findViewById(R.id.BTsave);
		mAuthor = (EditText) findViewById(R.id.editText_author);
		mTitle = (EditText) findViewById(R.id.editText_title);
		List<String> list = new ArrayList<>();
		list.add(getString(R.string.uncategory));
		list.addAll(mBookDao.getNamesLists());
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mSpinner = (Spinner) findViewById(R.id.spinner_category);
		mSpinner.setAdapter(mAdapter);
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

	public void onClick_save(View view) {
		String author = mAuthor.getText().toString();
		String title = mTitle.getText().toString();
		if (!author.isEmpty() && !title.isEmpty()) {
			Intent buf = new Intent();
			buf.putExtra(AddBookActivity.AUTHOR, author);
			buf.putExtra(AddBookActivity.TITLE, title);
			if (mSpinner.getSelectedItemPosition() != 0)
				buf.putExtra(AddBookActivity.CATEGORY, mSpinner.getSelectedItem().toString());
			setResult(RESULT_OK, buf);
			finish();
		} else {
			//TODO Сделать вывод предупреждения о некорректности ввода
		}

	}
}