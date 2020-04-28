package br.com.alura.ceep.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.alura.ceep.R;

import static br.com.alura.ceep.ui.activity.SharedPreferencesConstantes.NOME_SHARED_PREFERENCE;

public class SplashScreenActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCE_APP_ABERTO = "SH_JA_ABERTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        int tempoEmTela = verificaSeAppJaFoiAberto();
        vaiParaActivityPrincipal(tempoEmTela);

    }

    private int verificaSeAppJaFoiAberto() {
        if (!appJaFoiAberto()) {
            confirmarAberturaDoApp();
            return 2000;
        }
        return 500;
    }

    private void vaiParaActivityPrincipal(int tempoEmTela) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, ListaNotasActivity.class));
                finish();
            }
        }, tempoEmTela);
    }

    public boolean appJaFoiAberto() {
        SharedPreferences sharedPreferences = getSharedPreferences(NOME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_PREFERENCE_APP_ABERTO, false);
    }

    public void confirmarAberturaDoApp() {
        SharedPreferences sharedPreferences = getSharedPreferences(NOME_SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREFERENCE_APP_ABERTO, true);
        editor.apply();
    }
}
