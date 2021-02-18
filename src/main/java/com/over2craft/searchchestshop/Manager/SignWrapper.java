package com.over2craft.searchchestshop.Manager;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignWrapper implements ConfigurationSerializable {

    private final String[] lines;

    private final Location location;

    public SignWrapper(String[] lines, Location location) {
        this.lines = lines;
        this.location = location;
    }

    public SignWrapper(ArrayList<String> lines, Location location) {
        this.lines = new String[]{lines.get(0), lines.get(1), lines.get(2), lines.get(3)};
        this.location = location;
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int line) {
        return lines[line];
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("lines", lines);
        serialized.put("location", location);
        return serialized;
    }

    public static SignWrapper deserialize(Map<String, Object> deserialize) {

        return new SignWrapper(
                (ArrayList<String>) deserialize.get("lines"), (Location) deserialize.get("location")
        );
    }
}
