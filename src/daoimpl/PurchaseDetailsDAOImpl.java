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
import contants.Unit;
import dao.ProductDAO;
import dao.PurchaseDAO;
import dao.PurchaseDetailsDAO;
import dbmanager.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.Purchase;
import model.PurchaseDetails;

public class PurchaseDetailsDAOImpl implements PurchaseDetailsDAO {

    private Connection connection = DBConnection.getConnection();
    private ProductDAO productDAO = new ProductDAOImpl();
    private PurchaseDAO purchaseDAO = new PurchaseDAOImpl();

    // Constructor to initialize the connection
    public boolean addPurchaseDetails(PurchaseDetails purchaseDetails) {
        String query = "INSERT INTO purchase_details (quantity, unit, price, purchase_id, product_id,created_date,created_by) VALUES (?, ?, ?, ?, ?, now(),?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchaseDetails.getQuantity());
            statement.setString(2, purchaseDetails.getUnit().toString());
            statement.setDouble(3, purchaseDetails.getPrice());
            statement.setInt(4, purchaseDetails.getPurchase().getPurchaseId());
            statement.setInt(5, purchaseDetails.getProduct().getProductId());
            statement.setString(6, purchaseDetails.getCreatedBy());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePurchaseDetails(PurchaseDetails purchaseDetails) {
        String query = "UPDATE purchase_details SET quantity = ?, unit = ?, price = ?, "
                + "purchase_id = ?, product_id = ?, last_modified_date=now(),last_modified_by=? WHERE purchase_details_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchaseDetails.getQuantity());
            statement.setString(2, purchaseDetails.getUnit().toString());
            statement.setDouble(3, purchaseDetails.getPrice());
            statement.setInt(4, purchaseDetails.getPurchase().getPurchaseId());
            statement.setInt(5, purchaseDetails.getProduct().getProductId());
            statement.setString(6, purchaseDetails.getLastModifiedBy());
            statement.setInt(7, purchaseDetails.getPurchaseDetailsId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePurchaseDetails(int purchaseDetailsId) {
        String query = "DELETE FROM purchase_details WHERE purchase_details_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchaseDetailsId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PurchaseDetails getPurchaseDetailsById(int purchaseDetailsId) {
        String query = "SELECT * FROM purchase_details WHERE purchase_details_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchaseDetailsId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createPurchaseDetailsFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<PurchaseDetails> getAllPurchaseDetails() {
        List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
        String query = "SELECT * FROM purchase_details";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PurchaseDetails purchaseDetails = createPurchaseDetailsFromResultSet(resultSet);
                purchaseDetailsList.add(purchaseDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchaseDetailsList;
    }

    // Helper method to create PurchaseDetails object from ResultSet
    private PurchaseDetails createPurchaseDetailsFromResultSet(ResultSet resultSet) throws SQLException {
        PurchaseDetails purchaseDetails = new PurchaseDetails();
        purchaseDetails.setPurchaseDetailsId(resultSet.getInt("purchase_details_id"));
        purchaseDetails.setQuantity(resultSet.getInt("quantity"));
        purchaseDetails.setUnit(Unit.valueOf(resultSet.getString("unit")));
        purchaseDetails.setPrice(resultSet.getDouble("price"));

        // Assuming you have separate DAOs for Purchase and Product and their respective methods to retrieve the objects
        Purchase purchase = purchaseDAO.getPurchaseById(resultSet.getInt("purchase_id"));
        Product product = productDAO.getProductById(resultSet.getInt("product_id"));

        purchaseDetails.setPurchase(purchase);
        purchaseDetails.setProduct(product);

        return purchaseDetails;
    }

    @Override
    public List<PurchaseDetails> getPurchaseDetailsByPurchaseId(int purchaseId) {
        List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
        String query = "SELECT * FROM purchase_details where purchase_id=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchaseId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PurchaseDetails purchaseDetails = createPurchaseDetailsFromResultSet(resultSet);
                purchaseDetailsList.add(purchaseDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchaseDetailsList;
    }
}
