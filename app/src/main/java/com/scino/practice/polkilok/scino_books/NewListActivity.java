package com.scino.practice.polkilok.scino_books;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.scino.practice.polkilok.scino_books.model.Category;

public class NewListActivity extends AppCompatActivity {

	public static final String LIST_NAME = "com.scino.practice.polkilok.scino_books.list_name";
	public static final String LIST_ID = "com.scino.practice.polkilok.scino_books.list_id";
	private EditText mName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_list);
		mName = (EditText) findViewById(R.id.EDTtitle_list);
		mName.setText(getIntent().getStringExtra(LIST_NAME));
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

	public void onClick_save_new_list(View view) {
		Intent buf = new Intent();
		buf.putExtra(NewListActivity.LIST_NAME, mName.getText().toString());
		buf.putExtra(NewListActivity.LIST_ID, getIntent().getLongExtra(NewListActivity.LIST_ID, Category.AUTO_ID));
		setResult(RESULT_OK, buf);
		finish();
	}

}