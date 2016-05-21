/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.cache;

import internetcafe.cache.MemberCache;
import internetcafe.database.MemberDataSource;
import internetcafe.entity.Member;
import java.util.List;
import java.util.TimerTask;

/**
 *
 * @author Lorand
 */
class MemberFinderTask extends TimerTask {
    
    private MemberCache memberCache;
    private MemberDataSource memberDataSource;
    private int lastId;
    
    public MemberFinderTask(MemberCache memberCache, MemberDataSource memberDataSource, int lastId) {
        this.memberCache = memberCache;
        this.memberDataSource = memberDataSource;
        this.lastId = lastId;
    }

    @Override
    public void run() {
        
        List<Member> members = memberDataSource.findNewMembersAfter(lastId);
        
        for (Member m : members) {
            if (m.getId() > lastId) lastId = m.getId();
        }
        //System.out.println("Last ID: " + lastId);
        memberCache.add(members);
    }
    
}
