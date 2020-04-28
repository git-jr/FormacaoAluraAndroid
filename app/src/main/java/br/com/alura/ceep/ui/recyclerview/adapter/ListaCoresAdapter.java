package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Cor;

public class ListaCoresAdapter extends RecyclerView.Adapter<ListaCoresAdapter.CorViewHolder> {
    private final List<Cor> cores;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ListaCoresAdapter(List<Cor> cores, Context context) {
        this.cores = cores;
        this.context = context;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(context).inflate(R.layout.item_cor, parent, false);
        return new CorViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(CorViewHolder holder, int position) {
        Cor cor = cores.get(position);
        holder.vincula(cor);

    }

    @Override
    public int getItemCount() {
        return cores.size();
    }

    class CorViewHolder extends RecyclerView.ViewHolder {
        private final View cor_item;
        private Cor cor;

        public CorViewHolder(View itemView) {
            super(itemView);
            cor_item = itemView.findViewById(R.id.cor_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(cor, getAdapterPosition());
                }
            });
        }

        public void vincula(Cor cor) {
            this.cor = cor;

            Drawable drawableCor = ContextCompat.getDrawable(context, R.drawable.circulo_cor);
            drawableCor.setColorFilter(cor.getCor(), PorterDuff.Mode.SRC_ATOP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.cor_item.setBackground(drawableCor);
            } else {
                this.cor_item.setBackgroundColor(cor.getCor());
            }

            /*
             Fazer isso como solicitado modificando o "Background" não foi possível abaixo da API JELLY_BEAN.
             Como alternativa para essas versões a cor do background é modificada, deixando o fundo do item com a forma de um quadrado
            */
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Cor cor, int position);
    }
}
