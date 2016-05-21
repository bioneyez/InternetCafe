package internetcafe.cache;

import java.util.List;
import internetcafe.entity.Computer;

public interface ComputerCache {
    
    List<Computer> getAllComputers();
    Computer getComputerById(int id);
    //void add(List<Computer> computers);
    //void registerComputerListener(ComputerListener computerListener);
}
