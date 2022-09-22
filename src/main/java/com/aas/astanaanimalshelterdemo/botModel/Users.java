package com.aas.astanaanimalshelterdemo.botModel;



import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;

//    @JsonIgnore
//    @OneToMany(mappedBy = "user")
//    private Collection<Report> reports;

    public Users(Long id, Long chatId, String userName, String phoneNumber, String emailAddress) {
        this.id = id;
        this.chatId = chatId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public Users() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id) && Objects.equals(chatId, users.chatId)
                && Objects.equals(userName, users.userName)
                && Objects.equals(phoneNumber, users.phoneNumber)
                && Objects.equals(emailAddress, users.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, userName, phoneNumber,
                emailAddress);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
