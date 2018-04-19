package abblication.ergo.de.abblication.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ABBTableData {

    private static final String BUSINESS_UNIT_KEY = "BU";
    private static final String ORGANIZATION_UNIT_KEY = "Gruppen-ID";
    private static final String TAGS_KEY = "TAGS";

    private String bucket;

    public ABBTableData(String json) {
        this.bucket = json;
    }

    public List<ABBRow> getOverviewRows() {
        List<ABBRow> result = new ArrayList<>();
        try {
            JSONArray places = new JSONArray(bucket);
            for (int c = 0; c < places.length(); c++) {
                Object obj = places.get(c);
                if (obj instanceof JSONObject) {
                    JSONObject place = (JSONObject) obj;
                    JSONArray tagsObj = place.getJSONArray(TAGS_KEY);
                    ABBRow row = new ABBRow();
                    row.setBusinessUnit(place.getString(BUSINESS_UNIT_KEY));
                    row.setOrganizationUnit(place.getString(ORGANIZATION_UNIT_KEY));
                    row.setTags(tagsObj);
                    result.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getBUs() {
        List<String> result = new LinkedList<>();
        try {
            JSONArray places = new JSONArray(bucket);
            for (int c = 0; c < places.length(); c++) {
                Object obj = places.get(c);
                if (obj instanceof JSONObject) {
                    JSONObject place = (JSONObject) obj;
                    result.add(place.getString(BUSINESS_UNIT_KEY));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getOUs() {
        List<String> result = new LinkedList<>();
        try {
            JSONArray places = new JSONArray(bucket);
            for (int c = 0; c < places.length(); c++) {
                Object obj = places.get(c);
                if (obj instanceof JSONObject) {
                    JSONObject place = (JSONObject) obj;
                    result.add(place.getString(ORGANIZATION_UNIT_KEY));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getTags() {
        List<String> result = new LinkedList<>();
        try {
            JSONArray places = new JSONArray(bucket);
            for (int c = 0; c < places.length(); c++) {
                Object obj = places.get(c);
                if (obj instanceof JSONObject) {
                    JSONObject place = (JSONObject) obj;
                    JSONArray tagsObj = place.getJSONArray(TAGS_KEY);
                    for (int c2 = 0; c2 < tagsObj.length(); c2++) {
                        String tag = tagsObj.getString(c2);
                        result.add(tag);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
