import java.util.ArrayList;
import java.util.List;

public class User {
    private enum Gender {MALE, FEMALE};

    private String fullname;
    private int age;
    private Gender gender;
    private List<String> interests;

    public User(String fullname, int age, String occupation, Gender gender,
                List<String> interests) {
        this.fullname = fullname;

        this.age = age;
        this.gender = gender;
        this.interests = interests;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

}
