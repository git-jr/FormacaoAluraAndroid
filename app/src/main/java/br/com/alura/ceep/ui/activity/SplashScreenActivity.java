package br.com.alura.ceep.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.database.NotaDataBase;
import br.com.alura.ceep.model.Cor;

import static br.com.alura.ceep.constantes.SharedPreferencesConstantes.NOME_SHARED_PREFERENCE;

public class SplashScreenActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCE_APP_ABERTO = "SH_JA_ABERTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int tempoEmTela = verificaSeAppJaFoiAberto();
        vaiParaActivityPrincipal(tempoEmTela);
    }

    private int verificaSeAppJaFoiAberto() {
        if (!appJaFoiAberto()) {
            confirmarAberturaDoApp();
            salvaListaCoresPadraoNoBanco();
            return 2000;
        }
        return 500;
    }

    private void salvaListaCoresPadraoNoBanco() {

        List<Cor> cores = new ArrayList<>();
        cores.add(new Cor("AZUL", "#408EC9"));
        cores.add(new Cor("BRANCO", "#FFFFFF"));
        cores.add(new Cor("VERMELHO", "#EC2F4B"));
        cores.add(new Cor("VERDE", "#9ACD32"));
        cores.add(new Cor("AMARELO", "#F9F256"));
        cores.add(new Cor("LILÁS", "#F1CBFF"));
        cores.add(new Cor("CINZA", "#D2D4DC"));
        cores.add(new Cor("MARROM", "#A47C48"));
        cores.add(new Cor("ROXO", "#BE29EC"));

        NotaDataBase.getInstance(this).getRoomCorDAO().insere(cores);
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
