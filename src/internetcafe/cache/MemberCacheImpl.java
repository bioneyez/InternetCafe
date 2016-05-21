/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.cache;


import internetcafe.cache.MemberListener;
import internetcafe.database.MemberDataSourceImpl;
import internetcafe.entity.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;

/**
 *
 * @author Lorand
 */
public class MemberCacheImpl implements MemberCache{
    
    private MemberDataSourceImpl memberDataSource;
    private List<MemberListener> memberListeners;
    private List<Member> entries;
    private Timer finderTimer;
    
    public MemberCacheImpl(MemberDataSourceImpl memberDataSource) {
        this.memberDataSource = memberDataSource;
        memberListeners = new ArrayList<>();
        entries = new ArrayList<>();
        entries.addAll(memberDataSource.getAllMembers());
        
        setupFinderTask();
    }
    
    private synchronized void setupFinderTask() {
        int lastId = 0;
        if(!entries.isEmpty()) {
            final Member lastMember = entries.get(entries.size() - 1);
            lastId = lastMember.getId();
        }
        finderTimer = new Timer();
        finderTimer.scheduleAtFixedRate(
                new MemberFinderTask(this, memberDataSource, lastId),
                10 * 1000,
                20 * 1000
        );
    }
    
    @Override
    public void registerMemberListener(MemberListener memberListener) {
        memberListeners.add(memberListener);
    }
    
    @Override
    public List<Member> getAllMembers() {
        return Collections.unmodifiableList(entries);
    }

    @Override
    public synchronized Member getMemberById(int id) {
        Member member = null;
        try {
            member = entries.stream().filter(m -> {
                return id == m.getId();
            })
            .findFirst()
            .get();
        }
        catch (NoSuchElementException ex){
            member = memberDataSource.getMemberById(id);
            entries.add(member);
        }
        return member;
    }

    @Override
    public synchronized void add(List<Member> members) {
        entries.addAll(members);
        members.forEach(m -> {
            fireMemberInserted(m);
        });
    }
    
    @Override
    public synchronized void addMember(Member m) {
        entries.add(m);
    }
    
    private void fireMemberInserted(Member m) {
        memberListeners.forEach(ml -> {
            ml.memberInserted(m);
        });
    }

    @Override
    public void save(Member m) {
       memberDataSource.save(m);
    }
    
}
