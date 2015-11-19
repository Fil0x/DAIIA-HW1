import java.util.*;

public class Utilities {
    public static int generateItemId() {
        Random r = new Random();
        int id = r.nextInt(90000)+10000;
        return id;
    }

    public User getUser(int num_interests) {
        List<String> allUsers = MyFileReader.getPersons();
        List<String> allGenres = MyFileReader.getGenre();
        Random r = new Random();

        String name = allUsers.get(r.nextInt(allUsers.size()));
        int age = r.nextInt(50);

        Set<String> s = new TreeSet<String>();
        while(s.size() != num_interests)
            s.add(allGenres.get(r.nextInt(allGenres.size())));
        List<String> interests = new ArrayList<String>();
        interests.addAll(s);

        return new User(name, age, interests);
    }

    public Artifact getArtifact() {
        int id = generateItemId();
        List<String> allGenres = MyFileReader.getGenre();
        List<String> allNames = MyFileReader.getArtifacts();
        List<String> allCities = MyFileReader.getCities();
        List<String> allCreators = MyFileReader.getCreators();
        Random r = new Random();

        String name = allNames.get(r.nextInt(allNames.size()));
        String creator = allCreators.get(r.nextInt(allCreators.size()));
        String placeOfCreation = allCities.get(r.nextInt(allCities.size()));
        String genre = allGenres.get(r.nextInt(allGenres.size()));

        Date dateOfCreation = new Date(Math.abs(System.currentTimeMillis() - r.nextLong()));

        return new Artifact(id, name, creator, dateOfCreation, placeOfCreation, genre);
    }
}
