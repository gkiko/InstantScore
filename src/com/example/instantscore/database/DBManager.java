package com.example.instantscore.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.instantscore.model.Game;

public class DBManager {
	private static DBHelper dbHelper = null;

	private static SQLiteDatabase db = null;

	public static void init(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
			db = dbHelper.getWritableDatabase();
		}
	}

	// create table matches (tournament varchar, date varchar, " +
	// "time varchar, home_team varchar, away_team varchar, home_score varchar, away_score varchar)"

	public static void insertMatchIntoDatabase(Game game) {
		String tournament = game.getTournament(), time = game.getTime(), homeScore = game
				.getHomeTeamScore(), awayScore = game.getAwayTeamScore(), homeTeam = game
				.getHomeTeam(), awayTeam = game.getAwayTeam(), date = game
				.getDate();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("tournament", tournament);
		values.put("time", time);
		values.put("date", date);
		values.put("home_team", homeTeam);
		values.put("away_team", awayTeam);
		values.put("home_score", homeScore);
		values.put("away_score", awayScore);
		db.insert("matches", null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public static void insert(String word, int freq) {
		db.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("word", word);
		values.put("frequency", "" + freq);
		db.insert("statistics", null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public static Cursor getCursorOfWordsAndFrequencies() {
		return db.rawQuery("select * from statistics", null);
	}

	public static void dropTable(String tableName) {
		db.execSQL("drop table " + tableName);
	}

	// tourn, date, time, home, away, homesc, awaysc
	public static List<Game> getAllMatches() {
		List<Game> matches = new ArrayList<Game>();
		Cursor cursor = db.rawQuery("select * from matches", null);

		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				Game game = new Game();
				game.setTournament(cursor.getString(0));
				game.setDate(cursor.getString(1));
				game.setTime(cursor.getString(2));
				game.setHomeTeam(cursor.getString(3));
				game.setAwayTeam(cursor.getString(4));
				game.setHomeTeamScore(cursor.getString(5));
				game.setAwayTeamScore(cursor.getString(6));

				matches.add(game);
			}
		}

		return matches;
	}

	public static void updateFrequencyDatabase(String word) {
		Cursor cursor = db.rawQuery(
				"select frequency from statistics where word = '" + word + "'",
				null);
		cursor.moveToFirst();
		String freq = cursor.getString(0);
		int newFreq = 1 + Integer.parseInt(freq);
		db.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("word", word);
		values.put("frequency", "" + newFreq);
		db.update("statistics", values, "word = ?", new String[] { word });
		db.setTransactionSuccessful();
		db.endTransaction();
	}

}
