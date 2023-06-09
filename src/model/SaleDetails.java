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
public class SaleDetails extends AuditableEntity{

    private int saleDetailsId;
    private int quantity;
    private Unit unit;
    private double price;
    private Sale sale;
    private Product product;
    
    public SaleDetails(){}

    public SaleDetails(int purchaseDetailsId, int quantity, Unit unit, double price, Sale sale, Product product) {
        this.saleDetailsId = purchaseDetailsId;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.sale = sale;
        this.product = product;
    }
    
    // Constructors, getters, and setters
    public int getSaleDetailsId() {
        return saleDetailsId;
    }

    public void setSaleDetailsId(int saleDetailsId) {
        this.saleDetailsId = saleDetailsId;
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "PurchaseDetails{" + "purchaseDetailsId=" + saleDetailsId + ", quantity=" + quantity + ", unit=" + unit + ", price=" + price + ", sale=" + sale + ", product=" + product + '}';
    }
    
}
