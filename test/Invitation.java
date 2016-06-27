package test;

import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aaklilu on 6/27/16.
 */
public class Invitation {


    public static void main(String[] args){

        if(args == null || args.length < 2){
            System.out.println("USAGE: [file_name] [radius]");
            return;
        }

        //The GPS coordinates for our Dublin office are 53.3381985, -6.2592576.
        final Location dublin_office_location = new Location(53.3381985, -6.2592576);

        String customer_list_file_name = args[0];

        int radius = Integer.valueOf(args[1]);// 100 Kilometers

        listCustomersWithinRadius(customer_list_file_name, dublin_office_location, radius);
    }

    /**
     * Prints out invitation list located within the radius provided
     *
     * @param customer_list_file_name
     * @param dublin_office_location
     * @param radius
     */
    private static void listCustomersWithinRadius(String customer_list_file_name, Location dublin_office_location, int radius){

        JSONParser parser = new JSONParser();

        try {

            List<Customer> invitationList = new ArrayList<>();
            JSONArray a = (JSONArray) parser.parse(new FileReader(customer_list_file_name));

            for (Object o : a)
            {
                JSONObject customerJson = (JSONObject) o;

                long user_id = (long) customerJson.get("user_id");
                String name = (String) customerJson.get("name");
                double latitude = Double.valueOf(customerJson.get("latitude").toString());
                double longitude = Double.valueOf(customerJson.get("longitude").toString());

                Location customer_location = new Location(latitude, longitude);
                double distance_from_dublin_office = calculateDistance(dublin_office_location, customer_location);

                if(distance_from_dublin_office <= radius){

                    Customer customer = new Customer(user_id, name, customer_location, distance_from_dublin_office);
                    invitationList.add(customer);
                }
            }

            //Sort the list
            Collections.sort(invitationList);

            //Print the list
            for(Customer customer: invitationList){

                System.out.println(customer.toString());
            }

        } catch (FileNotFoundException e) {
            // File not found
            e.printStackTrace();
        } catch (IOException e) {
            // IO Exception
            e.printStackTrace();
        } catch (ParseException e) {
            // Failed to parse
            e.printStackTrace();
        }

    }

    /**
     * Calculates the distance between two locations
     * @param locationA
     * @param locationB
     * @return
     */
    private static double calculateDistance(Location locationA, Location locationB){

        double r = 6371.0; // sphere radius( i.e earth radius) in kilometers
        double cE; //Central Edge
        double cL; //change in longitude
        double d; //distance, i.e. the arc length

        double lat_a = Math.toRadians(locationA.getLatitude());
        double long_a = Math.toRadians(locationA.getLongitude());
        double lat_b = Math.toRadians(locationB.getLatitude());
        double long_b = Math.toRadians(locationB.getLongitude());

        cL = long_b - long_a;

        cE = Math.acos(Math.sin(lat_a) * Math.sin(lat_b)
                + Math.cos(lat_a) * Math.cos(lat_b) * Math.cos(cL));

        d = cE * r;

        return d;
    }
}

class Location{

    private final double latitude;

    private final double longitude;

    public Location(double latitude, double longitude){

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

class Customer implements Comparable<Customer>{

    private final long user_id;

    private final String name;

    private final Location location;

    private final double distance_from_dublin_office;

    public Customer(long user_id, String name, Location location, double distance_from_dublin_office){

        this.user_id = user_id;
        this.name = name;
        this.location = location;
        this.distance_from_dublin_office = distance_from_dublin_office;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public double getDistance_from_dublin_office() {
        return distance_from_dublin_office;
    }

    @Override
    public int compareTo(Customer o) {

        return this.user_id - o.user_id > 0? 1: this.user_id - o.user_id < 0? -1: 0;
    }

    @Override
    public String toString(){

        return String.format("Id: %d, Name: %s, Distance from dublin office: %f Km", user_id, name, distance_from_dublin_office);
    }
}
