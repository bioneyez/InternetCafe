/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.cache;

import internetcafe.entity.Member;

/**
 *
 * @author Lorand
 */
public interface MemberListener {
    void memberInserted(Member member);
}
