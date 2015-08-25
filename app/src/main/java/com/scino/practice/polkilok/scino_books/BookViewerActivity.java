package com.scino.practice.polkilok.scino_books;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.scino.practice.polkilok.scino_books.dao.BookDao;
import com.scino.practice.polkilok.scino_books.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by эмсиай on 11.08.2015.
 */
public class BookViewerActivity extends AppCompatActivity {

	public static final int BOOK_LOADER_ID = 1;
	private ListView mListView;
	private ArrayAdapter<Book> mAdapterListView;
	private ArrayAdapter<String> mAdapterSpinner;

	private BookDao mBookDao;
	private Spinner mSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//TODO принимать параметр, который определяет, какую категорию открыть - без- или все
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_viewer);

		mBookDao = new BookDao(this);
		mBookDao.open();

		List<String> list = new ArrayList<>();
		list.add(getString(R.string.all));
		list.add(getString(R.string.uncategory));
		list.addAll(mBookDao.getNamesLists());
		mAdapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
		mAdapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner = (Spinner) findViewById(R.id.spinner_category_filter);
		mSpinner.setAdapter(mAdapterSpinner);
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View itemSelected,
									   int selectedItemPosition, long selectedId) {
				mAdapterListView.clear();
				String category_name = mSpinner.getSelectedItem().toString();
				if (category_name == getString(R.string.all))
					mAdapterListView.addAll(mBookDao.getAllBooks());
				else {
					if (category_name == getString(R.string.uncategory))
						mAdapterListView.addAll(mBookDao.getUncategoryList());
					else
						mAdapterListView.addAll(mBookDao.getCategoryList(category_name));
				}
				mAdapterListView.notifyDataSetChanged();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mListView = (ListView) findViewById(R.id.book_list);
		//mAdapterListView = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
		//mAdapterListView = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_2)

		//mAdapterListView = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);
		mAdapterListView = new ArrayAdapter<Book>(this, R.layout.list_item)
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Book book = getItem(position);

				if (convertView == null) {
					//convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, null);
					convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
				}
				((TextView) convertView.findViewById(android.R.id.text1)).setText(book.getTitle());
				((TextView) convertView.findViewById(android.R.id.text2)).setText(book.getAuthor());
				return convertView;
			}
		};
		mListView.setAdapter(mAdapterListView);
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
