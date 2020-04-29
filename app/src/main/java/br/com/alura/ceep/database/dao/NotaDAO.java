package br.com.alura.ceep.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.alura.ceep.model.Nota;

@Dao
public interface NotaDAO {

    @Query("SELECT * FROM nota order by nota.posicao")
    List<Nota> todos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insere(Nota nota);

    @Query("DELETE FROM Nota where nota.posicao = :posicaoNota")
    void remove(Long posicaoNota);

    @Query("SELECT * FROM nota WHERE nota.posicao = :posicaoNota ")
    Nota buscaPorPosicao(Long posicaoNota);

}
