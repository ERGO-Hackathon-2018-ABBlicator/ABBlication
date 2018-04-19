package abblication.ergo.de.abblication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class AbbInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abbinfo);

        try {
            JSONObject jsonPosition = new JSONObject(getIntent().getStringExtra("position"));

            fillText(jsonPosition, R.id.txtOU, "Gruppen-ID");
            fillText(jsonPosition, R.id.txtBU, "BU");
            fillText(jsonPosition, R.id.txtPraxisarbeitJN, "Praxisarbeit");
            fillText(jsonPosition, R.id.txtBeschreibung, "Gruppenbeschreibung");
            fillText(jsonPosition, R.id.txtAufgabe, "Aufgabenbeschreibung");
            fillText(jsonPosition, R.id.txtStandort, "Standort");
            fillText(jsonPosition, R.id.txtABB, "ABB");
            fillText(jsonPosition, R.id.txtAnspartner, "Ansprechpartner");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillText(JSONObject jsonPosition, int txtBeschreibung, String gruppenbeschreibung) throws JSONException {
        ((TextView) findViewById(txtBeschreibung)).setText(jsonPosition.getString(gruppenbeschreibung));
    }

}
