package internetcafe.entity;

import java.time.LocalDateTime;
import java.util.Objects;


public class Member {
    private String username;
    private String password;
    private String name;
    private int id;
    private String address;
    private int balance = 0;
    private boolean loggedIn = false;
    private LocalDateTime loginTime;
    private int idNumber;
    //private static int counter = 1;

    
    private int points = 0;

    public Member(String username, String password, String name, int idNumber, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.idNumber = idNumber;
        this.address = address;
        this.id = 0;
        //counter++;
    }
    public Member() {
        
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getBalance() {
        return balance;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public int getPoints() {
        return points;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Member other = (Member) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
