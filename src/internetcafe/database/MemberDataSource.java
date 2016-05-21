/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.database;

import internetcafe.entity.Member;
import java.util.List;

/**
 *
 * @author Lorand
 */
public interface MemberDataSource {
    List<Member> getAllMembers();
    Member getMemberById(int id);
    List<Member> findNewMembersAfter(int id);
}
