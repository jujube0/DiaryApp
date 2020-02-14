package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=1;
    public DBHelper(Context context){
        super(context, "commentdb", null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String memoSQL = "create table tb_comment " +
                "(_id integer primary key autoincrement,"+
                "name,"+
                "comment," +
                "parent integer," +
                "deleted integer default 0)";

        db.execSQL(memoSQL);

        db.execSQL("insert into tb_comment (name, comment) values (?,?)", new String[]{"사용자1","안녕"});
        /*db.execSQL("insert into tb_comment (id, name, comment) values (?,?,?)", new String[]{"2","사용자2","뭐야?"});
        db.execSQL("insert into tb_comment (id, name, comment, parent) values (?,?,?,?)", new String[]{"3","사용자3","무슨 말을 해야하지","1"});
*/


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int ondVersion, int newVersion){
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_comment");
            onCreate(db);
        }
    }

}
