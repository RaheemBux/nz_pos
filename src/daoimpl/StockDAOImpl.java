package daoimpl;

import dao.ProductDAO;
import dao.StockDAO;
import dbmanager.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.Stock;

public class StockDAOImpl implements StockDAO {

    private final Connection conn = DBConnection.getConnection();
    private ProductDAO productDAO = new ProductDAOImpl();

    private static final String GET_STOCK_BY_ID = "SELECT * FROM stock WHERE stock_id=?";
    private static final String GET_ALL_STOCKS = "SELECT s.`stock_id`,pr.product_id,pr.name,"
            + "s.stock_date,SUM(s.purchased) AS 'Purchased', SUM(s.sold) AS 'Sold'\n"
            + "FROM stock s INNER JOIN product pr ON pr.`product_id` = s.`product_id` \n"
            + "GROUP BY s.`product_id`, s.`stock_date` ORDER BY s.`stock_date`";
    private static final String ADD_STOCK = "INSERT INTO stock (stock_id, product_id,"
            + " stock_date, purchased, sold,created_date,created_by) VALUES (?, ?, ?, ?, ?,now(),?)";
    private static final String UPDATE_STOCK = "UPDATE stock SET product_id=?, stock_date=?, "
            + "purchased=?, sold=?, last_modified_date=now(),last_modified_by=? WHERE stock_id=?";
    private static final String DELETE_STOCK = "DELETE FROM stock WHERE stock_id=?";

    @Override
    public Stock getStockById(int stockId) {
        Stock stock = null;
        try (
                PreparedStatement statement = conn.prepareStatement(GET_STOCK_BY_ID)) {
            statement.setInt(1, stockId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                stock = extractStockFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    @Override
    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        try (
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(GET_ALL_STOCKS)) {
            while (resultSet.next()) {
                Stock stock = extractStockFromResultSet(resultSet);
                stocks.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    @Override
    public boolean addStock(Stock stock) {
        try (
                PreparedStatement statement = conn.prepareStatement(ADD_STOCK)) {
            statement.setInt(1, stock.getStockId());
            statement.setInt(2, stock.getProduct().getProductId());
            statement.setDate(3, stock.getStockDate());
            statement.setInt(4, stock.getPurchased());
            statement.setInt(5, stock.getSold());
            statement.setString(6, stock.getCreatedBy());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStock(Stock stock) {
        try (
                PreparedStatement statement = conn.prepareStatement(UPDATE_STOCK)) {
            statement.setInt(1, stock.getProduct().getProductId());
            statement.setDate(2, stock.getStockDate());
            statement.setInt(3, stock.getPurchased());
            statement.setInt(4, stock.getSold());
            statement.setString(5, stock.getLastModifiedBy());
            statement.setInt(6, stock.getStockId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStock(int stockId) {
        try (
                PreparedStatement statement = conn.prepareStatement(DELETE_STOCK)) {
            statement.setInt(1, stockId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to extract Stock object from ResultSet
    private Stock extractStockFromResultSet(ResultSet resultSet) throws SQLException {
        int stockId = resultSet.getInt("stock_id");
        int productId = resultSet.getInt("product_id");
        Date stockDate = resultSet.getDate("stock_date");
        int purchased = resultSet.getInt("purchased");
        int sold = resultSet.getInt("sold");
        Product product = productDAO.getProductById(productId);
        return new Stock(stockId, product, stockDate, purchased, sold);
    }
}
