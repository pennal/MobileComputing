package mc.usi.org.mobilecomputingproject.REST.models;

import java.util.Date;

/**
 * Created by Lucas on 20.12.17.
 */

public class UserSignup {
    // Define all the fields needed for the user to sign up.

    private String firstname;
    private String lastname;
    private String email;
    private Date dateOfBirth;
    private String username;
    private String password;

    public UserSignup() {}

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
