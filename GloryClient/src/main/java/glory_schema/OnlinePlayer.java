package glory_schema;

import glory_schema.OnlinePlayerStatus;
import java.io.Serializable;


public class OnlinePlayer implements Serializable,Comparable<OnlinePlayer>{
    private Integer id;
    private String username;
    private String email;
    private String password;
    private OnlinePlayerStatus playerStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    } 

    public OnlinePlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(OnlinePlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.username != null ? this.username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OnlinePlayer other = (OnlinePlayer) obj;
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(OnlinePlayer other) {
        return this.getUsername().compareTo(other.getUsername());
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", playerStatus=" + playerStatus + '}';
    }

}
