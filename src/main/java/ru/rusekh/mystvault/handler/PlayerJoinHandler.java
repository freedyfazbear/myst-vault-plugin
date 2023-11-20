package ru.rusekh.mystvault.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.rusekh.mystvault.MystVaultPlugin;

public class PlayerJoinHandler implements Listener
{
    private final MystVaultPlugin vaultPlugin;

    public PlayerJoinHandler(MystVaultPlugin vaultPlugin) {
        this.vaultPlugin = vaultPlugin;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        var userData = vaultPlugin.getUserManager().getUser(player.getUniqueId());
        if (userData == null) vaultPlugin.getUserManager().register(player.getUniqueId(), player.getName());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        var userData = vaultPlugin.getUserManager().getUser(player.getUniqueId());
        userData.save();
    }
}
