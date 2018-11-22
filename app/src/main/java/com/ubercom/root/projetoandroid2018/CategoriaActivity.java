package com.ubercom.root.projetoandroid2018;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.amulyakhare.textdrawable.TextDrawable;
        import com.ubercom.root.projetoandroid2018.adapter.CategoriaAdapter;
        import com.ubercom.root.projetoandroid2018.adapter.ProdutoAdapter;
        import com.ubercom.root.projetoandroid2018.model.Categoria;
        import com.ubercom.root.projetoandroid2018.model.Produto;
        import com.ubercom.root.projetoandroid2018.service.CategoriaService;
        import com.ubercom.root.projetoandroid2018.util.APIUtils;

        import java.sql.Array;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.List;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class CategoriaActivity extends AppCompatActivity {

    ListView listView;
    CategoriaService categoriaService;
    CategoriaAdapter categoriaAdapter;
    ProdutoAdapter produtoAdapter;
    List<Categoria> listaCategorias = new ArrayList<Categoria>();
    private Categoria categoria;
    private AlertDialog alerta;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listaCategoria);
        categoriaService = APIUtils.getCategoriaService();
        //get nas categorias
        getCategoriasList();

        FloatingActionButton btnAddCategoria = (FloatingActionButton) findViewById(R.id.botaoMaisCategoria);
        btnAddCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirModalCadastro();
            }
        });

        // click para poder a modal para atualizar ou deletar
        //click para poder a modal para atualizar ou deletar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Categoria categoria = listaCategorias.get(i);
                // Chama a modal
                showAtualizaDeletaDialog(categoria.getId(), categoria.getNome());;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Categoria categoria = listaCategorias.get(i);
                // Chama a modal
                showAtualizaDeletaDialog(categoria.getId(), categoria.getNome());
            }
        });

        // Para o botão Up na Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getCategoriasList(){
        Call<List<Categoria>> call = categoriaService.getCategorias();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(response.isSuccessful()){
                    listaCategorias = response.body();
                    // Order a lista de categoria por ordem alfabetica
                    Collections.sort(listaCategorias, new Comparator<Categoria>() {
                        @Override
                        public int compare(Categoria o1, Categoria o2) {
                            return o1.getNome().compareTo(o2.getNome());
                        }

                    });
                    categoriaAdapter = new CategoriaAdapter(CategoriaActivity.this, R.layout.item_categoria, listaCategorias);
                    listView.setAdapter(categoriaAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });

    }
    @Deprecated
    private void buscaProdutoPorIdCategoria(int id) {
        Call<Categoria> call = categoriaService.getCategoria(id);
        call.enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                if(response.isSuccessful()){
                    Log.e("Mensagem : : : ", response.toString());
                    int statusCode = response.code();
                    categoria = response.body();
                    mostraProdutos(categoria);
                }
            }

            @Override
            public void onFailure(Call<Categoria> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    private void mostraProdutos(final Categoria categoria) {

            setTitle("Lista de produtos");
            if(categoria.getProdutos().size() == 0){
                Toast.makeText(CategoriaActivity.this, "Não existe produtos cadastrados para esta categoria!", Toast.LENGTH_SHORT).show();
            }
            final List<Produto> listaProdutos = categoria.getProdutos();

        // Order a lista de Produtos por ordem alfabetica
        Collections.sort(listaProdutos, new Comparator<Produto>() {
            @Override
            public int compare(Produto o1, Produto o2) {
                return o1.getNome().compareTo(o2.getNome());
            }

        });
        produtoAdapter = new ProdutoAdapter(CategoriaActivity.this, R.layout.item_produto, listaProdutos);
        listView.setAdapter(produtoAdapter);
        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.botaoMaisCategoria);
        btnAdd.setEnabled(false);
    }


    private void abrirModalCadastro() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Nova Categoria");

        LayoutInflater layoutInflater = LayoutInflater.from(CategoriaActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.dialog_add_categoria, null);

        final TextView imputNome = (TextView) popupInputDialogView.findViewById(R.id.categoria_nome);

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
                Intent intent = new Intent(CategoriaActivity.this, CategoriaActivity.class);

                Categoria cat = new Categoria();
                if(imputNome.getText().toString().isEmpty()){
                    Toast.makeText(CategoriaActivity.this, "Favor informar o nome da categoria!", Toast.LENGTH_SHORT).show();
                }else {
                    cat.setNome(imputNome.getText().toString());
                    //add categoria
                    addCategoria(cat);
                    startActivity(intent);
                    Toast.makeText(CategoriaActivity.this, "Categoria criada com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void showAtualizaDeletaDialog(final int categoriaId, String categoriaName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_atualizar_categoria, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNome = (EditText) dialogView.findViewById(R.id.edtCategoriaNome);
        final Button buttonAtualizar = (Button) dialogView.findViewById(R.id.buttonUpdateCategoria);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteCategoria);
        final Button buttonCancelar = (Button) dialogView.findViewById(R.id.buttonCancelar);


        dialogBuilder.setTitle("ID: "+categoriaId);
        editTextNome.setText(categoriaName);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Botão Atualizar
        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Categoria cat = new Categoria();
                cat.setNome(editTextNome.getText().toString());
                //update categoria
                updateCategoria(categoriaId, cat);
                b.dismiss();
                Intent intent = new Intent(CategoriaActivity.this, CategoriaActivity.class);
                startActivity(intent);
            }
        });

        // Botão detetar
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategoria(categoriaId);
                b.dismiss();
                Intent intent = new Intent(CategoriaActivity.this, CategoriaActivity.class);
                startActivity(intent);
            }
        });

        // Fechar a modal
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }

    public void addCategoria(Categoria cat){
        Call<Categoria> call = categoriaService.addCategoria(cat);
        call.enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                if(response.isSuccessful()){
                    Log.e("Mensagem : : : ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Categoria> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void deleteCategoria(int id){
        Call<Categoria> call = categoriaService.deleteCategoria(id);
        call.enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                if(response.isSuccessful())
                    Toast.makeText(CategoriaActivity.this, "Categoria deletada com sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Categoria> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void updateCategoria(int id, Categoria u){
        Call<Categoria> call = categoriaService.updateCategoria(id, u);
        call.enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                if(response.isSuccessful()){
                    Toast.makeText(CategoriaActivity.this, "Categoria atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Categoria> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

}
