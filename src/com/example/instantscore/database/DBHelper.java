package com.example.instantscore.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "InstantScoreDatabase";

	private static final int DATABASE_VERSION = 13;

	public static final String CREATE_MATCHES_TABLE_COMMAND = "create table matches (tournament varchar, date varchar, " +
			"time varchar, home_team varchar, away_team varchar, home_score varchar, away_score varchar)";
	
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