package internetcafe.controller;

import internetcafe.ValidationException;
import internetcafe.cache.ComputerCache;
import java.util.List;
import java.util.stream.Collectors;
import internetcafe.entity.Member;
import internetcafe.entity.Computer;

public class ComputerController {

    private ComputerCache computerCache;

    public ComputerController(ComputerCache computerCache) {
        this.computerCache = computerCache;
    }
    
    
    public List<Computer> getAllComputers() {
        return computerCache.getAllComputers();
    }
    public Computer getComputerById(int id) {
        return computerCache.getComputerById(id);
    }

    public List<Computer> getAllRentableComputers() {
        return getAllComputers().stream().filter(m -> {
            return !m.isInUse();
        }).collect(Collectors.toList());
    }

    public void rent(Member member, Computer computer) throws ValidationException{
        if (member.isLoggedIn() || computer.isInUse()) {
            throw new ValidationException();
        }
        member.setLoggedIn(true);
        computer.setInUse(true);
        
    }
}
