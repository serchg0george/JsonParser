import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Location;
import model.Municipality;
import model.Region;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private Map<String, Region> regions;
    ObjectMapper mapper = new ObjectMapper();

    static Connection connection = null;

    static PreparedStatement regionStatement = null;
    static PreparedStatement municipalityStatement = null;
    static PreparedStatement getRegionStatement = null;
    static PreparedStatement getMunicipalityStatement = null;

    public Main(String filePath) throws IOException {

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        regions = mapper.readValue(new File(filePath), new TypeReference<>() {
        });
    }

    public List<Location> searchByPostalCode(String postalCode) {
        return regions.values().stream()
                .flatMap(region -> region.getMunicipalities().values().stream())
                .flatMap(municipality -> municipality.getLocations().values().stream())
                .filter(location -> location.getPostCodes().contains(postalCode))
                .collect(Collectors.toList());
    }

    public List<Location> searchByCityName(String cityName) {
        return regions.values().stream()
                .flatMap(region -> region.getMunicipalities().values().stream())
                .flatMap(municipality -> municipality.getLocations().values().stream())
                .filter(location -> location.getName().equalsIgnoreCase(cityName))
                .collect(Collectors.toList());
    }

    public List<Location> searchByCityTownHall(String townHall) {
        return regions.values().stream()
                .flatMap(region -> region.getMunicipalities().values().stream())
                .flatMap(municipality -> municipality.getLocations().values().stream())
                .filter(location -> location.getTownHall().equalsIgnoreCase(townHall))
                .collect(Collectors.toList());
    }

    public void saveMunicipalitiesToDatabase() {

    }


    public static void main(String[] args) throws IOException {

        connection = ConnectionToDatabase.getConnection();

        Main main = new Main("C:/Users/Serchg/Desktop/Places-in-Bulgaria.json");

//        System.out.println(main.regions);

        String regionSql = "INSERT INTO regions(name) VALUES (?)";
        String municipalitySql = "INSERT INTO municipality(name, region_id) VALUES (?, ?)";
        String getRegionId = "SELECT id FROM regions WHERE name = ?";
        String getMunicipalityId = "SELECT id FROM municipality WHERE name = ? AND region_id = ?";
        String insertLocation = "INSERT INTO location(name, municipality_id) VALUES (?, ?)";
        String findLocationId = "SELECT id FROM location WHERE name = ? AND MUNICIPALITY_ID = ?";
        String insertPostcode = "INSERT INTO postcode(code, location_id) VALUES (?, ?)";
        try {
            regionStatement = connection.prepareStatement(regionSql);
            municipalityStatement = connection.prepareStatement(municipalitySql);
            getRegionStatement = connection.prepareStatement(getRegionId);
            getMunicipalityStatement = connection.prepareStatement(getMunicipalityId);
            for (Map.Entry<String, Region> regionEntry : main.regions.entrySet()) {
                Region region = regionEntry.getValue();
                regionStatement.setString(1, regionEntry.getKey());
                regionStatement.executeUpdate();
                getRegionStatement.setString(1, regionEntry.getKey());
                ResultSet result = getRegionStatement.executeQuery();
                Long regionId = null;
                if (result.next()) {
                    regionId = result.getLong("ID");
                }
                for (Map.Entry<String, Municipality> municipalityEntry : region.getMunicipalities().entrySet()) {
                    municipalityStatement.setString(1, municipalityEntry.getKey());
                    municipalityStatement.setLong(2, regionId);
                    municipalityStatement.executeUpdate();
                    Municipality municipality = municipalityEntry.getValue();

                    getMunicipalityStatement.setString(1, municipalityEntry.getKey());
                    getMunicipalityStatement.setLong(2, regionId);
                    ResultSet resultSet = getMunicipalityStatement.executeQuery();
                    Long municipalityId = null;
                    if (resultSet.next()) {
                        municipalityId = resultSet.getLong("ID");
                    }
                    for (Map.Entry<String, Location> locations : municipality.getLocations().entrySet()) {
                        PreparedStatement statement = connection.prepareStatement(insertLocation);
                        statement.setString(1, locations.getKey());
                        statement.setLong(2, municipalityId);
                        statement.executeUpdate();
                        
                        PreparedStatement findLocationIdStatement = connection.prepareStatement(findLocationId);
                        findLocationIdStatement.setString(1, locations.getKey());
                        findLocationIdStatement.setLong(2, municipalityId);
                        
                        var resultQuery = findLocationIdStatement.executeQuery();
                        Long locationId = null;
                        if (resultQuery.next()) {
                            locationId = resultQuery.getLong("ID");
                        }
                        
                        for (String postCodes : locations.getValue().getPostCodes()) {
                            PreparedStatement postcodeStatement = connection.prepareStatement(insertPostcode);
                            postcodeStatement.setString(1, postCodes);
                            postcodeStatement.setLong(2, locationId);
                            postcodeStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(main.regions);
//
//        try (Scanner scanner = new Scanner(System.in)) {
//            Main postalCodeSearch = new Main("C:/Users/Serchg/Desktop/Places-in-Bulgaria.json");
//            boolean isContinue = true;
//            while (isContinue) {
//                System.out.println("Enter the postal code here: ");
//                String searchRequest = scanner.nextLine();
//                List<Location> locations = postalCodeSearch.searchByPostalCode(searchRequest);
//
//                locations.forEach(location -> {
//                    System.out.println("Name: " + location.getName());
//                    System.out.println("Type: " + location.getType());
//                    System.out.println("Town Hall: " + location.getTownHall());
//                    System.out.println("Phone Code: " + location.getPhoneCode());
//                    System.out.println("Latitude: " + location.getLatitude());
//                    System.out.println("Longitude: " + location.getLongitude());
//                    System.out.println("Post Codes: " + location.getPostCodes());
//                    System.out.println();
//                });
//
//                System.out.println("Continue? (y/n)");
//                if (scanner.hasNext("n")) {
//                    isContinue = false;
//                }
//            }

//            System.out.println("Enter city name here: ");
//            String searchRequest = scanner.nextLine();
//
//            System.out.println("Enter town hall name here: ");
//            String searchRequest = scanner.nextLine();


//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
