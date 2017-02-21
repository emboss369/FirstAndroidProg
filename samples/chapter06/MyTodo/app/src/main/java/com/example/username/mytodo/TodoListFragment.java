package com.example.username.mytodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;

public class TodoListFragment extends Fragment {
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";

    private long mCategoryId;
    private String mCategoryName;

    public TodoListFragment() {
        // Required empty public constructor
    }

    public static TodoListFragment newInstance(long categoryId,
                                               String categoryName) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        args.putLong(CATEGORY_ID, categoryId);
        args.putString(CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryId = getArguments().getLong(CATEGORY_ID);
            mCategoryName = getArguments().getString(CATEGORY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // このフラグメントのレイアウトを生成します
        View view =
                inflater.inflate(R.layout.fragment_todo_list, container, false);

        // Realmインスタンスを取得します
        Realm realm = Realm.getDefaultInstance();
        // Realmにクエリを発行しカテゴリIDに紐づくTodoリストを取り出します。
        RealmResults<Todo> todoList = realm.where(Todo.class)
                .equalTo("categoryId", mCategoryId)
                .findAllSorted("done");
        // クエリはfindAllSortedでソートすることができます。
        TodoAdapter adapter = new TodoAdapter(getActivity(), todoList);

        ListView listView = (ListView) view.findViewById(R.id.todoList);
        listView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(mCategoryName);
    }
    public long getCategoryId() {
        return mCategoryId;
    }

}
