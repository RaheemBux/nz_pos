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
public class ExpenseCategory extends AuditableEntity{

    private int expenseCategoryId;
    private String category;

    public ExpenseCategory() {
        // Default constructor
    }

    public ExpenseCategory(int expenseCategoryId, String category) {
        this.expenseCategoryId = expenseCategoryId;
        this.category = category;
    }

    public int getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(int expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
