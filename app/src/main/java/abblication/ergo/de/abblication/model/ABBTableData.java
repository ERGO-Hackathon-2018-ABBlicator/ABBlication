package abblication.ergo.de.abblication.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
    private boolean wasAdded;

    public ABBTableData(String json) {
        this.bucket = json;
    }

    public List<ABBRow> getFilteredRows(String searchInput, String businessKey, String organizationUnit, Set<String> filterTags) {
        result = new ArrayList<>();
        wasAdded = false;
        String[] sInput = searchInput.split(" ");
        try {
            JSONArray places = new JSONArray(bucket);
            for (int c = 0; c < places.length(); c++) {
                Object obj = places.get(c);
                add = false;
                if (obj instanceof JSONObject) {
                    JSONObject place = (JSONObject) obj;
                    JSONArray tagsObj = place.getJSONArray(TAGS_KEY);
                    Collection<String> tags = getTags();

                    //Geschäftseinheit vergleich mit Dropdown selection
                    if (!add && place.getString(BUSINESS_UNIT_KEY).equalsIgnoreCase(businessKey)) {
                        add = true;
                    }

                    //Organisationseinheit vergleich mit Dropdown selection
                    if (!add && place.getString(ORGANIZATION_UNIT_KEY).equalsIgnoreCase(organizationUnit)) {
                        add = true;
                    }

                    //Vergleich mit Sucheingabe
                    if (!add) {
                        for (int i = 0; i < sInput.length; i++) {

                            //Geschäftseinheit vergleich mit Sucheingabe
                            if (place.getString(BUSINESS_UNIT_KEY).equalsIgnoreCase(sInput[i])) {
                                add = true;
                                break;
                            }

                            //Organisationseinheit vergleich  mit Sucheingabe
                            if (place.getString(ORGANIZATION_UNIT_KEY).equalsIgnoreCase(sInput[i])) {
                                add = true;
                                break;
                            }

                            //Tags vergleich mit Sucheingabe
                            for (int l = 0; l < tagsObj.length(); l++) {
                                String tag = tagsObj.getString(l);
                                if (tag.equalsIgnoreCase(sInput[i])) {
                                    add = true;
                                    i = sInput.length;
                                    break;
                                }
                            }
                        }
                    }

                    //Tags vergleich mit Dropdown Auswahl der Tags
                    for (String tag : filterTags) {
                        for (int j = 0; j < tagsObj.length(); j++) {
                            String tmp = tagsObj.getString(j);
                            if (tag.equalsIgnoreCase(tagsObj.getString(j))) {
                                add = true;
                                j = filterTags.size();
                                break;
                            }
                        }
                    }

                    if (add) {
                        wasAdded = true;
                        ABBRow row = new ABBRow();
                        row.setBusinessUnit(place.getString(BUSINESS_UNIT_KEY));
                        row.setOrganizationUnit(place.getString(ORGANIZATION_UNIT_KEY));
                        row.setTags(tagsObj);
                        result.add(row);
                    }
                }
            }
            if (!wasAdded) {
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

    public Set<String> getBUs() {
        Set<String> result = new HashSet<>();
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

    public Set<String> getOUs() {
        Set<String> result = new HashSet<>();
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

    public Set<String> getTags() {
        Set<String> result = new HashSet<>();
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

    public List<String> getTagsByUsageCount() {
        HashMap<String, TagCount> tagToCount = new HashMap<>();
        try {
            JSONArray places = new JSONArray(bucket);
            for (int c = 0; c < places.length(); c++) {
                Object obj = places.get(c);
                if (obj instanceof JSONObject) {
                    JSONObject place = (JSONObject) obj;
                    JSONArray tagsObj = place.getJSONArray(TAGS_KEY);
                    for (int c2 = 0; c2 < tagsObj.length(); c2++) {
                        String tag = tagsObj.getString(c2);
                        TagCount tagCount = tagToCount.get(tag);
                        if (tagCount == null) {
                            tagCount = new TagCount(tag);
                            tagToCount.put(tag, tagCount);
                        }
                        tagCount.count++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<TagCount> tagCountList = new ArrayList<>(tagToCount.values());
        Collections.sort(tagCountList, TagCountComparator.INSTANCE);
        List<String> result = new LinkedList<>();
        for (TagCount tagCount : tagCountList) {
            result.add(tagCount.getTag());
        }
        return result;
    }

    private static class TagCount {

        private final String tag;
        public int count;

        private TagCount(String tag) {
            this.tag = tag;
            this.count = 0;
        }

        public String getTag() {
            return tag;
        }
    }

    private static class TagCountComparator implements Comparator<TagCount> {

        public static final TagCountComparator INSTANCE = new TagCountComparator();

        @Override
        public int compare(TagCount o1, TagCount o2) {
            if (o1.count == o2.count) {
                return 0;
            } else if (o1.count < o2.count) {
                return 1;
            } else {
                return -1;
            }
        }

    }

    public JSONObject getPositionByOU(String ouName) {
        try {
            JSONArray places = new JSONArray(bucket);
            for (int c = 0; c < places.length(); c++) {
                Object obj = places.get(c);
                if (obj instanceof JSONObject) {
                    JSONObject place = (JSONObject) obj;
                    if (place.getString(ORGANIZATION_UNIT_KEY).equalsIgnoreCase(ouName)) {
                        return place;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

}
