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
public class Sale extends AuditableEntity{

    private int saleId;
    private Date saleDate;
    private int quantity;
    private Unit unit;
    private double price;
    private String saleNumber;
    private double amountPaid;
    private double amountRemaining;
    private double totalAmount;
    private boolean taxable;
    private PaymentType paymentType;
    private Customer customer;
    private Product product;
    private double taxAmount;
    private String recieptNumber;
    
    
    public Sale(){}

    public Sale(int purchaseId, Date purchaseDate, int quantity, Unit unit, 
            double price, String purchaseNumber, double amountPaid, double amountRemaining, 
            double totalAmount,boolean taxable, PaymentType paymentType, Customer customer, Product product,
            double taxAmount,String recieptNumber) {
        this.saleId = purchaseId;
        this.saleDate = purchaseDate;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.saleNumber = purchaseNumber;
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

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
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

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
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
