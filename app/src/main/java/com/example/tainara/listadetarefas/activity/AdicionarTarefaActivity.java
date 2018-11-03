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
    private Tarefa tarefaAtual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        editTarefa=findViewById(R.id.textTarefa);

        //Recuperar tarefa, caso seja edição
        tarefaAtual =(Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //Configurar tarefa na caixa de texto
        if(tarefaAtual != null){
            editTarefa.setText(tarefaAtual.getNomeTarefa());
        }
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
                if (tarefaAtual != null) {//ediçao
                    String nomeTarefa = editTarefa.getText().toString();
                    if(!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());

                        //atualiza no banco de dados
                        if(tarefaDAO.atualizar(tarefa)){
                            finish();
                            Toast.makeText(getApplicationContext(),"Tarefa atualizada com sucesso!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Erro ao atualizar tarefa!",Toast.LENGTH_SHORT).show();
                        }

                    }
                }else{//salvar

                    String nomeTarefa = editTarefa.getText().toString();
                    if(!nomeTarefa.isEmpty()) {//so vamos salvar a tarefa se ela nao estiver vazia
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);

                        if(tarefaDAO.salvar(tarefa)){
                            finish();
                            Toast.makeText(getApplicationContext(),"Tarefa salva com sucesso!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Erro ao salvar tarefa!",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
