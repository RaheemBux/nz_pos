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
import model.PurchaseDetails;

public interface PurchaseDetailsDAO {

    boolean addPurchaseDetails(PurchaseDetails purchaseDetails);

    boolean updatePurchaseDetails(PurchaseDetails purchaseDetails);

    boolean deletePurchaseDetails(int purchaseDetailsId);

    PurchaseDetails getPurchaseDetailsById(int purchaseDetailsId);

    List<PurchaseDetails> getAllPurchaseDetails();
    
    List<PurchaseDetails> getPurchaseDetailsByPurchaseId(int purchaseId);
}
