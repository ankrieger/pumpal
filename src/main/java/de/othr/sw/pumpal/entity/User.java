package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class User extends SingleIdEntity<String> {
    @Id
    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String firstName;

    @Embedded
    //@Valid
    private Address address;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @OneToMany(mappedBy = "author")
    private List<Workout> workouts;

    @ManyToMany
    @JoinTable(
            name = "saved_wrk_by_user",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "email", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "workout_id", referencedColumnName = "id", nullable = false)
    )
    private List <Workout> savedWorkouts;

    @OneToMany(/*fetch = FetchType.EAGER, */mappedBy = "requestingUser") //besseren namen
    private List<Friendship> friendsOut;  //isActive = False -> ausgehende Anfragen, True -> Freunde

    @OneToMany(/*fetch = FetchType.EAGER, */mappedBy = "requestedUser")
    private List<Friendship> friendsIn;  //isActive = False -> eingehende anfragen, True -> Freunde


    public User() {
    }

    public User(String email,
                String password,
                String name, String firstName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
    }

    public User(String email, String password, String name, String firstName, Address address, AccountType accountType,
                List<Workout> workouts, List<Workout> savedWorkouts, List<Friendship> friendsOut,
                List<Friendship> friendsIn) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.address = address;
        this.accountType = accountType;
        this.workouts = workouts;
        this.savedWorkouts = savedWorkouts;
        this.friendsOut = friendsOut;
        this.friendsIn = friendsIn;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public List<Workout> getSavedWorkouts() {
        return savedWorkouts;
    }

    public void setSavedWorkouts(List<Workout> savedWorkouts) {
        this.savedWorkouts = savedWorkouts;
    }

    public List<Friendship> getFriendsOut() {
        return friendsOut;
    }

    public void setFriendsOut(List<Friendship> friendsOut) {
        this.friendsOut = friendsOut;
    }

    public List<Friendship> getFriendsIn() {
        return friendsIn;
    }

    public void setFriendsIn(List<Friendship> friendsIn) {
        this.friendsIn = friendsIn;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", firstname='" + firstName + '\'' +
                ", address=" + address +
                ", accountType=" + accountType +
                ", workouts=" + workouts +
                ", savedWorkouts=" + savedWorkouts +
                ", friendsOut=" + friendsOut +
                ", friendsIn=" + friendsIn +
                '}';
    }

    @Override
    public String getID() {
        return this.email;
    }
}
