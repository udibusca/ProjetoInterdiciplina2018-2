package com.ubercom.root.projetoandroid2018.util;

import com.ubercom.root.projetoandroid2018.service.CategoriaService;
import com.ubercom.root.projetoandroid2018.service.ProdutoService;

public class APIUtils {

    public static final String API_URL = "https://ubercom.herokuapp.com/";

    private APIUtils(){
    }

    public static CategoriaService getCategoriaService(){
        return RetrofitClient.getClient(API_URL)
                .create(CategoriaService.class);
    }

    public static ProdutoService getProdutoService(){
        return RetrofitClient.getClient(API_URL)
                .create(ProdutoService.class);
    }

}
