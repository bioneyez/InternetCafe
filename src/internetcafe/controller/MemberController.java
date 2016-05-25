/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.controller;

import internetcafe.ValidationException;
import internetcafe.cache.MemberCache;
import internetcafe.entity.Computer;
import internetcafe.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Lorand
 */
public class MemberController {
     private MemberCache memberCache;

    public MemberController(MemberCache memberCache) {
        this.memberCache = memberCache;
    }
     
     public List<Member> getAllMembers() {
         return memberCache.getAllMembers();
     }
     
     public List<Member> getAllUnloggedMembers() {
         return getAllMembers().stream().filter(m -> {
            return !m.isLoggedIn();
        }).collect(Collectors.toList());
     }
     
     public void login(Member member,Computer computer,String givenPassword) throws ValidationException, LoginException {
        if (!member.isLoggedIn() && !computer.isInUse() && member.getPassword().equals(givenPassword)) {
            member.setLoggedIn(true);
            member.setLoginTime(LocalDateTime.now());
            computer.setInUse(true);
        }
        else {
            if (member.getPassword().equals(givenPassword)) 
                throw new ValidationException();
            else 
                throw new LoginException();
        }
    }
     
     public void calculatePoints(Member member, LocalDateTime logoutTime) {
        if (member.getPoints() < 1500) {
            
            int loginHour = member.getLoginTime().getHour();
            int loginMinute = member.getLoginTime().getMinute();
            
            int logoutHour = logoutTime.getHour();
            int logoutMinute = logoutTime.getMinute();
            int actualPoints = member.getPoints();

              while(loginHour != logoutHour) {
                 while ((loginHour == 15 && loginMinute == 0) && loginHour != logoutHour) {
                    actualPoints+= 2;
                    loginHour++;
                    }  
                 while ((loginHour == 15 && loginMinute != 0) && loginHour != logoutHour) {
                    actualPoints+= 1;
                    loginHour++;
                    }
                 
                 while ((loginHour < 15) && loginHour != logoutHour) {
                    actualPoints+= 2;
                    loginHour++;
                    }

               

                  while (loginHour < 21 && loginHour != logoutHour) {
                     actualPoints+=1;
                     loginHour++;
                    }
                  while (loginHour >= 21 && loginHour != logoutHour) {
                      actualPoints+=2;
                      loginHour++;
                      if (loginHour > 23) {
                          loginHour = 0;
                    }
                  }
               }
              
            if ( actualPoints>1500) {
                 actualPoints = 1500;
            }
            member.setPoints(actualPoints);
        }
    }
     
    private int calculateBill(Member member) {
        int discountInPercent = (member.getPoints() / 150)/100;
        int ratePerHour = 1000;
        int bill = ratePerHour * discountInPercent;
        return bill;
    }
    
    
     public void logout(Member member,Computer computer) throws ValidationException {
        calculatePoints(member,LocalDateTime.now());
        int bill = calculateBill(member);
        if ((member.getBalance() - bill) > 0) {
            member.setLoggedIn(false);
            computer.setInUse(false);
            member.setBalance(member.getBalance()-bill);
            
        }
        else {
            throw new ValidationException();
        }
    }
     
    public void deposit(Member member,int amount) {
        member.setBalance(member.getBalance()+amount);
    }
    
    public void addMember(String username, String password, String name, int id, String address) {
        Member newMember = new Member(username,password,name,id,address);
        memberCache.addMember(newMember);
        
    }
    
    public void saveMember(Member m) {
        memberCache.save(m);
    }
    
    public Member updateMember(Member m, String password,String name, int idNumber,String address,int money) {
        m.setAddress(address);
        m.setName(name);
        m.setPassword(password);
        m.setIdNumber(idNumber);
        m.setBalance(money);
        return m;
    }
    
}
