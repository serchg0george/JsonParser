import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class Region {
    private Map<String, Municipality> municipalities = new HashMap<>();

    @JsonAnySetter
    public void setMunicipality(String key, Municipality municipality) {
        this.municipalities.put(key, municipality);
    }

    public Map<String, Municipality> getMunicipalities() {
        return municipalities;
    }

    @Override
    public String toString() {
        return "Region{" +
                "municipalities=" + municipalities +
                '}';
    }
}