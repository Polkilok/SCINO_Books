package com.scino.practice.polkilok.scino_books;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.scino.practice.polkilok.scino_books.dao.BookDao;
import com.scino.practice.polkilok.scino_books.db.BookTable;
import com.scino.practice.polkilok.scino_books.model.Book;
import com.scino.practice.polkilok.scino_books.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookViewerActivity extends AppCompatActivity {

	public static final String AUTO_CATEGORY = "set_category";
	public static final String CATEGORY_UNCATEGORY = "uncategory";
	private static final int STANDART_CATEGORY_COUNT = 2;
	private static final int ID_CHANGE_READ = 1;
	private static final int ID_EDIT_BOOK = 2;
	private static final int ID_REMOVE_BOOK = 3;
	private static final int ACTIVITY_ID_ADD_BOOK = 4;
	private static final int ID_EDIT_CATEGORY = 5;
	private static final int ID_REMOVE_CATEGORY = 6;
	private static final int ACTIVITY_ID_ADD_CATEGORY = 7;
	private static final int DIALOG_ID_SEARCH = 8;
	AlertDialog mSearchDialog;
	Filter mFilter;
	private ListView mListView;
	private ArrayAdapter<Book> mAdapterListView;
	private ArrayAdapter<Category> mAdapterSpinner;
	private BookDao mBookDao;
	private Spinner mSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_viewer);
		mFilter = new Filter();
		mBookDao = new BookDao(this);
		mBookDao.open();

		List<Category> list = new ArrayList<>();
		list.add(new Category(getString(R.string.all)));
		list.add(new Category(getString(R.string.uncategory)));
		list.addAll(mBookDao.getAllCategory());
		mAdapterSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				//registerForContextMenu(parent);
				if (convertView == null) {
					convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, null);
				}
				convertView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
						menu.add(Menu.NONE, ID_EDIT_CATEGORY, 0, getString(R.string.editing));
						menu.add(Menu.NONE, ID_REMOVE_CATEGORY, 0, getString(R.string.remove));
					}
				});
				return super.getView(position, convertView, parent);
			}
		};
		mSpinner = (Spinner) findViewById(R.id.spinner_category_filter);
		mSpinner.setAdapter(mAdapterSpinner);
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View itemSelected,
									   int selectedItemPosition, long selectedId) {
				update_all_lists();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		String autocategory = getIntent().getStringExtra(AUTO_CATEGORY);
		if (CATEGORY_UNCATEGORY.equals(autocategory)) {
			mSpinner.setSelection(1);
		}

		mListView = (ListView) findViewById(R.id.book_list);
		mAdapterListView = new ArrayAdapter<Book>(this, R.layout.list_item) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Book book = getItem(position);
				registerForContextMenu(parent);
				if (convertView == null) {
					convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
				}
				((TextView) convertView.findViewById(R.id.TextViewTitle)).setText(book.getTitle());
				((TextView) convertView.findViewById(R.id.TextViewAuthor)).setText(book.getAuthor());
				((CheckBox) convertView.findViewById(R.id.CheckBox_is_read)).setChecked(book.isRead());
				return convertView;
			}
		};
		mListView.setAdapter(mAdapterListView);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, null);
		builder.setView(getLayoutInflater().inflate(R.layout.dialog_search_setings, null));
		builder.setCancelable(true);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText author = (EditText) mSearchDialog.findViewById(R.id.editText_s_author);
				dialog.dismiss();
				mAdapterListView.clear();
				mAdapterListView.addAll(mBookDao.find_book(author.getText().toString()));
				mAdapterListView.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // Кнопка ОК
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		mSearchDialog = builder.create();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, ID_CHANGE_READ, 0, getString(R.string.change_is_read));
		menu.add(Menu.NONE, ID_EDIT_BOOK, 0, getString(R.string.editing));
		menu.add(Menu.NONE, ID_REMOVE_BOOK, 0, getString(R.string.remove));
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		AdapterView.AdapterContextMenuInfo ad;
		Book b;
		switch (item.getItemId()) {
			// пункты меню для tvColor
			case ID_CHANGE_READ:
				ad = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				b = mAdapterListView.getItem(ad.position);
				b.setRead(!b.isRead());
				mAdapterListView.notifyDataSetChanged();
				mBookDao.change_read(b);
				break;
			case ID_EDIT_BOOK:
				ad = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				b = mAdapterListView.getItem(ad.position);
				Intent buf = new Intent(BookViewerActivity.this, AddBookActivity.class);
				buf.putExtra(AddBookActivity.TITLE, b.getTitle());
				buf.putExtra(AddBookActivity.AUTHOR, b.getAuthor());
				buf.putExtra(AddBookActivity.CATEGORY_ID, b.getCategory_id());
				if (b.isRead())
					buf.putExtra(AddBookActivity.IS_READ, BookDao.TRUE);
				else
					buf.putExtra(AddBookActivity.IS_READ, BookDao.FALSE);
				buf.putExtra(AddBookActivity.BOOK_ID, b.getId());
				startActivityForResult(buf, ACTIVITY_ID_ADD_BOOK);
				break;
			case ID_REMOVE_BOOK:
				ad = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				b = mAdapterListView.getItem(ad.position);
				AlertDialog dialog = AlertDialog_builder.getDialog(this, AlertDialog_builder.DIALOG_ACCEPT_REMOVE, b.toString(), new AlertDialog_builder.extra_action() {
					@Override
					public void action() {
						AdapterView.AdapterContextMenuInfo ad = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
						Book b = mAdapterListView.getItem(ad.position);
						mAdapterListView.remove(b);
						mAdapterListView.notifyDataSetChanged();
						mBookDao.deleteBookById(b.getId());
					}
				});
				dialog.show();
				break;
			case ID_EDIT_CATEGORY:
				if (mSpinner.getSelectedItemPosition() >= STANDART_CATEGORY_COUNT) {
					Intent intent = new Intent(BookViewerActivity.this, NewListActivity.class);
					intent.putExtra(NewListActivity.LIST_NAME, mSpinner.getSelectedItem().toString());
					intent.putExtra(NewListActivity.LIST_ID, ((Category) mSpinner.getSelectedItem()).getId());
					startActivityForResult(intent, ACTIVITY_ID_ADD_CATEGORY);
				} else {
					AlertDialog dialog3 = AlertDialog_builder.getDialog(this, AlertDialog_builder.DIALOG_ACTION_IMPOSSIBLE, null, null);
					dialog3.show();
				}
				break;
			case ID_REMOVE_CATEGORY:
				int a = mSpinner.getSelectedItemPosition();
				if (mSpinner.getSelectedItemPosition() >= STANDART_CATEGORY_COUNT) {//Низзя удалять первые элементы (СТАНДАРТНЫЕ)
					AlertDialog dialog2 = AlertDialog_builder.getDialog(this, AlertDialog_builder.DIALOG_ACCEPT_REMOVE, mSpinner.getSelectedItem().toString(), new AlertDialog_builder.extra_action() {
						@Override
						public void action() {
							Category name = (Category) mSpinner.getSelectedItem();
							mBookDao.deleteCategory(name);
							mAdapterSpinner.remove(name);
							mAdapterSpinner.notifyDataSetChanged();
							mSpinner.setSelection(0);
						}
					});
					dialog2.show();
				} else {
					AlertDialog dialog1 = AlertDialog_builder.getDialog(this, AlertDialog_builder.DIALOG_ACTION_IMPOSSIBLE, null, null);
					dialog1.show();
				}
				break;

		}

		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ACTIVITY_ID_ADD_BOOK) {
				String author = data.getStringExtra(AddBookActivity.AUTHOR);
				String title = data.getStringExtra(AddBookActivity.TITLE);
				String category = data.getStringExtra(AddBookActivity.CATEGORY);
				Book obj = new Book();
				obj.setTitle(title);
				obj.setAuthor(author);
				obj.setCategory(category);
				obj.setId(data.getLongExtra(AddBookActivity.BOOK_ID, -1));
				//TODO подумать, как можно убрать этот кастыль
				final short CASTIL = -1;
				obj.setRead(data.getShortExtra(AddBookActivity.IS_READ, CASTIL) == 1);
				mBookDao.update_book(obj, data.getLongExtra(AddBookActivity.CATEGORY_ID, Category.AUTO_ID));
				Book elem = (Book) mListView.getSelectedItem();
				elem = obj;
				mAdapterListView.notifyDataSetChanged();
				AlertDialog dialog = AlertDialog_builder.getDialog(this, AlertDialog_builder.DIALOG_SUCCESSFULLY, null, null);
				dialog.show();
			} else if (requestCode == ACTIVITY_ID_ADD_CATEGORY) {
				Category category = new Category(data.getStringExtra(NewListActivity.LIST_NAME));
				category.setId(data.getLongExtra(NewListActivity.LIST_ID, Category.AUTO_ID));
				if (!mBookDao.contain(BookTable.TABLE_CATEGORY, BookTable.COLUMN_CATEGORY, category.getName())) {
					mBookDao.update_category(category);
					AlertDialog dialog = AlertDialog_builder.getDialog(this, AlertDialog_builder.DIALOG_SUCCESSFULLY, null, null);
					dialog.show();
					update_category_list();
					update_all_lists();
				} else {
					AlertDialog dialog = AlertDialog_builder.getDialog(this, AlertDialog_builder.DIALOG_WARNING_CATEGORY_IS_EXIST, category.getName(), null);
					dialog.show();
				}
			} else {
				//Ничего
			}
		} else {
			//Ничего
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_filter_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

//noinspection SimplifiableIfStatement
		switch (id) {
			case R.id.filter_is_read:
				mFilter.is_read = true;
				mFilter.read_filter_on = true;
				break;
			case R.id.filter_is_no_read:
				mFilter.is_read = false;
				mFilter.read_filter_on = true;
				break;
			case R.id.filter_read_default:
				mFilter.read_filter_on = false;
				break;
			case R.id.filter_sort_by_author:
				if (mFilter.ODER_AZ_BY_AUTHOR == mFilter.comparator_author)
					mFilter.comparator_author = mFilter.ODER_ZA_BY_AUTHOR;
				else
					mFilter.comparator_author = mFilter.ODER_AZ_BY_AUTHOR;
				break;
			case R.id.filter_sort_by_title:
				if (mFilter.ODER_AZ_BY_TITLE == mFilter.comparator_title)
					mFilter.comparator_title = mFilter.ODER_ZA_BY_TITLE;
				else
					mFilter.comparator_title = mFilter.ODER_AZ_BY_TITLE;
				break;
			case R.id.filter_no_sort:
				mFilter.comparator_author = null;
				mFilter.comparator_title = null;
				break;
			case R.id.filter_search:
				mSearchDialog.show();
				break;
		}
		update_all_lists();
		return true;
	}

	private void update_category_list() {
		List<Category> list = new ArrayList<>();
		list.add(new Category(getString(R.string.all)));
		list.add(new Category(getString(R.string.uncategory)));
		list.addAll(mBookDao.getAllCategory());
		mAdapterSpinner.clear();
		mAdapterSpinner.addAll(list);
		mAdapterSpinner.notifyDataSetChanged();
	}

	private void update_all_lists() {
		mAdapterListView.clear();
		Category category = (Category) mSpinner.getSelectedItem();
		List<Book> list;
		if (category.toString().equals(getString(R.string.all)))
			list = mBookDao.getAllBooks();
		else {
			if (category.toString().equals(getString(R.string.uncategory)))
				list = mBookDao.getUncategoryList();
			else
				list = mBookDao.getCategoryList(category);
		}
		list = mFilter.filter(list);
		mAdapterListView.addAll(list);
		mAdapterListView.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBookDao.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBookDao.close();
	}

	private class Filter {
		public final Comparator<Book> ODER_AZ_BY_TITLE = new Comparator<Book>() {
			@Override
			public int compare(Book lhs, Book rhs) {
				return lhs.getTitle().compareTo(rhs.getTitle());
			}
		};

		public final Comparator<Book> ODER_ZA_BY_TITLE = new Comparator<Book>() {
			@Override
			public int compare(Book lhs, Book rhs) {
				return rhs.getTitle().compareTo(lhs.getTitle());
			}
		};

		public final Comparator<Book> ODER_AZ_BY_AUTHOR = new Comparator<Book>() {
			@Override
			public int compare(Book lhs, Book rhs) {
				return lhs.getAuthor().compareTo(rhs.getAuthor());
			}
		};

		public final Comparator<Book> ODER_ZA_BY_AUTHOR = new Comparator<Book>() {
			@Override
			public int compare(Book lhs, Book rhs) {
				return rhs.getAuthor().compareTo(lhs.getAuthor());
			}
		};

		public Comparator<Book> comparator_title;
		public Comparator<Book> comparator_author;
		public boolean is_read;
		public boolean read_filter_on;

		Filter() {
			comparator_title = null;
			comparator_author = null;
			is_read = false;
			read_filter_on = false;
		}

		List<Book> filter(List<Book> base) {
			if (comparator_title == null && comparator_author == null && !read_filter_on)
				return base;
			List<Book> answer = new ArrayList<>();
			if (read_filter_on)
				for (Book book : base) {
					if (book.isRead() == is_read)
						answer.add(book);
				}
			else
				answer = base;
			if (comparator_author != null)
				Collections.sort(answer, comparator_author);
			if (comparator_title != null)
				Collections.sort(answer, comparator_title);
			return answer;
		}
	}
}
