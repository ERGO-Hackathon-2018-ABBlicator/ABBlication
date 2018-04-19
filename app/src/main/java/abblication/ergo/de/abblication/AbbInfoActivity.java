package abblication.ergo.de.abblication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class AbbInfoActivity extends AppCompatActivity {

    private String abbMail;
    private String contactMail;

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
            fillText(jsonPosition, R.id.txtAnsprechpartner, "Ansprechpartner");
            abbMail = jsonPosition.getString("ABBMail");
            contactMail = jsonPosition.getString("AnsprechpartnerMail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillText(JSONObject jsonPosition, int txtBeschreibung, String gruppenbeschreibung) throws JSONException {
        ((TextView) findViewById(txtBeschreibung)).setText(jsonPosition.getString(gruppenbeschreibung));
    }

    public void onApplyNow(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", abbMail, null));
        intent.putExtra(Intent.EXTRA_CC, new String[]{contactMail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Bewerbung für Praxisphase");
        intent.putExtra(Intent.EXTRA_TEXT, "Hallo");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }
}
