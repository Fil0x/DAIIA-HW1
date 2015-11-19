import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private enum Gender {MALE, FEMALE};

    private String fullname;
    private int age;
    private Gender gender;
    private List<String> interests;

    public User(String fullname, int age, List<String> interests) {
        this.fullname = fullname;
        this.age = age;
        this.interests = interests;

        if(new Random().nextInt(2) == 0)
            this.gender = Gender.FEMALE;
        else
            this.gender = Gender.MALE;
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
