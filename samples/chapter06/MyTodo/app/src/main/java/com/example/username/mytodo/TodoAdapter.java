package com.example.username.mytodo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

/**
 * Created by user.name on 2017/01/25.
 */

public class TodoAdapter extends RealmBaseAdapter<Todo> {
    private static class ViewHolder {
        TextView title;
        ImageView done;
        ImageView deleteLine;
    }

    public TodoAdapter(@NonNull Context context,
                       @Nullable OrderedRealmCollection<Todo> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_cell, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.done = (ImageView) convertView.findViewById(R.id.done);
            viewHolder.deleteLine = (ImageView) convertView.findViewById(R.id.deleteLine);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Todo item = adapterData.get(position);
        viewHolder.title.setText(item.getTitle());
        if (item.isDone()) {
            // 完了済みのTodoであれば×マークおよび取り消し線
            viewHolder.done.setImageResource(R.drawable.ic_close);
            viewHolder.deleteLine.setVisibility(View.VISIBLE);
        } else {
            // 未完了のTodoであればチェックマークおよび取り消し線は非表示
            viewHolder.done.setImageResource(R.drawable.ic_check);
            viewHolder.deleteLine.setVisibility(View.INVISIBLE);
        }
        viewHolder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Todo item = adapterData.get(position);
                if (item.isDone()) {
                    item.deleteFromRealm();
                } else {
                    item.setDone(true);
                }
                realm.commitTransaction();
                realm.close();

            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Todo item = adapterData.get(position);
                item.setDone(false);
                realm.commitTransaction();
                realm.close();

                return true;
            }
        });

        return convertView;
    }

}
