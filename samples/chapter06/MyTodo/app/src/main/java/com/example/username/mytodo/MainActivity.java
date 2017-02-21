package com.example.username.mytodo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements InputCategoryDialog.NoticeDialogListener
        , MainActivityFragment.OnFragmentInteractionListener
        , InputTodoDialog.NoticeDialogListener {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();

                Fragment mainFragment =
                        manager.findFragmentByTag("MainActivityFragment");
                if (mainFragment != null) {
                    if (mainFragment.isVisible()) {
                        showInputCategoryDialog();
                    }
                }
                Fragment todoFragment = manager.findFragmentByTag("TodoListFragment");
                if (todoFragment != null) {
                    if (todoFragment.isVisible()) {
                        showInputTodoDialog();
                    }
                }
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("MainActivityFragment");
        if (fragment == null) {
            fragment = new MainActivityFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.content, fragment, "MainActivityFragment");
            transaction.commit();
        }

        mRealm = Realm.getDefaultInstance();

    }

    private void showInputTodoDialog() {
        InputTodoDialog fragment =
                InputTodoDialog.newInstance(R.drawable.ic_check,
                        getString(R.string.todo_title),
                        getString(R.string.enter_new_todo));
        FragmentManager manager = getSupportFragmentManager();
        fragment.show(manager, "InputTodoDialog");
    }


    private void showInputCategoryDialog() {
        InputCategoryDialog fragment =
                InputCategoryDialog.newInstance(R.drawable.ic_format_list_bulleted,
                        getString(R.string.category_title),
                        getString(R.string.enter_new_category));
        FragmentManager manager = getSupportFragmentManager();
        fragment.show(manager, "InputCategoryDialog");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(InputCategoryDialog dialog) {
        final String categoryName = dialog.getCategory();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // idフィールドの最大値を取得
                Number maxId = mRealm.where(Category.class).max("id");
                long nextId = 0;
                if (maxId != null) nextId = maxId.longValue() + 1;
                // カテゴリの追加
                // createObjectではキー項目を渡してオブジェクトを生成する
                Category category =
                        realm.createObject(Category.class, new Long(nextId));
                category.setCategory(categoryName);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        String message = "カテゴリ「" + categoryName + "」を追加しました。";
        // 上記は次のように記載することが可能
        String message = getString(R.string.add_category, categoryName);
        Snackbar.make(fab, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @Override
    public void onDialogNegativeClick(InputCategoryDialog dialog) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Snackbar.make(fab, getResources().getText(R.string.canceld),
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @Override
    public void onCategorySelected(long categoryId, String categoryName) {
        TodoListFragment todoListFragment =
                TodoListFragment.newInstance(categoryId, categoryName);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, todoListFragment, "TodoListFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDialogPositiveClick(InputTodoDialog dialog) {
        final String title = dialog.getTodo();
        FragmentManager manager = getSupportFragmentManager();
        TodoListFragment fragment =
                (TodoListFragment) manager.findFragmentByTag("TodoListFragment");
        final long categoryId = fragment.getCategoryId();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // idフィールドの最大値を取得
                Number maxId = mRealm.where(Todo.class).max("id");
                long nextId = 0;
                if (maxId != null) nextId = maxId.longValue() + 1;
                // カテゴリの追加
                // createObjectではキー項目を渡してオブジェクトを生成する
                Todo todo = realm.createObject(Todo.class, new Long(nextId));
                todo.setTitle(title);
                todo.setCategoryId(categoryId);
                todo.setDone(false);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        String message = getString(R.string.add_todo, title);
        Snackbar.make(fab, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @Override
    public void onDialogNegativeClick(InputTodoDialog dialog) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Snackbar.make(fab, getString(R.string.canceld), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
