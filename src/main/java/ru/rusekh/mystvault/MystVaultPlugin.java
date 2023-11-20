package ru.rusekh.mystvault;

import com.google.gson.Gson;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.rusekh.mystvault.command.AddVaultCommand;
import ru.rusekh.mystvault.command.RemoveVaultComand;
import ru.rusekh.mystvault.command.VaultCommand;
import ru.rusekh.mystvault.command.VaultShopCommand;
import ru.rusekh.mystvault.database.ServerData;
import ru.rusekh.mystvault.database.ServerDataManager;
import ru.rusekh.mystvault.database.UserManager;
import ru.rusekh.mystvault.handler.InventoryHandler;
import ru.rusekh.mystvault.handler.PlayerJoinHandler;

import java.io.File;
import java.util.Collections;

public class MystVaultPlugin extends JavaPlugin
{
    private MongoClient mongoClient;
    private MongoCollection<Document> vaultCollection;
    private MongoCollection<Document> serverDataCollection;
    private UserManager userManager;
    private PluginConfiguration configuration;
    private Gson gson;
    private ServerDataManager serverDataManager;

    @Override
    public void onEnable() {
        configuration = ConfigManager.create(PluginConfiguration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            //it.withSerdesPack(registry -> {});
            it.withBindFile(new File(this.getDataFolder(), "config.yml"));
            it.saveDefaults();
            it.load(true);
        });

        mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(
                        Collections.singletonList(new ServerAddress("136.243.156.104", 27017))))
                .credential(MongoCredential.createCredential("mo17555_hexrynek", "mo17555_hexrynek", "H@selko7".toCharArray()))
                .build());
        MongoDatabase mongoDatabase = mongoClient.getDatabase("mo17555_hexrynek");
        vaultCollection = mongoDatabase.getCollection("vaults");
        serverDataCollection = mongoDatabase.getCollection("myst_serverdata");

         gson = new Gson();

        userManager = new UserManager();
        serverDataManager = new ServerDataManager();

        if (serverDataManager.getServerData("data") == null) serverDataManager.addData(new ServerData());

        getCommand("addvault").setExecutor(new AddVaultCommand(this));
        getCommand("removevault").setExecutor(new RemoveVaultComand(this));
        getCommand("vault").setExecutor(new VaultCommand(this));
        getCommand("vaultshop").setExecutor(new VaultShopCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerJoinHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryHandler(this), this);
    }

    @Override
    public void onDisable() {
        userManager.getUserDataMap().forEach((uuid, userData) -> userData.saveAtShutdown());

        serverDataManager.getServerData("data").saveAtShutdown();
    }

    public PluginConfiguration getConfiguration() {
        return configuration;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ServerDataManager getServerDataManager() {
        return serverDataManager;
    }

    public Gson getGson() {
        return gson;
    }

    public MongoCollection<Document> getVaultCollection() {
        return vaultCollection;
    }

    public MongoCollection<Document> getServerDataCollection() {
        return serverDataCollection;
    }


    public static MystVaultPlugin getInstance() {
        return JavaPlugin.getPlugin(MystVaultPlugin.class);
    }
}
