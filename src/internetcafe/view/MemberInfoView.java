/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.view;

import internetcafe.controller.ComputerController;
import internetcafe.controller.MemberController;
import internetcafe.ValidationException;
import internetcafe.entity.Computer;
import internetcafe.entity.Member;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Lorand
 */
public class MemberInfoView extends JFrame{
    private JButton loginButton;
    private JButton logoutButton;
    private JButton addFundButton;
    private JButton saveButton;
    private JLabel nameLabel;
    private JLabel idLabel;
    private JLabel addressLabel;
    private JLabel computerLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel moneyLabel;
    private JLabel idInDB;
    private JTextField nameField;
    private JTextField idField;
    private JTextField addressField;
    private JLabel usernameField;
    private JPasswordField passwordField;
    private JTextField addFundField;
    private JLabel moneyAmountLabel;
    private JPanel panel;
    private JPanel buttonPanel;
    private JComboBox availableComputers;
    private MemberController memberController;
    private ComputerController computerController;
    private Member member;
    
    public MemberInfoView(MemberController memberController,ComputerController computerController,Member member) {
        this.member = member;
        this.memberController = memberController;
        this.computerController = computerController;
        setSize(600,400);
        initComponents();
        
        
        
    }
    
    private void initComponents() {
        panel = new JPanel();
        List<String> availableComputerNames = new ArrayList<>();
        for (Computer c : computerController.getAllRentableComputers()) {
            availableComputerNames.add(Integer.toString(c.getId()));
        }
        availableComputers = new JComboBox(availableComputerNames.toArray());
        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");
        if (!member.isLoggedIn()) logoutButton.setEnabled(false);
        else loginButton.setEnabled(false);
        addFundButton = new JButton("Add Fund");
        saveButton = new JButton("Save");
       
        nameLabel = new JLabel("Name: ");
        idLabel = new JLabel("ID: ");
        addressLabel = new JLabel("Address: ");
        usernameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");
        computerLabel = new JLabel("Computer: ");
        moneyLabel = new JLabel("Money: ");
        moneyAmountLabel = new JLabel(Integer.toString(member.getBalance()));
        idInDB = new JLabel("ID" + member.getId());
        
        nameField = new JTextField(member.getName());
        idField = new JTextField(Integer.toString(member.getIdNumber()));
        addressField = new JTextField(member.getAddress());
        usernameField = new JLabel(member.getUsername());
        passwordField = new JPasswordField(member.getPassword());
        addFundField = new JTextField(4);

        setLayout();
        setActionListeners();
        add(panel);
    }
    
    
    private void addFund() {
        int amount = Integer.parseInt(addFundField.getText());
        memberController.deposit(member, amount);
        addFundField.setText("");
        moneyAmountLabel.setText(Integer.toString(member.getBalance()));
    }
    
    private void save() {
         boolean noError = true;
         String name = nameField.getText();
         String address = addressField.getText();
         String password = passwordField.getText();
         try {
            Integer.parseInt(idField.getText());
         } 
         catch(NumberFormatException ex) {
             JOptionPane.showMessageDialog(null,"ID must be numeric");
             noError = false;
             idField.setText(Integer.toString(member.getIdNumber()));
         }
         if (noError) {
            int idNumber = Integer.parseInt(idField.getText());
            int money = member.getBalance();
            Member m = memberController.updateMember(member, password, name, idNumber, address, money);
            memberController.saveMember(m);
         }
         
    }
    
    private void setLayout() {
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().
            addComponent(nameLabel).addComponent(addressLabel).addComponent(idLabel).addComponent(usernameLabel).addComponent(passwordLabel).addComponent(moneyLabel).addComponent(computerLabel).addComponent(loginButton));
        
        hGroup.addGroup(layout.createParallelGroup().
            addComponent(nameField).addComponent(addressField).addComponent(idField).addComponent(usernameField).addComponent(passwordField).addComponent(moneyAmountLabel).addComponent(availableComputers).addComponent(logoutButton));
        
        hGroup.addGroup(layout.createParallelGroup().addComponent(addFundField).addComponent(addFundButton));
        hGroup.addGroup(layout.createParallelGroup().addComponent(saveButton).addComponent(idInDB));
        
        
        layout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
            addComponent(nameLabel).addComponent(nameField));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
            addComponent(addressLabel).addComponent(addressField));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
            addComponent(idLabel).addComponent(idField));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
            addComponent(usernameLabel).addComponent(usernameField));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
            addComponent(passwordLabel).addComponent(passwordField));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
            addComponent(moneyLabel).addComponent(moneyAmountLabel));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
            addComponent(computerLabel).addComponent(availableComputers).addComponent(addFundField).addComponent(idInDB));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
            addComponent(loginButton).addComponent(logoutButton).addComponent(addFundButton).addComponent(saveButton));
        layout.setVerticalGroup(vGroup);
    }

    private void setActionListeners() {
         logoutButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logoutAction();
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(null,"Not enough money");
                }
            }
        
        });
         
         loginButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loginAction();
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(null,"Already logged in");
                }
            }
        
        });
         
         
         addFundButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               addFund();
                   
            }
        
        });
         saveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               save();       
            }
        
        });
    }
    
    
    private void loginAction() throws ValidationException {
        if (availableComputers.getSelectedIndex() != -1) {
            String selectedComputerString = availableComputers.getSelectedItem().toString();
            int selectedComputerIndex = Integer.parseInt(selectedComputerString);

            memberController.login(member,computerController.getComputerById(selectedComputerIndex));
            loginButton.setEnabled(false);
            logoutButton.setEnabled(true);
            availableComputers.setEnabled(false);
        }
        else {
            JOptionPane.showMessageDialog(null,"Select a computer");
        }
    }
    
    private void logoutAction() throws ValidationException {
        String selectedComputerString = availableComputers.getSelectedItem().toString();
        int selectedComputerIndex = Integer.parseInt(selectedComputerString);
        
        memberController.logout(member,computerController.getComputerById(selectedComputerIndex));
        loginButton.setEnabled(true);
        logoutButton.setEnabled(false);
        availableComputers.setEnabled(true);
    }
    
    
    
}
