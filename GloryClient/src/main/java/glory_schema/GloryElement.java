/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glory_schema;

/**
 *
 * @author ADMIN
 */
public abstract class GloryElement {

    public abstract void score();
    public abstract void validityWord();
    
    public static void main(String[] args) {
        
        String word="intermezzo";
        
  
        RewardElement reward = new RewardElement(word);
        reward.score();
        int rewardTypeOne = reward.checkPalindrome();
        System.out.println("Final score after the reward Type 01 : "+rewardTypeOne+ "\n");
        
   
        reward.findRewardLetter();
        reward.findRewardLength();
       
        
        
    }
    
}
