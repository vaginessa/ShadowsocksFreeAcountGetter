package com.helloworld.shadowsocksfreeaccountgetter;

/**
 * Created by PinkD on 2016/9/4.
 * Javabean Account
 */
public class Account {
    private String name;
    private String server;
    private String encryption;
    private String password;
    private String description;

    public Account() {
    }

    public Account(String description, String encryption, String name, String password, String server) {
        this.description = description;
        this.encryption = encryption;
        this.name = name;
        this.password = password;
        this.server = server;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
