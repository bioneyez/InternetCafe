/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.cache;

import internetcafe.cache.MemberListener;
import internetcafe.entity.Member;
import java.util.List;

/**
 *
 * @author Lorand
 */
public interface MemberCache {
    List<Member> getAllMembers();
    
    Member getMemberById(int id);

    void add(List<Member> members);
    void save(Member m);
    void registerMemberListener(MemberListener memberListener);
    void addMember(Member m);
}
