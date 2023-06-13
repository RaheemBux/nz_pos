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
import dao.ProductDAO;
import dbmanager.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductDAOImpl implements ProductDAO {

    private Connection connection = DBConnection.getConnection();

    @Override
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO product (name, code, quantity, price,created_date,created_by) "
                + "VALUES (?, ?, ?, ?,now(),?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setString(2, product.getCode());
            statement.setInt(3, product.getQuantity());
            statement.setInt(4, product.getPrice());
            statement.setString(5, product.getCreatedBy());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        String sql = "UPDATE product SET name=?, code=?, quantity=?, price=?,last_modified_date=now(),last_modified_by=? WHERE product_id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setString(2, product.getCode());
            statement.setInt(3, product.getQuantity());
            statement.setInt(4, product.getPrice());
            statement.setString(5, product.getLastModifiedBy());
            statement.setInt(6, product.getProductId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE product_id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM product WHERE product_id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getString("code"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("price")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        List<Product> productList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                productList.add(new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getString("code"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public Product getProductByName(String productName) {
        String sql = "SELECT * FROM product WHERE name=? limit 1";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getString("code"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("price")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public double getPriceByProductName(String productName) {
        String sql = "SELECT sd.price FROM sale_details sd INNER JOIN product p\n"
                + "ON sd.`product_id`=p.`product_id`\n"
                + "WHERE p.`name`=? ORDER BY sd.`created_date` DESC LIMIT 1;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
