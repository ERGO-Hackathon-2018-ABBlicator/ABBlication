package abblication.ergo.de.abblication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    public Button login_button;
    public EditText username;
    public EditText password ;
    public TextView error;

    private String username_db = "Jonas";
    private String password_db = "123";
    private String username_gui;
    private String password_gui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        login_button = findViewById(R.id.button_login);
        username = findViewById(R.id.text_username);
        password = findViewById(R.id.text_password);
        error = findViewById(R.id.error_message);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.setText("");
                username_gui = String.valueOf(username.getText());
                password_gui = String.valueOf(password.getText());

                if (username_gui.equals(username_db) && password_gui.equals(password_db)){
                    Intent Intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(Intent);
                }else{
                    error.setText("Try again!");
                }
            }
        });
    }


}
