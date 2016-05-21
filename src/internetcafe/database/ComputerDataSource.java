/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.database;

import internetcafe.entity.Computer;
import java.util.List;

/**
 *
 * @author Lorand
 */
public interface ComputerDataSource {
    List<Computer> getAllComputers();
    Computer getComputerById(int id);
    List<Computer> findNewComputersAfter();
}
