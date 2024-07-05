import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class Municipality {
    private Map<String, Location> locations = new HashMap<>();

    @JsonAnySetter
    public void setLocation(String key, Location location) {
        this.locations.put(key, location);
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return "Municipality{" +
                "locations=" + locations +
                '}';
    }
}