package com.example.test2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.instantscore.adapter.ListAdapter;
import com.example.instantscore.database.DBManager;
import com.example.instantscore.model.Cart;

public class SelectedSectionFragment extends Fragment {
	private ListView gamesListView;
	private ListAdapter selectedGameListAdapter;
	private transient Context c;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_section_launchpad,container, false);
		gamesListView = (ListView) rootView.findViewById(R.id.list1);
		c = getActivity().getApplicationContext();

		selectedGameListAdapter = new ListAdapter(Cart.getCart(), c);
		gamesListView.setAdapter(selectedGameListAdapter);
		
		return rootView;
	}
	
	public void updateCart(){
		Cart.addAll(DBManager.getAllMatches());
		selectedGameListAdapter.notifyDataSetChanged();
	}

}
