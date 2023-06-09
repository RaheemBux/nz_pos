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
import dbmanager.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ExpenseCategory;

public class ExpenseCategoryDAOImpl implements ExpenseCategoryDAO {

    private Connection connection = DBConnection.getConnection();

    @Override
    public boolean addExpenseCategory(ExpenseCategory expenseCategory) {
        String query = "INSERT INTO expense_category (expense_category_id, category,created_date,created_by) VALUES (?, ?,now(),?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, expenseCategory.getExpenseCategoryId());
            statement.setString(2, expenseCategory.getCategory());
            statement.setString(3, expenseCategory.getCreatedBy());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ExpenseCategory getExpenseCategoryById(int expenseCategoryId) {
        String query = "SELECT * FROM expense_category WHERE expense_category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, expenseCategoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int categoryId = resultSet.getInt("expense_category_id");
                String category = resultSet.getString("category");
                return new ExpenseCategory(categoryId, category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ExpenseCategory> getAllExpenseCategories() {
        List<ExpenseCategory> expenseCategories = new ArrayList<>();
        String query = "SELECT * FROM expense_category";
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int categoryId = resultSet.getInt("expense_category_id");
                String category = resultSet.getString("category");
                expenseCategories.add(new ExpenseCategory(categoryId, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenseCategories;
    }

    @Override
    public boolean updateExpenseCategory(ExpenseCategory expenseCategory) {
        String query = "UPDATE expense_category SET category = ?,last_modified_date=now(),last_modified_by=? WHERE expense_category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, expenseCategory.getCategory());
            statement.setString(2, expenseCategory.getLastModifiedBy());
            statement.setInt(3, expenseCategory.getExpenseCategoryId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteExpenseCategory(int expenseCategoryId) {
        String query = "DELETE FROM expense_category WHERE expense_category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, expenseCategoryId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ExpenseCategory getExpenseCategoryByCategory(String category) {
        String query = "SELECT * FROM expense_category WHERE category = ? limit 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int categoryId = resultSet.getInt("expense_category_id");
                String cat = resultSet.getString("category");
                return new ExpenseCategory(categoryId, cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
