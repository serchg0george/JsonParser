import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Location {
    private String name;
    private String type;
    private String town_hall;
    private String phone_code;
    private double latitude;
    private double longitude;
    private List<String> post_codes;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("town_hall")
    public String getTownHall() {
        return town_hall;
    }

    @JsonProperty("town_hall")
    public void setTownHall(String town_hall) {
        this.town_hall = town_hall;
    }

    @JsonProperty("phone_code")
    public String getPhoneCode() {
        return phone_code;
    }

    @JsonProperty("phone_code")
    public void setPhoneCode(String phone_code) {
        this.phone_code = phone_code;
    }

    @JsonProperty("latitude")
    public double getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public double getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("post_codes")
    public List<String> getPostCodes() {
        return post_codes;
    }

    @JsonProperty("post_codes")
    public void setPostCodes(List<String> post_codes) {
        this.post_codes = post_codes;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", town_hall='" + town_hall + '\'' +
                ", phone_code='" + phone_code + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", post_codes=" + post_codes +
                '}';
    }
}