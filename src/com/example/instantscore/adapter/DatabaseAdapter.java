package com.example.instantscore.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.instantscore.R;
import com.example.instantscore.database.DBManager;

import java.io.Serializable;
import java.util.List;

public class DatabaseAdapter extends BaseAdapter implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<String> ls;
	private Context c;
	
	public DatabaseAdapter(Context context){
		c = context;
		DBManager.init(c);
		ls = DBManager.getAllMatchIds();
	}

	@Override
	public void notifyDataSetChanged() {
        ls = DBManager.getAllMatchIds();
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
			v = View.inflate(c, R.layout.list_item_new_tab3, null);
			cont = new Container();
            cont.matchId = (TextView)v.findViewById(R.id.match_id);
			v.setTag(cont);
		}else{
			v = arg1;
			cont = (Container) v.getTag();
		}

        (cont.matchId).setText(ls.get(arg0));
		return v;
	}
	
	private class Container{
		TextView matchId;
	}
}
