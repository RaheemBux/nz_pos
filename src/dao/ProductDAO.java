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
import model.Product;

public interface ProductDAO {

    boolean addProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProduct(int productId);

    Product getProductById(int productId);

    Product getProductByName(String productName);

    List<Product> getAllProducts();
    
    double getPriceByProductName(String productName);
}
