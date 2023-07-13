package com.clovercard.clovertrainerutils.configs.shuffler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamConfig {
    public static TeamConfig DATA = new TeamConfig(new HashMap<>());
    private HashMap<String, PokemonTeam> teams;

    public TeamConfig(HashMap<String, PokemonTeam> teams) {
        this.teams = teams;
    }

    public HashMap<String, PokemonTeam> getTeams() {
        return teams;
    }

    public class PokemonTeam {
        private String slot1;
        private String slot2;
        private String slot3;
        private String slot4;
        private String slot5;
        private String slot6;

        public List<String> getPokemonNames() {
            List<String> pokemonNames = new ArrayList<>();
            if(slot1 != null) pokemonNames.add(slot1);
            if(slot2 != null) pokemonNames.add(slot2);
            if(slot3 != null) pokemonNames.add(slot3);
            if(slot4 != null) pokemonNames.add(slot4);
            if(slot5 != null) pokemonNames.add(slot5);
            if(slot6 != null) pokemonNames.add(slot6);
            return pokemonNames;
        }
    }

    public void load() {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader("config/CloverTrainerUtils/shuffler/teams.json");
            DATA = gson.fromJson(reader, TeamConfig.class);
            reader.close();
        }
        catch (IOException err) {
            if(err instanceof FileNotFoundException) {
                try {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    TeamConfig config = new TeamConfig(new HashMap<>());

                    //Create example team
                    PokemonTeam team1 = new PokemonTeam();
                    team1.slot1 = "example1";
                    team1.slot2 = "example2";
                    config.teams.put("exampleteam", team1);

                    //Store Pokemon within Hashmap
                    DATA.teams.put("exampleteam", team1);

                    //Save config
                    File path = new File("config/CloverTrainerUtils/shuffler/");
                    if(!path.mkdirs()){
                        System.err.println("Unable to make parent directories!");
                    }
                    File json = new File(path, "teams.json");
                    json.createNewFile();
                    Writer writer = new FileWriter(json);
                    writer.write(gson.toJson(config));
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("An error occurred while trying to read the Boosts Config file!");
                System.err.println(err.fillInStackTrace());
            }
        }
    }
}
