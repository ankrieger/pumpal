package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User extends SingleIdEntity<String> {
    @Id
    @Column(name = "email")
    private String email;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String firstName;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateOfBirth;

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
    private List<Friendship> friendsRequested;  //isActive = False -> ausgehende Anfragen, True -> Freunde

    @OneToMany(/*fetch = FetchType.EAGER, */mappedBy = "requestedUser")
    private List<Friendship> friendRequests;  //isActive = False -> eingehende anfragen, True -> Freunde

//    @Transient
//    private List<Friendship> friends;
//    probably better to just use Repository methods to map depending on isActive boolean value


    public User() {
    }

    public User(AccountType accountType) {
        this.accountType = accountType;
    }

    public User(String email,
                String password,
                String name, String firstName, Date dateOfBirth,
                Address address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public User(String email, String password, String name, String firstName,  Date dateOfBirth,
                Address address, AccountType accountType, List<Workout> workouts, List<Workout> savedWorkouts,
                List<Friendship> friendsRequested, List<Friendship> friendRequests) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.accountType = accountType;
        this.workouts = workouts;
        this.savedWorkouts = savedWorkouts;
        this.friendsRequested = friendsRequested;
        this.friendRequests = friendRequests;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public void setFirstName(String firstName) {
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

    public List<Friendship> getFriendsRequested() {
        return friendsRequested;
    }

    public void setFriendsRequested(List<Friendship> friendsRequested) {
        this.friendsRequested = friendsRequested;
    }

    public List<Friendship> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<Friendship> friendRequests) {
        this.friendRequests = friendRequests;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", firstname='" + firstName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address=" + address +
                ", accountType=" + accountType +
                ", workouts=" + workouts +
                ", savedWorkouts=" + savedWorkouts +
                ", friendsRequested=" + friendsRequested +
                ", friendRequests=" + friendRequests +
                '}';
    }

    @Override
    public String getID() {
        return this.email;
    }
}
