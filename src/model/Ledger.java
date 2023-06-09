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
public class Ledger extends AuditableEntity{

    private int ledgerId;
    private String orderNumber;
    private double amountPaid;
    private double amountRemaining;
    private double totalAmount;
    private Customer customer;
    
    public Ledger(){}

    // Constructors, getters, and setters
    public Ledger(int ledgerId, String orderNumber, double amountPaid, double amountRemaining, double totalAmount,Customer customer) {
        this.ledgerId = ledgerId;
        this.orderNumber = orderNumber;
        this.amountPaid = amountPaid;
        this.amountRemaining = amountRemaining;
        this.totalAmount = totalAmount;
        this.customer = customer;
    }

    public int getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(int ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
