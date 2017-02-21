package com.example.username.my15puzzle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity
        implements GiveUpDialog.GiveUpDialogListener,
        CompleteDialog.CompleteDialogListener {
    protected static final int IC_EGG = 15;

    //    private TextView mTimeView;
    private GridView mGridView;


    class Panel {
        int drawResource;
        int number;

        Panel(int drawResource, int number) {
            this.drawResource = drawResource;
            this.number = number;
        }
    }

    public class BitmapAdapter extends ArrayAdapter<Panel> {
        private int resourceId;

        class ViewHolder {
            ImageView imageView;
            int number;
            int position;
        }

        public BitmapAdapter(
                @NonNull Context context,
                @LayoutRes int resource,
                @NonNull
                        List<Panel> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resourceId, null);
                final ViewHolder holder = new ViewHolder();
                holder.imageView = (ImageView) convertView;
                convertView.setTag(holder);
            }
            //convertView.setAnimation(null);
            final ViewHolder holder = (ViewHolder) convertView.getTag();

            holder.position = position;
            holder.number = getItem(position).number;
            holder.imageView.setMaxWidth(parent.getWidth()/4);
            holder.imageView.setMaxHeight(parent.getHeight()/4);
            holder.imageView.setMinimumWidth(parent.getWidth()/4);
            holder.imageView.setMinimumHeight(parent.getHeight()/4);
            holder.imageView.setImageResource(getItem(position).drawResource);

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

//        mTimeView = (TextView) findViewById(R.id.time);

        final ArrayList<Panel> list = new ArrayList<Panel>();
        list.add(new Panel(R.drawable.ic_piyo_01, 0));
        list.add(new Panel(R.drawable.ic_piyo_02, 1));
        list.add(new Panel(R.drawable.ic_piyo_03, 2));
        list.add(new Panel(R.drawable.ic_piyo_04, 3));

        list.add(new Panel(R.drawable.ic_piyo_05, 4));
        list.add(new Panel(R.drawable.ic_piyo_06, 5));
        list.add(new Panel(R.drawable.ic_piyo_07, 6));
        list.add(new Panel(R.drawable.ic_piyo_08, 7));

        list.add(new Panel(R.drawable.ic_piyo_09, 8));
        list.add(new Panel(R.drawable.ic_piyo_10, 9));
        list.add(new Panel(R.drawable.ic_piyo_11, 10));
        list.add(new Panel(R.drawable.ic_piyo_12, 11));

        list.add(new Panel(R.drawable.ic_piyo_13, 12));
        list.add(new Panel(R.drawable.ic_piyo_14, 13));
        list.add(new Panel(R.drawable.ic_piyo_15, 14));
        list.add(new Panel(R.drawable.ic_egg, IC_EGG));

        for (int i = 0; i < 10; i++) {
            swapRandom(list);
        }


        BitmapAdapter bitmapAdapter =
                new BitmapAdapter(getApplicationContext(),
                        R.layout.grid_cell, list);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(bitmapAdapter);

//        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
//        GridLayoutAnimationController
//                controller = new GridLayoutAnimationController(animation, .2f, .2f);
//        mGridView.setLayoutAnimation(controller);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                BitmapAdapter adapter = (BitmapAdapter) parent.getAdapter();
                BitmapAdapter.ViewHolder holder =
                        (BitmapAdapter.ViewHolder) view.getTag();
                boolean canUp = true;
                boolean canDown = true;
                boolean canLeft = true;
                boolean canRight = true;

                if (holder.position < 4) canUp = false;
                if (holder.position > 11) canDown = false;
                if (holder.position % 4 == 0) canLeft = false;
                if (holder.position % 4 == 3) canRight = false;
                if (canUp) {
                    final int panelNo = holder.position - 4;
                    final Panel panel = adapter.getItem(panelNo);
                    final int animationX = 0;
                    final int animationY = -view.getHeight();

                    if (panel.number == IC_EGG) {
                        swapPanel(adapter, view, panelNo, panel, animationX,
                                animationY);
                    }
                }
                if (canDown) {
                    final int panelNo = holder.position + 4;
                    final Panel panel = adapter.getItem(panelNo);
                    final int animationX = 0;
                    final int animationY = view.getHeight();

                    if (panel.number == IC_EGG) {
                        swapPanel(adapter, view, panelNo, panel, animationX,
                                animationY);
                    }
                }
                if (canRight) {
                    final int panelNo = holder.position + 1;
                    final Panel panel = adapter.getItem(panelNo);
                    final int animationX = view.getWidth();
                    final int animationY = 0;

                    if (panel.number == IC_EGG) {
                        swapPanel(adapter, view, panelNo, panel, animationX,
                                animationY);
                    }
                }
                if (canLeft) {
                    final int panelNo = holder.position - 1;
                    final Panel panel = adapter.getItem(panelNo);
                    final int animationX = -view.getWidth();
                    final int animationY = 0;

                    if (panel.number == IC_EGG) {
                        swapPanel(adapter, view, panelNo, panel, animationX,
                                animationY);
                    }
                }
            }

            private void swapPanel(final BitmapAdapter adapter, final View view,
                                   final int panelNo, final Panel panel,
                                   int animationX, int animationY) {
                final BitmapAdapter.ViewHolder holder =
                        (BitmapAdapter.ViewHolder) view.getTag();
                final ViewPropertyAnimator animator = view.animate();
                animator.withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        animator.setDuration(300);
                        animator.translationX(0);
                        animator.translationY(0);
                        Panel temp = adapter.getItem(holder.position);
                        adapter.remove(panel);
                        adapter.insert(panel, holder.position);
                        adapter.remove(temp);
                        adapter.insert(temp, panelNo);
//                        adapter.notifyDataSetChanged();
                    }
                });
                animator.setDuration(100);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.translationX(animationX);
                animator.translationY(animationY);
            }
        });


        mTimeView = (TextView) findViewById(R.id.time);
        mTimeView.setText("0");

        ImageView giveup = (ImageView) findViewById(R.id.giveup);
        giveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiveUpDialog fragment =
                        GiveUpDialog.newInstance(R.drawable.piyo,
                                getString(R.string.giveup),
                                getString(R.string.do_you_give_up));
                FragmentManager manager = getSupportFragmentManager();
                fragment.show(manager, "GiveUpDialog");
            }
        });

    }

    // ランダムにパネルを入れ替えます
    private void swapRandom(ArrayList<Panel> list) {
        for (int i = 0; i < 16; i++) {
            Panel p = list.get(i);
            if (p.number == IC_EGG) {
                boolean canUp = i < 4 ? false : true;
                boolean canDown = i > 11 ? false : true;
                boolean canLeft = i % 4 == 0 ? false : true;
                boolean canRight = i % 4 == 3 ? false : true;
                int ran = (int) (Math.random() * 4);   // 0〜3の乱数を得る
                int panelNo = i;
                if (ran == 0 && canUp) panelNo = i - 4;
                if (ran == 1 && canDown) panelNo = i + 4;
                if (ran == 2 && canRight) panelNo = i + 1;
                if (ran == 3 && canLeft) panelNo = i - 1;

                if (panelNo != i) { // パネルが移動可能であれば
                    Panel panel = list.get(panelNo);
                    list.remove(panel);
                    list.add(i, panel);
                    list.remove(p);
                    list.add(panelNo, p);
                }
            }
        }
    }

    public class MainTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateScore();
                    if (isComplete()) {
                        mTimer.cancel();
                        CompleteDialog fragment =
                                CompleteDialog.newInstance();
                        FragmentManager manager = getSupportFragmentManager();
                        fragment.show(manager, "CompleteDialog");
                    }
                }
            });
        }
    }

    Timer mTimer;
    TimerTask mTimerTask;
    Handler mHandler = new Handler();

    private TextView mTimeView;

    private void updateScore() {
        int time = Integer.parseInt(mTimeView.getText().toString());
        time++;
        mTimeView.setText(String.valueOf(time));
    }

    private boolean isComplete() {
        boolean isCompleted = true;
        int count = mGridView.getCount() - 1;
        for (int i = 0; i < count; i++) {
            Panel p = (Panel) mGridView.getItemAtPosition(i);
            if (p.number != i) {
                isCompleted = false;
                break;
            }
        }
        return isCompleted;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimer = new Timer();
        mTimerTask = new MainTimerTask();
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    public void onDialogPositiveClick(GiveUpDialog dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(GiveUpDialog dialog) {
    }


    @Override
    public void onDialogPositiveClick() {
        int time = Integer.parseInt(mTimeView.getText().toString());
        Intent intent = new Intent(this, RankingActivity.class);
        intent.putExtra(RankingActivity.RESULT_TIME,time);
        startActivity(intent);
    }
}
