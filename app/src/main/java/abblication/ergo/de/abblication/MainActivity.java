package abblication.ergo.de.abblication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TableLayout abbTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        abbTable = (TableLayout) findViewById(R.id.abblist);
    }

    @Override
    public void onStart() {
        super.onStart();

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult result) {
                AWSConnector.downloadWithTransferUtility(getApplicationContext(), "Stelle.json", new AWSConnector.ResultHandler() {
                    @Override
                    public void onComplete(String json) {
                        try {
                            JSONArray places = new JSONArray(json);
                            for (int c = 0; c < places.length(); c++) {
                                Object obj = places.get(c);
                                if (obj instanceof JSONObject) {
                                    JSONObject place = (JSONObject) obj;
                                    JSONArray tagsObj = place.getJSONArray("TAGS");
                                    String tagsStr = join(", ", tagsObj);
                                    createRow(abbTable, place.getString("BU"), place.getString("Gruppen-ID"), tagsStr);
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

    private void createRow(TableLayout ll, String businessArea, String organizationUnit, String tags) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        row.addView(createTextCell(businessArea));
        row.addView(createTextCell(organizationUnit));
        row.addView(createTextCell(tags));
        ll.addView(row);
    }

    private static String join(String delimiter, JSONArray jsonArray) throws JSONException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int c = 0 ; c < jsonArray.length(); c++) {
            String str = jsonArray.getString(c);
            if (!first) {
                sb.append(delimiter);
            }
            sb.append(str);
            first = false;
        }
        return sb.toString();
    }

    @NonNull
    private TextView createTextCell(String label) {
        TextView col2 = new TextView(this);
        col2.setText(label);
        col2.setPadding(5, 5, 5, 5);
        return col2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_abblist) {
            return true;
        } else if (id == R.id.action_quiz) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
