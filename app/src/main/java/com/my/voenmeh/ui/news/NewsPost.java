package com.my.voenmeh.ui.news;

import static android.view.ViewGroup.LayoutParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.my.voenmeh.R;
import com.my.voenmeh.Utils.Constants;
import com.squareup.picasso.Picasso;

public class NewsPost {
    TextView postText = null;
    ImageView postImage = null;

    public NewsPost(Context mContext) {
        NewsRepository nr = new NewsRepository(); // здесь заполняется список из медиа для постов

        Activity a = (Activity) mContext;
        LinearLayout ll = a.findViewById(R.id.news_posts);

        for (int index = 0; index < Constants.NUMBER_OF_POSTS; index++) {
            CardView cardView = new CardView(mContext);
            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
            cardView.setRadius(20f);
            cardView.setCardElevation(10f);
            cardView.setUseCompatPadding(true);

            LinearLayout cardContent = new LinearLayout(mContext);
            cardContent.setOrientation(LinearLayout.VERTICAL); // вертикальная ориентация для текста и изображения

            // текст
            postText = new TextView(mContext);
            String text = nr.listOfPosts.get(index).getText();
            postText.setText(text);
            postText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            postText.setPadding(16, 16, 16, 8); // отступы для текста

            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.montserrat_r);
            postText.setTypeface(typeface);
            postText.setTextSize(14);
            postText.setTextColor(Color.BLACK);

            // выделение первой строки жирным шрифтом
            SpannableString spanText = new SpannableString(text);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            int newlineIndex = text.indexOf("\n"); // Найдите индекс переноса строки
            if (newlineIndex != -1) { // Проверьте, есть ли перенос строки
                spanText.setSpan(boldSpan, 0, newlineIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // Выделяем текст до первого
                // переноса строки
            }
            postText.setText(spanText);
            // изображение
            postImage = new ImageView(mContext);
            String url = nr.listOfPosts.get(index).getImageUrl();
            // проверяем, не пустая ли строка с URL
            if (!url.isEmpty()) {
                Picasso.get().load(url).into(postImage);
            }
            postImage.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)); // заполняет всю ширину
            postImage.setScaleType(ImageView.ScaleType.CENTER_CROP); // масштабирование под размер

            // добавляем обработчик клика на изображение
            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImageDialog(mContext, url);
                }
            });

            // добавляем текст и изображение в cardContent
            cardContent.addView(postText);
            cardContent.addView(postImage);

            // добавляем cardContent в CardView
            cardView.addView(cardContent);

            // добавляем CardView в LinearLayout
            ll.addView(cardView);

            // добавляем отступ между постами
            if (index < Constants.NUMBER_OF_POSTS - 1) {
                View space = new View(mContext);
                space.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 52)); // 52dp отступ
                ll.addView(space);
            }
        }
    }

    private void showImageDialog(Context context, String imageUrl) {
        // создаём диалоговое окно
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.image_dialog, null);

        // настраиваем ImageView в диалоговом окне
        ImageView imageView = dialogView.findViewById(R.id.image_view);
        if (!imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(imageView);
        }

        // задаём View диалоговому окну
        builder.setView(dialogView);

        // создаём и показываем диалоговое окно
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}