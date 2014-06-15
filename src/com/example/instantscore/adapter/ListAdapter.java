package com.example.instantscore.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.instantscore.R;
import com.example.instantscore.model.Game;
import com.example.instantscore.utils.Utils;

public class ListAdapter extends BaseAdapter implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Game> ls;
	private Context c;
	
	public ListAdapter(List<Game> list, Context context){
		ls = list;
		c = context;
	}

	public void notifyDataSetChanged(){
		this.notifyDataSetInvalidated();
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
			v = View.inflate(c, R.layout.list_item, null);
			cont = new Container();
			cont.date = (TextView)v.findViewById(R.id.date);
			cont.time = (TextView)v.findViewById(R.id.time);
			cont.homeTeam = (TextView)v.findViewById(R.id.homeTeam);
			cont.awayTeam = (TextView)v.findViewById(R.id.awayTeam);
			cont.homeTeamScore = (TextView)v.findViewById(R.id.homeTeamScore);
			cont.awayTeamScore = (TextView)v.findViewById(R.id.awayTeamScore);
			v.setTag(cont);
		}else{
			v = arg1;
			cont = (Container) v.getTag();
		}
		
		(cont.homeTeam).setText(ls.get(arg0).getHomeTeam());
		(cont.awayTeam).setText(ls.get(arg0).getAwayTeam());
		(cont.date).setText(ls.get(arg0).getDate());
		(cont.time).setText(ls.get(arg0).getTime());
		(cont.homeTeamScore).setText(ls.get(arg0).getHomeTeamScore());
		(cont.awayTeamScore).setText(ls.get(arg0).getAwayTeamScore());
		if(ls.get(arg0).selected()){
			v.setBackgroundColor(c.getResources().getColor(android.R.color.holo_blue_light));
		}else{
			v.setBackgroundColor(c.getResources().getColor(android.R.color.transparent));
		}
		return v;
	}
	
	private class Container{
		TextView homeTeam, awayTeam, homeTeamScore, awayTeamScore, date, time;
	}
	
	public void add(Game g){
		ls.add(g);
		this.notifyDataSetChanged();
	}
	
	public void remove(Game g){
		ls.remove(g);
		this.notifyDataSetChanged();
	}

	public List<Game> getSelectedGames() {
		return ls;
	}
	
	//debug
	public Game getGame(int i){
		return ls.get(0);
	}

}
