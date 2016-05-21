import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import internetcafe.cache.ComputerCache;
import internetcafe.controller.ComputerController;
import internetcafe.ValidationException;
import internetcafe.entity.Member;
import internetcafe.entity.Computer;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComputerControllerTest {
    
    private ComputerController computerController;
    private ComputerCache computerCache;
//    private ValidationService validationService;
    
    public ComputerControllerTest() {
    }

    @Before
    public void setUp() {
        computerCache = EasyMock.createNiceMock(ComputerCache.class);
//        validationService = EasyMock.createNiceMock(ValidationService.class);
        computerController = new ComputerController(computerCache);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getAllComputersTest() {
        List<Computer> computers = new ArrayList<>();
        computers.add(new Computer());
        computers.add(new Computer());
        computers.add(new Computer());
        EasyMock.expect(computerCache.getAllComputers()).andReturn(computers);
        EasyMock.replay(computerCache);
        
        List<Computer> actual = computerController.getAllComputers();
        assertEquals(computers.size(), actual.size());
    }
    
    @Test(expected = RuntimeException.class)
    public void getAllComputersTestWithException() {
        EasyMock.expect(computerCache.getAllComputers()).andThrow(new RuntimeException());
        EasyMock.replay(computerCache);
        
        computerController.getAllComputers();
    } 
    
    @Test
    public void getAllRentableComputerTest() {
        List<Computer> computers = new ArrayList<>();
        computers.add(createComputer(5, false));
        computers.add(createComputer(0, false));
        computers.add(createComputer(2, true));
        computers.add(createComputer(0, true));
        computers.add(createComputer(1, false));
        EasyMock.expect(computerCache.getAllComputers()).andReturn(computers);
        EasyMock.replay(computerCache);
        
        List<Computer> actual = computerController.getAllRentableComputers();
        assertEquals(3, actual.size());
        for(Computer c : actual) {
            assertTrue(!c.isInUse());
        }
    }
    @Test
    public void rentTest() throws ValidationException {
        Member member = new Member();
        Computer computer = createComputer(5,false);
        
        computerController.rent(member,computer);
    }
    
    
     @Test(expected = ValidationException.class)
    public void rentTestInvalidComputer() throws ValidationException {
        Member member = new Member();
        Computer computer = createComputer(5,true);
        
        
        computerController.rent(member, computer);
    }
    
    @Test(expected = ValidationException.class)
    public void rentTestInvalidMember() throws ValidationException {
        Member member = createMember(5,true);
        Computer computer = new Computer();
        
        
        computerController.rent(member, computer);
    }
    
    private Computer createComputer(int id, boolean inUse) {
        Computer c = new Computer();
        c.setId(id);
        c.setInUse(inUse);
        return c;
    }
    
    private Member createMember(int id, boolean b) {
        Member m = new Member();
        m.setIdNumber(id);
        m.setLoggedIn(b);
        return m;
    }
}
