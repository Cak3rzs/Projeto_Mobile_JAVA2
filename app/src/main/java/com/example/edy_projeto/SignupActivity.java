package com.example.edy_projeto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private DatabaseHelper databaseHelper; // Adiciona o helper do banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Inicializa os campos e o botão
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);

        // Inicializa o DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Configura o listener do botão "Cadastrar"
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Captura os dados inseridos pelo usuário
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Verifica se os campos estão preenchidos
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    // Tenta salvar o usuário no banco de dados
                    boolean isInserted = databaseHelper.insertUser(username, email, password);

                    if (isInserted) {
                        // Exibe a mensagem de sucesso
                        Toast.makeText(SignupActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Exibe a mensagem de erro
                        Toast.makeText(SignupActivity.this, "Erro ao cadastrar o usuário.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
