package com.example.edy_projeto;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class ProjectDetailsActivity extends AppCompatActivity {

    private EditText projectTitleEditText, projectDescriptionEditText;
    private Button saveProjectButton, addTaskButton, deleteTaskButton;
    private ListView taskListView;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> taskNames;
    private HashMap<Integer, Integer> taskIdMap; // Mapeia a posição da lista para o ID da tarefa no BD
    private int selectedTaskId = -1; // ID da tarefa selecionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        projectTitleEditText = findViewById(R.id.projectTitleEditText);
        projectDescriptionEditText = findViewById(R.id.projectDescriptionEditText);
        saveProjectButton = findViewById(R.id.saveProjectButton);
        addTaskButton = findViewById(R.id.addTaskButton);
        deleteTaskButton = findViewById(R.id.deleteTaskButton);
        taskListView = findViewById(R.id.taskListView);
        databaseHelper = new DatabaseHelper(this);
        taskIdMap = new HashMap<>();

        // Inicializa o botão de excluir tarefa como invisível
        deleteTaskButton.setVisibility(View.GONE);

        // Carrega as tarefas do banco de dados e configura a lista
        loadTaskList();

        // Clique para adicionar uma nova tarefa
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectDetailsActivity.this, TaskManagementActivity.class);
                int projectId = getIntent().getIntExtra("project_id", -1); // Obtém o ID do projeto atual
                intent.putExtra("project_id", projectId); // Passa o ID do projeto para a próxima activity
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

        // Clique em um item da lista de tarefas
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Obtém o ID da tarefa selecionada a partir do mapa
                selectedTaskId = taskIdMap.get(position);
                deleteTaskButton.setVisibility(View.VISIBLE); // Exibe o botão de deletar tarefa
            }
        });

        // Clique no botão para excluir a tarefa selecionada
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTaskId != -1) {
                    showDeleteConfirmationDialog(); // Exibe diálogo de confirmação antes de excluir
                }
            }
        });
    }

    // Método para salvar os detalhes do projeto
    private void saveProject() {
        String projectTitle = projectTitleEditText.getText().toString().trim();
        String projectDescription = projectDescriptionEditText.getText().toString().trim();

        // Valida se os campos estão preenchidos
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

    // Método para carregar a lista de tarefas do banco de dados
    private void loadTaskList() {
        taskNames = new ArrayList<>();
        taskIdMap.clear();  // Limpa o mapa antes de recarregar

        // Obtém as tarefas do banco de dados associadas ao projeto
        int projectId = getIntent().getIntExtra("project_id", -1);
        Cursor cursor = databaseHelper.getTasksForProject(projectId);

        if (cursor.getCount() == 0) {
            taskNames.add("Sem tarefas para este projeto");
        }

        int index = 0;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int taskId = cursor.getInt(cursor.getColumnIndex("TASK_ID"));
            @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex("TASK_NAME"));

            // Adiciona a tarefa à lista e ao mapa de IDs
            taskNames.add(taskName);
            taskIdMap.put(index, taskId);
            index++;
        }
        cursor.close();

        // Configura o adaptador para a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, taskNames);
        taskListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTaskList();  // Recarrega a lista de tarefas sempre que a Activity é retomada
    }

    // Método para confirmar a exclusão de uma tarefa
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Você tem certeza que deseja excluir esta tarefa?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTask();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    // Método para excluir uma tarefa
    private void deleteTask() {
        boolean isDeleted = databaseHelper.deleteTask(selectedTaskId);
        if (isDeleted) {
            Toast.makeText(ProjectDetailsActivity.this, "Tarefa excluída!", Toast.LENGTH_SHORT).show();
            loadTaskList(); // Recarrega a lista de tarefas
            deleteTaskButton.setVisibility(View.GONE); // Oculta o botão de deletar
        } else {
            Toast.makeText(ProjectDetailsActivity.this, "Erro ao excluir a tarefa.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para salvar tarefa e atualizar a lista
    private void saveTask(String taskName, int projectId, String dueDate) {
        boolean isInserted = databaseHelper.insertTask(taskName, String.valueOf(projectId), dueDate);
        if (isInserted) {
            Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
            loadTaskList();  // Atualiza a lista imediatamente
        } else {
            Toast.makeText(this, "Erro ao salvar tarefa.", Toast.LENGTH_SHORT).show();
        }
    }
}
