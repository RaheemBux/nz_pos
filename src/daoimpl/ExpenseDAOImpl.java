/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoimpl;

/**
 *
 * @author HP
 */
import dao.ExpenseCategoryDAO;
import dao.ExpenseDAO;
import dbmanager.DBConnection;
import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import model.Expense;
import model.ExpenseCategory;

public class ExpenseDAOImpl implements ExpenseDAO {

    private Connection connection = DBConnection.getConnection();
    private ExpenseCategoryDAO expenseCategoryDAO = new ExpenseCategoryDAOImpl();

    @Override
    public boolean addExpense(Expense expense) {
        String query = "INSERT INTO expenses (name, ref_number, amount, expense_date, "
                + "expense_category,created_date,created_by) VALUES (?, ?, ?, ?, ?,now(),?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, expense.getName());
            statement.setString(2, expense.getRefNumber());
            statement.setDouble(3, expense.getAmount());
            statement.setDate(4, new java.sql.Date(expense.getExpenseDate().getTime()));
            statement.setInt(5, expense.getExpenseCategory().getExpenseCategoryId());
            statement.setString(6, expense.getCreatedBy());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Expense getExpenseById(int expenseId) {
        String query = "SELECT * FROM expenses WHERE expense_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, expenseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ExpenseCategoryDAO categoryDAO = new ExpenseCategoryDAOImpl();
                int categoryId = resultSet.getInt("expense_category");
                ExpenseCategory category = categoryDAO.getExpenseCategoryById(categoryId);
                int fetchedExpenseId = resultSet.getInt("expense_id");
                String name = resultSet.getString("name");
                String refNumber = resultSet.getString("ref_number");
                double amount = resultSet.getDouble("amount");
                Date expenseDate = resultSet.getDate("expense_date");
                return new Expense(fetchedExpenseId, name, refNumber, amount, expenseDate, category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM expenses";
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            ExpenseCategoryDAO categoryDAO = new ExpenseCategoryDAOImpl();
            while (resultSet.next()) {
                int categoryId = resultSet.getInt("expense_category");
                ExpenseCategory category = categoryDAO.getExpenseCategoryById(categoryId);
                int expenseId = resultSet.getInt("expense_id");
                String name = resultSet.getString("name");
                String refNumber = resultSet.getString("ref_number");
                double amount = resultSet.getDouble("amount");
                Date expenseDate = resultSet.getDate("expense_date");
                expenses.add(new Expense(expenseId, name, refNumber, amount, expenseDate, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public boolean updateExpense(Expense expense) {
        String query = "UPDATE expenses SET name = ?, ref_number = ?, amount = ?, expense_date = ?,"
                + " expense_category = ?,last_modified_date=now(),last_modified_by=? WHERE expense_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, expense.getName());
            statement.setString(2, expense.getRefNumber());
            statement.setDouble(3, expense.getAmount());
            statement.setDate(4, new java.sql.Date(expense.getExpenseDate().getTime()));
            statement.setInt(5, expense.getExpenseCategory().getExpenseCategoryId());
            statement.setString(6, expense.getLastModifiedBy());
            statement.setInt(7, expense.getExpenseId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteExpense(int expenseId) {
        String query = "DELETE FROM expenses WHERE expense_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, expenseId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
