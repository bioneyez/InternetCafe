/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.cache;

import internetcafe.cache.ComputerListener;
import internetcafe.database.ComputerDataSource;
import internetcafe.entity.Computer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

/**
 *
 * @author Lorand
 */
public class ComputerCacheImpl implements ComputerCache{
    
    private ComputerDataSource computerDataSource;
    private List<ComputerListener> computerListeners;
    private List<Computer> entries;
    private Timer finderTimer;
    
    public ComputerCacheImpl(ComputerDataSource computerDataSource) {
        this.computerDataSource = computerDataSource;
        computerListeners = new ArrayList<>();
        entries = new ArrayList<>();
        entries.addAll(computerDataSource.getAllComputers());
        
        //setupFinderTask();
    }
    /*
    private synchronized void setupFinderTask() {
        int lastId = 0;
        if(!entries.isEmpty()) {
            final Computer lastComputer = entries.get(entries.size() - 1);
            lastId = lastComputer.getId();
        }
        finderTimer = new Timer();
        finderTimer.scheduleAtFixedRate(
                new ComputerFinderTask(this, computerDataSource, lastId),
                10 * 1000,
                20 * 1000
        );
    }
    */
    @Override
    public List<Computer> getAllComputers() {
        return Collections.unmodifiableList(entries);
    }

    @Override
    public synchronized Computer getComputerById(int id) {
        return entries.stream().filter(c -> {
            return id == c.getId();
        })
        .findFirst()
        .orElse(computerDataSource.getComputerById(id));
    }

    
    /*
    private void fireComputerInserted(Computer c) {
        computerListeners.forEach(cl -> {
            cl.computerInserted(c);
        });
    }
    */
    
}
