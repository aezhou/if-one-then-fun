package com.aezhou.ifonethenfun;


import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) throws IOException, JSONException {
        if(args.length == 0) {
            throw new IllegalArgumentException("Please provide a team list file thing pls");
        }

        Organization organization = new Organization(getTeamsFromJSON(JSONUtils.readJsonArrayFromFile(new File(args[0]))));
        Matcher matcher = new Matcher(organization);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            System.out.print("Press enter to get next batch of matches, or 'exit' to quit...");
            String input = bufferedReader.readLine();
            if ("exit".equals(input)){
                System.out.println("goodbye :(");
                return;
            } else {
                System.out.println(matcher.getNextMatches().toString().replace(", ", ",\n")+"\n");
            }
        }

    }

    private static Map<String, Set<Person>> getTeamsFromJSON(JSONArray teamsJson){
        Map<String, Set<Person>> teams = new HashMap<>();
        Map<String, Person> employees = new HashMap<>();

        for(int i = 0; i < teamsJson.length(); i++){
            JSONObject teamJson = teamsJson.getJSONObject(i);
            JSONArray teammatesJson = teamJson.getJSONArray("members");

            String teamName = teamJson.getString("name");
            teams.put(teamName, new HashSet<Person>());

            for(int j = 0; j < teammatesJson.length(); j++){
                String email = teammatesJson.getString(j);

                if(!employees.containsKey(email)) employees.put(email, new Person(email));
                teams.get(teamName).add(employees.get(email));
            }
        }

        return teams;
    }
}
