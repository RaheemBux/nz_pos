/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoimpl;

import contants.CustomerType;
import dao.CustomerDAO;
import dbmanager.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Customer;

/**
 *
 * @author HP
 */
public class CustomerDAOImpl implements CustomerDAO {

    private final Connection connection = DBConnection.getConnection();

    @Override
    public boolean addCustomer(Customer customer) {
        boolean success = false;

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO customers (name, email, contact1, address, customer_type,emirates_id,expiry_date,contact2,contact3,"
                        + "created_date,created_by) VALUES (?, ?, ?, ?, ?,?,?,?,?,now(),?)"
        )) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getContact());
            statement.setString(4, customer.getAddress());
            statement.setString(5, customer.getCustomerType().toString());
            statement.setString(6, customer.getEmiratesId());
            statement.setDate(7, customer.getExpiryDate());
            statement.setString(8, customer.getContact2());
            statement.setString(9, customer.getContact3());
            statement.setString(10, customer.getCreatedBy());

            success = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        boolean success = false;

        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE customers SET name=?, email=?, contact1=?, address=?, customer_type=?, emirates_id=?, expiry_date=? "
                + ",contact2=?,contact3=?,last_modified_date=now(),last_modified_by=? WHERE customer_id=?"
        )) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getContact());
            statement.setString(4, customer.getAddress());
            statement.setString(5, customer.getCustomerType().toString());
            statement.setString(6, customer.getEmiratesId());
            statement.setDate(7, customer.getExpiryDate());
            statement.setString(8, customer.getContact2());
            statement.setString(9, customer.getContact3());
            statement.setString(10, customer.getLastModifiedBy());
            statement.setInt(11, customer.getCustomerId());

            success = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        boolean success = false;

        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM customers WHERE customer_id=?"
        )) {
            statement.setInt(1, customerId);

            success = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }

    @Override
    public Customer getCustomerById(int customerId) {
        Customer customer = null;

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM customers WHERE customer_id=?"
        )) {
            statement.setInt(1, customerId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                customer = new Customer(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("contact1"),
                        resultSet.getString("address"),
                        CustomerType.valueOf(resultSet.getString("customer_type")),
                        resultSet.getString("emirates_id"),
                        resultSet.getDate("expiry_date"),
                        resultSet.getString("contact2"),
                        resultSet.getString("contact3")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM customers"
        )) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("contact1"),
                        resultSet.getString("address"),
                        CustomerType.valueOf(resultSet.getString("customer_type")),
                        resultSet.getString("emirates_id"),
                        resultSet.getDate("expiry_date"),
                        resultSet.getString("contact2"),
                        resultSet.getString("contact3")
                );

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    @Override
    public Customer getCustomerByName(String customerName) {
        Customer customer = null;

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM customers WHERE name=? limit 1"
        )) {
            statement.setString(1, customerName);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                customer = new Customer(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("contact1"),
                        resultSet.getString("address"),
                        CustomerType.valueOf(resultSet.getString("customer_type")),
                        resultSet.getString("emirates_id"),
                        resultSet.getDate("expiry_date"),
                        resultSet.getString("contact2"),
                        resultSet.getString("contact3")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }
}
