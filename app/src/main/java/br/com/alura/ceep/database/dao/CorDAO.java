package br.com.alura.ceep.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.alura.ceep.model.Cor;

@Dao
public interface CorDAO {
    @Insert
    void insere(Cor cor);

    @Insert
    void insere(List<Cor> cores);

    @Query("SELECT * FROM cor where cor.idCor = :corId")
    Cor buscaCorPorID(Long corId);

    @Query("SELECT * FROM cor")
    List<Cor> todos();

    @Query("SELECT * FROM cor where cor.nome = :nomeCor")
    Cor getCorDeafult(String nomeCor);
}
