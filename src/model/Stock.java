/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author HP
 */
public class Stock extends AuditableEntity{

    private int stockId;
    private Product product;
    private Date stockDate;
    private int purchased;
    private int sold;

    public Stock() {
        // Empty constructor
    }

    public Stock(int stockId, Product product, Date stockDate, int purchased, int sold) {
        this.stockId = stockId;
        this.product = product;
        this.stockDate = stockDate;
        this.purchased = purchased;
        this.sold = sold;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getStockDate() {
        return stockDate;
    }

    public void setStockDate(Date stockDate) {
        this.stockDate = stockDate;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getAvailableStock() {
        return purchased - sold;
    }

    public int getTotalQuantity() {
        return purchased + sold;
    }
}
