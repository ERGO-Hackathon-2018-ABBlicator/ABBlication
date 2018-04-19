package abblication.ergo.de.abblication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AbbInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abbinfo);

        TextView txtOU = findViewById(R.id.txtOU);
        txtOU.setText(getIntent().getStringExtra("organizationUnit"));
        // FIXME fill other fields
    }

}
