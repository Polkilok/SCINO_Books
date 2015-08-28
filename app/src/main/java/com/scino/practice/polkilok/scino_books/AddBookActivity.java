package com.scino.practice.polkilok.scino_books;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.scino.practice.polkilok.scino_books.dao.BookDao;
import com.scino.practice.polkilok.scino_books.model.Category;

import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {

	public static final String AUTHOR = "com.scino.practice.polkilok.scino_books.Author";
	public static final String TITLE = "com.scino.practice.polkilok.scino_books.Title";
	public static final String CATEGORY = "com.scino.practice.polkilok.scino_books.Category";
	public static final String CATEGORY_ID = "com.scino.practice.polkilok.scino_books.Category_id";
	public static final String IS_READ = "com.scino.practice.polkilok.scino_books.is_read";
	public static final String BOOK_ID = "com.scino.practice.polkilok.scino_books.book_id";
	//private Button mSave;
	private EditText mAuthor;
	private EditText mTitle;
	private Spinner mSpinner;
	private BookDao mBookDao;
	private ArrayAdapter<Category> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_book);

		mBookDao = new BookDao(this);
		mBookDao.open();
		mAuthor = (EditText) findViewById(R.id.editText_author);
		mTitle = (EditText) findViewById(R.id.editText_title);

		mAuthor.setText(getIntent().getStringExtra(AUTHOR));
		mTitle.setText(getIntent().getStringExtra(TITLE));

		List<Category> list = new ArrayList<>();
		list.add(new Category(getString(R.string.uncategory)));
		list.addAll(mBookDao.getAllCategory());
		//mAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, list);
		mAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mSpinner = (Spinner) findViewById(R.id.spinner_category);
		mSpinner.setAdapter(mAdapter);

		long id = getIntent().getLongExtra(CATEGORY_ID, Category.AUTO_ID);
		if (id != Category.AUTO_ID) {
			Category prew = new Category();
			prew.setId(id);
			for (Category iter : list) {
				if (iter.getId() == id)
					prew = iter;
			}
			mSpinner.setSelection(mAdapter.getPosition(prew));
		}
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
		if (!title.isEmpty()) {
			Intent buf = new Intent();
			buf.putExtra(AddBookActivity.AUTHOR, author);
			buf.putExtra(AddBookActivity.TITLE, title);
			if (mSpinner.getSelectedItemPosition() != 0) {
				buf.putExtra(AddBookActivity.CATEGORY, mSpinner.getSelectedItem().toString());
				buf.putExtra(AddBookActivity.CATEGORY_ID, ((Category) mSpinner.getSelectedItem()).getId());
			} else {
				buf.putExtra(AddBookActivity.CATEGORY_ID, Category.AUTO_ID);
			}
			buf.putExtra(AddBookActivity.IS_READ, getIntent().getShortExtra(IS_READ, BookDao.FALSE));
			buf.putExtra(AddBookActivity.BOOK_ID, getIntent().getLongExtra(BOOK_ID, -1));
			setResult(RESULT_OK, buf);
			finish();
		} else {
			AlertDialog dialog = AlertDialog_builder.getDialog(this, AlertDialog_builder.DIALOG_INCORRECT_DATA, getString(R.string.warning_input_title), null);
			dialog.show();
		}

	}
}