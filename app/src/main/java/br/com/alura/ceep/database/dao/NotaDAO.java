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

    @Delete
    void remove(Nota nota);

    @Query("SELECT * FROM nota WHERE nota.posicao = :posicaoNota ")
    Nota buscaPorPosicao(Long posicaoNota);

/*    private final static ArrayList<Nota> notas = new ArrayList<>();

    public List<Nota> todos() {
        return (List<Nota>) notas.clone();
    }

    public void insere(Nota... notas) {
        NotaDAO.notas.addAll(Arrays.asList(notas));
    }

    public void altera(int posicao, Nota nota) {
        notas.set(posicao, nota);
    }

    public void remove(int posicao) {
        notas.remove(posicao);
    }

    public void troca(int posicaoInicio, int posicaoFim) {
        Collections.swap(notas, posicaoInicio, posicaoFim);
    }

    public void removeTodos() {
        notas.clear();
    }*/
}
