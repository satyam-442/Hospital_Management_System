package com.example.hospitalmanagementsystem.modal;

public class Patients {
    String Email, FirstName, Gender, LastName, Password, Phone, UsersID, image;

    public Patients() {
    }

    public Patients(String email, String firstName, String gender, String lastName, String password, String phone, String usersID, String image) {
        Email = email;
        FirstName = firstName;
        Gender = gender;
        LastName = lastName;
        Password = password;
        Phone = phone;
        UsersID = usersID;
        this.image = image;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUsersID() {
        return UsersID;
    }

    public void setUsersID(String usersID) {
        UsersID = usersID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
