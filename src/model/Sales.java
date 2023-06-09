/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author HP
 */
import contants.Unit;
import contants.PaymentType;
import java.sql.Date;

public class Sales extends AuditableEntity {

    private int salesId;
    private Date salesDate;
    private int quantity;
    private Unit unit;
    private int price;
    private String salesNumber;
    private int amountPaid;
    private int amountRemaining;
    private int totalAmount;
    private PaymentType paymentType;
    private Customer customer;
    private Product product;
    
    public Sales(){}

    public Sales(int salesId, Date salesDate, int quantity, Unit unit, int price, String salesNumber,
            int amountPaid, int amountRemaining, int totalAmount, PaymentType paymentType,
            Customer customer, Product product) {
        this.salesId = salesId;
        this.salesDate = salesDate;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.salesNumber = salesNumber;
        this.amountPaid = amountPaid;
        this.amountRemaining = amountRemaining;
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
        this.customer = customer;
        this.product = product;
    }

    // getters and setters
    public int getSalesId() {
        return salesId;
    }

    public void setSalesId(int salesId) {
        this.salesId = salesId;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSalesNumber() {
        return salesNumber;
    }

    public void setSalesNumber(String salesNumber) {
        this.salesNumber = salesNumber;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(int amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
