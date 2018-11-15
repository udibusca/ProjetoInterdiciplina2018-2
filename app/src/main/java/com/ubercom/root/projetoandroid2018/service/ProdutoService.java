package com.ubercom.root.projetoandroid2018.service;

import com.ubercom.root.projetoandroid2018.model.Produto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProdutoService {

    @GET("produtos/listaProdutos")
    Call<List<Produto>> getProdutos();

    @POST("produtos/")
    Call<Produto> addProduto(@Body Produto produto);

    @PUT("produtos/{id}")
    Call<Produto> updateProduto(@Path("id") int id, @Body Produto produto);

    @DELETE("produtos/{id}")
    Call<Produto> deleteProduto(@Path("id") int id);
}