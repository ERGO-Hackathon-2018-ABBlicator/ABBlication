package abblication.ergo.de.abblication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private static final String SKILLS = "Skills";
    private static final String STATIONEN = "Stationen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        AWSMobileClient.getInstance().initialize(ProfileActivity.this, new AWSStartupHandler() {
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

                                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
                                    String vNumber = sharedPref.getString("username", "");
                                    if (user.getString("ID").equals(vNumber)) {

                                        String userFirstname = user.getString("Vorname");
                                        String userLastname = user.getString("Nachname");

                                        TextView name = findViewById(R.id.fullname);
                                        name.setText(userLastname + ", " + userFirstname);

                                        JSONObject place = (JSONObject) obj;

                                        LinearLayout stationLayout = findViewById(R.id.layout_stations);

                                        JSONArray tagsObj_stationen = place.getJSONArray(STATIONEN);
                                        for (int c2 = 0; c2 < tagsObj_stationen.length(); c2++) {
                                            String station = tagsObj_stationen.getString(c2);
                                            TextView txtStation = new TextView(ProfileActivity.this);
                                            txtStation.setText(station);
                                            txtStation.setPadding(0, 0, 0, 5);
                                            stationLayout.addView(txtStation);
                                        }

                                        LinearLayout skillsLayout = findViewById(R.id.layout_skills);

                                        JSONArray skills = place.getJSONArray(SKILLS);
                                        for (int c2 = 0; c2 < skills.length(); c2++) {
                                            String skill = skills.getString(c2);
                                            TextView txtSkill = new TextView(ProfileActivity.this);
                                            txtSkill.setText(skill);
                                            txtSkill.setPadding(0, 0, 0, 5);
                                            skillsLayout.addView(txtSkill);
                                        }
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

}
