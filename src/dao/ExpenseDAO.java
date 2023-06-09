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
import java.util.List;
import model.Expense;

public interface ExpenseDAO {

    boolean addExpense(Expense expense);

    Expense getExpenseById(int expenseId);

    List<Expense> getAllExpenses();

    boolean updateExpense(Expense expense);

    boolean deleteExpense(int expenseId);
}
