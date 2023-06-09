/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoimpl;

import contants.Unit;
import dao.ProductDAO;
import dao.PurchaseDAO;
import dao.SaleDetailsDAO;
import dbmanager.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.Sale;
import model.SaleDetails;
import dao.SaleDAO;

/**
 *
 * @author HP
 */
public class SaleDetailsDAOImpl implements SaleDetailsDAO{
    
    private Connection connection = DBConnection.getConnection();
    private ProductDAO productDAO = new ProductDAOImpl();
    private PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
    private SaleDAO salesDAO = new SaleDAOImpl();

    @Override
    public boolean addSaleDetails(SaleDetails saleDetails) {
        String query = "INSERT INTO sale_details (quantity, unit, price, sale_id, product_id,created_date,created_by) VALUES (?, ?, ?, ?, ?, now(),?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, saleDetails.getQuantity());
            statement.setString(2, saleDetails.getUnit().toString());
            statement.setDouble(3, saleDetails.getPrice());
            statement.setInt(4, saleDetails.getSale().getSaleId());
            statement.setInt(5, saleDetails.getProduct().getProductId());
            statement.setString(6, saleDetails.getCreatedBy());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSaleDetails(SaleDetails saleDetails) {
        String query = "UPDATE sale_details SET quantity = ?, unit = ?, price = ?, "
                + "purchase_id = ?, product_id = ?, last_modified_date=now(),"
                + "last_modified_by=? WHERE sale_details_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, saleDetails.getQuantity());
            statement.setString(2, saleDetails.getUnit().toString());
            statement.setDouble(3, saleDetails.getPrice());
            statement.setInt(4, saleDetails.getSale().getSaleId());
            statement.setInt(5, saleDetails.getProduct().getProductId());
            statement.setString(6, saleDetails.getLastModifiedBy());
            statement.setInt(7, saleDetails.getSaleDetailsId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSaleDetails(int saleDetailsId) {
        String query = "DELETE FROM sale_details WHERE sale_details_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, saleDetailsId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public SaleDetails getSaleDetailsById(int saleDetailsId) {
         String query = "SELECT * FROM sale_details WHERE sale_details_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, saleDetailsId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createSaleDetailsFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<SaleDetails> getAllSaleDetails() {
       List<SaleDetails> saleDetailsList = new ArrayList<>();
        String query = "SELECT * FROM sale_details";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SaleDetails saleDetails = createSaleDetailsFromResultSet(resultSet);
                saleDetailsList.add(saleDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return saleDetailsList;
    }

    @Override
    public List<SaleDetails> getSaleDetailsBySaleId(int saleId) {
        List<SaleDetails> saleDetailsList = new ArrayList<>();
        String query = "SELECT * FROM sale_details where sale_id=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, saleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SaleDetails saleDetails = createSaleDetailsFromResultSet(resultSet);
                saleDetailsList.add(saleDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return saleDetailsList;
    }
    // Helper method to create PurchaseDetails object from ResultSet
    private SaleDetails createSaleDetailsFromResultSet(ResultSet resultSet) throws SQLException {
        SaleDetails saleDetails = new SaleDetails();
        saleDetails.setSaleDetailsId(resultSet.getInt("sale_details_id"));
        saleDetails.setQuantity(resultSet.getInt("quantity"));
        saleDetails.setUnit(Unit.valueOf(resultSet.getString("unit")));
        saleDetails.setPrice(resultSet.getDouble("price"));

        // Assuming you have separate DAOs for Purchase and Product and their respective methods to retrieve the objects
        Sale sale = salesDAO.getSalesById(resultSet.getInt("sale_id"));
        Product product = productDAO.getProductById(resultSet.getInt("product_id"));

        saleDetails.setSale(sale);
        saleDetails.setProduct(product);

        return saleDetails;
    }

    
}
