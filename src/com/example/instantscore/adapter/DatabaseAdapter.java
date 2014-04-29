package com.example.instantscore.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.instantscore.R;
import com.example.instantscore.database.DBManager;
import com.example.instantscore.model.Game;

public class DatabaseAdapter extends BaseAdapter implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Game> ls;
	private Context c;
	
	public DatabaseAdapter(Context context){
		c = context;
		DBManager.init(c);
		ls = DBManager.getAllMatches();
	}

	@Override
	public void notifyDataSetChanged() {
		ls = DBManager.getAllMatches();
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return ls.size();
	}

	@Override
	public Object getItem(int arg0) {
		return ls.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = null;
		Container cont;
		if(arg1 == null){
			v = View.inflate(c, R.layout.list_item_tab_2, null);
			cont = new Container();
			cont.homeTeam = (TextView)v.findViewById(R.id.homeTeam);
			cont.awayTeam = (TextView)v.findViewById(R.id.awayTeam);
			v.setTag(cont);
		}else{
			v = arg1;
			cont = (Container) v.getTag();
		}
		
		(cont.homeTeam).setText(ls.get(arg0).getHomeTeam());
		(cont.awayTeam).setText(ls.get(arg0).getAwayTeam());
		if(ls.get(arg0).isSelected()){
			v.setBackgroundColor(c.getResources().getColor(android.R.color.holo_blue_dark));
		}else{
			v.setBackgroundColor(c.getResources().getColor(android.R.color.transparent));
		}
		return v;
	}
	
	private class Container{
		TextView homeTeam, awayTeam;
	}
	
	public void setSelected(int position, boolean selected){
		ls.get(position).setSelected(selected);
		this.notifyDataSetChanged();
	}
	
	public void add(Game g){
		ls.add(g);
		DBManager.insertMatchIntoDatabase(g);
		this.notifyDataSetChanged();
	}
	
	public void remove(Game g){
		ls.remove(g);
		this.notifyDataSetChanged();
	}

	public List<Game> getSelectedGames() {
	//	return ls;
		return getSelectedGamesFromDatabase();
	}
	
	public List<Game> getSelectedGamesFromDatabase(){
		return DBManager.getAllMatches();
	}
	
	//debug
	public Game getGame(int i){
		return ls.get(0);
	}

}
