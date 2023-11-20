package ru.rusekh.mystvault.helper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationHelper {

    private static final int LEGACY_MIN_HEIGHT = 0;

    private LocationHelper() {
    }

    public static double flatDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getZ() - a.getZ(), 2));
    }

    public static String locToString(Location loc) {
        return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
    }

    public static int getX(Location location) {
        return (int) location.getX();
    }

    public static int getZ(Location location) {
        return (int) location.getZ();
    }

    public static String antiAfkLocString(Location loc) {
        return "X: " + loc.getX() + ", Y: " + loc.getY() + ", Z: " + loc.getZ();
    }

    public static Location locFromString(String str) {
            String[] str2loc = str.split(":");
            Location loc = new Location(Bukkit.getWorld(str2loc[0]), 0.0, 0.0, 0.0, 0.0f, 0.0f);
            loc.setX(Double.parseDouble(str2loc[1]));
            loc.setY(Double.parseDouble(str2loc[2]));
            loc.setZ(Double.parseDouble(str2loc[3]));
            loc.setYaw(Float.parseFloat(str2loc[4]));
            loc.setPitch(Float.parseFloat(str2loc[5]));
            return loc;
    }
    public static Location parseLocation(String string) {
        if (string == null) {
            return null;
        }

        String[] split = string.split(",");
        if (split.length < 4) {
            return null;
        }

        World world = Bukkit.getWorld(split[0]);
        if (world == null) {
            world = Bukkit.getWorlds().get(0);
        }

        return new Location(world, Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
    }

    public static boolean equals(Location location, Location to) {
        return (location.getBlockX() == to.getBlockX() && location.getBlockY() == to.getBlockY() && location.getBlockZ() == to.getBlockZ());
    }

    public static boolean equalsFlat(Location location, Location to) {
        return (location.getBlockX() == to.getBlockX() && location.getBlockZ() == to.getBlockZ());
    }

    public static String toString(Location location) {
        if (location == null) {
            return "";
        }

        World world = location.getWorld();
        if (world == null) {
            return "";
        }

        return location.getWorld().getName() + "," + (float) location.getX() + "," + (float) location.getY() +
                "," + (float) location.getZ();
    }

    public static boolean inArea(Location target, Location first, Location second) {
        if (first.getWorld().getName().equals(second.getWorld().getName()) && target.getWorld().getName().equals(first.getWorld().getName()) && (target.getBlockX() >= first.getBlockX() && target.getBlockX() <= second.getBlockX() || target.getBlockX() <= first.getBlockX() && target.getBlockX() >= second.getBlockX()) && (target.getBlockZ() >= first.getBlockZ() && target.getBlockZ() <= second.getBlockZ() || target.getBlockZ() <= first.getBlockZ() && target.getBlockZ() >= second.getBlockZ())) {
            return target.getBlockY() >= first.getBlockY() && target.getBlockY() <= second.getBlockY() || target.getBlockY() <= first.getBlockY() && target.getBlockY() >= second.getBlockY();
        }
        return false;
    }
}