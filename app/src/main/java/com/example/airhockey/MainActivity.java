package com.example.airhockey;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private ArrayList<Integer> imageList;
    private ImageAdapter adapter;

    private Button buttonSinglePlayer, buttonMultiPlayer;
    private Animation scaleUp, scaleDown;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpTransformer();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        //Single Player Button
        buttonSinglePlayer = findViewById(R.id.buttonSinglePlayer);
        //Multi Player Button
        buttonMultiPlayer = findViewById(R.id.buttonMultiPlayer);

        // ScaleUp + ScaleDown Animations
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        buttonSinglePlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    buttonSinglePlayer.startAnimation(scaleUp);
                    // Wait 150ms before opening calling the Function
                    new Handler().postDelayed(() -> goToGame(view), 150);
                } else if (motionEvent.getAction()== MotionEvent.ACTION_UP) {
                    buttonSinglePlayer.startAnimation(scaleDown);
                }
                return true;
            }
        });

        buttonMultiPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    buttonMultiPlayer.startAnimation(scaleUp);
                } else if (motionEvent.getAction()== MotionEvent.ACTION_UP) {
                    buttonMultiPlayer.startAnimation(scaleDown);
                }
                return true;
            }
        });
    }

    private void goToGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DIFFICULTY", "EASY");
        startActivity(intent);
    }

    private void setUpTransformer() {
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.14f);
        });

        viewPager2.setPageTransformer(transformer);
    }

    private void init() {
        viewPager2 = findViewById(R.id.viewPager2);
        imageList = new ArrayList<>();

        imageList.add(R.drawable.skinblue);
        imageList.add(R.drawable.skin2);
        imageList.add(R.drawable.skin3);
        imageList.add(R.drawable.skin4);
        imageList.add(R.drawable.skin5);
        imageList.add(R.drawable.skin6);
        imageList.add(R.drawable.skin7);
        imageList.add(R.drawable.skin8);

        adapter = new ImageAdapter(imageList, viewPager2);

        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        ((RecyclerView)viewPager2.getChildAt(0)).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // User has lifted their finger after a swipe
                int current = viewPager2.getCurrentItem();
                int total = adapter.getItemCount();

                if(current == 0) {
                    // User is at the first item, don't scroll left
                } else if(current == total - 1) {
                    // User is at the last item, don't scroll right
                } else {
                    // User is in between items, scroll left or right based on their swipe direction
                    float xDiff = ev.getX() - viewPager2.getX();
                    if(xDiff > 0) {
                        viewPager2.setCurrentItem(current - 1);
                    } else {
                        viewPager2.setCurrentItem(current + 1);
                    }
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}