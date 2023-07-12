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
import model.SaleDetails;

public interface SaleDetailsDAO {

    boolean addSaleDetails(SaleDetails saleDetails);

    boolean updateSaleDetails(SaleDetails saleDetails);

    boolean deleteSaleDetails(int saleDetailsId);

    SaleDetails getSaleDetailsById(int saleDetailsId);

    List<SaleDetails> getAllSaleDetails();
    
    List<SaleDetails> getSaleDetailsBySaleId(int saleId);
    
    SaleDetails getSaleDetailsIdBySaleNumberAndProductName(String saleNumber,String productName);
}
