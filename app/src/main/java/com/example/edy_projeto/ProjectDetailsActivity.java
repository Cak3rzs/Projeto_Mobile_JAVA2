package com.example.edy_projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProjectDetailsActivity extends AppCompatActivity {

    private EditText projectTitleEditText, projectDescriptionEditText;
    private Button saveProjectButton, addTaskButton;
    private ListView taskListView;
    private String[] taskNames = {"Tarefa 1", "Tarefa 2", "Tarefa 3"};
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        projectTitleEditText = findViewById(R.id.projectTitleEditText);
        projectDescriptionEditText = findViewById(R.id.projectDescriptionEditText);
        saveProjectButton = findViewById(R.id.saveProjectButton);
        addTaskButton = findViewById(R.id.addTaskButton);
        taskListView = findViewById(R.id.taskListView);
        databaseHelper = new DatabaseHelper(this);

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
                saveProject();
            }
        });
    }

    // Método para salvar os detalhes do projeto
    private void saveProject() {
        String projectTitle = projectTitleEditText.getText().toString().trim();
        String projectDescription = projectDescriptionEditText.getText().toString().trim();

        if (projectTitle.isEmpty() || projectDescription.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = databaseHelper.insertProject(projectTitle, projectDescription);

        if (isInserted) {
            Toast.makeText(this, "Projeto salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a Activity após salvar
        } else {
            Toast.makeText(this, "Erro ao salvar o projeto.", Toast.LENGTH_SHORT).show();
        }
    }
}
