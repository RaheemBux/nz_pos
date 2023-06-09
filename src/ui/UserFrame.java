/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.UserDAO;
import daoimpl.UserDAOImpl;
import java.sql.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.User;

/**
 *
 *
 */
public class UserFrame extends javax.swing.JFrame {

    /**
     * Creates new form RegisterFrame
     */
    Object columns[] = {"Id", "Name", "Email", "Password", "Phone", "Address","Emirated ID","Expiry Date"};

    DefaultTableModel defaultTableModel = new DefaultTableModel(columns, 0);

    private static Integer userId = 0;

    UserDAO userDAO = new UserDAOImpl();

    public UserFrame() {
        initComponents();
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        fillTable();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    public boolean isAnyFieldsEmpty() {
        if (nameField.getText().equals("") || emailField.getText().equals("") || phoneField.getText().equals("")
                || passwordField.getText().equals("") || addressField.getText().equals("") 
                || addressField.getText().equals("") || emaritsIdFeild.getText().equals("") ) {
            return true;

        }
        return false;
    }

    public void setFields(User user) {
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getContact());
        passwordField.setText(user.getPassword());
        addressField.setText(user.getAddress());
        emaritsIdFeild.setText(user.getEmiratesId());
        idExpiryDateField.setDate(user.getExpiryDate());
    }

    public User getUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String contact = phoneField.getText();
        String address = addressField.getText();
        String password = passwordField.getText();
        String emiratesId = emaritsIdFeild.getText();
        java.sql.Date expirayDate = new java.sql.Date(idExpiryDateField.getDate().getTime());
        User user = new User(0, name, email, password, contact, address,emiratesId,expirayDate);
        return user;
    }

    public void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        addressField.setText("");
        emaritsIdFeild.setText("");
    }

    public void fillTable() {
        defaultTableModel = new DefaultTableModel(columns, 0);
        List<User> users = userDAO.getAllUsers();

        for (User u : users) {
            Object row[] = {u.getUserId(), u.getName(), u.getEmail(), u.getPassword(), 
                u.getContact(), u.getAddress(),u.getEmiratesId(),u.getExpiryDate()};
            defaultTableModel.addRow(row);
            usersTable.setModel(defaultTableModel);
        }
        usersTable.getColumnModel().getColumn(0).setWidth(0);
        usersTable.getColumnModel().getColumn(0).setMinWidth(0);
        usersTable.getColumnModel().getColumn(0).setMaxWidth(0);

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
        nameField = new javax.swing.JTextField();
        phoneLbl = new javax.swing.JLabel();
        emailLbl = new javax.swing.JLabel();
        phoneField = new javax.swing.JTextField();
        addressLbl = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        addBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        addressField = new javax.swing.JTextField();
        backBtn = new javax.swing.JButton();
        passwordLbl = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        emiratesIdLbl = new javax.swing.JLabel();
        emaritsIdFeild = new javax.swing.JTextField();
        idExpiryDateField = new com.toedter.calendar.JDateChooser();
        idExpirayDateLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        headerLbl.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        headerLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLbl.setText("User Details");

        nameLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        nameLbl.setText("Name");

        nameField.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        phoneLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        phoneLbl.setText("Phone");

        emailLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        emailLbl.setText("Email");

        phoneField.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        addressLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addressLbl.setText("Address");

        emailField.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        usersTable.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        usersTable.setModel(new javax.swing.table.DefaultTableModel(
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
        usersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usersTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(usersTable);

        addBtn.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        addBtn.setText("Add");
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

        deleteBtn.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        addressField.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        backBtn.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        passwordLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        passwordLbl.setText("Password");

        passwordField.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        emiratesIdLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        emiratesIdLbl.setText("Emirates ID");

        emaritsIdFeild.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        idExpiryDateField.setDateFormatString("dd-MM-yyyy");

        idExpirayDateLbl.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        idExpirayDateLbl.setText("Expiry Date");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(backBtn)
                        .addGap(155, 155, 155)
                        .addComponent(headerLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(nameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(emailLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(passwordLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(phoneLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(emiratesIdLbl)
                        .addGap(33, 33, 33)
                        .addComponent(emaritsIdFeild, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(idExpirayDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(idExpiryDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 836, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(addressLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(96, 96, 96))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(backBtn))
                    .addComponent(headerLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(passwordLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emiratesIdLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emaritsIdFeild, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idExpirayDateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idExpiryDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addressLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersTableMouseClicked
        userId = (Integer) usersTable.getValueAt(usersTable.getSelectedRow(), 0);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
        User existingUser = userDAO.getUserById(userId);
        setFields(existingUser);

    }//GEN-LAST:event_usersTableMouseClicked

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        if (isAnyFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!!");
        } else {
            User user = getUser();
            boolean b = userDAO.addUser(user);
            if (b) {
                JOptionPane.showMessageDialog(this, "User Added Successfully");
                fillTable();
                clearFields();
            }
        }

    }//GEN-LAST:event_addBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        if (isAnyFieldsEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!!");
        } else {
            User user = getUser();
            user.setUserId(userId);
            boolean b = userDAO.updateUser(user);
            if (b) {
                JOptionPane.showMessageDialog(this, "User Updated Successfully");
                fillTable();
                clearFields();
                editBtn.setEnabled(false);
                deleteBtn.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Error Occurred");
            }
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        userId = (Integer) usersTable.getValueAt(usersTable.getSelectedRow(), 0);
        boolean b = userDAO.deleteUser(userId);
        if (b) {
            JOptionPane.showMessageDialog(this, "User Deleted Successfully");
            fillTable();
            clearFields();
            editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        Home frame = new Home();
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backBtnActionPerformed

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
            java.util.logging.Logger.getLogger(UserFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JTextField addressField;
    private javax.swing.JLabel addressLbl;
    private javax.swing.JButton backBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLbl;
    private javax.swing.JTextField emaritsIdFeild;
    private javax.swing.JLabel emiratesIdLbl;
    private javax.swing.JLabel headerLbl;
    private javax.swing.JLabel idExpirayDateLbl;
    private com.toedter.calendar.JDateChooser idExpiryDateField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLbl;
    private javax.swing.JTextField phoneField;
    private javax.swing.JLabel phoneLbl;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables
}
