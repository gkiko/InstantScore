package com.example.instantscore.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.instantscore.layout.ItemView;
import com.example.instantscore.model.Item;

import java.util.List;

/**
 * Created by gkiko on 8/25/14.
 */
public class ItemAdapter extends BaseAdapter {
    List<Item> items;
    public ItemAdapter(Context c, List<Item> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView itemView = (ItemView)convertView;
        if (null == itemView) {
            itemView = ItemView.inflate(parent);
        }
        itemView.setItem((Item)getItem(position));
        return itemView;
    }
}
