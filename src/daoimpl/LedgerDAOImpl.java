/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoimpl;

import dao.CustomerDAO;
import dao.LedgerDAO;
import dbmanager.DBConnection;
import dto.TransactionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Ledger;

/**
 *
 * @author HP
 */
public class LedgerDAOImpl implements LedgerDAO {

    private final Connection connection = DBConnection.getConnection();
    CustomerDAO customerDAO = new CustomerDAOImpl();

    @Override
    public boolean addLedger(Ledger ledger) {
        String sql = "INSERT INTO ledger (order_number, amount_paid, amount_remaining, total_amount, customer_id,created_date,created_by) "
                + "VALUES (?, ?, ?, ?, ?,now(),?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ledger.getOrderNumber());
            statement.setDouble(2, ledger.getAmountPaid());
            statement.setDouble(3, ledger.getAmountRemaining());
            statement.setDouble(4, ledger.getTotalAmount());
            statement.setInt(5, ledger.getCustomer().getCustomerId());
            statement.setString(6, ledger.getCreatedBy());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Ledger getByLedgerId(int ledgerId) {
        String sql = "SELECT * FROM ledger WHERE ledger_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ledgerId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                Ledger ledger = new Ledger(
                        resultSet.getInt("ledger_id"),
                        resultSet.getString("order_number"),
                        resultSet.getDouble("amount_paid"),
                        resultSet.getDouble("amount_remaining"),
                        resultSet.getDouble("total_amount"),
                        customer
                );
                return ledger;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateLedger(Ledger ledger) {
        String sql = "UPDATE ledger SET order_number = ?, amount_paid = ?, amount_remaining = ?, total_amount = ?,"
                + " customer_id = ?, last_modified_date=now(),last_modified_by=? WHERE ledger_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ledger.getOrderNumber());
            statement.setDouble(2, ledger.getAmountPaid());
            statement.setDouble(3, ledger.getAmountRemaining());
            statement.setDouble(4, ledger.getTotalAmount());
            statement.setInt(5, ledger.getCustomer().getCustomerId());
            statement.setString(6, ledger.getLastModifiedBy());
            statement.setInt(7, ledger.getLedgerId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteLedger(int ledgerId) {
        String sql = "DELETE FROM ledger WHERE ledger_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ledgerId);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Ledger> getAllLedgers() {
        List<Ledger> ledgers = new ArrayList<>();
        String sql = "SELECT * FROM ledger";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                Ledger ledger = new Ledger(
                        resultSet.getInt("ledger_id"),
                        resultSet.getString("order_number"),
                        resultSet.getDouble("amount_paid"),
                        resultSet.getDouble("amount_remaining"),
                        resultSet.getDouble("total_amount"),
                        customer
                );
                ledgers.add(ledger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ledgers;
    }

    @Override
    public List<Ledger> getAllLedgersByCustomerId(int customerId) {
        List<Ledger> ledgers = new ArrayList<>();
        String sql = "SELECT * FROM ledger WHERE customer_id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                Ledger ledger = new Ledger(
                        resultSet.getInt("ledger_id"),
                        resultSet.getString("order_number"),
                        resultSet.getDouble("amount_paid"),
                        resultSet.getDouble("amount_remaining"),
                        resultSet.getDouble("total_amount"),
                        customer
                );
                ledgers.add(ledger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ledgers;
    }

    @Override
    public List<Ledger> getAllLedgersByOrderNumber(String orderNumber) {
        List<Ledger> ledgers = new ArrayList<>();
        String sql = "SELECT * FROM ledger WHERE order_number=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, orderNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                Ledger ledger = new Ledger(
                        resultSet.getInt("ledger_id"),
                        resultSet.getString("order_number"),
                        resultSet.getDouble("amount_paid"),
                        resultSet.getDouble("amount_remaining"),
                        resultSet.getDouble("total_amount"),
                        customer
                );
                ledgers.add(ledger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ledgers;
    }

    @Override
    public List<TransactionDTO> getAllPurchasesLedgers() {
        List<TransactionDTO> purchaseLedgerList = new ArrayList<>();
        // SQL query
        String sql = "SELECT p.`purchase_id`,p.purchase_number,p.purchase_date,p.rec_number, c.name, c.contact1, p.amount_paid, p.amount_remaining,\n"
                + "p.tax_amount,p.total_amount  FROM purchase p \n"
                + "INNER JOIN customers c ON p.customer_id = c.customer_id ORDER BY p.`created_date` DESC;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // statement.setString(1, orderNumber);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                TransactionDTO purchaseLedger = new TransactionDTO();

                // Set the values from the result set to the DTO object
                purchaseLedger.setTransactionId(rs.getInt("purchase_id"));
                purchaseLedger.setOrderNumber(rs.getString("purchase_number"));
                purchaseLedger.setTranscationDate(rs.getDate("purchase_date"));
                purchaseLedger.setRecieptNo(rs.getString("rec_number"));
                purchaseLedger.setCustomerName(rs.getString("name"));
                purchaseLedger.setContact(rs.getString("contact1"));
                purchaseLedger.setAmountPaid(rs.getDouble("amount_paid"));
                purchaseLedger.setAmountRemaining(rs.getDouble("amount_remaining"));
                purchaseLedger.setTaxAmount(rs.getDouble("tax_amount"));
                purchaseLedger.setTotalAmount(rs.getDouble("total_amount"));

                // Add the DTO object to the list
                purchaseLedgerList.add(purchaseLedger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchaseLedgerList;
    }

    @Override
    public List<TransactionDTO> getAllPurchasesLedgerByCustomerId(int customerId) {
        List<TransactionDTO> purchaseLedgerList = new ArrayList<>();
        // SQL query
        String sql = "SELECT p.purchase_number, c.name, c.contact1, pd.name, p.quantity, p.unit, p.price, "
                + "p.amount_paid, p.amount_remaining, p.tax_amount, p.total_amount FROM purchase p "
                + "INNER JOIN customers c ON p.customer_id = c.customer_id "
                + "INNER JOIN product pd ON pd.product_id = p.product_id "
                + "WHERE p.customer_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                TransactionDTO purchaseLedger = new TransactionDTO();

                // Set the values from the result set to the DTO object
                purchaseLedger.setOrderNumber(rs.getString("purchase_number"));
                purchaseLedger.setCustomerName(rs.getString("name"));
                purchaseLedger.setContact(rs.getString("contact1"));
                purchaseLedger.setProductName(rs.getString("name"));
                purchaseLedger.setQuantity(rs.getInt("quantity"));
                purchaseLedger.setUnit(rs.getString("unit"));
                purchaseLedger.setPrice(rs.getDouble("price"));
                purchaseLedger.setAmountPaid(rs.getDouble("amount_paid"));
                purchaseLedger.setAmountRemaining(rs.getDouble("amount_remaining"));
                purchaseLedger.setTaxAmount(rs.getDouble("tax_amount"));
                purchaseLedger.setTotalAmount(rs.getDouble("total_amount"));

                // Add the DTO object to the list
                purchaseLedgerList.add(purchaseLedger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchaseLedgerList;
    }

    @Override
    public List<TransactionDTO> getAllSaleLedgers() {
        List<TransactionDTO> saleLedgerList = new ArrayList<>();
        // SQL query
        String sql = "SELECT s.`sale_id`,s.sale_number,s.sale_date,s.rec_number, c.name, c.contact1, "
                + "s.amount_paid, s.amount_remaining,\n"
                + "s.tax_amount,s.total_amount FROM sale s\n"
                + "INNER JOIN customers c ON s.customer_id = c.customer_id ORDER BY s.`created_date` DESC;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // statement.setString(1, orderNumber);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                TransactionDTO purchaseLedger = new TransactionDTO();

                // Set the values from the result set to the DTO object
                purchaseLedger.setTransactionId(rs.getInt("sale_id"));
                purchaseLedger.setOrderNumber(rs.getString("sale_number"));
                purchaseLedger.setTranscationDate(rs.getDate("sale_date"));
                purchaseLedger.setRecieptNo(rs.getString("rec_number"));
                purchaseLedger.setCustomerName(rs.getString("name"));
                purchaseLedger.setContact(rs.getString("contact1"));
                purchaseLedger.setAmountPaid(rs.getDouble("amount_paid"));
                purchaseLedger.setAmountRemaining(rs.getDouble("amount_remaining"));
                purchaseLedger.setTaxAmount(rs.getDouble("tax_amount"));
                purchaseLedger.setTotalAmount(rs.getDouble("total_amount"));

                // Add the DTO object to the list
                saleLedgerList.add(purchaseLedger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saleLedgerList;
    }

}
