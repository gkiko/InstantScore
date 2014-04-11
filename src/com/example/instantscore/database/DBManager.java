package com.example.instantscore.database;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.instantscore.model.Game;

public class DBManager {

	private static final int MAX_NUM_SELECTED_MATCHES_PER_DAY = 10;
		
	private static DBHelper dbHelper = null;

	private static SQLiteDatabase db = null;

	public static void init(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
			db = dbHelper.getWritableDatabase();
//			Game g = new Game();
//			g.setHomeTeam("A");
//			g.setAwayTeam("B");
//			g.setHomeTeamScore("12");
//			g.setAwayTeamScore("11");
//			g.setDate("April 1");
//			g.setTournament("yleebis tasi");
//			g.setTime("59");
//			insertMatchIntoDatabase(g);
		}
	}

	// create table matches (tournament varchar, date varchar, " +
	// "time varchar, home_team varchar, away_team varchar, home_score varchar, away_score varchar)"

	public static InsertStatus insertMatchIntoDatabase(Game game) {
		if(isAlreadyInDatabase(game)) return InsertStatus.ALREADY_EXISTS;
		if(!isSpaceForExtraMatch()) return InsertStatus.TOO_MANY_MATCHES;
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
		return InsertStatus.INSERTED_OK;
	}
	
	private static boolean isAlreadyInDatabase(Game game){
		Cursor cursor = db.rawQuery("select * from matches where home_team = '"+game.getHomeTeam()+"' and away_team = '"+
				game.getAwayTeam()+"'", null);
		return cursor.moveToFirst();
	}
	
	public static void removeOldMatchesFrom(HashMap<String, ArrayList<Game>> activeMatches){
		HashSet<String> gameIdsSet = new HashSet<String>();
		for(ArrayList<Game> listGames : activeMatches.values()){
			for(Game game : listGames){
				gameIdsSet.add(game.getGameId());
			}
		}
		
		List<Game> gamesFromDb = getAllMatches();
		for(Game game : gamesFromDb){
			String gameId = game.getGameId();
			if(gameIdsSet.contains(gameId)){
				continue; // everything OK
			}
			// otherwise remove it from database
			removeGameFromDatabase(game);
		}
	}
	
	private static boolean isSpaceForExtraMatch(){
		return getAllMatches().size() < MAX_NUM_SELECTED_MATCHES_PER_DAY;
	}
	
	public static void removeGameFromDatabase(Game game){
		db.execSQL("delete from matches where home_team = '"+game.getHomeTeam()+"' and away_team = '"+game.getAwayTeam()+"'");
	}

	public static void dropTable(String tableName) {
		db.execSQL("drop table " + tableName);
	}

	public static List<Game> getAllMatches() {
		List<Game> matches = new ArrayList<Game>();
		Cursor cursor = db.rawQuery("select * from matches", null);

		if (cursor.moveToFirst()) {
			Game game = new Game();
			game.setTournament(cursor.getString(0));
			game.setDate(cursor.getString(1));
			game.setTime(cursor.getString(2));
			game.setHomeTeam(cursor.getString(3));
			game.setAwayTeam(cursor.getString(4));
			game.setHomeTeamScore(cursor.getString(5));
			game.setAwayTeamScore(cursor.getString(6));

			matches.add(game);
			while (cursor.moveToNext()) {
				game = new Game();
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
