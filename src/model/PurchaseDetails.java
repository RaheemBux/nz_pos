/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import contants.Unit;

/**
 *
 * @author HP
 */
public class PurchaseDetails extends AuditableEntity{

    private int purchaseDetailsId;
    private int quantity;
    private Unit unit;
    private double price;
    private Purchase purchase;
    private Product product;
    
    public PurchaseDetails(){}

    public PurchaseDetails(int purchaseDetailsId, int quantity, Unit unit, double price, Purchase purchase, Product product) {
        this.purchaseDetailsId = purchaseDetailsId;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.purchase = purchase;
        this.product = product;
    }
    
    // Constructors, getters, and setters
    public int getPurchaseDetailsId() {
        return purchaseDetailsId;
    }

    public void setPurchaseDetailsId(int purchaseDetailsId) {
        this.purchaseDetailsId = purchaseDetailsId;
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

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "PurchaseDetails{" + "purchaseDetailsId=" + purchaseDetailsId + ", quantity=" + quantity + ", unit=" + unit + ", price=" + price + ", purchase=" + purchase + ", product=" + product + '}';
    }
    
}
