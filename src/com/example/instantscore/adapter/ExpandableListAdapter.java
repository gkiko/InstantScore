package com.example.instantscore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.instantscore.R;
import com.example.instantscore.model.League;
import com.example.instantscore.model.Match;

import java.util.List;

/**
 * Created by gkiko on 6/29/14.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<League> _listDataHeader; // header titles

    public ExpandableListAdapter(Context context, List<League> listDataHeader) {
        this._context = context;
        this._listDataHeader = listDataHeader;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataHeader.get(groupPosition).getMatches().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataHeader.get(groupPosition).getMatches().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        League header = (League) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_header_new, null);
        }

        TextView leagueListHeader = (TextView) convertView.findViewById(R.id.header);
        TextView leagueListDate = (TextView) convertView.findViewById(R.id.date);

        leagueListHeader.setText(header.getName());
        leagueListDate.setText(header.getDate());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Match child = (Match) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_new, null);
        }

        TextView childTime = (TextView) convertView.findViewById(R.id.time);
        TextView childTeam1 = (TextView) convertView.findViewById(R.id.team1);
        TextView childScore = (TextView) convertView.findViewById(R.id.score);
        TextView childTeam2 = (TextView) convertView.findViewById(R.id.team2);

        childTime.setText(child.getTime());
        childTeam1.setText(child.getTeam1());
        childScore.setText(child.getScore());
        childTeam2.setText(child.getTeam2());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
