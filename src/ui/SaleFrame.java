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
import dto.TransactionDTO;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import model.SaleDetails;
import model.Stock;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 *
 */
public class SaleFrame extends javax.swing.JFrame {

    /**
     * Creates new form RegisterFrame
     */
    Object columns[] = {"Id", "Customer", "Sale-Date", "Sale-No", "Slip No",
        "Product", "Quantity", "Unit", "Price", "Product-Total",
        "Grand-Total", "Paid", "Remaning", "Tax", "Payment Type", "Action"};

    //DefaultTableModel defaultTableModel = new DefaultTableModel(columns, 0);

   // Object[] cartTableColumns = {"Id", "Name", "Quantity", "Unit", "Price", "Total", "Action"};
   // DefaultTableModel cartTableModel = new DefaultTableModel(cartTableColumns, 0);

    Object[] saleDetailsColumns = {"Name", "Quantity", "Unit", "Price", "Total"};
    DefaultTableModel saleDetailsTableModel = new DefaultTableModel(saleDetailsColumns, 0);

    private static Integer saleId = 0;
    private double taxAmount = 0;
    boolean isProductAlreadySelected = false;
    private static Integer saleDetailsId = 0;
    private double previousTotal = 0;
    private double lastTotal = 0;

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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        validateSlipLbl.setVisible(false);
        saleDateField.setDate(new java.util.Date());
        saleNumberField.setText(getPurchaseCode());
        editBtn.setEnabled(false);
        addBtn.setEnabled(false);
        AutoCompleteDecorator.decorate(productCombo);
        AutoCompleteDecorator.decorate(vendorCombo);
        productTotalLbl.setVisible(false);
        // deleteBtn.setEnabled(false);
        // fillSalesTable();
        fillCustomerCombo();
        fillProductCombo();
        fillSaleTable();
        cartTableAction();
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row : " + row);
            }

            @Override
            public void onDelete(int row) {
                if (saleTable.isEditing()) {
                    saleTable.getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) saleTable.getModel();
                model.removeRow(row);
            }

            @Override
            public void onView(int row) {
                System.out.println("View row : " + row);
            }
        };
        saleTable.getColumnModel().getColumn(15).setCellRenderer(new TableActionCellRender());
        saleTable.getColumnModel().getColumn(15).setCellEditor(new TableActionCellEditor(event));
        // setSize(930, 820);
        // setLocation(300, 30);

    }

    private void cartTableAction() {
        cartTable.setModel(new javax.swing.table.DefaultTableModel(
                null,
                new String[]{"Id", "Name", "Quantity", "Unit", "Price", "Total", "Action"}
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row : " + row);
            }

            @Override
            public void onDelete(int row) {
                if (cartTable.isEditing()) {
                    cartTable.getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
                model.removeRow(row);
                System.out.println("Delete Row : "+row);
                model = (DefaultTableModel) cartTable.getModel();
                double grandTotal = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    double total = (double) cartTable.getValueAt(i, 5);
                    grandTotal+= total;
                }
                if(vatCheck.isSelected()){
                    double tax = grandTotal*0.05;
                    taxAmountField.setText(String.valueOf(tax));
                    grandTotal= grandTotal+tax;
                }
                totalField.setText(String.valueOf(grandTotal));
                paidAmountField.setText(totalField.getText()); 
            }

            @Override
            public void onView(int row) {
                System.out.println("View row : " + row);
            }
        };
        cartTable.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        cartTable.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event));
        cartTable.setRowHeight(38);

    }

    private void fillSaleTable() {

        List<TransactionDTO> sales = saleDAO.getAllSales1();
        Object rows[][] = new Object[sales.size()][];
        int index = 0;
        for (TransactionDTO t : sales) {
            Object row[] = {t.getTransactionId(), t.getCustomerName(), t.getTranscationDate(),
                t.getOrderNumber(), t.getRecieptNo(), t.getProductName(),
                t.getQuantity(), t.getUnit(), t.getPrice(), t.getTotalAmount(),
                t.getGrandAmount(), t.getAmountPaid(), t.getAmountRemaining(),
                t.getTaxAmount(), t.getPaymentType()};
            rows[index] = row;
            index++;
        }

        saleTable.setModel(new javax.swing.table.DefaultTableModel(
                rows,
                new String[]{"Id", "Customer", "Sale-Date", "Sale-No", "Slip No",
                    "Product", "Quantity", "Unit", "Price", "Product-Total",
                    "Grand-Total", "Paid", "Remaning", "Tax", "Payment Type", "Action"}
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        saleTable.setRowHeight(38);

        saleTable.getColumnModel().getColumn(0).setWidth(0);
        saleTable.getColumnModel().getColumn(0).setMinWidth(0);
        saleTable.getColumnModel().getColumn(0).setMaxWidth(0);
        saleTable.getColumnModel().getColumn(3).setWidth(180);
        saleTable.getColumnModel().getColumn(3).setMinWidth(180);
        saleTable.getColumnModel().getColumn(3).setMaxWidth(180);
        saleTable.getColumnModel().getColumn(6).setWidth(70);
        saleTable.getColumnModel().getColumn(6).setMinWidth(70);
        saleTable.getColumnModel().getColumn(6).setMaxWidth(70);
        saleTable.getColumnModel().getColumn(7).setWidth(40);
        saleTable.getColumnModel().getColumn(7).setMinWidth(40);
        saleTable.getColumnModel().getColumn(7).setMaxWidth(40);
        saleTable.getColumnModel().getColumn(15).setWidth(100);
        saleTable.getColumnModel().getColumn(15).setMinWidth(100);
        saleTable.getColumnModel().getColumn(15).setMaxWidth(100);

    }

    public boolean isAnyFieldsEmpty() {
        return priceField.getText().equals("") || recieptNo.getText().equals("");
    }

    public void setFields(Sale sale) {
        saleDateField.setDate(sale.getSaleDate());
        saleNumberField.setText(sale.getSaleNumber());
        vendorCombo.setSelectedItem(sale.getCustomer().getName());
        productCombo.setSelectedItem(sale.getProduct().getName());;
        quantityField.setText(String.valueOf(sale.getQuantity()));
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
        Integer quantity=0;
        if(!quantityField.getText().equals("")){
            quantity = Integer.parseInt(quantityField.getText());
        }
        
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
        sale.setCreatedBy(Login.createdBy);
        return sale;
    }

    public void clearFields() {
        saleNumberField.setText(getPurchaseCode());
        recieptNo.setText("");
        vendorCombo.setSelectedIndex(0);
        productCombo.setSelectedIndex(0);
        quantityField.setText("");
        priceField.setText("");
        vatCheck.setSelected(false);
        totalField.setText("0.0");
        paidAmountField.setText("");
        remainingAmountField.setText("0.0");
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        model.setRowCount(0);
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

    /*public void fillTable() {
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
        saleTable.setSelectionBackground(new java.awt.Color(56, 138, 112));

    }*/

   /* public void fillSalesTable() {
        //instance table model
        defaultTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        List<TransactionDTO> sales = saleDAO.getAllSales1();
        for (TransactionDTO t : sales) {
            Object row[] = {t.getTransactionId(), t.getCustomerName(), t.getTranscationDate(),
                t.getOrderNumber(), t.getRecieptNo(), t.getProductName(),
                t.getQuantity(), t.getUnit(), t.getPrice(), t.getTotalAmount(),
                t.getGrandAmount(), t.getAmountPaid(), t.getAmountRemaining(),
                t.getTaxAmount(), t.getPaymentType()};
            defaultTableModel.addRow(row);
            saleTable.setModel(defaultTableModel);
        }
        saleTable.getColumnModel().getColumn(0).setWidth(0);
        saleTable.getColumnModel().getColumn(0).setMinWidth(0);
        saleTable.getColumnModel().getColumn(0).setMaxWidth(0);
        saleTable.getColumnModel().getColumn(3).setWidth(180);
        saleTable.getColumnModel().getColumn(3).setMinWidth(180);
        saleTable.getColumnModel().getColumn(3).setMaxWidth(180);
        TableCellRenderer tableRenderer = saleTable.getDefaultRenderer(JButton.class);

        saleTable.getColumnModel().getColumn(15).setCellRenderer(new JTableButtonRenderer(tableRenderer));

    }*/

    private void sale() {
        if (isAnyFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!!");
        } else if (validateSlipLbl.isVisible()) {
            JOptionPane.showMessageDialog(this, "Please write a unique slip number!!");
        } else {
            Sale sale = getSale();
            boolean b = saleDAO.addSales(sale);
            if (b) {
                 DefaultTableModel cartTableModel = (DefaultTableModel) cartTable.getModel();
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

                fillSaleTable();

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
        taxAmountField = new javax.swing.JLabel();
        headerLbl2 = new javax.swing.JLabel();
        validateSlipLbl = new javax.swing.JLabel();
        productTotalLbl = new javax.swing.JLabel();
        quantityField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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
        addBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addBtnKeyPressed(evt);
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
        priceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceFieldActionPerformed(evt);
            }
        });
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
        paidAmountField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paidAmountFieldActionPerformed(evt);
            }
        });
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

        taxAmountField.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        taxAmountField.setText("0.0");

        headerLbl2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        headerLbl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLbl2.setText("Cart Products Detail");

        validateSlipLbl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        validateSlipLbl.setForeground(new java.awt.Color(255, 0, 0));
        validateSlipLbl.setText("Slip No already please user different");

        productTotalLbl.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        productTotalLbl.setText("Total : ");

        quantityField.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        quantityField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityFieldActionPerformed(evt);
            }
        });
        quantityField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                quantityFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(backBtn)
                        .addGap(645, 645, 645)
                        .addComponent(headerLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(saleDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(saleDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(520, 520, 520)
                        .addComponent(serachLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saleNumberLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pNumberLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saleNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(validateSlipLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recieptNo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vendorCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quantityField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1430, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(emailLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(unitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(addressLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(productTotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(163, 163, 163)
                        .addComponent(headerLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressLbl4))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paidAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(paymentTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addItem, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(vatCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(40, 40, 40)
                                        .addComponent(taxAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(155, 155, 155)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(remainingLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(remainingAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(backBtn))
                    .addComponent(headerLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(serachLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saleDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saleDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(saleNumberLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(pNumberLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(passwordLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(phoneLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(nameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saleNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(validateSlipLbl)
                        .addGap(5, 5, 5)
                        .addComponent(recieptNo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(vendorCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(productCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(quantityField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emailLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(addressLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(productTotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(headerLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(totalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(addressLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(addressLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(addressLbl4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(vatCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(taxAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addComponent(paidAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(paymentTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(remainingLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(remainingAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saleTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleTableMouseClicked
        saleId = (Integer) saleTable.getValueAt(saleTable.getSelectedRow(), 0);
        editBtn.setEnabled(true);
        productTotalLbl.setVisible(true);

        Object columns[] = {"Id", "Customer", "Sale-Date", "Sale-No", "Slip No",
            "Product", "Quantity", "Unit", "Price", "Product-Total",
            "Grand-Total", "Paid", "Remaning", "Tax", "Payment Type"};

        String customer = saleTable.getValueAt(saleTable.getSelectedRow(), 1).toString();
        String saleNumber = saleTable.getValueAt(saleTable.getSelectedRow(), 3).toString();
        String slipNumber = saleTable.getValueAt(saleTable.getSelectedRow(), 4).toString();
        String product = saleTable.getValueAt(saleTable.getSelectedRow(), 5).toString();
        Integer quantity = (Integer) saleTable.getValueAt(saleTable.getSelectedRow(), 6);
        String unit = (String) saleTable.getValueAt(saleTable.getSelectedRow(), 7);
        Double price = (Double) saleTable.getValueAt(saleTable.getSelectedRow(), 8);
        Double productTotal = (Double) saleTable.getValueAt(saleTable.getSelectedRow(), 9);
        taxAmount = (Double) saleTable.getValueAt(saleTable.getSelectedRow(), 13);
        if (taxAmount > 0) {
            vatCheck.setSelected(true);
        }

        vendorCombo.setSelectedItem(customer);
        saleNumberField.setText(saleNumber);
        recieptNo.setText(slipNumber);
        productCombo.setSelectedItem(product);
        quantityField.setText(quantity.toString());
        unitCombo.setSelectedItem(unit);
        priceField.setText(price.toString());
        productTotalLbl.setText("Total : "+productTotal.toString());

        SaleDetails saleDetails = saleDetailsDAO.getSaleDetailsIdBySaleNumberAndProductName(saleNumber, product);
        if (Objects.nonNull(saleDetails)) {
            saleDetailsId = saleDetails.getSaleDetailsId();
        }
        Sale sale = saleDAO.getSalesById(saleId);
        taxAmountField.setText(String.valueOf(sale.getTaxAmount()));
        totalField.setText(String.valueOf(sale.getTotalAmount()));
        paidAmountField.setText(String.valueOf(sale.getAmountPaid()));
        remainingAmountField.setText(String.valueOf(sale.getAmountRemaining()));
        paymentTypeCombo.setSelectedItem(sale.getPaymentType().toString());

        previousTotal = sale.getTotalAmount() - productTotal-taxAmount;
    }//GEN-LAST:event_saleTableMouseClicked

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        sale();
    }//GEN-LAST:event_addBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        if (isAnyFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!!");
        } else if (validateSlipLbl.isVisible()) {
            JOptionPane.showMessageDialog(this, "Please write a unique slip number!!");
        } else {
            Sale sale = getSale();
            sale.setSaleId(saleId);
            boolean b = saleDAO.updateSales(sale);
            if (b) {

                SaleDetails saleDetails = saleDetailsDAO.getSaleDetailsById(saleDetailsId);
                saleDetails.setSaleDetailsId(saleDetailsId);
                saleDetails.setProduct(sale.getProduct());
                saleDetails.setQuantity(sale.getQuantity());
                saleDetails.setUnit(sale.getUnit());
                saleDetails.setPrice(sale.getPrice());

                saleDetailsDAO.updateSaleDetails(saleDetails);

                JOptionPane.showMessageDialog(this, "Record Updated Successfully");
                /*Ledger ledger = new Ledger(0, sale.getSaleNumber(),
                        sale.getAmountPaid(), sale.getAmountRemaining(),
                        sale.getTotalAmount(), sale.getCustomer());
                ledgerDAO.addLedger(ledger);*/

                fillSaleTable();

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
        }
        clearFields();
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
        double lastPrice = productDAO.getPriceByProductName(productCombo.getSelectedItem().toString());
        priceField.setText(String.valueOf(lastPrice));
    }//GEN-LAST:event_productComboActionPerformed

    private void paymentTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentTypeComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentTypeComboActionPerformed

    private void vatCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vatCheckActionPerformed
        if (vatCheck.isSelected()) {
            System.out.println("Id selected ");
            Double total = Double.parseDouble(totalField.getText());
            taxAmount = total * 0.05;
            taxAmountField.setText(String.valueOf(taxAmount));
            total = total + taxAmount;
            totalField.setText(total.toString());
            paidAmountField.setText(totalField.getText());
            remainingAmountField.setText("0.0");
        } else if (!vatCheck.isSelected()) {
            System.out.println("No selected ");
            Double total = Double.parseDouble(totalField.getText())-Double.parseDouble(taxAmountField.getText());;
            System.out.println("total Amount "+total);
            //taxAmount = total * 0.05;
           // System.out.println("Tax amount "+taxAmount);
            taxAmountField.setText("0.0");
            //total = total - taxAmount;
            System.out.println("total Amount after tax ded "+total);
            totalField.setText(total.toString());
            paidAmountField.setText(totalField.getText());
            remainingAmountField.setText("0.0");
        }
    }//GEN-LAST:event_vatCheckActionPerformed

    private void paidAmountFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidAmountFieldKeyReleased
        Double value = Double.parseDouble(paidAmountField.getText());
        if(value<0){
            JOptionPane.showMessageDialog(this, "Paid Amount Can't be Negative");
            paidAmountField.setText("0.0");
        }
        
        Double total = Double.parseDouble(totalField.getText());
        Double paid = Double.parseDouble(paidAmountField.getText());
        if(paid>total){
            JOptionPane.showMessageDialog(this, "Paid Amount Can't be greater than Total");
            paidAmountField.setText(total.toString());
        }
        Double remaining = total - paid;
        remainingAmountField.setText(remaining.toString());
        Double remainingCheck = Double.parseDouble(remainingAmountField.getText());
        if(remainingCheck<0){
            JOptionPane.showMessageDialog(this, "Remaining Amount Can't be Negative");
            remainingAmountField.setText("0.0");
        }
        if(remainingCheck>total){
            JOptionPane.showMessageDialog(this, "Remaining Amount Can't be greater than Total");
            remainingAmountField.setText(total.toString());
        }
        addBtn.setEnabled(true);
    }//GEN-LAST:event_paidAmountFieldKeyReleased

    private void priceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_priceFieldKeyReleased
        Double value = Double.parseDouble(priceField.getText());
        if(value<=0){
            JOptionPane.showMessageDialog(this, "Price Can't be Negative or Zero!");
            priceField.setText("");
        }
        if (isProductAlreadySelected()) {
            JOptionPane.showMessageDialog(this, "This Product is already added in cart!!");
            quantityField.setText("");
        }
        else if(editBtn.isEnabled() && !priceField.getText().equals("") && !quantityField.getText().equals("")){
         Integer quantity = Integer.parseInt(quantityField.getText());
            Double price = Double.parseDouble(priceField.getText());
            Double total = quantity * price;
            Double grandTotal = total + previousTotal;
            
            System.out.println("Previous total "+previousTotal);
            System.out.println(" total "+total);
            totalField.setText(grandTotal.toString());
            paidAmountField.setText(totalField.getText());
            remainingAmountField.setText("0.0");
        }
        
        else if (!priceField.getText().equals("") && !quantityField.getText().equals("")) {
            Integer quantity = Integer.parseInt(quantityField.getText());
            Double price = Double.parseDouble(priceField.getText());
            Double total = quantity * price;
            Double grandTotal = total + getPreviousTotal();
            
            System.out.println("Previous Total From Method "+getPreviousTotal());
            totalField.setText(grandTotal.toString());
            paidAmountField.setText(totalField.getText());
            remainingAmountField.setText("0.0");
        }
         if(vatCheck.isSelected()){
            double total = Double.parseDouble(totalField.getText());
            System.out.println("Totalll "+total);
            double tax = total*0.05;
            taxAmountField.setText(String.valueOf(tax));
            totalField.setText(String.valueOf(total+tax));
            paidAmountField.setText(totalField.getText());
            remainingAmountField.setText("0.0");
        }
         Integer quantity = Integer.parseInt(quantityField.getText());
        Double price = Double.parseDouble(priceField.getText());
        Double total = quantity * price;
        productTotalLbl.setText("Total: "+total); 

    }//GEN-LAST:event_priceFieldKeyReleased
    private double getPreviousTotal(){
        DefaultTableModel cartTableModel = (DefaultTableModel) cartTable.getModel();
        lastTotal = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            double total = (double) cartTable.getValueAt(i, 5);
            lastTotal+= total;
        }
        return lastTotal;
    }
    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased

        TableRowSorter sorter = new TableRowSorter<>(saleTable.getModel());
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
        Double reciept = Double.parseDouble(recieptNo.getText());
        if(reciept<=0){
            JOptionPane.showMessageDialog(this, "Reciept No Can't be Negative or Zero!");
            recieptNo.setText("");
        }       
        boolean exists = saleDAO.isReceiptNoExists(recieptNo.getText());
        if (exists) {
            validateSlipLbl.setVisible(true);
        } else {
            validateSlipLbl.setVisible(false);
        }
    }//GEN-LAST:event_recieptNoKeyReleased
    private boolean isProductAlreadySelected(){
        boolean alreadyExists = false;
        DefaultTableModel cartTableModel = (DefaultTableModel) cartTable.getModel();
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            int existingProductId = (Integer) cartTable.getValueAt(i, 0);
            Product product = productDAO.getProductByName(productCombo.getSelectedItem().toString());
            if (product.getProductId() == existingProductId) {
                alreadyExists = true;
                break;
            }
        }
        return alreadyExists;
    }
    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        int productId = productDAO.getProductByName(productCombo.getSelectedItem().toString()).getProductId();
        if (isProductAlreadySelected()) {
            JOptionPane.showMessageDialog(this, "This Product is already added in cart!!");
            quantityField.setText("");
        } 
        else {
            DefaultTableModel cartTableModel = (DefaultTableModel) cartTable.getModel();
            String productName = productCombo.getSelectedItem().toString();
            Integer quantity = 0;
            if(!quantityField.getText().equals("")){
                quantity = Integer.parseInt(quantityField.getText());
            }
            Unit unit = Unit.valueOf(unitCombo.getSelectedItem().toString());
            Double price = Double.parseDouble(priceField.getText());
            Double total = quantity * price;
            Object row[] = {productId, productName, quantity, unit, price, total};
            cartTableModel.addRow(row);

            cartTable.getColumnModel().getColumn(0).setWidth(0);
            cartTable.getColumnModel().getColumn(0).setMinWidth(0);
            cartTable.getColumnModel().getColumn(0).setMaxWidth(0);
            addBtn.setEnabled(true);
        }
        quantityField.setText(""); 
    }//GEN-LAST:event_addItemActionPerformed

    private void addBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            sale();
        }
    }//GEN-LAST:event_addBtnKeyPressed

    private void paidAmountFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paidAmountFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paidAmountFieldActionPerformed

    private void quantityFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantityFieldKeyReleased
        Double qty = Double.parseDouble(quantityField.getText());
        if(qty<=0){
            JOptionPane.showMessageDialog(this, "Quantity Can't be Negative or Zero!");
            quantityField.setText("");
        } 
        if (isProductAlreadySelected()) {
            JOptionPane.showMessageDialog(this, "This Product is already added in cart!!");
            quantityField.setText("");
        }
        else if(editBtn.isEnabled() && !priceField.getText().equals("") && !quantityField.getText().equals("")){
            Integer quantity = Integer.parseInt(quantityField.getText());
            Double price = Double.parseDouble(priceField.getText());
            Double total = quantity * price;
           // previousTotal = Double.parseDouble(totalField.getText())-Double.parseDouble(taxAmountField.getText());
            System.out.println("Edit mai ayaa haiii ");
            Double grandTotal = total + previousTotal;            
            System.out.println("Previous total "+previousTotal);
            System.out.println(" total "+total);
            totalField.setText(grandTotal.toString());
            paidAmountField.setText(totalField.getText());
            remainingAmountField.setText("0.0");
        }
        else if (!priceField.getText().equals("") && !quantityField.getText().equals("")) {
            Integer quantity = Integer.parseInt(quantityField.getText());
            Double price = Double.parseDouble(priceField.getText());
            Double total = quantity * price;
            Double grandTotal = total + getPreviousTotal();
            
            System.out.println("Not Edit mai ayaa haiii ");
            
            System.out.println("Previous total "+previousTotal);
            System.out.println(" total "+total);
            totalField.setText(grandTotal.toString());
            paidAmountField.setText(totalField.getText());
            remainingAmountField.setText("0.0");
            
        }
        if(vatCheck.isSelected()){
            double total = Double.parseDouble(totalField.getText());
            System.out.println("Totalll "+total);
           // total = total-Double.parseDouble(taxAmountField.getText());
            System.out.println("Total AFter minus tax "+total);
            double tax = total*0.05;
            taxAmountField.setText(String.valueOf(tax));
            totalField.setText(String.valueOf(total+tax));
            paidAmountField.setText(totalField.getText());
            remainingAmountField.setText("0.0");
        }
        Integer quantity = Integer.parseInt(quantityField.getText());
        Double price = Double.parseDouble(priceField.getText());
        Double total = quantity * price;
        productTotalLbl.setText("Total: "+total); 
    }//GEN-LAST:event_quantityFieldKeyReleased

    private void priceFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceFieldActionPerformed

    private void quantityFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityFieldActionPerformed

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
            /* for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }*/
            UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
        } catch (Exception ex) {
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
    private javax.swing.JLabel headerLbl1;
    private javax.swing.JLabel headerLbl2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JLabel pNumberLbl1;
    private javax.swing.JTextField paidAmountField;
    private javax.swing.JLabel passwordLbl;
    private javax.swing.JComboBox<String> paymentTypeCombo;
    private javax.swing.JLabel phoneLbl;
    private javax.swing.JTextField priceField;
    private javax.swing.JComboBox<String> productCombo;
    private javax.swing.JLabel productTotalLbl;
    private javax.swing.JTextField quantityField;
    private javax.swing.JTextField recieptNo;
    private javax.swing.JLabel remainingAmountField;
    private javax.swing.JLabel remainingLbl;
    private com.toedter.calendar.JDateChooser saleDateField;
    private javax.swing.JLabel saleDateLbl;
    private javax.swing.JLabel saleNumberField;
    private javax.swing.JLabel saleNumberLbl;
    private javax.swing.JTable saleTable;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel serachLbl;
    private javax.swing.JLabel taxAmountField;
    private javax.swing.JLabel totalField;
    private javax.swing.JLabel totalLbl;
    private javax.swing.JComboBox<String> unitCombo;
    private javax.swing.JLabel validateSlipLbl;
    private javax.swing.JCheckBox vatCheck;
    private javax.swing.JComboBox<String> vendorCombo;
    // End of variables declaration//GEN-END:variables

}

class JTableButtonRenderer implements TableCellRenderer {

    private TableCellRenderer defaultRenderer;

    public JTableButtonRenderer(TableCellRenderer renderer) {
        defaultRenderer = renderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Component) {
            return (Component) value;
        }
        return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}

class JTableButtonModel extends AbstractTableModel {

    private Object[][] rows = {{"Button1", new JButton("Button1")}, {"Button2", new JButton("Button2")}, {"Button3", new JButton("Button3")}, {"Button4", new JButton("Button4")}};
    private String[] columns = {"Count", "Buttons"};

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return rows.length;
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Object getValueAt(int row, int column) {
        return rows[row][column];
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
}
