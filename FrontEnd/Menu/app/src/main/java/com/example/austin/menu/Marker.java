package com.example.austin.menu;

/**
 * Created by flame on 10/28/2017.
 */

public class Marker implements Comparable<Marker>{
    private double lat;
    private double lon;
    private String name;
    private String info;
    private double distance;
    public enum TYPE {
        NEUTRAL, FEMALE, MALE
    };
    private TYPE type;
    public Marker(double _lat, double _lon, String _name, TYPE _type, String _info, double _distance){
        lat = _lat;
        lon = _lon;
        name = _name;
        type = _type;
        info = _info;
        distance = _distance;
    }
    public double getLat(){
        return lat;
    }
    public double getLon(){
        return lon;
    }
    public String getName(){
        return name;
    }

    public TYPE getType(){
        return type;
    }

    public String getInfo(){
        return info;
    }

    public float getHue(){
        return (type == TYPE.NEUTRAL ? 30 : (type == TYPE.FEMALE ? 330 : 210));
    }

    public int compareTo(Marker other){
        double comp = (this.distance - other.distance);
        if(comp < 0){
            return -1;
        } else if(comp == 0){
            return 0;
        } else {
            return 1;
        }
    }
}
