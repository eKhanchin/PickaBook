package com.hitorus.pick_a_book;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;


public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "booksDB.db";
    public static final String TABLE_NAME = "Book";
    public static final String COLUMN_NAME = "BookName";
    public static final String COLUMN_AUTHOR = "AuthorName";
    public static final String COLUMN_STATUS = "Status";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_NAME +
                " TEXT," + COLUMN_AUTHOR + " TEXT," + COLUMN_STATUS + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_NAME + "," + COLUMN_AUTHOR + "))";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
        onCreate(db);
    }

    public ArrayList<Book> loadBooks()
    {
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ArrayList<Book> books = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            String result_0 = cursor.getString(0);
            String result_1 = cursor.getString(1);
            String result_2 = cursor.getString(2);

            Book book = new Book(result_0, result_1, result_2);
            books.add(book);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return books;
    }

    public void addBook(Book book)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, book.getName().replace("'","''"));
        values.put(COLUMN_AUTHOR, book.getAuthor().replace("'","''"));
        values.put(COLUMN_STATUS, String.valueOf(book.getStatus()));

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Book findBook(String name, String author)
    {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = '"
                + name.replace("'","''") + "' AND " + COLUMN_AUTHOR + " = '"
                + author.replace("'","''") + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Book book = new Book();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            book.setName(cursor.getString(0));
            book.setAuthor(cursor.getString(1));
            book.setStatus(Book.Status.valueOf(cursor.getString(2)));
            cursor.close();
        } else {
            book = null;
        }

        db.close();

        return book;
    }

    public boolean deleteBook(String name, String author)
    {
        boolean result = false;

        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = '"
                + name.replace("'","''") + "' AND " + COLUMN_AUTHOR + " = '"
                + author.replace("'","''") + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Book book = new Book();

        if (cursor.moveToFirst()) {

            book.setName(cursor.getString(0));
            book.setAuthor(cursor.getString(1));

            db.delete(TABLE_NAME, COLUMN_NAME + "=?" + " AND " + COLUMN_AUTHOR + "=?",
                    new String[] { name, author });
            cursor.close();
            result = true;
        }

        db.close();

        return result;
    }

    public boolean updateBook(Book book, String[] newValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();

        String original_name = book.getName().replace("'","''");
        String original_author = book.getAuthor().replace("'","''");
        String original_status = book.getStatus().toString();

        if (newValues[0].equals("")) {
            args.put(COLUMN_NAME, newValues[0].replace("'","''"));
        }
        else {
            args.put(COLUMN_NAME, original_name);
        }

        if (newValues[1].equals("")) {
            args.put(COLUMN_AUTHOR, newValues[1].replace("'","''"));
        }
        else {
            args.put(COLUMN_AUTHOR, original_author);
        }

        if (newValues[2].equals("")) {
            args.put(COLUMN_STATUS, newValues[2]);
        }
        else {
            args.put(COLUMN_STATUS, original_status);
        }

        return db.update(TABLE_NAME, args, COLUMN_NAME + "=?" + " AND " + COLUMN_AUTHOR
                + "=?", new String[] { original_name, original_author }) > 0;
    }
}

