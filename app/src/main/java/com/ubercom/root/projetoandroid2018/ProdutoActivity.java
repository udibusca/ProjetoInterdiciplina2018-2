        package com.ubercom.root.projetoandroid2018;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.ubercom.root.projetoandroid2018.adapter.CustomAdapter;
        import com.ubercom.root.projetoandroid2018.adapter.ProdutoAdapter;
        import com.ubercom.root.projetoandroid2018.model.Categoria;
        import com.ubercom.root.projetoandroid2018.model.Produto;
        import com.ubercom.root.projetoandroid2018.service.CategoriaService;
        import com.ubercom.root.projetoandroid2018.service.ProdutoService;
        import com.ubercom.root.projetoandroid2018.util.APIUtils;

        import java.math.BigDecimal;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.List;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;


        public class ProdutoActivity extends AppCompatActivity {

            ListView listView;
            ListView listViewCat;
            ListView listViewCatAtualiza;
            View popupInputDialogView;
            ViewGroup parent;
            ProdutoService produtoService;
            CategoriaService categoriaService;
            List<Produto> listaProdutos = new ArrayList<Produto>();
            List<Categoria> listaCategorias;
            List<Categoria> listaCategoriasUpdate;
            private AlertDialog alerta;
            ProdutoAdapter produtoAdapter;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_produto);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                listView = (ListView) findViewById(R.id.listaProdutos);
                produtoService = APIUtils.getProdutoService();
                //get nos Produto
                getProdutosList();

                FloatingActionButton btnAddProduto = (FloatingActionButton) findViewById(R.id.botaoMais);
                btnAddProduto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        abrirModalCadastro();
                    }
                });

                // Long click para poder a modal para atualizar ou deletar
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                        Produto produto = listaProdutos.get(posicao);
                        Call<Produto> call = produtoService.getProduto(produto.getId());
                        call.enqueue(new Callback<Produto>() {
                            @Override
                            public void onResponse(Call<Produto> call, Response<Produto> response) {
                                if(response.isSuccessful()){
                                    Produto produtoUnico = response.body();
                                    // Chama a modal
                                    showAtualizaDeletaDialog(produtoUnico);
                                }
                            }
                            @Override
                            public void onFailure(Call<Produto> call, Throwable t) {
                                Log.e("ERROR: ", t.getMessage());
                            }
                        });
                        }
                });

                // Para o botão Up na Action Bar
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            public void getProdutosList(){
                Call<List<Produto>> call = produtoService.getProdutos();
                call.enqueue(new Callback<List<Produto>>() {
                    @Override
                    public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                        if(response.isSuccessful()){
                            listaProdutos = response.body();
                            // Order a lista de Produtos por ordem alfabetica
                            Collections.sort(listaProdutos, new Comparator<Produto>() {
                                @Override
                                public int compare(Produto o1, Produto o2) {
                                    return o1.getNome().compareTo(o2.getNome());
                                }

                            });
                            produtoAdapter = new ProdutoAdapter(ProdutoActivity.this, R.layout.item_produto, listaProdutos);
                            listView.setAdapter(produtoAdapter);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Produto>> call, Throwable t) {
                        Log.e("ERROR: ", t.getMessage());
                    }
                });
            }

            private void abrirModalCadastro() {
                //Cria o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //define o titulo
                builder.setTitle("Novo Produto");

                LayoutInflater layoutInflater = LayoutInflater.from(ProdutoActivity.this);
                popupInputDialogView = layoutInflater.inflate(R.layout.dialog_add_produto, null);

                final TextView imputNomeProduto = (TextView) popupInputDialogView.findViewById(R.id.produto_nome);
                final TextView imputPrecoProduto = (TextView) popupInputDialogView.findViewById(R.id.produto_preco);
                listViewCat =  (ListView) popupInputDialogView.findViewById(R.id.listCategoriasCheck);

                categoriaService = APIUtils.getCategoriaService();
                Call<List<Categoria>> call = categoriaService.getCategorias();
                call.enqueue(new Callback<List<Categoria>>() {//chamada assíncrona
                    public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                        int statusCode = response.code();
                        listaCategorias = response.body();
                        listViewCat.setAdapter(new CustomAdapter(ProdutoActivity.this, listaCategorias));
                    }

                    public void onFailure(Call<List<Categoria>> call, Throwable t) {
                        // Log error here since request failed
                        Log.i("teste", t.toString());
                    }
                });
                builder.setView(popupInputDialogView);

                //define um botão como negativo.
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });

                //define um botão como positivo
                builder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Criar um intent para chamar novamente apos cadastrar um item
                        Intent intent = new Intent(ProdutoActivity.this, ProdutoActivity.class);

                        Produto produto = new Produto();
                        if(imputNomeProduto.getText().toString().isEmpty() || imputPrecoProduto.getText().toString().isEmpty()){
                            Toast.makeText(ProdutoActivity.this, "Favor preencher todos campos!", Toast.LENGTH_SHORT).show();
                        }else {
                            produto.setNome(imputNomeProduto.getText().toString());
                            BigDecimal valor = new BigDecimal(imputPrecoProduto.getText().toString());
                            produto.setPreco(valor);
                            ArrayList<Categoria> listSelecionados = new ArrayList<>();
                            for(int i = 0; i < listaCategorias.size(); i++){
                                if (listaCategorias.get(i).getSelected()) {
                                    listSelecionados.add(listaCategorias.get(i));
                                    produto.setCategorias(listSelecionados);
                                }
                            }
                            //add produto
                            Log.i("Vai ser Cadastrado", " : "+produto.toString());
                            addProduto(produto);
                            startActivity(intent);
                            Toast.makeText(ProdutoActivity.this, "Produto criada com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //cria o AlertDialog
                alerta = builder.create();
                //Exibe
                alerta.show();
            }

            private void showAtualizaDeletaDialog(final Produto produto) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_atualizar_produto, null);
                listViewCatAtualiza =  (ListView) dialogView.findViewById(R.id.listCategoriasCheckUpdate);

                dialogBuilder.setView(dialogView);

                final EditText editTextNome = (EditText) dialogView.findViewById(R.id.edtProdutoNome);
                final EditText editTextPreco = (EditText) dialogView.findViewById(R.id.edtProdutoPreco);
                //final EditText listaCategoriasEdit = (EditText) dialogView.findViewById(R.id.listCategoriasCheckUpdate);
                final Button buttonAtualizar = (Button) dialogView.findViewById(R.id.buttonUpdateProduto);
                final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduto);
                final Button buttonCancelarProduto = (Button) dialogView.findViewById(R.id.buttonCancelarProduto);


                dialogBuilder.setTitle("ID: "+produto.getId());
                editTextNome.setText(produto.getNome());
                editTextPreco.setText(produto.getPreco().toString());

                // Pega todas as categorias para poder dar o check somente nas que vier do produto.
                categoriaService = APIUtils.getCategoriaService();
                Call<List<Categoria>> call = categoriaService.getCategorias();
                call.enqueue(new Callback<List<Categoria>>() {//chamada assíncrona
                    public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                        int statusCode = response.code();
                        listaCategoriasUpdate = response.body();

                        for (Categoria cat : produto.getCategorias()) {
                            if (listaCategoriasUpdate.contains(cat)) {
                                int pos = listaCategoriasUpdate.indexOf(cat);
                                listaCategoriasUpdate.get(pos).setSelected(true);
                            }
                            listViewCatAtualiza.setAdapter(new CustomAdapter(ProdutoActivity.this, listaCategoriasUpdate));
                        }

                    }

                    public void onFailure(Call<List<Categoria>> call, Throwable t) {
                    }
                });

                final AlertDialog b = dialogBuilder.create();
                b.show();

                // Botão Atualizar
                buttonAtualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Produto produtoUpdate = new Produto();
                        produtoUpdate.setNome(editTextNome.getText().toString());
                        BigDecimal valor = new BigDecimal(editTextPreco.getText().toString());
                        produtoUpdate.setPreco(valor);
                        produtoUpdate.setCategorias(listaCategoriasUpdate);
                        //update produto
                        updateProduto(produto.getId(), produtoUpdate);
                        b.dismiss();
                        Intent intent = new Intent(ProdutoActivity.this, ProdutoActivity.class);
                        startActivity(intent);
                    }
                });

                // Botão detetar
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteProduto(produto.getId());
                        b.dismiss();
                        Intent intent = new Intent(ProdutoActivity.this, ProdutoActivity.class);
                        startActivity(intent);
                    }
                });

                // Fechar a modal
                buttonCancelarProduto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();
                    }
                });

            }


            // Add produto
            public void addProduto(Produto produto){
                Call<Produto> call = produtoService.addProduto(produto);
                call.enqueue(new Callback<Produto>() {
                    @Override
                    public void onResponse(Call<Produto> call, Response<Produto> response) {
                        if(response.isSuccessful()){
                            Log.e("Mensagem : : : ", response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Produto> call, Throwable t) {
                        Log.e("ERROR: ", t.getMessage());
                    }
                });
            }

            public void deleteProduto(int id){
                Call<Produto> call = produtoService.deleteProduto(id);
                call.enqueue(new Callback<Produto>() {
                    @Override
                    public void onResponse(Call<Produto> call, Response<Produto> response) {
                        if(response.isSuccessful())
                            Toast.makeText(ProdutoActivity.this, "Categoria deletada com sucesso!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Produto> call, Throwable t) {
                        Log.e("ERROR: ", t.getMessage());
                    }
                });
            }

            public void updateProduto(int id, Produto u){
                Call<Produto> call = produtoService.updateProduto(id, u);
                call.enqueue(new Callback<Produto>() {
                    @Override
                    public void onResponse(Call<Produto> call, Response<Produto> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(ProdutoActivity.this, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Produto> call, Throwable t) {
                        Log.e("ERROR: ", t.getMessage());
                    }
                });
            }

        }