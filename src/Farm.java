/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 250677
 */
public class Farm extends Tower {
    int reward;
    int total = 0;
    public Farm(char c, int x, int y, boolean b, int yield) {
        super(c,x,y,b);
        reward = yield;
    }
    
    public int getReward() {
        return reward;
    }
    
    public void upgradeReward() {
        reward++;
    }
    
    public void addTotal() {
        total+= reward*25;
    }
    
    public int getTotal() {
        return total;
    }
    
}
