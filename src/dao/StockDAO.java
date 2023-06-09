/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Stock;

/**
 *
 * @author HP
 */
import java.util.List;

public interface StockDAO {
    Stock getStockById(int stockId);
    List<Stock> getAllStocks();
    boolean addStock(Stock stock);
    boolean updateStock(Stock stock);
    boolean deleteStock(int stockId);
}

