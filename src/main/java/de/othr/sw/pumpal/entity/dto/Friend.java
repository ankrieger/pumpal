package de.othr.sw.pumpal.entity.dto;


public class Friend {
    private String email;
    private String firstName;
    private String name;

    public Friend() {
    }

    public Friend(String email, String firstName, String name) {
        this.email = email;
        this.firstName = firstName;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //TODO:do i even need those?

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }

    @Override
    public String toString() {
        {
            return "Friend{" +
                    "email='" + email + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
