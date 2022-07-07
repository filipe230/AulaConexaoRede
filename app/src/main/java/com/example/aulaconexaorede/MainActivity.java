package com.example.aulaconexaorede;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{
    private Button btnEnviar;
    private TextView txtMostrarMsg, txtDataHora;
    private EditText edtTexto;

    private Socket client;
    private PrintWriter printwriter;
    private String message;

    private String dateTime;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String valor_zero = "0";

    public static final int PORT = 4000;
    public static final String HOSTNAME = "192.168.1.184";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnviar = findViewById(R.id.btnEnviar);
        txtMostrarMsg = findViewById(R.id.txtMostrarMsg);
        txtDataHora = findViewById(R.id.txtDataHora);
        edtTexto = findViewById(R.id.edtTexto);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMostrar();
//Toast.makeText(getApplicationContext(), "antes do enviarMostrar",2000).show();
                //Toast.makeText(getApplicationContext(), "Cliente mandando",2000).show();

                if(edtTexto.getText().equals("0")){
                    //Toast.makeText(getApplicationContext(), "Cliente encerrado",2000).show();
                    txtMostrarMsg.setText("Servidor Ecncerrado!");
                    finish();
                    //Toast.makeText(getApplicationContext(), "Finish",2000).show();
                }
                else{
                    //Toast.makeText(getApplicationContext(), "Antes do message",2000).show();

                    message = edtTexto.getText().toString();
                    //Toast.makeText(getApplicationContext(), "Deposi do Message",2000).show();

                    new Thread(new ClientThread(message)).start();
                    //Toast.makeText(getApplicationContext(), "Depois da tread",2000).show();

                }
            }
        });
    }

    private void enviarMostrar() {
        txtMostrarMsg.setText(edtTexto.getText());
        String texto = (edtTexto.getText().toString());
        if(texto.equals("0")){
            txtMostrarMsg.setText("Encerrado");
            finish();
        }

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        txtDataHora.setText(dateTime);
    }

    class ClientThread implements Runnable {
        private final String message;

        ClientThread(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            try {
                client = new Socket(HOSTNAME, PORT);
                printwriter = new PrintWriter(client.getOutputStream(),true);
                printwriter.write(message);

                printwriter.flush();
                printwriter.close();

                client.close();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Erro no cliente",5000).show();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edtTexto.setText("");
                }
            });
        }
    }
}