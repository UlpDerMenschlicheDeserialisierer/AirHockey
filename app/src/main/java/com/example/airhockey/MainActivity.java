package com.example.airhockey;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private ArrayList<Integer> imageList;
    private ImageAdapter adapter;
    private TextView coinTextView;
    private Button buttonSinglePlayer, buttonMultiPlayer, buttonBuy;
    private Animation scaleUp, scaleDown;
    private Database db;
    private Handler handler;
    private Runnable dotRunnable;
    private int dotCount = 0;
    private boolean isSearching = false;
    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;
    private String mServiceName = "MyService";
    private String mServiceType = "_http._tcp.";
    private boolean mIsServer = false;
    private ServerSocket mServerSocket;
    private Thread mServerThread;
    private boolean mIsConnected = false;
    private boolean mSearchInProgress = false;
    private NsdManager.RegistrationListener mRegistrationListener;

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

        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d("NSD", "Discovery started");
                mSearchInProgress = true;
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d("NSD", "Discovery stopped");
                mSearchInProgress = false;
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.d("NSD", "Service found: " + serviceInfo);
                if (serviceInfo.getServiceType().equals(mServiceType) && !mIsConnected) {
                    connectToService(serviceInfo);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.d("NSD", "Service lost: " + serviceInfo);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("NSD", "Discovery failed: " + errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("NSD", "Stop discovery failed: " + errorCode);
            }
        };

        // Initialize mRegistrationListener
        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                System.out.println("registration failure");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                System.out.println("unregistration failure");
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                System.out.println("successful registration");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                System.out.println("successful unregistration");
            }
        };


        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e("NSD", "Resolve failed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d("NSD", "Service resolved: " + serviceInfo);
            }
        };

        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e("NSD", "Resolve failed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d("NSD", "Service resolved: " + serviceInfo);
            }
        };

        mNsdManager = null;
        //mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
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
        // Handler initialisieren, um Punkte zu animieren
        handler = new Handler(Looper.getMainLooper());
        dotRunnable = new Runnable() {
            @Override
            public void run() {
                if (dotCount >= 4) {
                    dotCount = 0;
                }
                String dots = "";
                for (int i = 0; i < dotCount; i++) {
                    dots += ".";
                }
                buttonMultiPlayer.setText("Player search began" + dots);
                dotCount++;
                handler.postDelayed(this, 500);
            }
        };

        buttonMultiPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    buttonMultiPlayer.startAnimation(scaleUp);
                    if (!isSearching) {
                        isSearching = true;
                        // Animation starten
                        startAnimation();

                    } else {
                        // Animation stoppen
                        isSearching = false;
                        stopAnimation();
                    }
                    if (mIsConnected) {
                        // Eine Verbindung besteht bereits, also können wir nichts tun
                        return false;
                    }

                    if (mDiscoveryListener == null) {
                        // Wenn der Listener null ist, dann wurde die Suche noch nicht gestartet
                        discoverServices();
                    } else {
                        // Wenn der Listener nicht null ist, dann läuft die Suche gerade und wir müssen sie beenden
                        stopSearch();
                    }
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

        adapter = new ImageAdapter(imageList, viewPager2, db, this, coinTextView);

        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(6);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        ((RecyclerView)viewPager2.getChildAt(0)).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float downX = 0;
        switch(ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // User hat sein Finger nach einem Swipe angehoben
                int current = viewPager2.getCurrentItem();
                int total = adapter.getItemCount();
                System.out.println("Current: " + current);
                System.out.println("Total: " + total);
                if(current == 0 || current == total - 1) {
                    System.out.println("Erstes oder letztes Item");
                    float xDiff = ev.getRawX() - downX;
                    System.out.println(xDiff);
                    // User befindet sich beim ersten, oder letzten Skin, --> nicht nach recht oder links scrollen
                    return super.dispatchTouchEvent(ev);
                } else {
                    // User befindet sich zwischen zwei Items --> Nach rechts oder Links, je nach Swipe-Richtung wischen
                    float xDiff = ev.getRawX() - downX;
                    System.out.println(xDiff);
                    if(xDiff > 500) {
                        viewPager2.setCurrentItem(current - 1);
                    } else {
                        viewPager2.setCurrentItem(current + 1);
                    }
                }
            break;
            case MotionEvent.ACTION_DOWN: downX = ev.getRawX();break;
        }
        return super.dispatchTouchEvent(ev);
    }
    private void startAnimation() {
        dotCount = 0;
        buttonMultiPlayer.setText("Player search began");
        handler.post(dotRunnable);
    }

    private void stopAnimation() {
        handler.removeCallbacks(dotRunnable);
        buttonMultiPlayer.setText("Multiplayer");
    }

    private void stopSearch() {
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            mDiscoveryListener = null;
        }

        if (mServerThread != null) {
            mServerThread.interrupt();
            mServerThread = null;
        }

        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mServerSocket = null;
        }

        if (mIsServer) {
            mNsdManager.unregisterService(mRegistrationListener);
            mRegistrationListener = null;
            mIsServer = false;
        }

        mIsConnected = false;
    }

    private void connectToService(NsdServiceInfo serviceInfo) {
        // Perform the connection on a background thread to avoid blocking the UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(serviceInfo.getHost(), serviceInfo.getPort());
                    // Connection established, do something with the socket
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void discoverServices() {
        mNsdManager.discoverServices(mServiceType, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        // Wenn nach 5 Sekunden kein Gerät gefunden wird, selbst zum Server werden
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mIsServer) {
                    registerService();
                }
            }
        }, 5000); // Verzögerung von 5 Sekunden
    }

    private void registerService() {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(mServiceType);
        serviceInfo.setPort(1234);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }
}