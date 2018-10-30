package com.example.tainara.listadetarefas.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
                Log.i( "clique ","onItemClick");
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Log.i( "clique ","onLongItemClick");
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
        cv.put("nome","teste");
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
