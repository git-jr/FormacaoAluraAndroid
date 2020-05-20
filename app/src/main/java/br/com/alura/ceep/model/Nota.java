package br.com.alura.ceep.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Color;

import java.io.Serializable;

@Entity
public class Nota implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String titulo;
    private String descricao;
    @ForeignKey(entity = Cor.class,
            parentColumns = "idCor",
            childColumns = "corId")
    private Long corId;
    @Ignore
    private Cor cor;
    private Long posicao = (long) -1;

    public Long getPosicao() {
        return posicao;
    }

    public void setPosicao(Long posicao) {
        this.posicao = posicao;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCorId() {
        return corId;
    }

    public void setCorId(Long corId) {
        this.corId = corId;
    }
}