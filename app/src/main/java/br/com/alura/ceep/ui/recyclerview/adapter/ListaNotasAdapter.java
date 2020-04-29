package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.database.NotaDataBase;
import br.com.alura.ceep.model.Cor;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final List<Nota> notas;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    NotaDataBase databaseDao;

    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
        databaseDao = NotaDataBase.getInstance(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaNotasAdapter.NotaViewHolder holder, int position) {
        Nota nota = notas.get(position);
        holder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }


    public void altera(Nota nota) {
        notas.set(nota.getPosicao().intValue(), nota);
        notifyItemChanged(nota.getPosicao().intValue(), nota);
    }

    public void remove(int posicao) {
        if (notas.isEmpty()) return;
        notas.remove(posicao);
        notifyItemRemoved(posicao);

        for (Nota nota : notas) {
            if (nota.getPosicao() > posicao) {
                nota.setPosicao(nota.getPosicao() - 1);
                databaseDao.getRoomNotaDAO().insere(nota);
            }
        }

    }

    public void troca(int posicaoInicial, int posicaoFinal) {

        Collections.swap(notas, posicaoInicial, posicaoFinal);
        notifyItemMoved(posicaoInicial, posicaoFinal);
    }

    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;
        private final CardView cardView;
        private Nota nota;

        public NotaViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
            cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(nota);
                }
            });
        }

        public void vincula(Nota nota) {
            this.nota = nota;
            preencheCampo(nota);
        }

        private void preencheCampo(Nota nota) {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());

            Cor corBuscada = databaseDao.getRoomCorDAO().buscaCorPorID(nota.getCorId());
            nota.setCor(corBuscada);
            cardView.setCardBackgroundColor(corBuscada.getCor());
        }
    }

    public void adiciona(Nota nota) {
        if (verificaSeNotaJapossuiPosicaoSalvaNoBanco(nota)) {
            nota.setPosicao((long) notas.size());
            databaseDao.getRoomNotaDAO().insere(nota);
        }
        notas.add(nota);
        notifyItemInserted(nota.getPosicao().intValue());
    }

    private boolean verificaSeNotaJapossuiPosicaoSalvaNoBanco(Nota nota) {
        return nota.getPosicao() == -1;
    }

}
