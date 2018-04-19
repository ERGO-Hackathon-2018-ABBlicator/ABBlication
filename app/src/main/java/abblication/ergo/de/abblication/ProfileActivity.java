package abblication.ergo.de.abblication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import abblication.ergo.de.abblication.model.ABBRow;

public class ProfileActivity extends AppCompatActivity {

    private static final String SKILLS = "Skills";
    private static final String STATIONEN = "Stationen";
    private static final String NAME = "Name";

    private int num_station = 7;
    private int num_skills = 5;
    private String[] stations_db = new String[num_station];
    private String[] skills_db = new String[num_skills];
    private String[] stations_gui = new String[num_station];
    private String[] skills_gui = new String[num_skills];

    private List<String> result_skills;
    private List<String> result_stationen;
    private String result_firstname;
    private String result_name;

    private int[] station_id = new int[]{R.id.text_station1, R.id.text_station2, R.id.text_station3, R.id.text_station4,
            R.id.text_station5, R.id.text_station6, R.id.text_station7};
    private int[] skill_id = new int[]{R.id.text_skill1, R.id.text_skill2, R.id.text_skill3, R.id.text_skill4, R.id.text_skill5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        result_skills = new ArrayList<>();
        result_stationen = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // ImageView imageUser = findViewById(R.id.imageUser);
        // imageUser.setImageBitmap();

        for (int i = 0; i < num_station; ++i) {
            TextView text_station1 = findViewById(station_id[i]);
            text_station1.setText(result_stationen.get(i));
        }

        for (int i = 0; i < num_skills; ++i) {
            TextView text_skill1 = findViewById(skill_id[i]);
            text_skill1.setText(result_skills.get(i));
        }

        TextView name = findViewById(R.id.name_);
        name.setText(result_name);

        TextView firstname = findViewById(R.id.firstname_);
        name.setText(result_firstname);

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

                                    result_firstname = user.getString("Vorname");
                                    result_name = user.getString("Nachname");

                                    JSONObject place = (JSONObject) obj;
                                    JSONArray tagsObj_skills = place.getJSONArray(SKILLS);
                                    for (int c2 = 0; c2 < tagsObj_skills.length(); c2++) {
                                        String skill = tagsObj_skills.getString(c2);
                                        result_skills.add(skill);
                                    }

                                    JSONArray tagsObj_stationen = place.getJSONArray(STATIONEN);
                                    for (int c2 = 0; c2 < tagsObj_stationen.length(); c2++) {
                                        String station = tagsObj_stationen.getString(c2);
                                        result_stationen.add(station);
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
