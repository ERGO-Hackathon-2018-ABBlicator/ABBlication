package abblication.ergo.de.abblication.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ABBTableData {

    private static final String BUSINESS_UNIT_KEY = "BU";
    private static final String ORGANIZATION_UNIT_KEY = "Gruppen-ID";
    private static final String TAGS_KEY = "TAGS";
    private static final String VORNAME = "Vorname";
    private static final String NACHNAME = "Nachname";
    private static final String SKILLS = "Skills";
    private static final String STATIONEN = "Stationen";

    private String bucket;
    private List<ABBRow> result;
    private boolean add;

    public ABBTableData(String json) {
        this.bucket = json;
    }

    public List<ABBRow> getFilteredRows(String searchInput, String businessKey, String organizationUnit, Set<String> filterTags) {
        result = new ArrayList<>();
        String[] sInput = searchInput.split(" ");
        try {
            JSONArray places = new JSONArray(bucket);
            for (int c = 0; c < places.length(); c++) {
                Object obj = places.get(c);
                add = false;
                if (obj instanceof JSONObject) {
                    JSONObject place = (JSONObject) obj;
                    JSONArray tagsObj = place.getJSONArray(TAGS_KEY);
                    List tags = getTags();

                    //Geschäftseinheit vergleich mit Dropdown selection
                    if (!add && place.getString(BUSINESS_UNIT_KEY).equals(businessKey)){
                        add = true;
                    }

                    //Organisationseinheit vergleich mit Dropdown selection
                    if (!add && place.getString(ORGANIZATION_UNIT_KEY).equals(organizationUnit)){
                        add = true;
                    }

                    //Vergleich mit Sucheingabe
                    if(!add){
                        for (int i = 0; i < sInput.length; i++){

                            //Geschäftseinheit vergleich mit Sucheingabe
                            if (place.getString(BUSINESS_UNIT_KEY).equals(sInput[i])){
                                add = true;
                                break;
                            }

                            //Organisationseinheit vergleich  mit Sucheingabe
                            if (place.getString(ORGANIZATION_UNIT_KEY).equals(sInput[i])){
                                add = true;
                                break;
                            }

                            //Tags vergleich mit Sucheingabe
                            for(int k = 0; k < tags.size(); k++){
                                if(tags.get(k).equals(sInput[i])) {
                                    add = true;
                                    k = tags.size();
                                    i = sInput.length;
                                }
                            }
                        }
                    }

                    //Tags vergleich mit Dropdown Auswahl der Tags
                    for(int k = 0; k < tags.size(); k++){
                        for(int j = 0; j < filterTags.size(); j++){
                            if(tags.get(k).equals(sInput[j])){
                                add = true;
                                k = tags.size();
                                j = filterTags.size();
                            }
                        }
                    }

                    if(add) {
                        ABBRow row = new ABBRow();
                        row.setBusinessUnit(place.getString(BUSINESS_UNIT_KEY));
                        row.setOrganizationUnit(place.getString(ORGANIZATION_UNIT_KEY));
                        row.setTags(tagsObj);
                        result.add(row);
                    }
                }
            }
            if(!add){
                result = getAllRows();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ABBRow> getAllRows() {
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
