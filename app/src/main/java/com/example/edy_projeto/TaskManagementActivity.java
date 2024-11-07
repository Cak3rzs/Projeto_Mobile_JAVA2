package com.example.edy_projeto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import java.util.Calendar;

public class TaskManagementActivity extends AppCompatActivity {

    private EditText taskTitleEditText, taskDescriptionEditText, taskDueDateEditText;
    private Button saveTaskButton;
    private DatabaseHelper databaseHelper;  // Instância do helper de banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);

        // Inicializando os componentes da interface
        taskTitleEditText = findViewById(R.id.taskTitleEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        taskDueDateEditText = findViewById(R.id.taskDueDateEditText);
        saveTaskButton = findViewById(R.id.saveTaskButton);

        // Inicializando o helper de banco de dados
        databaseHelper = new DatabaseHelper(this);

        // Abrir um DatePicker ao clicar no campo de data
        taskDueDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskManagementActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                taskDueDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Clique para salvar a tarefa
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obter os dados da tarefa
                String taskTitle = taskTitleEditText.getText().toString();
                String taskDescription = taskDescriptionEditText.getText().toString();
                String taskDueDate = taskDueDateEditText.getText().toString();

                // Validar os dados
                if (taskTitle.isEmpty() || taskDescription.isEmpty() || taskDueDate.isEmpty()) {
                    Toast.makeText(TaskManagementActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    // Inserir a tarefa no banco de dados
                    boolean isInserted = databaseHelper.insertTask(taskTitle, taskDescription, taskDueDate);

                    // Verificar se a tarefa foi salva com sucesso
                    if (isInserted) {
                        Toast.makeText(TaskManagementActivity.this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
                        finish(); // Fechar a atividade e voltar à tela anterior
                    } else {
                        Toast.makeText(TaskManagementActivity.this, "Erro ao salvar a tarefa.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
