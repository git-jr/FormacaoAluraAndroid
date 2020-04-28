package br.com.alura.ceep.model;

import android.graphics.Color;

import java.io.Serializable;

public class Nota implements Serializable {

    private String titulo;
    private String descricao;
    private Cor cor;

    public Nota(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public Nota() {
        cor = new Cor();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cor getCor() {
        return cor;
    }
}