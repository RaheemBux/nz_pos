/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.TransactionDTO;
import java.util.List;
import model.Ledger;

/**
 *
 * @author HP
 */
public interface LedgerDAO {
    boolean addLedger(Ledger ledger);
    Ledger getByLedgerId(int ledgerId);
    boolean updateLedger(Ledger ledger);
    boolean deleteLedger(int ledgerId);
    List<Ledger> getAllLedgers();
    List<Ledger> getAllLedgersByCustomerId(int customerId);
    List<Ledger> getAllLedgersByOrderNumber(String orderNumber);
    List<TransactionDTO> getAllPurchasesLedgers();
    List<TransactionDTO> getAllPurchasesLedgerByCustomerId(int customerId);
    List<TransactionDTO> getAllSaleLedgers();
}
