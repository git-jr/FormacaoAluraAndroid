package br.com.alura.ceep.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.alura.ceep.database.dao.CorDAO;
import br.com.alura.ceep.database.dao.NotaDAO;
import br.com.alura.ceep.model.Cor;
import br.com.alura.ceep.model.Nota;

@Database(entities = {Nota.class, Cor.class}, version = 1, exportSchema = false)
public abstract class NotaDataBase extends RoomDatabase {

    private static final String NOME_BANCO = "Ceep.db";

    public abstract NotaDAO getRoomNotaDAO();
    public abstract CorDAO getRoomCorDAO();

    public static NotaDataBase getInstance(Context context) {
        return Room
                .databaseBuilder(context, NotaDataBase.class, NOME_BANCO)
                .allowMainThreadQueries()
                .build();
    }

}
