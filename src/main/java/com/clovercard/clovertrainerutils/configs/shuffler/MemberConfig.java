package com.clovercard.clovertrainerutils.configs.shuffler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;

public class MemberConfig {
    public static MemberConfig DATA = new MemberConfig(new HashMap<>());
    private HashMap<String, PokemonMember> pokemon;
    private MemberConfig(HashMap<String, PokemonMember> pokemon) {
        this.pokemon = pokemon;
        DATA = this;
    }

    public HashMap<String, PokemonMember> getPokemon() {
        return pokemon;
    }

    public void load() {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader("config/CloverTrainerUtils/shuffler/pokemon.json");
            DATA = gson.fromJson(reader, MemberConfig.class);
            reader.close();
        }
        catch (IOException err) {
            if(err instanceof FileNotFoundException) {
                try {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    MemberConfig config = new MemberConfig(new HashMap<>());

                    //Create example of two Pokemon
                    PokemonMember example1 = new PokemonMember();
                    PokemonMember example2 = new PokemonMember();
                    example1.species = "Pikachu";
                    example1.form = "";
                    example1.gender = "female";
                    example1.ability = "Moody";
                    example1.nature = "Adamant";
                    example1.heldItem = "pixelmon:leftovers";
                    example1.ivs = new int[] {31, 31, 31, 31, 31, 31};
                    example1.evs = new int[] {252, 0, 0, 0, 0, 252};
                    example1.moveset = new String[] {"Tackle", "Growl", "Roar", "Sheer Cold"};
                    example1.level = 50;

                    example2.species = "Bidoof";
                    example2.form = "";
                    example2.gender = "female";
                    example2.ability = "Moody";
                    example2.nature = "Adamant";
                    example2.heldItem = "pixelmon:leftovers";
                    example2.ivs = new int[] {31, 31, 31, 31, 31, 31};
                    example2.evs = new int[] {252, 0, 0, 0, 0, 252};
                    example2.moveset = new String[] {"Tackle", "Growl", "Roar", "Sheer Cold"};
                    example2.level = 52;

                    //Store Pokemon within Hashmap
                    DATA.pokemon.put("example1", example1);
                    DATA.pokemon.put("example2", example2);

                    //Save config
                    File path = new File("config/CloverTrainerUtils/shuffler/");
                    if(!path.mkdirs()){
                        System.err.println("Unable to make parent directories!");
                    }
                    File json = new File(path, "pokemon.json");
                    json.createNewFile();
                    Writer writer = new FileWriter(json);
                    writer.write(gson.toJson(config));
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("An error occurred while trying to read the Member Config file!");
                System.err.println(err.fillInStackTrace());
            }
        }
    }
    public class PokemonMember {
        String species;
        String form;
        String gender;
        String ability;
        String nature;
        String heldItem;

        int level;
        int[] ivs;
        int[] evs;
        String[] moveset;

        public String getSpecies() {
            return species;
        }

        public String getForm() {
            return form;
        }

        public String getGender() {
            return gender;
        }

        public String getAbility() {
            return ability;
        }

        public String getNature() {
            return nature;
        }

        public String getHeldItem() {
            return heldItem;
        }

        public int[] getIvs() {
            return ivs;
        }

        public int[] getEvs() {
            return evs;
        }

        public String[] getMoveset() {
            return moveset;
        }

        public int getLevel() {
            return level;
        }
    }
}
