/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import static contants.CustomerType.CUSTOMER;
import contants.PaymentType;
import contants.Unit;
import dao.CustomerDAO;
import dao.LedgerDAO;
import dao.ProductDAO;
import dao.PurchaseDAO;
import dao.PurchaseDetailsDAO;
import dao.StockDAO;
import daoimpl.CustomerDAOImpl;
import daoimpl.LedgerDAOImpl;
import daoimpl.ProductDAOImpl;
import daoimpl.PurchaseDAOImpl;
import daoimpl.PurchaseDetailsDAOImpl;
import daoimpl.StockDAOImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.Customer;
import model.Ledger;
import model.Product;
import model.Purchase;
import model.PurchaseDetails;
import model.Stock;

/**
 *
 *
 */
public class PurchaseFrame extends javax.swing.JFrame {

    /**
     * Creates new form RegisterFrame
     */
    Object columns[] = {"Id", "Customer", "P-Date", "P-No", "Recipet No", "Total-Amount", "Paid", "Remaning", "Tax", "Payment Type"};

    DefaultTableModel defaultTableModel = new DefaultTableModel(columns, 0);

    Object[] cartTableColumns = {"Id", "Name", "Quantity", "Unit", "Price", "Total"};
    DefaultTableModel cartTableModel = new DefaultTableModel(cartTableColumns, 0);

    Object purchaseDetailsColumns[] = {"Name", "Quantity", "Unit", "Price", "Total"};
    DefaultTableModel purchaseDetailsTableModel = new DefaultTableModel(purchaseDetailsColumns, 0);

    private static Integer purchaseId = 0;

    PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
    CustomerDAO customerDAO = new CustomerDAOImpl();
    ProductDAO productDAO = new ProductDAOImpl();
    LedgerDAO ledgerDAO = new LedgerDAOImpl();
    PurchaseDetailsDAO purchaseDetailsDAO = new PurchaseDetailsDAOImpl();
    StockDAO stockDAO = new StockDAOImpl();

    public PurchaseFrame() {
        initComponents();
        purchaseDateField.setDate(new java.util.Date());
        purchaseNumberField.setText(getPurchaseCode());
        editBtn.setEnabled(false);
        addBtn.setEnabled(false);
        // deleteBtn.setEnabled(false);
        fillTable();
        fillCustomerCombo();
        fillProductCombo();
        // setSize(930, 820);
        // setLocation(300, 30);

    }

    public boolean isAnyFieldsEmpty() {
        if (priceField.getText().equals("") || recieptNo.equals("")) {
            return true;
        }
        return false;
    }

    public void setFields(Purchase purchase) {
        purchaseDateField.setDate(purchase.getPurchaseDate());
        purchaseNumberField.setText(purchase.getPurchaseNumber());
        vendorCombo.setSelectedItem(purchase.getCustomer().getName());
        productCombo.setSelectedItem(purchase.getProduct().getName());;
        quantityField.setValue(purchase.getQuantity());
        unitCombo.setSelectedItem(purchase.getUnit());
        priceField.setText(String.valueOf(purchase.getPrice()));
        paidAmountField.setText(String.valueOf(purchase.getAmountPaid()));
        remainingAmountField.setText(String.valueOf(purchase.getAmountRemaining()));
        totalField.setText(String.valueOf(purchase.getTotalAmount()));
        paymentTypeCombo.setSelectedItem(purchase.getPaymentType());
        if (purchase.isTaxable()) {
            vatCheck.setSelected(true);
        } else {
            vatCheck.setSelected(false);
        }

    }

    public void fillCustomerCombo() {
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            if (!CUSTOMER.equals(customer.getCustomerType())) {
                vendorCombo.addItem(customer.getName());
            }
        }
    }

    public void fillProductCombo() {
        List<Product> products = productDAO.getAllProducts();
        for (Product product : products) {
            productCombo.addItem(product.getName());
        }
    }

    public Purchase getPurchase() {
        java.sql.Date purchaseDate = new java.sql.Date(purchaseDateField.getDate().getTime());
        String purchaseNumber = purchaseNumberField.getText();
        String customerName = vendorCombo.getSelectedItem().toString();
        Customer customer = customerDAO.getCustomerByName(customerName);
        String productName = productCombo.getSelectedItem().toString();
        Product product = productDAO.getProductByName(productName);
        Integer quantity = Integer.parseInt(quantityField.getValue().toString());
        String reciptNumber = recieptNo.getText();
        Unit unit = Unit.valueOf(unitCombo.getSelectedItem().toString());
        double price = Double.parseDouble(priceField.getText());
        double total = Double.parseDouble(totalField.getText());
        double paid = Double.parseDouble(paidAmountField.getText());
        PaymentType paymentType = PaymentType.valueOf(paymentTypeCombo.getSelectedItem().toString());
        double taxAmount = Double.parseDouble(taxAmountField.getText());
        boolean vat = false;
        if (vatCheck.isSelected()) {
            vat = true;
        }
        double remaining = total - paid;

        Purchase purchase = new Purchase(0, purchaseDate, quantity, unit, price,
                purchaseNumber, paid, remaining, total, vat, paymentType, customer,
                product, taxAmount, reciptNumber);
        return purchase;
    }

    public void clearFields() {
        purchaseNumberField.setText(getPurchaseCode());
        recieptNo.setText("");
        vendorCombo.setSelectedIndex(0);
        productCombo.setSelectedIndex(0);
        quantityField.setValue(0);
        priceField.setText("");
        vatCheck.setSelected(false);
        totalField.setText("0.0");
        paidAmountField.setText("");
        remainingAmountField.setText("0.0");
        cartTableModel.setRowCount(0);
        taxAmountField.setText("0.0");

    }

    public String getPurchaseCode() {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);

        // Generate the auto number
        return "NM-" + formattedDateTime;
    }

    public void fillTable() {
        defaultTableModel = new DefaultTableModel(columns, 0);
        List<Purchase> purchases = purchaseDAO.getAllPurchases();
        double taxAmount = 0;
        for (Purchase p : purchases) {
            if (p.isTaxable()) {
                taxAmount = p.getTaxAmount();
            }
            Object row[] = {p.getPurchaseId(), p.getCustomer().getName(),
                p.getPurchaseDate(), p.getPurchaseNumber(), p.getRecieptNumber(),
                p.getTotalAmount(), p.getAmountPaid(), p.getAmountRemaining(),
                taxAmount, p.getPaymentType()};
            defaultTableModel.addRow(row);
            purchaseTable.setModel(defaultTableModel);
        }
        purchaseTable.getColumnModel().getColumn(0).setWidth(0);
        purchaseTable.getColumnModel().getColumn(0).setMinWidth(0);
        purchaseTable.getColumnModel().getColumn(0).setMaxWidth(0);

    }

    private void fillPurchaseDetails(List<PurchaseDetails> purchaseDetailses) {
        purchaseDetailsTableModel = new DefaultTableModel(purchaseDetailsColumns, 0);
        for (PurchaseDetails pd : purchaseDetailses) {
            double total = pd.getQuantity() * pd.getPrice();
            Object row[] = {pd.getProduct().getName(), pd.getQuantity(), pd.getUnit(), pd.getPrice(), total};
            purchaseDetailsTableModel.addRow(row);
            purchaseDetailsTable.setModel(purchaseDetailsTableModel);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerLbl = new javax.swing.JLabel();
        nameLbl = new javax.swing.JLabel();
        phoneLbl = new javax.swing.JLabel();
        emailLbl = new javax.swing.JLabel();
        addressLbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        purchaseTable = new javax.swing.JTable();
        addBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        priceField = new javax.swing.JTextField();
        backBtn = new javax.swing.JButton();
        passwordLbl = new javax.swing.JLabel();
        vendorCombo = new javax.swing.JComboBox<>();
        unitCombo = new javax.swing.JComboBox<>();
        productCombo = new javax.swing.JComboBox<>();
        addressLbl1 = new javax.swing.JLabel();
        paidAmountField = new javax.swing.JTextField();
        addressLbl2 = new javax.swing.JLabel();
        remainingLbl = new javax.swing.JLabel();
        paymentTypeCombo = new javax.swing.JComboBox<>();
        addressLbl4 = new javax.swing.JLabel();
        vatCheck = new javax.swing.JCheckBox();
        quantityField = new javax.swing.JSpinner();
        purchaseNumberField = new javax.swing.JLabel();
        purchaseDateField = new com.toedter.calendar.JDateChooser();
        purchaseDateLbl = new javax.swing.JLabel();
        pNumberLbl = new javax.swing.JLabel();
        totalLbl = new javax.swing.JLabel();
        totalField = new javax.swing.JLabel();
        remainingAmountField = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        serachLbl = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cartTable = new javax.swing.JTable();
        addItem = new javax.swing.JButton();
        headerLbl1 = new javax.swing.JLabel();
        pNumberLbl1 = new javax.swing.JLabel();
        recieptNo = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        purchaseDetailsTable = new javax.swing.JTable();
        taxAmountField = new javax.swing.JLabel();
        headerLbl2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        headerLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        headerLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLbl.setText("Purchased Products Detail");

        nameLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        nameLbl.setText("Quantity");

        phoneLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        phoneLbl.setText("Product");

        emailLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        emailLbl.setText("Unit");

        addressLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addressLbl.setText("Price");

        purchaseTable.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        purchaseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        purchaseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchaseTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(purchaseTable);

        addBtn.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addBtn.setText("Purchase");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        editBtn.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        editBtn.setText("Edit");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        priceField.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        priceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                priceFieldKeyReleased(evt);
            }
        });

        backBtn.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        passwordLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        passwordLbl.setText("Vendor");

        vendorCombo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        vendorCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vendorComboActionPerformed(evt);
            }
        });

        unitCombo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        unitCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MT", "KG" }));
        unitCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitComboActionPerformed(evt);
            }
        });

        productCombo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        productCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productComboActionPerformed(evt);
            }
        });

        addressLbl1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addressLbl1.setText("Paid");

        paidAmountField.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        paidAmountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paidAmountFieldKeyReleased(evt);
            }
        });

        addressLbl2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addressLbl2.setText("VAT");

        remainingLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        remainingLbl.setText("Remainig");

        paymentTypeCombo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        paymentTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CASH", "CHEQUE", "ONLINE" }));
        paymentTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentTypeComboActionPerformed(evt);
            }
        });

        addressLbl4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addressLbl4.setText("Payment Type");

        vatCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vatCheckActionPerformed(evt);
            }
        });

        purchaseNumberField.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        purchaseNumberField.setText("Purchase #");

        purchaseDateField.setDateFormatString("dd-MM-yyyy");

        purchaseDateLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        purchaseDateLbl.setText("Purchase Date");

        pNumberLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        pNumberLbl.setText("Purchase #");

        totalLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        totalLbl.setText("Total Amount");

        totalField.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        totalField.setText("0.0");

        remainingAmountField.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        remainingAmountField.setText("0.0");

        searchField.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        serachLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        serachLbl.setText("Search");

        cartTable.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        cartTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        cartTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cartTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(cartTable);

        addItem.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        addItem.setText("Add To Cart");
        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemActionPerformed(evt);
            }
        });

        headerLbl1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        headerLbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLbl1.setText("Purchase");

        pNumberLbl1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        pNumberLbl1.setText("Slip #");

        recieptNo.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        recieptNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                recieptNoKeyReleased(evt);
            }
        });

        purchaseDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(purchaseDetailsTable);

        taxAmountField.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        taxAmountField.setText("0.0");

        headerLbl2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        headerLbl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLbl2.setText("Cart Products Detail");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(purchaseDateLbl)
                                    .addComponent(pNumberLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pNumberLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(passwordLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(phoneLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(emailLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(35, 35, 35))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(addressLbl4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addressLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(addressLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(remainingLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(addressLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(33, 33, 33)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(purchaseDateField, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addComponent(purchaseNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vendorCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(productCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unitCombo, 0, 290, Short.MAX_VALUE)
                            .addComponent(quantityField)
                            .addComponent(priceField, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addComponent(recieptNo)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(paymentTypeCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(11, 11, 11))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(vatCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(taxAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(164, 164, 164)
                                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(161, 161, 161)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(paidAmountField, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                                    .addComponent(remainingAmountField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(11, 11, 11))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(headerLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1308, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(449, 449, 449)
                                .addComponent(serachLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(132, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(headerLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(headerLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(211, 211, 211))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(backBtn))
                    .addComponent(headerLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(purchaseDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(purchaseNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(purchaseDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pNumberLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pNumberLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recieptNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(vendorCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(quantityField, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(remainingLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(remainingAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(44, 44, 44))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(addressLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(addItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(addressLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                    .addComponent(taxAmountField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                    .addComponent(vatCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(totalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(addressLbl4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(paymentTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(addressLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(paidAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(170, 170, 170))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(serachLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(headerLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(headerLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void purchaseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchaseTableMouseClicked
        purchaseId = (Integer) purchaseTable.getValueAt(purchaseTable.getSelectedRow(), 0);
        // editBtn.setEnabled(true);
        // deleteBtn.setEnabled(true);
        // Purchase purchase = purchaseDAO.getPurchaseById(purchaseId);
        List<PurchaseDetails> purchaseDetailses = purchaseDetailsDAO.getPurchaseDetailsByPurchaseId(purchaseId);
        fillPurchaseDetails(purchaseDetailses);

        //setFields(purchase);

    }//GEN-LAST:event_purchaseTableMouseClicked

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        if (isAnyFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!!");
        } else {
            Purchase purchase = getPurchase();
            boolean b = purchaseDAO.addPurchase(purchase);
            if (b) {
                for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                    int productId = (Integer) cartTable.getValueAt(i, 0);
                    Integer quantity = (Integer) cartTable.getValueAt(i, 2);
                    Unit unit = (Unit) cartTable.getValueAt(i, 3);
                    double price = (double) cartTable.getValueAt(i, 4);

                    PurchaseDetails purchaseDetails = new PurchaseDetails();
                    Product product = productDAO.getProductById(productId);
                    purchaseDetails.setProduct(product);
                    purchaseDetails.setPurchase(purchaseDAO.getPurchaseByPurchaseNumber(purchaseNumberField.getText()));
                    purchaseDetails.setQuantity(quantity);
                    purchaseDetails.setUnit(unit);
                    purchaseDetails.setPrice(price);
                    product.setQuantity(product.getQuantity() + quantity);
                    productDAO.updateProduct(product);
                    purchaseDetailsDAO.addPurchaseDetails(purchaseDetails);
                    Stock stock = new Stock();
                    stock.setProduct(product);
                    stock.setPurchased(quantity);
                    stock.setStockDate(purchase.getPurchaseDate());
                    stockDAO.addStock(stock);
                }

                JOptionPane.showMessageDialog(this, "Record Added Successfully");
                Ledger ledger = new Ledger(0, purchase.getPurchaseNumber(),
                        purchase.getAmountPaid(), purchase.getAmountRemaining(),
                        purchase.getTotalAmount(), purchase.getCustomer());
                ledgerDAO.addLedger(ledger);

                fillTable();
                clearFields();
            }
        }

    }//GEN-LAST:event_addBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        if (isAnyFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!!");
        } else {
            Purchase purchase = getPurchase();
            purchase.setPurchaseId(purchaseId);
            boolean b = purchaseDAO.updatePurchase(purchase);
            if (b) {
                JOptionPane.showMessageDialog(this, "Record Updated Successfully");
                fillTable();
                clearFields();
                editBtn.setEnabled(false);
                // deleteBtn.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Error Occurred");
            }
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        Home frame = new Home();
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backBtnActionPerformed

    private void vendorComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vendorComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vendorComboActionPerformed

    private void unitComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitComboActionPerformed

    private void productComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productComboActionPerformed

    private void paymentTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentTypeComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentTypeComboActionPerformed

    private void vatCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vatCheckActionPerformed
        if (vatCheck.isEnabled()) {
            Double total = Double.parseDouble(totalField.getText());
            Double taxAmount = total * 0.05;
            taxAmountField.setText(taxAmount.toString());
            total = total + taxAmount;
            totalField.setText(total.toString());
            paidAmountField.setText(totalField.getText());
        }
    }//GEN-LAST:event_vatCheckActionPerformed

    private void paidAmountFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidAmountFieldKeyReleased
        Double total = Double.parseDouble(totalField.getText());
        Double paid = Double.parseDouble(paidAmountField.getText());
        Double remaining = total - paid;
        remainingAmountField.setText(remaining.toString());
        addBtn.setEnabled(true);
    }//GEN-LAST:event_paidAmountFieldKeyReleased

    private void priceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_priceFieldKeyReleased
        // Integer quantity = Integer.parseInt(quantityField.getValue().toString());
        // Double price = Double.parseDouble(priceField.getText());
        // Double total = quantity * price;
        // totalField.setText(total.toString());

    }//GEN-LAST:event_priceFieldKeyReleased

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased

        TableRowSorter sorter = new TableRowSorter<>(defaultTableModel);
        purchaseTable.setRowSorter(sorter);

        String content = searchField.getText();
        if (content.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(content));
        }
    }//GEN-LAST:event_searchFieldKeyReleased

    private void cartTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cartTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cartTableMouseClicked

    private void recieptNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_recieptNoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_recieptNoKeyReleased

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        int productId = productDAO.getProductByName(productCombo.getSelectedItem().toString()).getProductId();
        String productName = productCombo.getSelectedItem().toString();
        Integer quantity = Integer.parseInt(quantityField.getValue().toString());
        Unit unit = Unit.valueOf(unitCombo.getSelectedItem().toString());
        Double price = Double.parseDouble(priceField.getText());
        Double total = quantity * price;
        Object row[] = {productId, productName, quantity, unit, price, total};
        cartTableModel.addRow(row);
        cartTable.setModel(cartTableModel);
        Double previousTotal = Double.parseDouble(totalField.getText());
        Double grandTotal = total + previousTotal;
        totalField.setText(grandTotal.toString());
        paidAmountField.setText(totalField.getText());

        cartTable.getColumnModel().getColumn(0).setWidth(0);
        cartTable.getColumnModel().getColumn(0).setMinWidth(0);
        cartTable.getColumnModel().getColumn(0).setMaxWidth(0);
        addBtn.setEnabled(true);

    }//GEN-LAST:event_addItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PurchaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PurchaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PurchaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PurchaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PurchaseFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton addItem;
    private javax.swing.JLabel addressLbl;
    private javax.swing.JLabel addressLbl1;
    private javax.swing.JLabel addressLbl2;
    private javax.swing.JLabel addressLbl4;
    private javax.swing.JButton backBtn;
    private javax.swing.JTable cartTable;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel emailLbl;
    private javax.swing.JLabel headerLbl;
    private javax.swing.JLabel headerLbl1;
    private javax.swing.JLabel headerLbl2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JLabel pNumberLbl;
    private javax.swing.JLabel pNumberLbl1;
    private javax.swing.JTextField paidAmountField;
    private javax.swing.JLabel passwordLbl;
    private javax.swing.JComboBox<String> paymentTypeCombo;
    private javax.swing.JLabel phoneLbl;
    private javax.swing.JTextField priceField;
    private javax.swing.JComboBox<String> productCombo;
    private com.toedter.calendar.JDateChooser purchaseDateField;
    private javax.swing.JLabel purchaseDateLbl;
    private javax.swing.JTable purchaseDetailsTable;
    private javax.swing.JLabel purchaseNumberField;
    private javax.swing.JTable purchaseTable;
    private javax.swing.JSpinner quantityField;
    private javax.swing.JTextField recieptNo;
    private javax.swing.JLabel remainingAmountField;
    private javax.swing.JLabel remainingLbl;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel serachLbl;
    private javax.swing.JLabel taxAmountField;
    private javax.swing.JLabel totalField;
    private javax.swing.JLabel totalLbl;
    private javax.swing.JComboBox<String> unitCombo;
    private javax.swing.JCheckBox vatCheck;
    private javax.swing.JComboBox<String> vendorCombo;
    // End of variables declaration//GEN-END:variables

}
