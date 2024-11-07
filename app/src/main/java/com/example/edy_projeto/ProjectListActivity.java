package com.example.edy_projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProjectListActivity extends AppCompatActivity {

    private ListView projectListView;
    private Button addProjectButton, deleteProjectButton;
    private DatabaseHelper dbHelper;
    private ArrayList<String> projectTitles;
    private int selectedProjectId = -1;  // Variável para armazenar o ID do projeto selecionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        projectListView = findViewById(R.id.projectListView);
        addProjectButton = findViewById(R.id.addProjectButton);
        deleteProjectButton = findViewById(R.id.deleteProjectButton); // Referencia o botão de deletar
        dbHelper = new DatabaseHelper(this);

        // Obtém todos os títulos de projetos do banco de dados
        projectTitles = dbHelper.getAllProjectTitles();

        // Configura o adaptador para a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, projectTitles);
        projectListView.setAdapter(adapter);

        // Clique em um item da lista
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Armazena o ID do projeto selecionado
                selectedProjectId = position;  // Aqui, seria necessário pegar o ID real do banco de dados
                deleteProjectButton.setVisibility(View.VISIBLE); // Exibe o botão de deletar

                // Abrir a tela de detalhes do projeto
                Intent intent = new Intent(ProjectListActivity.this, ProjectDetailsActivity.class);
                // Passar informações do projeto selecionado, como título
                intent.putExtra("project_title", projectTitles.get(position));
                startActivity(intent);
            }
        });

        // Clique no botão para adicionar um novo projeto
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectListActivity.this, ProjectDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Clique no botão para excluir o projeto selecionado
        deleteProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedProjectId != -1) {
                    // Excluir o projeto do banco de dados (apenas se um projeto estiver selecionado)
                    boolean isDeleted = dbHelper.deleteProject(selectedProjectId);
                    if (isDeleted) {
                        Toast.makeText(ProjectListActivity.this, "Projeto excluído!", Toast.LENGTH_SHORT).show();
                        // Recarregar a lista de projetos
                        loadProjectList();
                    } else {
                        Toast.makeText(ProjectListActivity.this, "Falha ao excluir projeto.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Método para atualizar a lista de projetos
    private void loadProjectList() {
        // Aqui, você pode recarregar os projetos da base de dados e atualizar a ListView
        // Exemplo simples de recarga, adaptado conforme a sua necessidade:
        projectTitles = dbHelper.getAllProjectTitles(); // Isso deve retornar o array de projetos reais
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, projectTitles);
        projectListView.setAdapter(adapter);
    }
}
