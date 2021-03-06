package msadaka.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Gender {
    MALE("MALE"),FEMALE("FEMALE"),UNKNOWN("UNKNOWN");

    private String name;

    private static final Map<String,Gender> ENUM_MAP;

    Gender (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    // Build an immutable map of String name to enum pairs.
    // Any Map impl can be used.

    static {
        Map<String,Gender> map = new ConcurrentHashMap<String,Gender>();
        for (Gender instance : Gender.values()) {
            map.put(instance.getName(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static Gender get (String name) {
        return ENUM_MAP.get(name);
    }

}
