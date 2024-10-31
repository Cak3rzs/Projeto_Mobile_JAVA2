package com.example.edy_projeto;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Certifique-se de que o layout de login está correto

        // Inicializando os componentes
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        databaseHelper = new DatabaseHelper(this);

        // Definindo o clique do botão de login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (validateLogin(username, password)) {
                    Toast.makeText(HomeActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                    // Aqui você pode iniciar a próxima atividade (por exemplo, HomeActivity)
                    Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Opcional: fecha a LoginActivity
                } else {
                    Toast.makeText(HomeActivity.this, "Nome de usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para validar o login
    private boolean validateLogin(String username, String password) {
        Cursor cursor = databaseHelper.getAllUsers();
        while (cursor.moveToNext()) {
            String dbUsername = cursor.getString(cursor.getColumnIndex("USERNAME"));
            String dbPassword = cursor.getString(cursor.getColumnIndex("PASSWORD"));
            if (dbUsername.equals(username) && dbPassword.equals(password)) {
                return true; // Login bem-sucedido
            }
        }
        return false; // Login falhou
    }
}
