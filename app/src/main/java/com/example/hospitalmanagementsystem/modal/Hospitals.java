package com.example.hospitalmanagementsystem.modal;

public class Hospitals {
    String FromTime, HospitalAddress, HospitalEmail, HospitalID, HospitalName, HospitalPhone, Password , ToTime;

    public Hospitals() {
    }

    public Hospitals(String fromTime, String hospitalAddress, String hospitalEmail, String hospitalID, String hospitalName, String hospitalPhone, String password , String toTime) {
        FromTime = fromTime;
        HospitalAddress = hospitalAddress;
        HospitalEmail = hospitalEmail;
        HospitalID = hospitalID;
        HospitalName = hospitalName;
        HospitalPhone = hospitalPhone;
        Password = password;
        ToTime = toTime;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getHospitalAddress() {
        return HospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        HospitalAddress = hospitalAddress;
    }

    public String getHospitalEmail() {
        return HospitalEmail;
    }

    public void setHospitalEmail(String hospitalEmail) {
        HospitalEmail = hospitalEmail;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getHospitalPhone() {
        return HospitalPhone;
    }

    public void setHospitalPhone(String hospitalPhone) {
        HospitalPhone = hospitalPhone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }
}
