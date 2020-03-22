package com.brauma.withinyourmeans.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.brauma.withinyourmeans.R;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public Integer[] icons;

    public ImageAdapter(Context c, Integer[] icons) {
        mContext = c;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.icon_item, null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.icon_grid_item);
            imageView.setImageResource(icons[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
