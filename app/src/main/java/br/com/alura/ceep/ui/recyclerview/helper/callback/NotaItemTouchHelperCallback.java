package br.com.alura.ceep.ui.recyclerview.helper.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import br.com.alura.ceep.database.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;
    private NotaDAO dao;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter, NotaDAO notaDAO) {
        this.adapter = adapter;
        dao = notaDAO;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        trocaNotas(posicaoInicial, posicaoFinal);
        return true;
    }

    private void trocaNotas(int posicaoInicial, int posicaoFinal) {

        adapter.troca(posicaoInicial, posicaoFinal);
    }

    private void trocaNotas(long posicaoInicial, long posicaoFinal) {
        Nota notaInicial = dao.buscaPorPosicao(posicaoInicial);
        Nota notaFinal = dao.buscaPorPosicao(posicaoFinal);

        notaInicial.setId(notaFinal.getId());
        notaInicial.setPosicao(posicaoFinal);

        notaFinal.setId(dao.buscaPorPosicao(posicaoInicial).getId());
        notaFinal.setPosicao(posicaoInicial);

        dao.insere(notaInicial);
        dao.insere(notaFinal);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getAdapterPosition();
        removeNota(posicaoDaNotaDeslizada);
    }

    private void removeNota(int posicao) {
        Nota notaParaRemover = dao.buscaPorPosicao((long) posicao);
        dao.remove(notaParaRemover);
        adapter.remove(posicao);
    }
}
