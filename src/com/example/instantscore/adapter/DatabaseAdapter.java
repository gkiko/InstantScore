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
import com.example.instantscore.utils.Utils;

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
		for(Game g : DBManager.getAllMatches()){
			if(!ls.contains(g)){
				ls.add(g);
			}
		}
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
		View v;
		Container cont;
		if(arg1 == null){
			v = View.inflate(c, R.layout.list_item, null);
			cont = new Container();
            cont.date = (TextView)v.findViewById(R.id.date);
			cont.homeTeam = (TextView)v.findViewById(R.id.homeTeam);
			cont.awayTeam = (TextView)v.findViewById(R.id.awayTeam);
			v.setTag(cont);
		}else{
			v = arg1;
			cont = (Container) v.getTag();
		}

        (cont.date).setText(ls.get(arg0).getDate());
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
		TextView homeTeam, awayTeam, date;
	}
	
	public void setSelected(int position, boolean selected){
		ls.get(position).setSelected(selected);
        this.notifyDataSetChanged();
	}
	
	public void deleteSelected(){
		Game g;
		for(int i=0;i<ls.size();i++){
			g = ls.get(i);
			if(g.isSelected()){
				ls.remove(i);
				DBManager.removeMatchFromDatabase(g);
                i--;
            }
		}
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
	
	public void unselectAll(){
		for(int i=0;i<ls.size();i++){
			setSelected(i, false);
		}
	}
	
}
