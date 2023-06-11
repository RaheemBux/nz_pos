/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import static contants.CustomerType.VENDOR;
import contants.PaymentType;
import contants.Unit;
import dao.CustomerDAO;
import dao.LedgerDAO;
import dao.ProductDAO;
import dao.PurchaseDAO;
import dao.PurchaseDetailsDAO;
import dao.SaleDetailsDAO;
import daoimpl.CustomerDAOImpl;
import daoimpl.LedgerDAOImpl;
import daoimpl.ProductDAOImpl;
import daoimpl.PurchaseDAOImpl;
import daoimpl.PurchaseDetailsDAOImpl;
import daoimpl.SaleDetailsDAOImpl;
import daoimpl.SaleDAOImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.Customer;
import model.Product;
import model.Sale;
import dao.SaleDAO;
import dao.StockDAO;
import daoimpl.StockDAOImpl;
import dbmanager.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SaleDetails;
import model.Stock;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 *
 */
public class SaleFrame extends javax.swing.JFrame {

    /**
     * Creates new form RegisterFrame
     */
    Object columns[] = {"Id", "Customer", "Sale-Date", "Sale-No", "Slip No", "Total-Amount", "Paid", "Remaning", "Tax", "Payment Type"};

    DefaultTableModel defaultTableModel = new DefaultTableModel(columns, 0);

    Object[] cartTableColumns = {"Id", "Name", "Quantity", "Unit", "Price", "Total"};
    DefaultTableModel cartTableModel = new DefaultTableModel(cartTableColumns, 0);

    Object[] saleDetailsColumns = {"Name", "Quantity", "Unit", "Price", "Total"};
    DefaultTableModel saleDetailsTableModel = new DefaultTableModel(saleDetailsColumns, 0);

    private static Integer saleId = 0;

    PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
    CustomerDAO customerDAO = new CustomerDAOImpl();
    ProductDAO productDAO = new ProductDAOImpl();
    LedgerDAO ledgerDAO = new LedgerDAOImpl();
    PurchaseDetailsDAO purchaseDetailsDAO = new PurchaseDetailsDAOImpl();
    StockDAO stockDAO = new StockDAOImpl();

    SaleDAO saleDAO = new SaleDAOImpl();
    SaleDetailsDAO saleDetailsDAO = new SaleDetailsDAOImpl();

    public SaleFrame() {
        initComponents();
        saleDateField.setDate(new java.util.Date());
        saleNumberField.setText(getPurchaseCode());
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

    public void setFields(Sale sale) {
        saleDateField.setDate(sale.getSaleDate());
        saleNumberField.setText(sale.getSaleNumber());
        vendorCombo.setSelectedItem(sale.getCustomer().getName());
        productCombo.setSelectedItem(sale.getProduct().getName());;
        quantityField.setValue(sale.getQuantity());
        unitCombo.setSelectedItem(sale.getUnit());
        priceField.setText(String.valueOf(sale.getPrice()));
        paidAmountField.setText(String.valueOf(sale.getAmountPaid()));
        remainingAmountField.setText(String.valueOf(sale.getAmountRemaining()));
        totalField.setText(String.valueOf(sale.getTotalAmount()));
        paymentTypeCombo.setSelectedItem(sale.getPaymentType());
        if (sale.isTaxable()) {
            vatCheck.setSelected(true);
        } else {
            vatCheck.setSelected(false);
        }

    }

    public void fillCustomerCombo() {
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            if (!VENDOR.equals(customer.getCustomerType())) {
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

    public Sale getSale() {
        java.sql.Date saleDate = new java.sql.Date(saleDateField.getDate().getTime());
        String saleNumber = saleNumberField.getText();
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

        Sale sale = new Sale(0, saleDate, quantity, unit, price,
                saleNumber, paid, remaining, total, vat, paymentType, customer,
                product, taxAmount, reciptNumber);
        return sale;
    }

    public void clearFields() {
        saleNumberField.setText(getPurchaseCode());
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
        List<Sale> sales = saleDAO.getAllSales();
        double taxAmount = 0;
        for (Sale s : sales) {
            if (s.isTaxable()) {
                taxAmount = s.getTaxAmount();
            }
            Object row[] = {s.getSaleId(), s.getCustomer().getName(),
                s.getSaleDate(), s.getSaleNumber(), s.getRecieptNumber(),
                s.getTotalAmount(), s.getAmountPaid(), s.getAmountRemaining(),
                taxAmount, s.getPaymentType()};
            defaultTableModel.addRow(row);
            saleTable.setModel(defaultTableModel);
        }
        saleTable.getColumnModel().getColumn(0).setWidth(0);
        saleTable.getColumnModel().getColumn(0).setMinWidth(0);
        saleTable.getColumnModel().getColumn(0).setMaxWidth(0);

    }

    private void fillSaleDetails(List<SaleDetails> saleDetails) {
        saleDetailsTableModel = new DefaultTableModel(saleDetailsColumns, 0);
        for (SaleDetails sd : saleDetails) {
            double total = sd.getQuantity() * sd.getPrice();
            Object row[] = {sd.getProduct().getName(), sd.getQuantity(), sd.getUnit(), sd.getPrice(), total};
            saleDetailsTableModel.addRow(row);
            saleDetailsTable.setModel(saleDetailsTableModel);
        }
    }
    //private List<>

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
        saleTable = new javax.swing.JTable();
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
        saleNumberField = new javax.swing.JLabel();
        saleDateField = new com.toedter.calendar.JDateChooser();
        saleDateLbl = new javax.swing.JLabel();
        saleNumberLbl = new javax.swing.JLabel();
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
        saleDetailsTable = new javax.swing.JTable();
        taxAmountField = new javax.swing.JLabel();
        headerLbl2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        headerLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        headerLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLbl.setText("Saled Products Detail");

        nameLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        nameLbl.setText("Quantity");

        phoneLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        phoneLbl.setText("Product");

        emailLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        emailLbl.setText("Unit");

        addressLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addressLbl.setText("Price");

        saleTable.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        saleTable.setModel(new javax.swing.table.DefaultTableModel(
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
        saleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saleTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(saleTable);

        addBtn.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addBtn.setText("Sale");
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

        saleNumberField.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        saleNumberField.setText("Sale #");

        saleDateField.setDateFormatString("dd-MM-yyyy");

        saleDateLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        saleDateLbl.setText("Sale Date");

        saleNumberLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        saleNumberLbl.setText("Sale #");

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
        headerLbl1.setText("Sale");

        pNumberLbl1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        pNumberLbl1.setText("Slip #");

        recieptNo.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        recieptNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                recieptNoKeyReleased(evt);
            }
        });

        saleDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(saleDetailsTable);

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
                        .addComponent(backBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(headerLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(saleDateLbl)
                                    .addComponent(pNumberLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(saleNumberLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(passwordLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(phoneLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(emailLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addressLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(35, 35, 35))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addressLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(addressLbl4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(addressLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(totalLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(remainingLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(addItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saleDateField, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
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
                                    .addComponent(taxAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(saleNumberField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(paidAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(remainingAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                                .addComponent(saleDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saleNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(saleDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saleNumberLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addressLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(addItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(vatCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(taxAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(addressLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(paymentTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressLbl4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(addressLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(paidAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(remainingAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(remainingLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(66, Short.MAX_VALUE))
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

    private void saleTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleTableMouseClicked
        saleId = (Integer) saleTable.getValueAt(saleTable.getSelectedRow(), 0);
        List<SaleDetails> saleDetails = saleDetailsDAO.getSaleDetailsBySaleId(saleId);
        fillSaleDetails(saleDetails);
    }//GEN-LAST:event_saleTableMouseClicked

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        if (isAnyFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!!");
        } else {
            Sale sale = getSale();
            boolean b = saleDAO.addSales(sale);
            if (b) {
                for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                    int productId = (Integer) cartTable.getValueAt(i, 0);
                    Integer quantity = (Integer) cartTable.getValueAt(i, 2);
                    Unit unit = (Unit) cartTable.getValueAt(i, 3);
                    double price = (double) cartTable.getValueAt(i, 4);

                    SaleDetails saleDetails = new SaleDetails();
                    Product product = productDAO.getProductById(productId);
                    saleDetails.setProduct(product);
                    saleDetails.setSale(saleDAO.getSaleBySaleNumber(saleNumberField.getText()));
                    saleDetails.setQuantity(quantity);
                    saleDetails.setUnit(unit);
                    saleDetails.setPrice(price);

                    product.setQuantity(product.getQuantity() - quantity);
                    productDAO.updateProduct(product);
                    saleDetailsDAO.addSaleDetails(saleDetails);

                    Stock stock = new Stock();
                    stock.setProduct(product);
                    stock.setSold(quantity);
                    stock.setStockDate(sale.getSaleDate());
                    stockDAO.addStock(stock);
                }

                JOptionPane.showMessageDialog(this, "Record Added Successfully");
                /*Ledger ledger = new Ledger(0, sale.getSaleNumber(),
                        sale.getAmountPaid(), sale.getAmountRemaining(),
                        sale.getTotalAmount(), sale.getCustomer());
                ledgerDAO.addLedger(ledger);*/

                fillTable();

                try {
                  

                    JasperDesign jdesign = JRXmlLoader.load("report4.jrxml");
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("order_num", saleNumberField.getText());

                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, parameters, DBConnection.getConnection());

                    JasperViewer.viewReport(jprint);
                } catch (Exception ex) {
                    Logger.getLogger(SaleFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            clearFields();

        }

    }//GEN-LAST:event_addBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        if (isAnyFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!!");
        } else {
            Sale sale = getSale();
            sale.setSaleId(saleId);
            boolean b = saleDAO.updateSales(sale);
            if (b) {
                JOptionPane.showMessageDialog(this, "Record Updated Successfully");
                fillTable();
                clearFields();
                editBtn.setEnabled(false);
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
        saleTable.setRowSorter(sorter);

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
            java.util.logging.Logger.getLogger(SaleFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SaleFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SaleFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SaleFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new SaleFrame().setVisible(true);
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
    private javax.swing.JLabel pNumberLbl1;
    private javax.swing.JTextField paidAmountField;
    private javax.swing.JLabel passwordLbl;
    private javax.swing.JComboBox<String> paymentTypeCombo;
    private javax.swing.JLabel phoneLbl;
    private javax.swing.JTextField priceField;
    private javax.swing.JComboBox<String> productCombo;
    private javax.swing.JSpinner quantityField;
    private javax.swing.JTextField recieptNo;
    private javax.swing.JLabel remainingAmountField;
    private javax.swing.JLabel remainingLbl;
    private com.toedter.calendar.JDateChooser saleDateField;
    private javax.swing.JLabel saleDateLbl;
    private javax.swing.JTable saleDetailsTable;
    private javax.swing.JLabel saleNumberField;
    private javax.swing.JLabel saleNumberLbl;
    private javax.swing.JTable saleTable;
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
