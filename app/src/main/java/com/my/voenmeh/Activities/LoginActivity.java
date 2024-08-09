package com.my.voenmeh.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.my.voenmeh.R;

import com.my.voenmeh.Authentication.UserRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LoginActivity extends AppCompatActivity {

    private void pullGroups() {
        Thread GettingGroups; //второй поток во избежание перегрузки мэйна
        Runnable runnable;
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        GettingGroups = new Thread(runnable); //запускаем поток
        GettingGroups.start();
    }

    private void getWeb() {
        try {
            Document WebSchedule = Jsoup.connect("http://www.voenmeh.com/schedule_green.php").get();
            Elements semesters = WebSchedule.getElementsByTag("option");
            String semester_id = semesters.get(0).val();
            String url = "http://www.voenmeh.com/schedule_green.php?semestr_id=" + semester_id + "&page_mode=group";
            WebSchedule = Jsoup.connect(url).get();
            Elements groups = WebSchedule.getElementsByTag("option"); //берем группы
            UserRepository.FillGroups(groups); //заполняем в user repositoriy хешсет с группами
        } catch (Exception e) {
            Log.d("Exceptions", e.toString());
        }
    }

    // Основной метод, вызываемый при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pullGroups();
        setContentView(R.layout.login_activity);
        // Находим элемент TextView по его ID
        TextView textView = findViewById(R.id.textView4);
        // Получаем текст из TextView
        String fullText = textView.getText().toString();
        String subString = "согласие на обработку персональных данных";
        // Создаем объект SpannableString для текста
        SpannableString spannableString = new SpannableString(fullText);
        // Устанавливаем цвет текста
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#C89E68"));
        int startIndex = fullText.indexOf(subString);
        int endIndex = startIndex + subString.length();
        // Устанавливаем подчеркивание для всего текста
        spannableString.setSpan(new UnderlineSpan(), 4, fullText.length(), 0);
        // Устанавливаем цвет для подстроки
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Устанавливаем отформатированный текст в TextView
        textView.setText(spannableString);
        // Обработчик нажатия на TextView
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Открываем ссылку в браузере
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://iksh.voenmeh.ru/soglasie"));
                startActivity(intent);
            }
        });
        // Находим элементы CheckBox и Button по их ID
        CheckBox checkBox = findViewById(R.id.checkBox);
        Button loginButton = findViewById(R.id.loginButton);
        // Устанавливаем кнопку в неактивное состояние и серый цвет
        loginButton.setEnabled(false);
        loginButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        // Обработчик изменения состояния CheckBox
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                // Если CheckBox не отмечен, делаем кнопку неактивной и меняем цвет
                loginButton.setEnabled(false);
                loginButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            } else {
                // Если CheckBox отмечен, делаем кнопку активной и восстанавливаем цвет
                loginButton.setEnabled(true);
                loginButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_main)));
            }
        });

        ImageButton button = findViewById(R.id.imageButton2); // замените imageButton2 на реальный id вашей кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                // Проверка, поддерживает ли устройство вибрацию
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(100); // указываем длительность в миллисекундах
                }

                Intent intent = new Intent(LoginActivity.this, StartUpActivity.class);
                startActivity(intent);
            }
        });


        // Находим элементы EditText по их ID
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        // Обработчик нажатия на кнопку
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserRepository.SetLogin(username.getText().toString());
                UserRepository.SetPassword(password.getText().toString());
                // Проверяем правильность введенных данных
                if (UserRepository.CorrectLogin() && UserRepository.CorrectPassword()) {
                    // Если данные верны, переходим на основную страницу
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Если данные неверны, выводим сообщение об ошибке и меняем стиль EditText
                    Toast.makeText(LoginActivity.this, "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                    username.setBackground(getResources().getDrawable(R.drawable.corner_login_error));
                    password.setBackground(getResources().getDrawable(R.drawable.corner_login_error));
                }
            }
        });
    }
}