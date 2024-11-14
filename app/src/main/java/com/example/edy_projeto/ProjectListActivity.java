package com.example.edy_projeto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class ProjectListActivity extends AppCompatActivity {

    private ListView projectListView;
    private Button addProjectButton, deleteProjectButton;
    private DatabaseHelper dbHelper;
    private ArrayList<String> projectTitles;
    private HashMap<Integer, Integer> projectIdMap; // Mapeia posição da lista para o ID real
    private int selectedProjectId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        projectListView = findViewById(R.id.projectListView);
        addProjectButton = findViewById(R.id.addProjectButton);
        deleteProjectButton = findViewById(R.id.deleteProjectButton); // Botão de exclusão
        dbHelper = new DatabaseHelper(this);
        projectIdMap = new HashMap<>();

        // Carrega todos os projetos do banco de dados
        loadProjectList();

        // Clique em um item da lista
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedProjectId = projectIdMap.get(position);
                Intent intent = new Intent(ProjectListActivity.this, ProjectDetailsActivity.class);
                intent.putExtra("project_id", selectedProjectId);
                startActivity(intent);
            }
        });

        // Clique para adicionar um novo projeto
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectListActivity.this, ProjectDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Clique para excluir o projeto selecionado
        deleteProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedProjectId != -1) {
                    boolean isDeleted = dbHelper.deleteProject(selectedProjectId);
                    if (isDeleted) {
                        Toast.makeText(ProjectListActivity.this, "Projeto excluído!", Toast.LENGTH_SHORT).show();
                        loadProjectList();
                    } else {
                        Toast.makeText(ProjectListActivity.this, "Erro ao excluir projeto.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProjectListActivity.this, "Selecione um projeto para excluir.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para carregar a lista de projetos
    private void loadProjectList() {
        projectTitles = new ArrayList<>();
        projectIdMap.clear();

        // Obtém todos os projetos do banco de dados
        Cursor cursor = dbHelper.getAllProjects();
        int index = 0;

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int projectId = cursor.getInt(cursor.getColumnIndex("PROJECT_ID"));
            @SuppressLint("Range") String projectTitle = cursor.getString(cursor.getColumnIndex("TITLE"));

            projectTitles.add(projectTitle);
            projectIdMap.put(index, projectId);
            index++;
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, projectTitles);
        projectListView.setAdapter(adapter);

        deleteProjectButton.setVisibility(View.VISIBLE); // Certifica-se de que o botão está visível
    }
}
