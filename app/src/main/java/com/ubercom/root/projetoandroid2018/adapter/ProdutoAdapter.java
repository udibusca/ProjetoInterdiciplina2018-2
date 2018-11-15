package com.ubercom.root.projetoandroid2018.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ubercom.root.projetoandroid2018.R;
import com.ubercom.root.projetoandroid2018.model.Produto;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdutoAdapter extends ArrayAdapter<Produto> {

    private Context context;
    private List<Produto> produtos;

    ColorGenerator generatorColor = ColorGenerator.MATERIAL;

    public ProdutoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Produto> objects) {
        super(context, resource, objects);
        this.context = context;
        this.produtos = objects;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_produto, parent, false);


        // Montar a imagem com base na primeira letra do nome
        String nomeProd = produtos.get(pos).getNome();
        String firstCharNome = nomeProd.substring(0, 1);
        int color = generatorColor.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstCharNome, color);

        ImageView image = (ImageView) rowView.findViewById(R.id.txtProdutoImagem);
        //TextView txtProdutoId = (TextView) rowView.findViewById(R.id.txtProdutoId);
        TextView txtProdutoNome = (TextView) rowView.findViewById(R.id.txtProdutoNome);
        TextView txtProdutoPreco = (TextView) rowView.findViewById(R.id.txtProdutoPreco);

        image.setImageDrawable(drawable);
        //txtProdutoId.setText(String.format("ID: %d", produtos.get(pos).getId()));
        txtProdutoNome.setText(String.format("%s", produtos.get(pos).getNome()));

        NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        txtProdutoPreco.setText("R$ " + nf.format(produtos.get(pos).getPreco()));

        return rowView;
    }

    public int getItemCount() {
        return produtos.size();
    }


}