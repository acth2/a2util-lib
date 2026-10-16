package fr.acth2.a2util.memory;

import fr.acth2.a2util.logging.Logger;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class MemoryManager {
    private final Preferences prefs;

    public MemoryManager(String name) {
        this.prefs = Preferences.userRoot().node(name);
    }

    public String getStr(String key) {
        return prefs.get(key, "");
    }

    public String getStr(String key, String def) {
        return prefs.get(key, def);
    }

    public int getInt(String key) {
        return prefs.getInt(key, 0);
    }

    public int getInt(String key, int def) {
        return prefs.getInt(key, def);
    }

    public boolean getBool(String key) {
        return prefs.getBoolean(key, false);
    }

    public boolean getBool(String key, boolean def) {
        return prefs.getBoolean(key, def);
    }

    public void setStr(String key, String value) {
        Logger.log("Changed key " + key + " to " + value);
        prefs.put(key, value);
        flush();
    }

    public void setInt(String key, int value) {
        Logger.log("Changed key " + key + " to " + value);
        prefs.putInt(key, value);
        flush();
    }

    public void setBool(String key, boolean value) {
        Logger.log("Changed key " + key + " to " + value);
        prefs.putBoolean(key, value);
        flush();
    }

    public void remove(String key) {
        prefs.remove(key);
        flush();
    }

    public void clear() {
        try {
            prefs.clear();
            flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private void flush() {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }
}