package abblication.ergo.de.abblication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import java.util.HashSet;
import java.util.Set;

import abblication.ergo.de.abblication.model.ABBRow;
import abblication.ergo.de.abblication.model.ABBTableData;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, View.OnClickListener {

    private static final String POSITIONS_BUCKET_NAME = "Stelle.json";

    private final Set<String> filterTags = new HashSet<>();
    private TableLayout abbTable;
    private ABBTableData data = new ABBTableData("[]");
    private PopupMenu menuBU;
    private PopupMenu menuOU;
    private PopupMenu menuTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        abbTable = findViewById(R.id.abblist);

        Button buttonBU = findViewById(R.id.button_bu);
        menuBU = new PopupMenu(this, buttonBU);
        Button buttonOU = findViewById(R.id.button_ou);
        menuOU = new PopupMenu(this, buttonOU);
        Button buttonTags = findViewById(R.id.button_tags);
        menuTags = new PopupMenu(this, buttonTags);
        menuTags.setOnMenuItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult result) {
                AWSConnector.downloadWithTransferUtility(getApplicationContext(), POSITIONS_BUCKET_NAME, new AWSConnector.ResultHandler() {
                    @Override
                    public void onComplete(String json) {
                        data = new ABBTableData(json);
                        refreshTable();
                        populateMenus(data);
                    }
                });
            }
        }).execute();
    }

    private void refreshTable() {
        abbTable.removeAllViews();
        for (ABBRow row : data.getOverviewRows()) {
            // FIXME filter rows
            createRow(abbTable, row);
        }
    }

    private void createRow(TableLayout ll, ABBRow row) {
        TableRow tableRow = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(lp);
        tableRow.addView(createTextCell(row.getBusinessUnit()));
        tableRow.addView(createTextCell(row.getOrganizationUnit()));
        tableRow.addView(createTextCell(row.getJoinedTags()));
        tableRow.setClickable(true);
        tableRow.setOnClickListener(this);
        ll.addView(tableRow);
    }

    @NonNull
    private TextView createTextCell(String label) {
        TextView txt = new TextView(this);
        txt.setText(label);
        txt.setPadding(5, 10, 5, 10);
        txt.setTextSize(20);
        return txt;
    }

    private void populateMenus(ABBTableData data) {
        for (String item : data.getBUs()) {
            menuBU.getMenu().add(item);
        }
        for (String item : data.getOUs()) {
            menuOU.getMenu().add(item);
        }
        for (String item : data.getTags()) {
            menuTags.getMenu().add(item).setCheckable(true);
        }
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

    public void onClickBU(View view) {
        menuBU.show();
    }

    public void onClickOU(View view) {
        menuOU.show();
    }

    public void onClickTags(View view) {
        menuTags.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        item.setChecked(!item.isChecked());
        if (item.isChecked()) {
            filterTags.add(item.getTitle().toString());
        } else {
            filterTags.remove(item.getTitle().toString());
        }
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        item.setActionView(new View(MainActivity.this));
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                refreshTable();
                return false;
            }
        });
        return false;
    }

    @Override
    public void onClick(View v) {
        TableRow tableRow = (TableRow) v;
        TextView organizationCell = (TextView) tableRow.getChildAt(1);
        String organizationName = organizationCell.getText().toString();
        // FIXME call DetailsActivity with organizationName
        Log.i(this.getClass().getSimpleName(), "calling details for " + organizationName);
    }

}
