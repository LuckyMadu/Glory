package glory_schema;

import java.util.HashMap;
import java.util.Map;


class CalculateScore {
    
     private int alpha = 0;
     private int beta = 0;
     private int gamma = 0;
     private int totalValue;
     private String word,rev="";
     private int noOfPlayers;
    
   
     
     
     public int score() {
         HashMap<Character, Integer> lettersMap=new HashMap<Character, Integer>();  
        String lettersCap = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < lettersCap.length(); i++) {
            
            if (lettersCap.charAt(i) == 'A' || lettersCap.charAt(i) == 'E' ||
                    lettersCap.charAt(i) == 'I' || lettersCap.charAt(i) == 'O' ||
                    lettersCap.charAt(i) == 'O' || lettersCap.charAt(i) == 'U' ||
                    lettersCap.charAt(i) == 'L' || lettersCap.charAt(i) == 'N' ||
                    lettersCap.charAt(i) == 'R' || lettersCap.charAt(i) == 'S' ||
                    lettersCap.charAt(i) == 'T') {

                lettersMap.put(lettersCap.charAt(i), 1);
                lettersMap.put(lettersCap.toLowerCase().charAt(i), 1);
            }

            if (lettersCap.charAt(i) == 'D' || lettersCap.charAt(i) == 'G') {
                lettersMap.put(lettersCap.charAt(i), 2);
                lettersMap.put(lettersCap.toLowerCase().charAt(i), 2);
            }

            if (lettersCap.charAt(i) == 'B' || lettersCap.charAt(i) == 'C' ||
                    lettersCap.charAt(i) == 'M' || lettersCap.charAt(i) == 'P') {
                lettersMap.put(lettersCap.charAt(i), 3);
                lettersMap.put(lettersCap.toLowerCase().charAt(i), 3);
            }

            if (lettersCap.charAt(i) == 'F' || lettersCap.charAt(i) == 'H' ||
                    lettersCap.charAt(i) == 'V' || lettersCap.charAt(i) == 'W' ||
                    lettersCap.charAt(i) == 'Y') {
                lettersMap.put(lettersCap.charAt(i), 4);
                lettersMap.put(lettersCap.toLowerCase().charAt(i), 4);
            }

            if (lettersCap.charAt(i) == 'K') {
                lettersMap.put(lettersCap.charAt(i), 5);
                lettersMap.put(lettersCap.toLowerCase().charAt(i), 5);
            }

            if (lettersCap.charAt(i) == 'J' || lettersCap.charAt(i) == 'X') {
                lettersMap.put(lettersCap.charAt(i), 8);
                lettersMap.put(lettersCap.toLowerCase().charAt(i), 8);
            }

            if (lettersCap.charAt(i) == 'Q' || lettersCap.charAt(i) == 'Z') {
                lettersMap.put(lettersCap.charAt(i), 10);
                lettersMap.put(lettersCap.toLowerCase().charAt(i), 10);
            }

        }


        for (int j = 0; j < word.length(); j++) {

            totalValue += lettersMap.get(word.charAt(j));
        }
        
        return totalValue;
    }
     
      
    public void setAlpha(){
        alpha = totalValue ;  
        
    }
    
    
    public void checkPalindrome(){
        int length = word.length();
        for ( int i = length - 1; i >= 0; i-- ){
         
            rev = rev + word.charAt(i);
        }
            if (word.equals(rev)){
               totalValue = totalValue + 10;
               System.out.println("You make a palindrome word, CONGRATS. You have reward Type1 and got bonus 10 points");    
            }
            else{
                System.out.println("This is not a palindrome word, No bonus points"); 
            }    
        
    }
     
    
    public void setRewardAlpha(){
        checkPalindrome();
        
        
    }
    
 
    public void setBeta(){
        
        if(word.length()==11){
            totalValue = totalValue*noOfPlayers;
        }
        beta = totalValue;
        
    }
   
    public void setGamma(){
        gamma = gamma ;
     
    }
    
    public int setScores(){
           
        totalValue = alpha + beta + gamma;
        return totalValue;
    }
    
    
    
}
