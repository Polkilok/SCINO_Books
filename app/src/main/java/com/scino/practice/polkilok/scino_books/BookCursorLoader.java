package com.scino.practice.polkilok.scino_books;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.scino.practice.polkilok.scino_books.dao.BookDao;

/**
 * Created by Name on 10/08/15.
 */
public class BookCursorLoader extends CursorLoader {

    private BookDao mBookDao;

    public BookCursorLoader(Context context, BookDao dao) {
        super(context);
        mBookDao = dao;
    }

    @Override
    public Cursor loadInBackground() {
        return mBookDao.getAllBooksCursor();
    }
}
