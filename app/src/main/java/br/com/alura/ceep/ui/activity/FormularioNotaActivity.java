package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.database.NotaDataBase;
import br.com.alura.ceep.model.Cor;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaCoresAdapter;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;

public class FormularioNotaActivity extends AppCompatActivity {


    public static final String TITULO_APPBAR_INSERE = "Insere nota";
    public static final String TITULO_APPBAR_ALTERA = "Altera nota";
    private TextView titulo;
    private TextView descricao;

    public static final String NOTA_EM_MEMORIA = "NOTA_MEMORIA";
    public static final String TITULO_APPBAR = "TITULO_APPBAR";
    private ConstraintLayout formulario_nota_container;
    private Nota notaRecebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);


        if (savedInstanceState != null) {
            recarregaDadosInstanciaAnteriror(savedInstanceState);
        } else {
            setTitle(TITULO_APPBAR_INSERE);
            inicializaCampos();
            Intent dadosRecebidos = getIntent();
            if (dadosRecebidos.hasExtra(CHAVE_NOTA)) {
                setTitle(TITULO_APPBAR_ALTERA);
                notaRecebida = (Nota) dadosRecebidos
                        .getSerializableExtra(CHAVE_NOTA);
                preencheCampos(notaRecebida);
            } else {
                notaRecebida = new Nota();
            }
        }
        configuraTrocaDeTrocaDeCores();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(NOTA_EM_MEMORIA, notaRecebida);
        outState.putString("TITULO_APPBAR", getSupportActionBar().getTitle().toString());
    }

    private void recarregaDadosInstanciaAnteriror(Bundle savedInstanceState) {
        salvaDadosEstadoAtualDaActivity(savedInstanceState);
    }

    private void salvaDadosEstadoAtualDaActivity(Bundle savedInstanceState) {
        setTitle(savedInstanceState.getString(TITULO_APPBAR));
        Nota nota = (Nota) savedInstanceState.getSerializable(NOTA_EM_MEMORIA);
        notaRecebida = nota;
        inicializaCampos();
        preencheCampos(notaRecebida);
    }

    private void configuraTrocaDeTrocaDeCores() {
        trocaCorDoFundoFormulario(notaRecebida.getCor());
        configuraRecyclerView(listaCores());
    }

    private void configuraRecyclerView(List<Cor> cores) {
        RecyclerView recyclerView = findViewById(R.id.lista_cores_recyclerview);
        configuraAdapter(cores, recyclerView);
    }

    private void configuraAdapter(List<Cor> cores, RecyclerView recyclerView) {
        ListaCoresAdapter adapterCor = new ListaCoresAdapter(cores, this);
        adapterCor.setOnItemClickListener(new ListaCoresAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cor cor, int position) {
                trocaCorDoFundoFormulario(cor);
                notaRecebida.setCor(cor);
                notaRecebida.setCorId(cor.getIdCor());
            }
        });
        recyclerView.setAdapter(adapterCor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void trocaCorDoFundoFormulario(Cor cor) {
        formulario_nota_container.setBackgroundColor(cor.getCor());
    }

    private List<Cor> listaCores() {
        return NotaDataBase.getInstance(this).getRoomCorDAO().todos();
    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
        formulario_nota_container = findViewById(R.id.formulario_nota_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ehMenuSalvaNota(item)) {
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    @NonNull
    private Nota criaNota() {
        notaRecebida.setTitulo(titulo.getText().toString());
        notaRecebida.setDescricao(descricao.getText().toString());
        return notaRecebida;
    }

    private boolean ehMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}
