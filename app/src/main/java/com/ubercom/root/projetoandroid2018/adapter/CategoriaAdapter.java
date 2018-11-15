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
import com.ubercom.root.projetoandroid2018.model.Categoria;

import java.util.List;

public class CategoriaAdapter extends ArrayAdapter<Categoria> {

    private Context context;
    private List<Categoria> categorias;

    ColorGenerator generatorColor = ColorGenerator.MATERIAL;

    public CategoriaAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Categoria> objects) {
        super(context, resource, objects);
        this.context = context;
        this.categorias = objects;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_categoria, parent, false);


        // Montar a imagem com base na primeira letra do nome
        String nome = categorias.get(pos).getNome();
        String firstCharNome = nome.substring(0, 1);
        int color = generatorColor.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstCharNome, color);

        ImageView image = (ImageView) rowView.findViewById(R.id.txtCategoriaImagem);
        TextView txtCategoriaId = (TextView) rowView.findViewById(R.id.txtCategoriaId);
        TextView txtCategoriaNome = (TextView) rowView.findViewById(R.id.txtCategoriaNome);

        image.setImageDrawable(drawable);
        txtCategoriaId.setText(String.format("ID: %d", categorias.get(pos).getId()));
        txtCategoriaNome.setText(String.format("%s", categorias.get(pos).getNome()));

        return rowView;
    }

    public int getItemCount() {
        return categorias.size();
    }


}