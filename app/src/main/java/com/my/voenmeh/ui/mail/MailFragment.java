package com.my.voenmeh.ui.mail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.my.voenmeh.R;
import com.my.voenmeh.databinding.FragmentMailBinding;

public class MailFragment extends Fragment {

    private WebView webView;
    private FragmentMailBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MailViewModel mailViewModel =
                new ViewModelProvider(this).get(MailViewModel.class);

        binding = FragmentMailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        webView = root.findViewById(R.id.mailView);
        webView.getSettings().setJavaScriptEnabled(true); // Включаем поддержку JavaScript
        webView.loadUrl("https://mail.voenmeh.ru/mail/"); // Загружаем страницу почты

        // Настройки WebView для открытия ссылок внутри приложения, а не в браузере
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}