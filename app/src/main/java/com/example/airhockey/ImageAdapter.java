package com.example.airhockey;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<Integer> imageList;
    private ArrayList<Skin> skinList;
    private ViewPager2 viewPager2;
    private TextView coinTextView;
    private Animation scaleUp, scaleDown;
    private Database db;
    private Context context;

    public ImageAdapter(ArrayList<Integer> imageList, ViewPager2 viewPager2, Database db, Context context, TextView coinTextView) {
        this.imageList = imageList;
        this.skinList = new ArrayList<>();
        this.viewPager2 = viewPager2;
        this.db = db;
        this.coinTextView = coinTextView;
        this.context = context;
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_container, parent, false);
        return new ImageViewHolder(view);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // if (position < 8 && holder.getAdapterPosition() < 8 ) {
            holder.imageView.setImageResource(imageList.get(position));
            skinList = db.getallSkins();
            holder.skinNameTextView.setText(skinList.get(position).getName() + " - " + skinList.get(position).getPrice() + " Coins");
            if (skinList.get(position).getSelected() == 1) {
                holder.buyButton.setText("SELECTED");
                holder.buyButton.setBackgroundResource(R.drawable.button_selected);
            } else {
                holder.buyButton.setBackgroundResource(R.drawable.button_background);

                // click listener für den Buy-Button
                holder.buyButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            holder.buyButton.startAnimation(scaleUp);
                            if ((db.getCoins() - skinList.get(holder.getAdapterPosition()).getPrice()) < 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                // Titel
                                SpannableString title = new SpannableString("Not enough coins!");
                                title.setSpan(new RelativeSizeSpan(1.2f), 0, title.length(), 0);
                                builder.setTitle(title);

                                // Nachricht
                                SpannableString message = new SpannableString("You don't have enough coins to purchase this skin.\n\n");
                                message.setSpan(new RelativeSizeSpan(1.1f), 0, message.length(), 0);
                                builder.setMessage(message);

                                builder.setPositiveButton("OK", null);

                                AlertDialog dialog = builder.create();
                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                                        ViewGroup.LayoutParams params = positiveButton.getLayoutParams();
                                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                        positiveButton.setLayoutParams(params);
                                        positiveButton.setBackgroundResource(R.drawable.button_background);
                                    }
                                });

                                dialog.show();
                            } else {
                                // Immer zuerst Skin deselecten, dann neuen Skin selecten
                                db.deselectOldSkin();
                                db.purchaseSkin(holder.getAdapterPosition());
                                // Coins aktualisieren
                                db.insertCoin(skinList.get(holder.getAdapterPosition()).getPrice() - db.getCoins());
                                coinTextView.setText("Coins: " + db.getCoins());

                                for (int i = 0; i < skinList.size(); i++) {
                                    Skin skin = skinList.get(i);
                                    if (skin.getSelected() == 1) {
                                        // Skin ist ausgewählt
                                        holder.buyButton.setText("SELECTED");
                                        holder.buyButton.setBackgroundResource(R.drawable.button_selected);
                                    } else if (skin.getPurchased() == 1) {
                                        holder.buyButton.setText("SELECT");
                                        holder.buyButton.setBackgroundResource(R.drawable.button_background);
                                    } else {
                                        // Skin ist nicht ausgewählt
                                        holder.buyButton.setText("BUY");
                                        holder.buyButton.setBackgroundResource(R.drawable.button_background);
                                    }
                                }
                            }
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            holder.buyButton.startAnimation(scaleDown);
                        }
                        return true;
                    }
                });
            }
        //}
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView skinNameTextView;
        public Button buyButton;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            skinNameTextView = itemView.findViewById(R.id.skinNameTextView);
            buyButton = itemView.findViewById(R.id.buyButton);
        }
    }
}

