package abblication.ergo.de.abblication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public Button login_button;
    public EditText username;
    public EditText password ;
    public TextView error;

    private String username_db;
    private String password_db;
    private String username_gui;
    private String password_gui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = findViewById(R.id.button_login);
        username = findViewById(R.id.text_username);
        password = findViewById(R.id.text_password);
        error = findViewById(R.id.error_message);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                error.setText("");
                username_gui = String.valueOf(username.getText());
                password_gui = String.valueOf(password.getText());

                AWSMobileClient.getInstance().initialize(LoginActivity.this, new AWSStartupHandler() {
                    @Override
                    public void onComplete(AWSStartupResult result) {
                        AWSConnector.downloadWithTransferUtility(getApplicationContext(), "User.json", new AWSConnector.ResultHandler() {
                            @Override
                            public void onComplete(String json) {
                                try {
                                    JSONArray users = new JSONArray(json);
                                    for (int c = 0; c < users.length(); c++) {
                                        Object obj = users.get(c);
                                        if (obj instanceof JSONObject) {
                                            JSONObject user = (JSONObject) obj;
                                            username_db = user.getString("ID");
                                            password_db = user.getString("Passwort");

                                            if (username_gui.equals(username_db) && password_gui.equals(password_db)){
                                                Intent Intent = new Intent(view.getContext(), MainActivity.class);
                                                startActivity(Intent);
                                            } else if(c == users.length() - 1){
                                                error.setText("Try again!");
                                            }else{
                                                continue;
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).execute();
            }
        });
    }


}
