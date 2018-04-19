package abblication.ergo.de.abblication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import abblication.ergo.de.abblication.model.ABBRow;
import abblication.ergo.de.abblication.model.ABBTableData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String POSITIONS_BUCKET_NAME = "Stelle.json";

    private final Set<String> filterTags = new HashSet<>();
    private SearchView searchBar;
    private TableLayout abbTable;
    private ABBTableData data = new ABBTableData("[]");
    private Button buttonBU;
    private PopupMenu menuBU;
    private Button buttonOU;
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
                // FIXME call DetailsView to create new
            }
        });
        // FIXME check permission and enable button
        fab.setVisibility(View.INVISIBLE);

        searchBar = findViewById(R.id.search_bar);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                refreshTable();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                refreshTable();
                return true;
            }
        });

        abbTable = findViewById(R.id.abblist);

        buttonBU = findViewById(R.id.button_bu);
        menuBU = new PopupMenu(this, buttonBU);
        menuBU.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                buttonBU.setText(item.getTitle());
                refreshTable();
                return false;
            }
        });
        buttonOU = findViewById(R.id.button_ou);
        menuOU = new PopupMenu(this, buttonOU);
        menuOU.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                buttonOU.setText(item.getTitle());
                refreshTable();
                return false;
            }
        });
        Button buttonTags = findViewById(R.id.button_tags);
        menuTags = new PopupMenu(this, buttonTags);
        menuTags.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    filterTags.add(item.getTitle().toString());
                } else {
                    filterTags.remove(item.getTitle().toString());
                }
                refreshTable();
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                item.setActionView(new View(MainActivity.this));
                item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return false;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return false;
                    }
                });
                return false;
            }
        });
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
        for (ABBRow row : data.getFilteredRows(searchBar.getQuery().toString(), getBusinessValue(), getOrganizationUnit(), filterTags)) {
            createRow(abbTable, row);
        }
    }

    private String getBusinessValue() {
        String bu = buttonBU.getText().toString();
        if (bu.equalsIgnoreCase(getString(R.string.placeholder_business_unit))) {
            return null;
        } else {
            return bu;
        }
    }

    private String getOrganizationUnit() {
        String ou = buttonOU.getText().toString();
        if (ou.equalsIgnoreCase(getString(R.string.placeholder_organization_unit))) {
            return null;
        } else {
            return ou;
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
        txt.setPadding(5, 14, 5, 14);
        txt.setTextSize(14);
        txt.setTextColor(Color.BLACK);
        return txt;
    }

    private void populateMenus(ABBTableData data) {
        updateMenu(menuBU.getMenu(), getString(R.string.placeholder_business_unit), data.getBUs(), false);
        updateMenu(menuOU.getMenu(), getString(R.string.placeholder_organization_unit), data.getOUs(), false);
        updateMenu(menuTags.getMenu(), getString(R.string.placeholder_tags), data.getTags(), true);
    }

    private void updateMenu(Menu menu, String placeholder, Collection<String> items, boolean checkable) {
        menu.clear();
        if (!checkable) {
            menu.add(placeholder);
        }
        for (String item : items) {
            menu.add(item).setCheckable(checkable);
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
        } else if (id == R.id.action_myprofile) {
            Intent intent_login = new Intent(this, ProfileActivity.class);
            startActivity(intent_login);
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
    public void onClick(View view) {
        TableRow tableRow = (TableRow) view;
        TextView organizationCell = (TextView) tableRow.getChildAt(1);
        String organizationName = organizationCell.getText().toString();
        Intent intent = new Intent(view.getContext(), AbbInfoActivity.class);
        intent.putExtra("position", data.getPositionByOU(organizationName).toString());
        startActivity(intent);
    }

}
