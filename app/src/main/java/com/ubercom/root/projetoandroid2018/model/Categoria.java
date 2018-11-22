package com.ubercom.root.projetoandroid2018.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Categoria {

    private int id;
    private String nome;
    private List<Produto> produtos = new ArrayList<Produto>();
    private boolean isSelected;

    public Categoria() {
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id + ", nome='" + nome +'}';
    }
}