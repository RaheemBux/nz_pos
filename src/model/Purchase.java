/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import contants.PaymentType;
import contants.Unit;
import java.sql.Date;

/**
 *
 * @author HP
 */
public class Purchase extends AuditableEntity{

    private int purchaseId;
    private Date purchaseDate;
    private int quantity;
    private Unit unit;
    private double price;
    private String purchaseNumber;
    private double amountPaid;
    private double amountRemaining;
    private double totalAmount;
    private boolean taxable;
    private PaymentType paymentType;
    private Customer customer;
    private Product product;
    private double taxAmount;
    private String recieptNumber;
    
    
    public Purchase(){}

    public Purchase(int purchaseId, Date purchaseDate, int quantity, Unit unit, 
            double price, String purchaseNumber, double amountPaid, double amountRemaining, 
            double totalAmount,boolean taxable, PaymentType paymentType, Customer customer, Product product,
            double taxAmount,String recieptNumber) {
        this.purchaseId = purchaseId;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.purchaseNumber = purchaseNumber;
        this.amountPaid = amountPaid;
        this.amountRemaining = amountRemaining;
        this.totalAmount = totalAmount;
        this.taxable = taxable;
        this.paymentType = paymentType;
        this.customer = customer;
        this.product = product;
        this.taxAmount = taxAmount;
        this.recieptNumber = recieptNumber;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(String purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(double amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
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

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getRecieptNumber() {
        return recieptNumber;
    }

    public void setRecieptNumber(String recieptNumber) {
        this.recieptNumber = recieptNumber;
    }
    
    
    
}
