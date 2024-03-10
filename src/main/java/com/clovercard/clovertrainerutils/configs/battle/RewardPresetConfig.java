package com.clovercard.clovertrainerutils.configs.battle;

import com.clovercard.clovertrainerutils.configs.shuffler.MemberConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RewardPresetConfig {
    public static RewardPresetConfig DATA = new RewardPresetConfig(new HashMap<>());
    private HashMap<String, List<RewardInstance>> presets;
    public RewardPresetConfig(HashMap<String, List<RewardInstance>> presets){
        this.presets = presets;
        DATA = this;
    }

    public HashMap<String, List<RewardInstance>> getPresets() {
        return presets;
    }

    public void load() {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader("config/CloverTrainerUtils/conddrops/presets.json");
            DATA = gson.fromJson(reader, RewardPresetConfig.class);
            reader.close();
        }
        catch (IOException err) {
            if(err instanceof FileNotFoundException) {
                try {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    RewardPresetConfig config = new RewardPresetConfig(new HashMap<>());

                    //Create an example list to hold example rewards
                    List<RewardInstance> list = new ArrayList<>();
                    RewardInstance reward1 = new RewardInstance("item", "pixelmon:great_ball", 100, 5);
                    RewardInstance reward2 = new RewardInstance("cmd", "say Hello World!", 100, 1);
                    list.add(reward1);
                    list.add(reward2);
                    config.getPresets().put("example", list);
                    DATA.presets = config.presets;

                    //Save config
                    File path = new File("config/CloverTrainerUtils/conddrops/");
                    if(!path.mkdirs()){
                        System.err.println("Unable to make parent directories!");
                    }
                    File json = new File(path, "presets.json");
                    json.createNewFile();
                    Writer writer = new FileWriter(json);
                    writer.write(gson.toJson(config));
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("An error occurred while trying to read the Reward Preset Config file!");
                System.err.println(err.fillInStackTrace());
            }
        }
    }
}
