package com.example.edy_projeto;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationsActivity extends AppCompatActivity {

    private ListView notificationsListView;
    private String[] notifications = {
            "Tarefa 'Design da Interface' vence em 2 dias",
            "Projeto 'Novo Aplicativo' foi atualizado",
            "Tarefa 'Teste de Usabilidade' está atrasada"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationsListView = findViewById(R.id.notificationsListView);

        // Configurar o adaptador para a ListView de notificações
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notifications);
        notificationsListView.setAdapter(adapter);
    }
}
