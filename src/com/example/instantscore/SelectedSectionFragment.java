package com.example.instantscore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.instantscore.adapter.DatabaseAdapter;

public class SelectedSectionFragment extends Fragment {
    private DatabaseAdapter selectedGameListAdapter;
	private transient Context activity;
    private RelativeLayout backgroundWarning;
    private TextView textWarning;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_section_launchpad_old,container, false);

        backgroundWarning = (RelativeLayout) rootView.findViewById(R.id.layout_warning);
        textWarning = (TextView) backgroundWarning.findViewById(R.id.message_text);
        selectedGameListAdapter = new DatabaseAdapter(activity);

        ListView gamesListView = (ListView) rootView.findViewById(R.id.list);
		gamesListView.setAdapter(selectedGameListAdapter);

        gamesListView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(selectedGameListAdapter.getCount()==0){
                    setListBackground();
                }else{
                    removeListBackground();
                }
            }
        });
		
		return rootView;
	}
	
	public void updateList(){
		selectedGameListAdapter.notifyDataSetChanged();
	}

    void setListBackground() {
        textWarning.setText(getResources().getString(R.string.warning_background_empty_list));
        textWarning.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_info_details, 0,0,0);
        backgroundWarning.setVisibility(RelativeLayout.VISIBLE);
    }

    void removeListBackground() {
        backgroundWarning.setVisibility(RelativeLayout.GONE);
    }


}
