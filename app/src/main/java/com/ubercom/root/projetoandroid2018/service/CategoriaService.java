package com.ubercom.root.projetoandroid2018.service;
import com.ubercom.root.projetoandroid2018.model.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoriaService {

    @GET("categorias/")
    Call<List<Categoria>> getCategorias();

    @GET("categorias/{id}")
    Call<Categoria> getCategoria(@Path("id") int id);

    @POST("categorias/")
    Call<Categoria> addCategoria(@Body Categoria categoria);

    @PUT("categorias/{id}")
    Call<Categoria> updateCategoria(@Path("id") int id, @Body Categoria categoria);

    @DELETE("categorias/{id}")
    Call<Categoria> deleteCategoria(@Path("id") int id);
}