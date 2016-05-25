/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import internetcafe.cache.MemberCache;
import internetcafe.controller.MemberController;
import internetcafe.ValidationException;
import internetcafe.entity.Computer;
import internetcafe.entity.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.LoginException;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lorand
 */
public class MemberControllerTest {
    
    private MemberController memberController;
    private MemberCache memberCache;
    
    public MemberControllerTest() {
    }
    
    
    @Before
    public void setUp() {
        memberCache = EasyMock.createNiceMock(MemberCache.class);
        memberController = new MemberController(memberCache);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getAllMemberTest() {
        List<Member> members = new ArrayList<>();
        members.add(new Member());
        members.add(new Member());
        members.add(new Member());
        EasyMock.expect(memberCache.getAllMembers()).andReturn(members);
        EasyMock.replay(memberCache);
        
        List<Member> actual = memberController.getAllMembers();
        assertEquals(members.size(),actual.size());
        
    }
    
    @Test(expected = RuntimeException.class)
    public void getAllMembersTestWithException() {
        EasyMock.expect(memberCache.getAllMembers()).andThrow(new RuntimeException());
        EasyMock.replay(memberCache);
        
        memberController.getAllMembers();
    } 
    
    @Test
    public void getAllRentableComputerTest() {
        List<Member> members = new ArrayList<>();
        members.add(createMember(5, false));
        members.add(createMember(0, false));
        members.add(createMember(2, true));
        members.add(createMember(0, true));
        members.add(createMember(1, false));
        EasyMock.expect(memberCache.getAllMembers()).andReturn(members);
        EasyMock.replay(memberCache);
        
        List<Member> actual = memberController.getAllUnloggedMembers();
        assertEquals(3, actual.size());
        for(Member m : actual) {
            assertTrue(!m.isLoggedIn());
        }
    }
    @Test
    public void loginTest() throws ValidationException, LoginException {
        Member m = createMember(5,false);
        Computer c = createComputer(1,false); 
        memberController.login(m,c,"");
    }
    
    @Test(expected = ValidationException.class)
    public void loginInvalidMemberTest() throws ValidationException, LoginException {
        Member m = createMember(5,true);
        Computer c = createComputer(1,false); 
        memberController.login(m,c,"");
    }
    
    @Test(expected = ValidationException.class)
    public void loginInvalidComputerTest() throws ValidationException, LoginException {
        Member m = createMember(5,false);
        Computer c = createComputer(1,true); 
        memberController.login(m,c,"");
    }
    
    @Test(expected = ValidationException.class)
    public void loginInvalidComputerAndMemberTest() throws ValidationException, LoginException {
        Member m = createMember(5,true);
        Computer c = createComputer(1,true); 
        memberController.login(m,c,"");
    }
    
    @Test(expected = LoginException.class)
    public void loginInvalidPasswordTest() throws ValidationException, LoginException {
        Member m = createMember(5,true);
        Computer c = createComputer(1,true); 
        memberController.login(m,c,"fafafa");
    }
    @Test
    public void calculatePointsZeroTest() {
        Member m = new Member();
        m.setLoginTime(LocalDateTime.now());
        memberController.calculatePoints(m,LocalDateTime.now());
        assertEquals(0,m.getPoints());
    }
    
    @Test
    public void calculatePointsMaxTest() throws ValidationException {
        Member m = new Member();
        Computer c = new Computer();
        m.setPoints(1499);
        m.setLoginTime(LocalDateTime.of(2016, 4, 23, 11, 0));
        memberController.calculatePoints(m,LocalDateTime.of(2016, 4, 23, 13, 0));
        assertEquals(1500,m.getPoints());
    }
    
    @Test
    public void calculatePointsBeforeRushHourTest1() throws ValidationException {
        Member m = new Member();
        Computer c = new Computer();
        m.setLoginTime(LocalDateTime.of(2016, 4, 23, 15, 0));
        memberController.calculatePoints(m,LocalDateTime.of(2016, 4, 23, 16, 0));
        assertEquals(m.getPoints(),2);
    }
    @Test
    public void calculatePointsBeforeRushHourTest2() throws ValidationException {
        Member m = new Member();
        Computer c = new Computer();
        m.setLoginTime(LocalDateTime.of(2016, 4, 23, 11, 0));
        memberController.calculatePoints(m,LocalDateTime.of(2016, 4, 23, 13, 0));
        assertEquals(m.getPoints(),4);
    }
    @Test
    public void calculatePointsBeforeRushHourTest3() throws ValidationException {
        Member m = new Member();
        Computer c = new Computer();
        m.setLoginTime(LocalDateTime.of(2016, 4, 23, 15, 15));
        memberController.calculatePoints(m,LocalDateTime.of(2016, 4, 23, 16, 15));
        assertEquals(m.getPoints(),1);
    }
    @Test
    public void calculatePointsAfterRushHourTest1() throws ValidationException {
        Member m = new Member();
        Computer c = new Computer();
        m.setLoginTime(LocalDateTime.of(2016, 4, 23, 21, 0));
        memberController.calculatePoints(m,LocalDateTime.of(2016, 4, 23, 22, 0));
        assertEquals(m.getPoints(),2);
    }
    @Test
    public void calculatePointsAfterRushHourTest2() throws ValidationException {
        Member m = new Member();
        Computer c = new Computer();
        m.setLoginTime(LocalDateTime.of(2016, 4, 23, 22, 0));
        memberController.calculatePoints(m,LocalDateTime.of(2016, 4, 23, 1, 0));
        assertEquals(m.getPoints(),6);
    }
    
    @Test
    public void calculatePointsInRushHourTest1() throws ValidationException {
        Member m = new Member();
        Computer c = new Computer();
        m.setLoginTime(LocalDateTime.of(2016, 4, 23, 16, 0));
        memberController.calculatePoints(m,LocalDateTime.of(2016, 4, 23, 17, 0));
        assertEquals(m.getPoints(),1);
    }
    @Test
    public void calculatePointsInRushHourTest2() throws ValidationException {
        Member m = new Member();
        Computer c = new Computer();
        m.setLoginTime(LocalDateTime.of(2016, 4, 23, 15, 15));
        memberController.calculatePoints(m,LocalDateTime.of(2016, 4, 23, 16, 15));
        assertEquals(m.getPoints(),1);
    }
    
    @Test
    public void logoutTest() throws ValidationException, LoginException {
        Member m = new Member();
        Computer c = new Computer();
        m.setBalance(2000);
        memberController.login(m, c,"");
        memberController.logout(m,c);
        assertEquals(m.isLoggedIn(),false);
    }
    
    @Test(expected = ValidationException.class)
    public void invalidLogoutTest() throws ValidationException, LoginException {
        Member m = new Member();
        Computer c = new Computer();
        m.setBalance(0);
        memberController.login(m, c,"");
        memberController.logout(m,c);
        assertEquals(m.isLoggedIn(),false);
    }
    
    @Test
    public void depositTest() {
        Member m = new Member();
        memberController.deposit(m,2000);
        assertEquals(2000, m.getBalance());
    }
    
    @Test
    public void addMemberTest() {
        List<Member> members = new ArrayList<>();
        final Capture<Member> memberCapture = new Capture<>();
        memberCache.addMember(EasyMock.capture(memberCapture));
        EasyMock.expectLastCall().andAnswer(() -> {
	members.add(memberCapture.getValue());
	return null;
        });
        EasyMock.replay(memberCache);
        memberController.addMember("test", "test", "test", 0, "test");
        assertEquals(members.size(),1);
    }
    
    
    
    @Test
    public void updateMember() {
        Member m = new Member();
        memberController.updateMember(m, "Test", "Test", 0, "Test", 0);
        assertEquals(m.getAddress(),"Test");
        assertEquals(m.getPassword(),"Test");
        assertEquals(m.getName(),"Test");
        assertEquals(m.getIdNumber(),0);
        assertEquals(m.getBalance(),0);
    }
    
    
    private Member createMember(int id, boolean b) {
        Member m = new Member();
        m.setIdNumber(id);
        m.setLoggedIn(b);
        return m;
    }
    
    private Computer createComputer(int id, boolean inUse) {
        Computer c = new Computer();
        c.setId(id);
        c.setInUse(inUse);
        return c;
    }
    
    @Test
    public void saveMember() {
        Member m = new Member();
        memberController.saveMember(m);
        assertTrue(true);
    }
}
