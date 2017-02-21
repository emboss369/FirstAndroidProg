package com.example.username.mytodo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Realm mRealm;

    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        void onCategorySelected(long categoryId, String categoryName);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mRealm = Realm.getDefaultInstance();

        RealmResults<Category> categories = mRealm.where(Category.class).findAll();
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories);


        GridView gridView = (GridView) view.findViewById(R.id.categories);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CategoryAdapter adapter = (CategoryAdapter) parent.getAdapter();
                Category category = adapter.getItem(position);
                long categoryId = category.getId();
                String categoryName = category.getCategory();
                if (mListener != null) {
                    mListener.onCategorySelected(categoryId, categoryName);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.app_name);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
