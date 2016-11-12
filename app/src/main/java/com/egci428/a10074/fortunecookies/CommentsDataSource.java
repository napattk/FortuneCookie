package com.egci428.a10074.fortunecookies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 6272user on 10/21/2016 AD.
 */
public class CommentsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_COMMENT,MySQLiteHelper.COLUMN_DATE};


    public CommentsDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void  open() throws SQLException {
        database = dbHelper.getWritableDatabase();// call everytime you want to connect to DB close after
    }

    public void close() throws SQLException {
        dbHelper.close();
    }

    public Comment createComment(int comment, String date){
        ContentValues values = new ContentValues();//
        values.put(MySQLiteHelper.COLUMN_COMMENT,comment);
        values.put(MySQLiteHelper.COLUMN_DATE,date);
        System.out.println("INSERTED: "+comment+"DATE: "+date);
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS,null,values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,allColumns,MySQLiteHelper.COLUMN_ID+" = "+insertId,null,null,null,null);

        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;

    }

    public void deleteComment(Comment comment){
        String id = comment.getPositionID();
        System.out.println("Comment deleted with id: "+id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID+" = "+id,null);
    }

    public List<Comment> getAllComments(){
        List<Comment> comments = new ArrayList<Comment>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns, null, null, null, null, null); // query with no filtering

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            System.out.println("CURSOR POSITION: "+ cursor.getPosition());
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();

        }
        cursor.close();
        return comments;

    }

    private Comment cursorToComment(Cursor cursor){
        Comment comment = new Comment();

        comment.setPositionID(cursor.getString(0));
        comment.setCookieID(cursor.getInt(1));
        comment.setDate(cursor.getString(2));
        return comment;
    }

}
