package ru.rusekh.mystvault.database;


import com.google.gson.Gson;
import org.bson.Document;
import ru.rusekh.mystvault.MystVaultPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class UserManager
{
    public static final Gson gson = new Gson();
    private final Map<UUID, UserData> userDataMap = new ConcurrentHashMap<>();

    public UserManager() {
        MystVaultPlugin.getInstance().getVaultCollection().find().forEach((Consumer<? super Document>) document -> {
            UserData userData = new UserData(document);
            userDataMap.putIfAbsent(userData.getPlayerUUID(), userData);
        });
    }

    public void register(UUID uuid, String playerName) {
        UserData user = new UserData(uuid, playerName);
        userDataMap.put(uuid, user);
        user.save();
    }

    public UserData getUser(UUID uuid) {
        return userDataMap.get(uuid);
    }

    public UserData findUser(String name) {
        return userDataMap.values().stream().filter(it -> it.getPlayerName().equals(name)).findFirst().orElse(null);
    }

    public Map<UUID, UserData> getUserDataMap() {
        return userDataMap;
    }
}