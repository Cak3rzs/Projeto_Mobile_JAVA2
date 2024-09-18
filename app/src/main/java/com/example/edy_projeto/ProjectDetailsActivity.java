package com.example.edy_projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class ProjectDetailsActivity extends AppCompatActivity {

    private EditText projectTitleEditText, projectDescriptionEditText;
    private Button saveProjectButton, addTaskButton;
    private ListView taskListView;
    private String[] taskNames = {"Tarefa 1", "Tarefa 2", "Tarefa 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        projectTitleEditText = findViewById(R.id.projectTitleEditText);
        projectDescriptionEditText = findViewById(R.id.projectDescriptionEditText);
        saveProjectButton = findViewById(R.id.saveProjectButton);
        addTaskButton = findViewById(R.id.addTaskButton);
        taskListView = findViewById(R.id.taskListView);

        // Configurar o adaptador para a ListView de tarefas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, taskNames);
        taskListView.setAdapter(adapter);

        // Clique para adicionar uma nova tarefa
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectDetailsActivity.this, TaskManagementActivity.class);
                startActivity(intent);
            }
        });

        // Clique para salvar o projeto
        saveProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Salvar os detalhes do projeto (implementação necessária)
            }
        });
    }
}
