package com.example.tainara.listadetarefas.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tainara.listadetarefas.R;
import com.example.tainara.listadetarefas.adapter.TarefaAdapter;
import com.example.tainara.listadetarefas.helper.DbHelper;
import com.example.tainara.listadetarefas.helper.RecyclerItemClickListener;
import com.example.tainara.listadetarefas.helper.TarefaDAO;
import com.example.tainara.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas= new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //configurando o recycler view
        recyclerView= findViewById(R.id.recyclerView);
        //adicionando evento de clique no recycler view
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Recuperar tarefa para edição
                Tarefa tarefaSelecionada = listaTarefas.get(position);

                //Envia tarefa para tela adicionar tarefa
                Intent intent = new Intent(MainActivity.this,AdicionarTarefaActivity.class);
                intent.putExtra("tarefaSelecionada",tarefaSelecionada);

                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

                //recuperar a tarefa para deletar
                tarefaSelecionada = listaTarefas.get(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                //Configurar titulo e mensagem
                dialog.setTitle("Confirmar Exclusão");
                dialog.setMessage("Deseja excluir a tarefa : " + tarefaSelecionada.getNomeTarefa()  + " ?");

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                        if(tarefaDAO.deletar(tarefaSelecionada)){
                            carregarListaTarefas();
                            Toast.makeText(getApplicationContext(),"Tarefa Excluida!",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getApplicationContext(),"Erro ao excluir Tarefa!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.setNegativeButton("Não",null);

                //exibir dialog
                dialog.create();
                dialog.show();

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }) {
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),AdicionarTarefaActivity.class);
                startActivity(intent);
            }
        });
    }
    public void carregarListaTarefas(){
        //listar tarefas
        TarefaDAO tarefaDAO= new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();


        //configurando o adapter
        tarefaAdapter=new TarefaAdapter(listaTarefas);

        //banco de dados
        DbHelper db= new DbHelper(getApplicationContext());
        ContentValues cv= new ContentValues();
        //metodo para a escrita no banco de dados
        db.getWritableDatabase().insert("tarefas", null, cv);

        //configurar recycler view
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);


    }

    @Override
    protected void onStart() {
        carregarListaTarefas();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
