package model;

import java.time.LocalDateTime;

public class Customer {
    private int id ;
    private String Fullname;
    private String phone;
    private String email;
    private LocalDateTime creatDate;
    public Customer(int id , String fullname , String phone, String email , LocalDateTime creatDate){
        this.id=id;
        this.Fullname=fullname;
        this.phone=phone;
        this.email=email;
        this.creatDate=creatDate;
    }
    public Customer(String fullname , String phone, String email , LocalDateTime creatDate){
        this.Fullname=fullname;
        this.phone=phone;
        this.email=email;
        this.creatDate=creatDate;
    }
    public int getId(){
        return id;
    }
    public String getFullname(){
        return Fullname;
    }
    public String getPhone(){
        return phone;
    }
    public String getEmail(){
        return email;
    }
    public LocalDateTime getCreatDate(){
        return creatDate;
    }
    public void setId(int id ){
        this.id=id;
    }
    public void setFullname(String fullname){
        this.Fullname=fullname;
    }
    public void setPhone(String phone) {
        this.phone=phone;
    }
    public void setEmail(String email) {
        this.email=email;
    }
    public void setCreatDate(LocalDateTime creatDate){
        this.creatDate=creatDate;
    }
    @Override
    public String toString(){
        return "model.Customer{" +
                "id=" + id +
                ", fullName='" + Fullname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public LocalDateTime getCreatedAt() {
        return creatDate;
    }

    public void setCreatedAt(LocalDateTime creatDate) {
        this.creatDate = creatDate;
    }

    public String getFullName() {
        return Fullname;
    }

    public void setFullName(String fullName) {
        this.Fullname = fullName;
    }
}
