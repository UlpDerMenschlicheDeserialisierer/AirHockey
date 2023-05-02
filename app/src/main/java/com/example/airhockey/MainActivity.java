package com.example.airhockey;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private ArrayList<Integer> imageList;
    private ImageAdapter adapter;
    private TextView coinTextView;
    private Button buttonSinglePlayer, buttonMultiPlayer, buttonBuy;
    private Animation scaleUp, scaleDown;
    private Database db;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Datenbankinstanz erstellen
        db = new Database(this);
        setContentView(R.layout.activity_main);
        // Coin Text View
        coinTextView = findViewById(R.id.textViewCoins);
        coinTextView.setText(coinTextView.getText() + " " + db.getCoins());
        // Skins in Slider laden
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

        imageList.add(R.drawable.ohneschatten);
        imageList.add(R.drawable.ohneschatten);
        imageList.add(R.drawable.skin3);
        imageList.add(R.drawable.skin4);
        imageList.add(R.drawable.skin5);
        imageList.add(R.drawable.skin6);
        // imageList.add(R.drawable.skin7);
        // imageList.add(R.drawable.skin8);

        adapter = new ImageAdapter(imageList, viewPager2, db, this, coinTextView);

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
                // User hat sein Finger nach einem Swipe angehoben
                int current = viewPager2.getCurrentItem();
                int total = adapter.getItemCount();

                if(current == 0 || current == total - 1) {
                    // User befindet sich beim ersten, oder letzten Skin, --> nicht nach recht oder links scrollen
                    return super.dispatchTouchEvent(ev);
                } else {
                    // User befindet sich zwischen zwei Items --> Nach rechts oder Links, je nach Swipe-Richtung wischen
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

    /*
    public void saveImagesToInternalStorage() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        boolean imagesSaved = sharedPreferences.getBoolean("imagesSaved", false);

        if (!imagesSaved) {
            try {
                String[] imageNames = {"skin1.png", "skin2.png", "skin3.png", "skin4.png", "skin5.png", "skin6.png", "skin7.png", "skin8.png"};

                for (String imageName : imageNames) {
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);

                    FileOutputStream outputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("imagesSaved", true);
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}