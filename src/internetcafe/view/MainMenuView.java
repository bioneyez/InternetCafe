/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.view;

import internetcafe.cache.ComputerCache;
import internetcafe.cache.ComputerCacheImpl;
import internetcafe.controller.ComputerController;
import internetcafe.database.ComputerDataSource;
import internetcafe.cache.MemberCache;
import internetcafe.cache.MemberCacheImpl;
import internetcafe.controller.MemberController;
import internetcafe.database.MemberDataSource;
import internetcafe.database.ComputerDataSourceImpl;
import internetcafe.database.ConnectionFactory;
import internetcafe.database.MemberDataSourceImpl;
import internetcafe.entity.Computer;
import internetcafe.entity.Member;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Lorand
 */
public class MainMenuView extends JFrame{
    private JPanel controlPanel;
    private JButton listComputersButton;
    private JButton listMembersButton;
    private JButton addMemberButton;
    private final ComputerCache computerCache;
    private final MemberCache memberCache;
    private final MemberController memberController;
    private final ComputerController computerController;
    
    public MainMenuView() {
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,400);
        setTitle("Main Menu");
        initComponents();
        
       /* 
        computerCache = new ComputerCache() {
            @Override
            public List<Computer> getAllComputers() {
                List<Computer> result = new ArrayList<>();
                for(int i = 0; i < 10; i++) {
                    Computer c = new Computer();
                    c.setId(i);
                    c.setSpecification("Specification" + i);
                    c.setOperationSystem("Operation System" + i);
                    result.add(c);
                }
                return result;
            }

            @Override
            public Computer getComputerById(int id) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            
        };
        */
        /*
        memberCache = new MemberCache() {
            @Override
            public List<Member> getAllMembers() {
                List<Member> result = new ArrayList<>();
                for(int i = 0; i < 10; i++) {
                    Member m = new Member();
                    m.setUsername("Username " + i);
                    m.setAddress("Address" + i);
                    m.setId(i);
                    result.add(m);
                }
                return result;
            }

            @Override
            public Member getMemberById(int id) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void add(List<Member> members) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void registerMemberListener(MemberListener memberListener) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void save(Member m) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        */
        ConnectionFactory connection = new ConnectionFactory();
        
        ComputerDataSourceImpl computerDataSource = new ComputerDataSourceImpl(connection);
        computerCache = new ComputerCacheImpl(computerDataSource);
        
        MemberDataSourceImpl memberDataSource = new MemberDataSourceImpl(connection);
        memberCache = new MemberCacheImpl(memberDataSource);
        this.memberController = new MemberController(memberCache);
        this.computerController = new ComputerController(computerCache);
    }
    
    public void initComponents() {
        // mainmenu layout
        controlPanel = new JPanel(new FlowLayout());
        
        //buttons
        listComputersButton = new JButton("List Computers");
        listMembersButton = new JButton("List Members");
        addMemberButton = new JButton("Add Member");
        
        //actionListeners
        listComputersButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new ComputerView(computerController).setVisible(true);
            }
        
        });
        
        listMembersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MemberView(memberController,computerController).setVisible(true);
            }
        });
        
        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddMemberView(memberController).setVisible(true);
            }
        });
        
        //add buttons to panel
        controlPanel.add(listComputersButton);
        controlPanel.add(listMembersButton);
        controlPanel.add(addMemberButton);
        //add panel to windows
        add(controlPanel);
    }
    
    
}
