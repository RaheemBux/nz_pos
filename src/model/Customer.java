/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import contants.CustomerType;
import java.sql.Date;

/**
 *
 * @author HP
 */
public class Customer extends AuditableEntity{
    private int customerId;
    private String name;
    private String email;
    private String contact;
    private String address;
    private CustomerType customerType;
    private String emiratesId;
    private Date expiryDate;
    private String contact2;
    private String contact3;

    public Customer() {
    }

    public Customer(int customerId, String name, String email, String contact, String address, CustomerType customerType, String emiratesId, Date expiryDate, String contact2, String contact3) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.customerType = customerType;
        this.emiratesId = emiratesId;
        this.expiryDate = expiryDate;
        this.contact2 = contact2;
        this.contact3 = contact3;
    }

    // Getters and setters for all fields

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getEmiratesId() {
        return emiratesId;
    }

    public void setEmiratesId(String emiratesId) {
        this.emiratesId = emiratesId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getContact3() {
        return contact3;
    }

    public void setContact3(String contact3) {
        this.contact3 = contact3;
    }
    
    
}

