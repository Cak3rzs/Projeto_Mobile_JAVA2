package com.example.edy_projeto;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReportsActivity extends AppCompatActivity {

    private ProgressBar overallProgressBar;
    private TextView overallProgressPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        overallProgressBar = findViewById(R.id.overallProgressBar);
        overallProgressPercentage = findViewById(R.id.overallProgressPercentage);

        // Definir o progresso (exemplo)
        int progress = 70;
        overallProgressBar.setProgress(progress);
        overallProgressPercentage.setText(progress + "%");

        // Implementar lógica para gerar relatórios reais
    }
}
