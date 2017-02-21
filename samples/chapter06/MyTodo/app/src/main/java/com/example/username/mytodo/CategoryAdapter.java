package com.example.username.mytodo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by user.name on 2017/01/23.
 */

public class CategoryAdapter extends RealmBaseAdapter<Category> {

    private static class ViewHolder {
        TextView category;
    }

    public CategoryAdapter(@NonNull Context context, @Nullable
            OrderedRealmCollection<Category> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_cell, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.category
                    = (TextView) convertView.findViewById(R.id.category);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Category item = this.adapterData.get(position);
        viewHolder.category.setText(item.getCategory());

        return convertView;

    }
}
