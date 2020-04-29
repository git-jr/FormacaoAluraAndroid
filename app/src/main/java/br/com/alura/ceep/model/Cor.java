package br.com.alura.ceep.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Color;

import java.io.Serializable;

@Entity
public class Cor implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long idCor;
    private String nome;
    private String codigoHex;

    public Cor(Long idCor, String nome, String codigoHex) {
        this.idCor = idCor;
        this.nome = nome;
        this.codigoHex = codigoHex;
    }

    @Ignore
    public Cor(String nome, String codigoHex) {
        this.nome = nome;
        this.codigoHex = codigoHex;
    }

    @Ignore
    public Cor() {
        nome = "BRANCO";
        codigoHex = "#FFFFFF";
    }

    public Long getIdCor() {
        return idCor;
    }

    public void setIdCor(Long idCor) {
        this.idCor = idCor;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoHex() {
        return codigoHex;
    }

    public void setCodigoHex(String codigoHex) {
        this.codigoHex = codigoHex;
    }

    public String getNome() {
        return nome;
    }

    public int getCor() {
        return Color.parseColor(codigoHex);
    }
}
