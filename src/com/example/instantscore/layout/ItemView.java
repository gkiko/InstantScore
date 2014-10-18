package com.example.instantscore.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.instantscore.R;
import com.example.instantscore.model.Item;

/**
 * Created by gkiko on 8/25/14.
 */
public class ItemView extends RelativeLayout {
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private ImageView mImageView;

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.country_item_view_child, this, true);
        setupChildren();
    }

    public static ItemView inflate(ViewGroup parent) {
        return (ItemView) LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item_view, parent, false);
    }

    private void setupChildren() {
        mTitleTextView = (TextView) findViewById(R.id.item_titleTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.item_descriptionTextView);
        mImageView = (ImageView) findViewById(R.id.item_imageView);
    }

    public void setItem(Item item) {
        mTitleTextView.setText(item.getCountryName());
        mDescriptionTextView.setText(item.getCountryPrefix());
        mImageView.setImageResource(item.getImgId());
    }
}
