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
import dto.TransactionDTO;
import java.util.List;
import model.Report;
import model.Sale;

public interface SaleDAO {

    boolean addSales(Sale sale);

    boolean updateSales(Sale sale);

    boolean deleteSales(int saleId);

    Sale getSalesById(int saleId);

    List<Sale> getAllSales();

    Sale getSaleBySaleNumber(String saleNumber);

    List<Report> getSaleRecieptBySaleNumber(String saleNumber);

    List<TransactionDTO> getAllSales1();

    boolean isReceiptNoExists(String receiptNo);
}
