/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.view;

import internetcafe.controller.MemberController;
import internetcafe.ValidationException;
import internetcafe.entity.Member;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
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
public class AddMemberView extends JFrame{
    private JButton saveButton;
    private JLabel nameLabel;
    private JLabel idLabel;
    private JLabel addressLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField nameField;
    private JTextField idField;
    private JTextField addressField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final MemberController memberController;
    private JPanel panel;
    
    public AddMemberView(MemberController memberController) {
        this.memberController = memberController;
        
        setSize(600,400);
        setTitle("Add Member");
        initComponents();
    }

    private void initComponents() {
        panel = new JPanel();
        
        saveButton = new JButton("Save");
        nameLabel = new JLabel("Name: ");
        idLabel = new JLabel("ID: ");
        addressLabel = new JLabel("Address: ");
        usernameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");
        
        nameField = new JTextField();
        idField = new JTextField();
        addressField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        
        setLayout();
        setActionListeners();
        add(panel);
    }
    
    private void save() {
        boolean noError = true;
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String id = idField.getText();
        if (!(username.equals("")||password.equals("") ||name.equals("") || address.equals("") || id.equals(""))) {
            try {
                int idInt = Integer.parseInt(id);
                if (idInt < 0) throw new NumberFormatException();
            } 
            catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,"ID must be numeric");
                noError = false;
             }
            if (noError) {
                int idNumber = Integer.parseInt(idField.getText());

                //memberController.addMember(username, password, name, id, address);
                Member m = new Member(username,password,name,idNumber,address);
                memberController.saveMember(m);
                dispose();
            }
        }
        else {
            JOptionPane.showMessageDialog(null,"All field must be filled");
        }
    }
    
    private void setLayout() {
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().
            addComponent(nameLabel).addComponent(addressLabel).addComponent(idLabel).addComponent(usernameLabel).addComponent(passwordLabel).addComponent(saveButton));
        
        hGroup.addGroup(layout.createParallelGroup().
            addComponent(nameField).addComponent(addressField).addComponent(idField).addComponent(usernameField).addComponent(passwordField));
        
        layout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
            addComponent(nameLabel).addComponent(nameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
            addComponent(addressLabel).addComponent(addressField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
            addComponent(idLabel).addComponent(idField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
            addComponent(usernameLabel).addComponent(usernameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
            addComponent(passwordLabel).addComponent(passwordField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
            addComponent(saveButton));
        
        layout.setVerticalGroup(vGroup);
    }

    private void setActionListeners() {
        saveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        
        });
    }
}
