package com.example.tainara.listadetarefas.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tainara.listadetarefas.R;
import com.example.tainara.listadetarefas.helper.TarefaDAO;
import com.example.tainara.listadetarefas.model.Tarefa;

import java.util.zip.Inflater;

public class AdicionarTarefaActivity extends AppCompatActivity {
    private TextInputEditText editTarefa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        editTarefa=findViewById(R.id.textTarefa);
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemSalvar:
                //Executa acao para o item salvar
                TarefaDAO tarefaDAO=new TarefaDAO(getApplicationContext());
                String nomeTarefa = editTarefa.getText().toString();
                if(!nomeTarefa.isEmpty()) {//so vamos salvar a tarefa se ela nao estiver vazia
                    Tarefa tarefa = new Tarefa();
                    tarefa.setNomeTarefa(nomeTarefa);
                    tarefaDAO.salvar(tarefa);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
