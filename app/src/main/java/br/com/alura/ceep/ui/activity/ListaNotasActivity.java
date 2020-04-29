package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import br.com.alura.ceep.ui.FeedbackActivity;
import br.com.alura.ceep.R;
import br.com.alura.ceep.database.NotaDataBase;
import br.com.alura.ceep.database.dao.NotaDAO;
import br.com.alura.ceep.enums.MenuEnum;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.SharedPreferencesConstantes.NOME_SHARED_PREFERENCE;

public class ListaNotasActivity extends AppCompatActivity {


    private static final String SHARED_PREFERENCE_MENU = "SH_MENU";
    private MenuEnum estiloMenu;
    private Menu menuAtual;

    public static final String TITULO_APPBAR = "Notas";
    private ListaNotasAdapter adapter;
    RecyclerView listaNotasRecyclerView;

    private NotaDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        // Inicializando o estilo do menu
        obtemEstiloMenu();

        // Inicializando DAO
        dao = NotaDataBase.getInstance(this).getRoomNotaDAO();

        setTitle(TITULO_APPBAR);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraItemTouchHelper();
        configuraBotaoInsereNota();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuAtual = menu;
        getMenuInflater().inflate(R.menu.menu_lista_notas, menuAtual);
        selecionaEstiloMenu();
        return super.onPrepareOptionsMenu(menuAtual);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.feedback) {
            startActivity(new Intent(ListaNotasActivity.this, FeedbackActivity.class));
        } else {
            selecionaEstiloMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    private void selecionaEstiloMenu() {
        salvaEstiloMenu();
        MenuItem menuLinear = menuAtual.findItem(R.id.layout_linear);
        MenuItem menuGrid = menuAtual.findItem(R.id.layout_grid);

        if (estiloMenu == MenuEnum.LIENAR) {
            menuLinear.setVisible(true);
            menuGrid.setVisible(false);
            estiloMenu = MenuEnum.GRID;
        } else {
            menuGrid.setVisible(true);
            menuLinear.setVisible(false);
            estiloMenu = MenuEnum.LIENAR;
        }

        definirLayoutAdapter();

    }

    private void salvaEstiloMenu() {
        SharedPreferences sharedPreferences = getSharedPreferences(NOME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCE_MENU, estiloMenu.toString());
        editor.apply();
    }

    private void obtemEstiloMenu() {
        SharedPreferences sharedPreferences = getSharedPreferences(NOME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String estiloMenuSalvo = sharedPreferences.getString(SHARED_PREFERENCE_MENU, MenuEnum.GRID.toString());
        estiloMenu = MenuEnum.valueOf(estiloMenuSalvo);
    }


    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota =
                new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota,
                CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ehResultadoInsereNota(requestCode, data)) {

            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }

        }

        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                altera(notaRecebida);
            }
        }
    }

    private void altera(Nota nota) {
        Long idNotaInserida = dao.insere(nota);
        nota.setId(idNotaInserida);
        adapter.altera(nota);
    }


    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adiciona(Nota nota) {
        Long idNotaInserida = dao.insere(nota);
        nota.setId(idNotaInserida);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data);
    }

    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        listaNotasRecyclerView = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotasRecyclerView);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);

        definirLayoutAdapter();

        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota) {
                vaiParaFormularioNotaActivityAltera(nota);
            }
        });
    }

    private void configuraItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter, dao));
        itemTouchHelper.attachToRecyclerView(listaNotasRecyclerView);
    }

    private void definirLayoutAdapter() {
        if (estiloMenu.equals(MenuEnum.LIENAR)) {
            listaNotasRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else {
            listaNotasRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

}
