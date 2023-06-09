/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Purchase;

public interface PurchaseDAO {

    boolean addPurchase(Purchase purchase);

    boolean updatePurchase(Purchase purchase);

    boolean deletePurchase(int purchaseId);

    List<Purchase> getAllPurchases();

    Purchase getPurchaseById(int purchaseId);

    Purchase getPurchaseByPurchaseNumber(String purchaseNumber);
}
