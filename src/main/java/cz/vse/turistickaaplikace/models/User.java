package cz.vse.turistickaaplikace.models;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private String jmeno;
    private String prijmeni;
    private String email;
    private String username;
    private String password;
    private Boolean banned;
    private String bannedUntil;//bude pak potřeba převést
    public User() {
        // Bez argumentů pro Jackson
    }
    public User(String username, String hashedHeslo, String hashedEmail,
                String hashedJmeno, String hashedPrijmeni, Boolean banned, String bannedUntil) {
        this.username = username;
        //přidáno zabezpečení
        this.password = hashedHeslo;
        this.email = hashedEmail;
        this.jmeno = hashedJmeno;
        this.prijmeni = hashedPrijmeni;
        this.banned = Boolean.FALSE;
        this.bannedUntil = bannedUntil;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }
    public String getEmail() {
        return email;
    }
    public Boolean getBanned() {
        return banned;
    }
    public String getBannedUntil() {
        return bannedUntil;
    }
}




