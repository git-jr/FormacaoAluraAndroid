package br.com.alura.ceep.ui.recyclerview.helper.callback;

import android.os.AsyncTask;
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
        new TrocaNotasPosicaoNoBanco().execute((long) posicaoInicial, (long) posicaoFinal);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getAdapterPosition();
        removeNota(posicaoDaNotaDeslizada);
    }

    private void removeNota(int posicao) {
        dao.remove(Long.valueOf(posicao));
        adapter.remove(posicao);
    }

    private class TrocaNotasPosicaoNoBanco extends AsyncTask<Long, Void, Long[]> {

        @Override
        protected Long[] doInBackground(Long... posicoes) {
            // troca(posicoes[0], posicoes[1]);
            Long posicaoInicial = posicoes[0];
            Long posicaoFinal = posicoes[1];

            Nota notaInicial = dao.buscaPorPosicao(posicaoInicial);
            Nota notaFinal = dao.buscaPorPosicao(posicaoFinal);

            notaInicial.setId(notaFinal.getId());
            notaInicial.setPosicao(posicaoFinal);

            notaFinal.setId(dao.buscaPorPosicao(posicaoInicial).getId());
            notaFinal.setPosicao(posicaoInicial);

            dao.insere(notaInicial);
            dao.insere(notaFinal);

            return posicoes;
        }

        @Override
        protected void onPostExecute(Long... posicoes) {
            super.onPostExecute(posicoes);
            adapter.troca(posicoes[0], posicoes[1]);
        }
    }
}
