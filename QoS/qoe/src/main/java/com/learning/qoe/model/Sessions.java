/**
 * 
 */
package com.learning.qoe.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sushant
 *
 */
public class Sessions implements Serializable {

  
    private Set<Session> session;
    
    public Sessions() {
        
    }

    public Set<Session> getSession() {
        return session;
    }

    public void setSession(Set<Session> session) {
        this.session = session;
    }
    
    public void addSession(Session sess) {
        if (session == null) {
            session = new HashSet<Session>();
        }
        session.add(sess);        
    }
   
}
