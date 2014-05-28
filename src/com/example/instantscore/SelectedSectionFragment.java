package com.example.instantscore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instantscore.adapter.DatabaseAdapter;

public class SelectedSectionFragment extends Fragment {
    private DatabaseAdapter selectedGameListAdapter;
	private transient Context activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_section_launchpad,container, false);

        ListView gamesListView = (ListView) rootView.findViewById(R.id.list1);
		selectedGameListAdapter = new DatabaseAdapter(activity);
		gamesListView.setAdapter(selectedGameListAdapter);

		gamesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		gamesListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            private int selectedGameCounter = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Here you can do something when items are selected/unselected,
                // such as update the title in the CAB
                Toast.makeText(activity, position + " " + checked, Toast.LENGTH_SHORT).show();
                selectedGameListAdapter.setSelected(position, checked);

                selectedGameCounter += checked ? 1 : -1;
                mode.setTitle(getResources().getQuantityString(R.plurals.matches, selectedGameCounter, selectedGameCounter));
            }


            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        selectedGameListAdapter.deleteSelected();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
                selectedGameListAdapter.unselectAll();
                selectedGameCounter = 0;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });
		
		return rootView;
	}
	
	public void updateList(){
//		Cart.addAll(DBManager.getAllMatches());
		selectedGameListAdapter.notifyDataSetChanged();
	}

}
