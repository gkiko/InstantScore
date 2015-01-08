package com.kuku.instantscore.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "InstantScoreDatabase";

	private static final int DATABASE_VERSION = 19;

	public static final String CREATE_MATCHES_TABLE_COMMAND = "create table matches (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
            ", match varchar, date int)";
	
	public static final String DROP_MATCHES_TABLE = "drop table if exists matches";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MATCHES_TABLE_COMMAND);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropMatchesTable(db);
		onCreate(db);
	}
	
	public static void dropMatchesTable(SQLiteDatabase db){
		db.execSQL(DROP_MATCHES_TABLE);
	}
}