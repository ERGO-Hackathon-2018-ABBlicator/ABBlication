package abblication.ergo.de.abblication.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ABBRow {

    private String businessUnit;
    private String organizationUnit;
    private String joinedTags;
    private List<String> tags = new ArrayList<>();

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getJoinedTags() {
        return joinedTags;
    }

    public void setTags(JSONArray jsonArray) throws JSONException {
        tags.clear();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int c = 0; c < jsonArray.length(); c++) {
            String tag = jsonArray.getString(c);
            tags.add(tag);
            if (!first) {
                sb.append(", ");
            }
            sb.append(tag);
            first = false;
        }
        joinedTags = sb.toString();
    }

}