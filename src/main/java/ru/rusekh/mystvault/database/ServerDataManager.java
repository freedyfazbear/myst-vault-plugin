package ru.rusekh.mystvault.database;

import org.bson.Document;
import ru.rusekh.mystvault.MystVaultPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ServerDataManager
{
    private final Map<String, ServerData> serverDataMap = new HashMap<>();

    public ServerDataManager() {
        MystVaultPlugin.getInstance().getServerDataCollection().find().forEach((Consumer<? super Document>) document -> {
            ServerData serverData = new ServerData(document);
            serverDataMap.putIfAbsent("data", serverData);
        });
    }

    public void addData(ServerData data) {
        serverDataMap.putIfAbsent("data", data);
        data.save();
    }

    public ServerData getServerData(String name) {
        return serverDataMap.get(name);
    }

    public Map<String, ServerData> getServerDataMap() {
        return serverDataMap;
    }
}
