package com.scino.practice.polkilok.scino_books.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.scino.practice.polkilok.scino_books.db.BookTable;

/**
 * Created by Name on 10/08/15.
 */
public class BookAdapter extends CursorAdapter {

    public BookAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView bookName = (TextView) view.findViewById(android.R.id.text1);

        String title = cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_TITLE));
        //String author = cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_AUTHOR));
        //String category = cursor.getString(cursor.getColumnIndex(BookTable.COLUMN_CATEGORY_PTR));
        //bookName.setText(title + ' ' + author);
		bookName.setText(title);
    }
}
