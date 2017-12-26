package mc.usi.org.mobilecomputingproject.REST.models;

/**
 * Created by Lucas on 17.12.17.
 */

public class PlainCredentials {
    private String username;
    private String password;

    public PlainCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return username;
    }

    public void setEmail(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
