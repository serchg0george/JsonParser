import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PostalCodeSearch {

    private Map<String, Region> regions;

    public PostalCodeSearch(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        regions = mapper.readValue(new File(filePath), new TypeReference<Map<String, Region>>() {});
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

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            PostalCodeSearch postalCodeSearch = new PostalCodeSearch("C:/Users/Serchg/Desktop/Places-in-Bulgaria.json");

            System.out.println("Enter the postal code here: ");
            String searchRequest = scanner.nextLine();

//            System.out.println("Enter city name here: ");
//            String searchRequest = scanner.nextLine();

//            System.out.println("Enter town hall name here: ");
//            String searchRequest = scanner.nextLine();

            List<Location> locations = postalCodeSearch.searchByPostalCode(searchRequest);

            locations.forEach(location -> {
                System.out.println("Name: " + location.getName());
                System.out.println("Type: " + location.getType());
                System.out.println("Town Hall: " + location.getTownHall());
                System.out.println("Phone Code: " + location.getPhoneCode());
                System.out.println("Latitude: " + location.getLatitude());
                System.out.println("Longitude: " + location.getLongitude());
                System.out.println("Post Codes: " + location.getPostCodes());
                System.out.println();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
