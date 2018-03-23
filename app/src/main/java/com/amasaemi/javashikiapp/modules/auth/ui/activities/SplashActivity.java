package com.amasaemi.javashikiapp.modules.auth.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.amasaemi.javashikiapp.data.managers.PreferencesManager;
import com.amasaemi.javashikiapp.data.managers.StaticAppManager;
import com.amasaemi.javashikiapp.data.services.UserService;
import com.amasaemi.javashikiapp.modules.list.ui.activities.ListActivity;

/**
 * Created by Alex on 26.02.2018.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferencesManager preferencesManager = new PreferencesManager(this);
        // загружаем настройки приложения
        preferencesManager.loadSettings();
        // загружаем данные авторизации и если данные отсутствуют - переходим на экран авторизации
        if (preferencesManager.loadAccessData()) {
            // проверяем верность сохраненных данных
            new UserService().getCurrentUser((response) -> {
                    if (response.getNickname().equals(StaticAppManager.getInstance().getCurrentUser().getLogin())) {
                        startActivity(new Intent(this, ListActivity.class));
                    } else {
                        preferencesManager.forgetAccessData();
                        startActivity(new Intent(this, AuthActivity.class));
                    }
                }, (t) -> {
                    preferencesManager.forgetAccessData();
                    startActivity(new Intent(this, AuthActivity.class));
                });
        } else {
            startActivity(new Intent(this, AuthActivity.class));
        }

        this.finish();
    }
}