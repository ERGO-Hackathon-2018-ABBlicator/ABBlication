package abblication.ergo.de.abblication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        TableLayout ll = (TableLayout) findViewById(R.id.abblist);

        createRow(ll, "Pass", "ISEAD", join(", ", Arrays.asList("Security")));
        createRow(ll, "ADM", "AMC1HH", join(", ", Arrays.asList("SQL")));
        createRow(ll, "I & O", "IVZ2K", join(", ", Arrays.asList("Provider", "Big Data", "KI", "Kobol", "EKS", "Host", "DB2", "NoSQL")));
        createRow(ll, "Demand", "ITSD", join(", ", Arrays.asList("Nachfrage")));
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

    private static String join(String delimiter, List<String> list) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String str : list) {
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
