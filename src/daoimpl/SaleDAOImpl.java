/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoimpl;

import contants.PaymentType;
import dao.CustomerDAO;
import dao.ProductDAO;
import dbmanager.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Sale;
import dao.SaleDAO;
import dto.TransactionDTO;
import model.Report;

/**
 *
 * @author HP
 */
public class SaleDAOImpl implements SaleDAO {

    private Connection conn = DBConnection.getConnection();
    private CustomerDAO customerDAO = new CustomerDAOImpl();
    private ProductDAO productDAO = new ProductDAOImpl();

    @Override
    public boolean addSales(Sale sale) {
        try {
            String query = "INSERT INTO sale(sale_date, sale_number, amount_paid, "
                    + "amount_remaining, total_amount, payment_type,customer_id,is_taxable,"
                    + "tax_amount,rec_number,created_date,created_by) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,now(),?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(sale.getSaleDate().getTime()));
            ps.setString(2, sale.getSaleNumber());
            ps.setDouble(3, sale.getAmountPaid());
            ps.setDouble(4, sale.getAmountRemaining());
            ps.setDouble(5, sale.getTotalAmount());
            ps.setString(6, sale.getPaymentType().toString());
            ps.setInt(7, sale.getCustomer().getCustomerId());
            ps.setBoolean(8, sale.isTaxable());
            ps.setDouble(9, sale.getTaxAmount());
            ps.setString(10, sale.getRecieptNumber());
            ps.setString(11, sale.getCreatedBy());

            int count = ps.executeUpdate();
            if (count > 0) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateSales(Sale sale) {
        try {
            String query = "UPDATE sale SET sale_date=?, quantity=?, unit=?, price=?, sale_number=?, amount_paid=?, "
                    + "amount_remaining=?, total_amount=?, payment_type=?,customer_id=?, product_id=?,"
                    + "is_taxable=?,tax_amount=?,last_modified_date=now(),last_modified_by=?,rec_number=?"
                    + "WHERE sale_id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(sale.getSaleDate().getTime()));
            ps.setInt(2, sale.getQuantity());
            ps.setString(3, sale.getUnit().toString());
            ps.setDouble(4, sale.getPrice());
            ps.setString(5, sale.getSaleNumber());
            ps.setDouble(6, sale.getAmountPaid());
            ps.setDouble(7, sale.getAmountRemaining());
            ps.setDouble(8, sale.getTotalAmount());
            ps.setString(9, sale.getPaymentType().toString());
            ps.setInt(10, sale.getCustomer().getCustomerId());
            ps.setInt(11, sale.getProduct().getProductId());
            ps.setBoolean(12, sale.isTaxable());
            ps.setDouble(13, sale.getTaxAmount());
            ps.setString(14, sale.getLastModifiedBy());
            ps.setString(15, sale.getRecieptNumber());
            ps.setInt(16, sale.getSaleId());
            int count = ps.executeUpdate();
            if (count > 0) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteSales(int saleId) {
        try {
            String query = "DELETE FROM sale WHERE sale_id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, saleId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Sale> getAllSales() {
        List<Sale> saleList = new ArrayList<>();
        String query = "SELECT * FROM sale";
        try (
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Sale sale = new Sale();
                sale.setSaleId(resultSet.getInt("sale_id"));
                sale.setSaleDate(resultSet.getDate("sale_date"));
                sale.setSaleNumber(resultSet.getString("sale_number"));
                sale.setRecieptNumber(resultSet.getString("rec_number"));
                sale.setAmountPaid(resultSet.getInt("amount_paid"));
                sale.setAmountRemaining(resultSet.getInt("amount_remaining"));
                sale.setTotalAmount(resultSet.getInt("total_amount"));
                sale.setTaxAmount(resultSet.getInt("tax_amount"));
                sale.setTaxable(resultSet.getBoolean("is_taxable"));
                sale.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                sale.setCustomer(customer);
                saleList.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saleList;
    }

    @Override
    public Sale getSalesById(int saleId) {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Sale sale = null;
        String query = "SELECT * FROM sale WHERE sale_id = ?";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, saleId);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                sale = new Sale();
                sale.setSaleId(resultSet.getInt("sale_id"));
                sale.setSaleDate(resultSet.getDate("sale_date"));
                sale.setSaleNumber(resultSet.getString("sale_number"));
                sale.setRecieptNumber(resultSet.getString("rec_number"));
                sale.setAmountPaid(resultSet.getInt("amount_paid"));
                sale.setAmountRemaining(resultSet.getInt("amount_remaining"));
                sale.setTotalAmount(resultSet.getInt("total_amount"));
                sale.setTaxAmount(resultSet.getInt("tax_amount"));
                sale.setTaxable(resultSet.getBoolean("is_taxable"));
                sale.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                sale.setCustomer(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sale;
    }

    @Override
    public Sale getSaleBySaleNumber(String saleNumber) {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Sale sale = null;
        String query = "SELECT * FROM sale WHERE sale_number = ?";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, saleNumber);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                sale = new Sale();
                sale.setSaleId(resultSet.getInt("sale_id"));
                sale.setSaleDate(resultSet.getDate("sale_date"));
                sale.setSaleNumber(resultSet.getString("sale_number"));
                sale.setRecieptNumber(resultSet.getString("rec_number"));
                sale.setAmountPaid(resultSet.getInt("amount_paid"));
                sale.setAmountRemaining(resultSet.getInt("amount_remaining"));
                sale.setTotalAmount(resultSet.getInt("total_amount"));
                sale.setTaxAmount(resultSet.getInt("tax_amount"));
                sale.setTaxable(resultSet.getBoolean("is_taxable"));
                sale.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                sale.setCustomer(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sale;
    }

    @Override
    public List<Report> getSaleRecieptBySaleNumber(String saleNumber) {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        List<Report> list = new ArrayList<>();
        String query = "SELECT s.sale_number,s.rec_number,s.created_by,s.`amount_paid`,\n"
                + "s.`amount_remaining`,s.`tax_amount`,s.`total_amount`,\n"
                + "c.name AS 'customer',p.name AS 'product',\n"
                + "sd.quantity,sd.unit,sd.price FROM sale s\n"
                + "INNER JOIN sale_details sd ON s.`sale_id`=sd.`sale_id`\n"
                + "INNER JOIN product p ON sd.`product_id`=p.`product_id`\n"
                + "INNER JOIN customers c ON s.`customer_id`=c.`customer_id`\n"
                + "WHERE s.`sale_number`=?";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, saleNumber);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Report report = new Report();
                report.setOrderNumber(resultSet.getString("sale_number"));
                report.setRecNumber(resultSet.getString("rec_number"));
                report.setCreatedBy(resultSet.getString("created_by"));
                report.setAmountPaid(resultSet.getDouble("amount_paid"));
                report.setAmountRemaining(resultSet.getDouble("amount_remaining"));
                report.setTotalAmount(resultSet.getDouble("total_amount"));
                report.setPrice(resultSet.getDouble("price"));
                report.setQuantity(resultSet.getInt("quantity"));
                report.setCustomer(resultSet.getString("customer"));
                report.setProduct(resultSet.getString("product"));
                list.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TransactionDTO> getAllSales1() {
        List<TransactionDTO> sales = new ArrayList<>();
        // SQL query
        String sql = "SELECT s.`sale_id`,c.name AS 'customer_name',s.sale_number,s.rec_number,s.sale_date,\n"
                + "p.name AS 'product_name',sd.quantity,sd.unit,sd.price,sd.quantity*sd.price AS 'total',\n"
                + "s.amount_paid,s.amount_remaining,s.tax_amount,s.total_amount,s.payment_type\n"
                + "FROM sale s INNER JOIN sale_details sd \n"
                + "ON s.`sale_id`=sd.`sale_id`\n"
                + "INNER JOIN product p ON p.`product_id`= sd.`product_id`\n"
                + "INNER JOIN customers c ON s.`customer_id`= c.`customer_id` \n"
                + "ORDER BY s.`created_date` DESC";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                TransactionDTO transactionDto = new TransactionDTO();

                // Set the values from the result set to the DTO object
                transactionDto.setTransactionId(rs.getInt("sale_id"));
                transactionDto.setOrderNumber(rs.getString("sale_number"));
                transactionDto.setTranscationDate(rs.getDate("sale_date"));
                transactionDto.setRecieptNo(rs.getString("rec_number"));
                transactionDto.setProductName(rs.getString("product_name"));
                transactionDto.setCustomerName(rs.getString("customer_name"));
                transactionDto.setQuantity(rs.getInt("quantity"));
                transactionDto.setUnit(rs.getString("unit"));
                transactionDto.setPrice(rs.getDouble("price"));
                transactionDto.setTotalAmount(rs.getDouble("total"));
                transactionDto.setAmountPaid(rs.getDouble("amount_paid"));
                transactionDto.setAmountRemaining(rs.getDouble("amount_remaining"));
                transactionDto.setTaxAmount(rs.getDouble("tax_amount"));
                transactionDto.setGrandAmount(rs.getDouble("total_amount"));
                transactionDto.setPaymentType(rs.getString("payment_type"));

                // Add the DTO object to the list
                sales.add(transactionDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }

    @Override
    public boolean isReceiptNoExists(String receiptNo) {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        String query = "SELECT rec_number FROM sale WHERE rec_number = ? limit 1";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, receiptNo);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
