/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author HP
 */
import model.ExpenseCategory;
import java.util.List;

public interface ExpenseCategoryDAO {
    boolean addExpenseCategory(ExpenseCategory expenseCategory);
    ExpenseCategory getExpenseCategoryById(int expenseCategoryId);
    List<ExpenseCategory> getAllExpenseCategories();
    boolean updateExpenseCategory(ExpenseCategory expenseCategory);
    boolean deleteExpenseCategory(int expenseCategoryId);

    ExpenseCategory getExpenseCategoryByCategory(String category);
}


