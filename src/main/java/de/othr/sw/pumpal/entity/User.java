package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Entity
public class User extends SingleIdEntity<String> implements UserDetails {
    @Id
    @Column(name = "email")
    @Email(message = "An email address requires an @")
    private String email;

//    @Column(nullable = false)
    @Size(min = 5, message = "Your password must be at least 5 characters long")
    private String password;

    @Size(min = 2, max = 35, message = "Your surname must be between 2 and 35 characters long")
//    @Column(nullable = false)
    private String name;

    @Size(min = 2, max = 35, message = "Your firstname must be between 2 and 35 characters long")
//    @Column(nullable = false)
    private String firstName;

//    //@Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "dd.MM.yyyy")
//    @Column(nullable = false)
//    private Date dateOfBirth;

    @Embedded
    @Valid
    private Address address;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Workout> workouts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "saved_wrk_by_user",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "email", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "workout_id", referencedColumnName = "id", nullable = false)
    )
    private List <Workout> savedWorkouts;

    @OneToMany(mappedBy = "requestingUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY) //besseren namen
    private List<Friendship> friendsRequested;  //isActive = False -> ausgehende Anfragen, True -> Freunde

    @OneToMany(mappedBy = "requestedUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Friendship> friendRequests;  //isActive = False -> eingehende anfragen, True -> Freunde

//    @Transient
//    private List<Friendship> friends;
//    probably better to just use Repository methods to map depending on isActive boolean value

    private String description;


    public User() {
    }


    public User (AccountType accountType) {
        this.accountType = accountType;
    }

    public User(String email,
                String password,
                String name,
                String firstName,
                AccountType accountType,
                Address address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.accountType = accountType;
        this.address = address;
    }

    public User(String email, String password, String name, String firstName,
                Address address, AccountType accountType, List<Workout> workouts, List<Workout> savedWorkouts,
                List<Friendship> friendsRequested, List<Friendship> friendRequests) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
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

    public String getDescription() {
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public List<Workout> getWorkouts() {
        return Collections.unmodifiableList(workouts);
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(this.accountType.name());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
//                ", workouts=" + workouts +
//                ", savedWorkouts=" + savedWorkouts +
//                ", friendsRequested=" + friendsRequested +
//                ", friendRequests=" + friendRequests +
                '}';
    }

    @Override
    public String getID() {
        return this.email;
    }
}
