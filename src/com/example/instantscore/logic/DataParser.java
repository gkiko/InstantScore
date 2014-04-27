package com.example.instantscore.logic;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.example.instantscore.model.Game;

public class DataParser {
	@SuppressWarnings("rawtypes")
	public static HashMap[] parseData(String data){
		HashMap<String, ArrayList<Game>> map = null;
		HashMap<String, Integer> priorityMap = null;
		try {
			map = getHashMapFromScoresFile(data);
			priorityMap = getTournamentPriorities(data);
			return new HashMap[]{map, priorityMap};
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static HashMap<String, Integer> getTournamentPriorities(String data) throws IOException {
		InputStream is = new ByteArrayInputStream(data.getBytes());
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		int numTourn = Integer.parseInt(rd.readLine());
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i=0; i<numTourn; i++){
			StringTokenizer st = new StringTokenizer(rd.readLine());
			int numMatches = Integer.parseInt(st.nextToken());
			String tournamentName = rd.readLine();
			map.put(tournamentName, i+1);
			for(int j=0; j<numMatches; j++){
				for(int k=0; k<6; k++){
					rd.readLine();
				}
			}
		}
		rd.close();
		return map;
	}
	
	public static HashMap<String, ArrayList<Game>> getHashMapFromScoresFile(String data) throws IOException {
		InputStream is = new ByteArrayInputStream(data.getBytes());
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		int numTourn = Integer.parseInt(rd.readLine());
		HashMap<String, ArrayList<Game>> map = new HashMap<String, ArrayList<Game>>();
		for(int i=0; i<numTourn; i++){
			StringTokenizer st = new StringTokenizer(rd.readLine());
			int numMatches = Integer.parseInt(st.nextToken());
			String tournamentName = rd.readLine();
			map.put(tournamentName, new ArrayList<Game>());
			ArrayList<Game> gamesList = map.get(tournamentName);
			for(int j=0; j<numMatches; j++){
				rd.readLine(); // unnecessary line
				String date = rd.readLine();
				String time = rd.readLine();
				String home = rd.readLine();
				String score = rd.readLine();
				String away = rd.readLine();
				Game game = new Game();
				gamesList.add(game);
				game.setAwayTeam(away);
				game.setAwayTeamScore(getSecondScore(score));
				game.setDate(date);
				game.setHomeTeam(home);
				game.setHomeTeamScore(getFirstScore(score));
				game.setTime(time);
				game.setTournament(tournamentName);
				//game.setGameId(id);
			}
		}
		rd.close();
		return map;
	}
	
	public static String getFirstScore(String score){
		if(score.indexOf("?")!=-1){
			return "?";
		}
		int index = score.indexOf(":");
		return removeSpacesBothSides(score.substring(0, index));
	}
	
	public static String getSecondScore(String score){
		if(score.indexOf("?")!=-1){
			return "?";
		}
		int index = score.indexOf(":");
		return removeSpacesBothSides(score.substring(index+1));
	}
	
	private static String removeSpacesBothSides(String S){
		int min = -1, max = S.length();
		for(int i=0; i<S.length(); i++){
			if(S.charAt(i)==' '){
				min = i;
			}
			else break;
		}
		for(int i=S.length()-1; i>=0; i--){
			if(S.charAt(i)==' '){
				max = i;
			}
			else break;
		}
		if(min>max) return "";
		return S.substring(min+1, max);
	}
}
