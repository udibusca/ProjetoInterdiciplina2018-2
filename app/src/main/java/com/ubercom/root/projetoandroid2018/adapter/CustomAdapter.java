package com.ubercom.root.projetoandroid2018.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ubercom.root.projetoandroid2018.R;
import com.ubercom.root.projetoandroid2018.model.Categoria;

import java.util.List;

import retrofit2.Callback;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private List<Categoria> listCategoria;

    ViewHolder listViewHolder;

    public CustomAdapter(Context context, List<Categoria> customizedListView) {
        lInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listCategoria = customizedListView;
    }

    public CustomAdapter(Context context, Callback<List<Categoria>> callback, List<Categoria> listaCategorias) {
        lInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listCategoria = listaCategorias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.row_item_categoria, parent, false);

            listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.txtCategoriaNomeCheck);
            listViewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.textInListView.setText(listCategoria.get(position).getNome());
        listViewHolder.checkBox.setChecked(false);
        listViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewHolder.checkBox.isChecked()){
                    listViewHolder.checkBox.setChecked(false);
                }else{
                    listViewHolder.checkBox.setChecked(true);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder{

        TextView textInListView;
        CheckBox checkBox;
    }

    @Override
    public int getCount() {
        return listCategoria.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}