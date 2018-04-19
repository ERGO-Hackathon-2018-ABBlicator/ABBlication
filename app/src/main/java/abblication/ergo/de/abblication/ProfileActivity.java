package abblication.ergo.de.abblication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // ImageView imageUser = findViewById(R.id.imageUser);
        // imageUser.setImageBitmap();

        TextView text_station1 = findViewById(R.id.text_station1);
        text_station1.setText(getIntent().getStringExtra("organizationUnit1"));

        TextView text_station2 = findViewById(R.id.text_station2);
        text_station2.setText(getIntent().getStringExtra("organizationUnit2"));

        TextView text_station3 = findViewById(R.id.text_station3);
        text_station3.setText(getIntent().getStringExtra("organizationUnit3"));

        TextView text_station4 = findViewById(R.id.text_station4);
        text_station4.setText(getIntent().getStringExtra("organizationUnit4"));

        TextView text_station5 = findViewById(R.id.text_station5);
        text_station5.setText(getIntent().getStringExtra("organizationUnit5"));

        TextView text_station6 = findViewById(R.id.text_station6);
        text_station6.setText(getIntent().getStringExtra("organizationUnit6"));

        TextView text_station7 = findViewById(R.id.text_station7);
        text_station7.setText(getIntent().getStringExtra("organizationUnit7"));

        TextView name = findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("Name"));

        TextView text_skill1 = findViewById(R.id.text_skill1);
        text_skill1.setText(getIntent().getStringExtra("Skill1"));

        TextView text_skill2 = findViewById(R.id.text_skill2);
        text_skill2.setText(getIntent().getStringExtra("Skill2"));

        TextView text_skill3 = findViewById(R.id.text_skill3);
        text_skill3.setText(getIntent().getStringExtra("Skill3"));

        TextView text_skill4 = findViewById(R.id.text_skill4);
        text_skill4.setText(getIntent().getStringExtra("Skill4"));

        TextView text_skill5 = findViewById(R.id.text_skill5);
        text_skill5.setText(getIntent().getStringExtra("Skill5"));
    }

}
