package com.example.austin.menu;
import org.json.*;
/**
 * Created by flame on 10/29/2017.
 */

public class Parser {
    private static Marker parserBathroomJSONObject(JSONObject bathroom) throws JSONException{
        double lat = bathroom.getDouble("latitude");
        double lon = bathroom.getDouble("longitude");
        String title = bathroom.getString("title");
        Marker.TYPE bathroomType = null;
        switch(bathroom.getString("gender")){
            case "n": bathroomType = Marker.TYPE.NEUTRAL; break;
            case "f": bathroomType = Marker.TYPE.FEMALE; break;
            case "m": bathroomType = Marker.TYPE.MALE; break;
        }
        String comment = bathroom.getString("comments");
        double distanceTo = bathroom.getDouble("distance_to");
        return new Marker(lat, lon, title, bathroomType, comment, distanceTo);
    }
    public static Marker[] parseBathroomsInRadius(String data) throws JSONException{
        JSONArray ja = new JSONArray(new JSONTokener(data));
        Marker[] markers = new Marker[ja.length()];
        for(int i = 0; i < ja.length(); i++){
            String bathroomData = ja.getString(i);
            markers[i] = parseBathroom(bathroomData);
        }
        return markers;
    }

    public static Marker parseBathroom(String data) throws JSONException{
        JSONObject bathroom = new JSONObject(new JSONTokener(data));
        return parserBathroomJSONObject(bathroom);
    }

    public static UserData parseProfile(String data) throws JSONException{
        JSONObject ud = new JSONObject(new JSONTokener(data));
        UserData u = new UserData();
        u.handle = ud.getString("handle");
        u.userId = ud.getString("googleUserId");
        u.points = ud.getLong("points");
        return u;
    }
}
