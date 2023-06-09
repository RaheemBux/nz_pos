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
import java.sql.Date;

public class Expense extends AuditableEntity{
    private int expenseId;
    private String name;
    private String refNumber;
    private double amount;
    private Date expenseDate;
    private ExpenseCategory expenseCategory;

    public Expense() {
        // Default constructor
    }

    public Expense(int expenseId, String name, String refNumber, double amount, Date expenseDate, ExpenseCategory expenseCategory) {
        this.expenseId = expenseId;
        this.name = name;
        this.refNumber = refNumber;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.expenseCategory = expenseCategory;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }
}

