package cz.vse.turistickaaplikace.models;

public class User {
    private String jmeno;
    private String prijmeni;
    private String email;
    private String username;
    private String heslo;
    private Boolean banned;
    private String bannedUntil;//bude pak potřeba převést
    public User(String username, String heslo, String email, String jmeno, String prijmeni, Boolean banned, String bannedUntil) {
        this.username = username;
        this.heslo = heslo;
        this.banned = banned;//možná bude lepší Boolean.FALSE
        this.bannedUntil = bannedUntil;
        this.email = email;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return heslo;
    }
}
