package com.example.instantscore.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.instantscore.model.League;
import com.example.instantscore.model.Match;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
	private static DBHelper dbHelper = null;

	private static SQLiteDatabase db = null;

	public static void init(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
			db = dbHelper.getWritableDatabase();
		}
	}

    public static InsertStatus insertMatchIntoDb(Match match, League league) {
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("match", match.getId());
        values.put("date", league.getDate());
        db.insert("matches", null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        return InsertStatus.INSERTED_OK;
    }
	
	public static boolean isAlreadyInDatabase(Match match, League league){
		Cursor cursor = db.rawQuery("select * from matches where match = ? and date = ?", new String[]{match.getId(), league.getDate()});
		return cursor.moveToFirst();
	}

	public static void removeMatchFromDatabase(Match match, League league){
        db.delete("matches", "match = ? and date = ?", new String[]{match.getId(), league.getDate()});
	}

    public static List<String> getAllMatchIds() {
        List<String> matches = new ArrayList<String>();
        Cursor cursor = db.rawQuery("select match from matches", null);

        String id;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(0);
                matches.add(id);
            } while (cursor.moveToNext());
        }

        return matches;
    }

}
