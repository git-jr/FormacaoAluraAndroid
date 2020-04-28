package br.com.alura.ceep.model;

import android.graphics.Color;

import java.io.Serializable;

public class Cor implements Serializable {
    private String nome;
    private String codigoHex;

    public Cor(String nome, String codigoHex) {
        this.nome = nome;
        this.codigoHex = codigoHex;
    }

    public Cor() {
        nome = "BRANCO";
        codigoHex = "#FFFFFF";
    }

    public String getNome() {
        return nome;
    }

    public int getCor() {
        return Color.parseColor(codigoHex);
    }
}
