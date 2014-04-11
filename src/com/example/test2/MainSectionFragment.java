package com.example.test2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instantscore.adapter.ListAdapter;
import com.example.instantscore.adapter.SeparatedListAdapter;
import com.example.instantscore.communication.DataFetcher;
import com.example.instantscore.communication.DataSender;
import com.example.instantscore.database.DBManager;
import com.example.instantscore.database.InsertStatus;
import com.example.instantscore.listener.CallbackListener;
import com.example.instantscore.listener.MyChangeEvent;
import com.example.instantscore.logic.DataParser;
import com.example.instantscore.model.Cart;
import com.example.instantscore.model.Game;

public class MainSectionFragment extends Fragment implements CallbackListener {
	private ListView gamesListView;
	private DataFetcher fetcher;
	private transient Context c;
	private SeparatedListAdapter separatedListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
		gamesListView = (ListView) rootView.findViewById(R.id.list1);
		c = getActivity().getApplicationContext();
		DBManager.init(c);

		gamesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
				Game item = (Game) separatedListAdapter.getItem(position);
				

//				if (item.isSelectable()) {
//					if (!item.isSelected()) {
//						InsertStatus insertStatus = DBManager.insertMatchIntoDatabase(item);
//						if(insertStatus!=InsertStatus.INSERTED_OK){
//							String text = (insertStatus==InsertStatus.ALREADY_EXISTS ? "The match is already selected" : 
//								"You can't choose more matches today because you've already reached your daily maximum");
//							Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//						}
//					} else {
//						Toast.makeText(getActivity().getApplicationContext(), "You can't subscribe to this match because it " +
//								"has already finished ", Toast.LENGTH_SHORT).show();
//					}
//					
//					
//					
//					item.setSelected();
//					separatedListAdapter.notifyDataSetChanged();
//				}
//			}
				if (!item.isSelected()) {
					if(!item.isSelectable()){
						Toast.makeText(getActivity().getApplicationContext(), "You can't subscribe to this match because it " +
								"has already finished ", Toast.LENGTH_SHORT).show();
					}
					else{
						InsertStatus insertStatus = DBManager.insertMatchIntoDatabase(item);
						if(insertStatus!=InsertStatus.INSERTED_OK){
							String text = (insertStatus==InsertStatus.ALREADY_EXISTS ? "The match is already selected" : 
								"You can't choose more matches today because you've already reached your daily maximum");
							Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
						}
						else{
							Toast.makeText(getActivity().getApplicationContext(), "The match has been chosen", Toast.LENGTH_SHORT).show();
						}
					}
				}
				item.setSelected();
				separatedListAdapter.notifyDataSetChanged();
			}
				
		});

		fetchList();

		return rootView;
	}

	@Override
	public void onUpdate(MyChangeEvent evt) {
		fetcher.removeMyChangeListener(this);
		String data = (String) evt.source;
		HashMap<String, ArrayList<Game>> map = DataParser.parseData(data);
		DBManager.removeOldMatchesFrom(map);
		fillListAdapter(map);
	}

	private void fillListAdapter(HashMap<String, ArrayList<Game>> map) {
		Iterator<String> it = map.keySet().iterator();
		separatedListAdapter = new SeparatedListAdapter(c);
		while (it.hasNext()) {
			String t = it.next();
			ArrayList<Game> list = map.get(t);
			ListAdapter adapter1 = new ListAdapter(list, c);
			separatedListAdapter.addSection(t, adapter1);
		}

		gamesListView.setAdapter(separatedListAdapter);
		separatedListAdapter.notifyAllAdaptersDataSetChanged();
	}

	void fetchList() {
		android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
		fetcher = new DataFetcher(manager);
		fetcher.addMyChangeListener(this);
		fetcher.execute((Void) null);
	}
	
	@SuppressWarnings("unchecked")
	void submitGames() {
		List<Game> games = Cart.getCart();
		StringBuilder sb = new StringBuilder();
		// TODO: insert real phone number
		sb.append("fake mobile");
		for(Game game : games){
			sb.append("[").append(game.getGameId()).append("]");
		}
		Toast.makeText(c, sb.toString(), Toast.LENGTH_SHORT).show();
		
		DataSender sender = new DataSender();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("data", sb.toString()));
		sender.execute(nameValuePairs);
	}

}
