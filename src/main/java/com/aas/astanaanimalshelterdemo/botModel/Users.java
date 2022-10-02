package com.aas.astanaanimalshelterdemo.botModel;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@DiscriminatorColumn(name = "user_type")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;

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


    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id) && Objects.equals(chatId, users.chatId)
               && Objects.equals(userName, users.userName) && Objects.equals(phoneNumber, users.phoneNumber)
               && Objects.equals(emailAddress, users.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, userName, phoneNumber, emailAddress);
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
