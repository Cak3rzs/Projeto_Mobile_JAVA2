package com.example.edy_projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class ProjectListActivity extends AppCompatActivity {

    private ListView projectListView;
    private Button addProjectButton;
    private String[] projectNames = {"Projeto 1", "Projeto 2", "Projeto 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        projectListView = findViewById(R.id.projectListView);
        addProjectButton = findViewById(R.id.addProjectButton);

        // Configurar o adaptador para a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, projectNames);
        projectListView.setAdapter(adapter);

        // Clique em um item da lista
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Abrir a tela de detalhes do projeto
                Intent intent = new Intent(ProjectListActivity.this, ProjectDetailsActivity.class);
                // Passar informações do projeto selecionado (se necessário)
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
    }
}
