package cz.waterchick.configwrapper;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.Settings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;

public abstract class Config {
    private final String name;
    private final File dataFolder;
    private final File file;
    private YamlDocument config;

    public Config(File dataFolder, String name){
        this.name = name;
        this.dataFolder = dataFolder;
        this.file = new File(dataFolder, name);
    }

    public void loadConfig(){
        if(!dataFolder.exists()){
            dataFolder.mkdirs();
        }
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            if(getSettings() != null){
                this.config = YamlDocument.create(file, classLoader.getResourceAsStream(name), getSettings());
            }else{
                this.config = YamlDocument.create(file, classLoader.getResourceAsStream(name));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        onLoad();
    }

    public void reloadConfig(){
        loadConfig();
    }

    public void saveConfig(){
        onSave();
        if(config != null) {
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public YamlDocument getConfig() {
        return config;
    }

    public abstract void onLoad();

    public abstract void onSave();

    public abstract Settings[] getSettings();
}
