/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Customer;

/**
 *
 * @author HP
 */
public interface CustomerDAO {
    boolean addCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(int customerId);
    Customer getCustomerById(int customerId);
    Customer getCustomerByName(String customerName);
    List<Customer> getAllCustomers();
}

