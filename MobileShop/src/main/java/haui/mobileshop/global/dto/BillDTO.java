package haui.mobileshop.global.dto;

import java.util.Date;

public class BillDTO {
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getAddressReceive() {
        return addressReceive;
    }

    public void setAddressReceive(String addressReceive) {
        this.addressReceive = addressReceive;
    }

    private String fullName;
    private String email;
    private String phone;
    private Date createdDate;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BillDTO(String fullName, String email, String phone, String addressReceive, Date createdDate, int status) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.createdDate = createdDate;
        this.addressReceive = addressReceive;
        this.status = status;
    }

    private String addressReceive;
}
