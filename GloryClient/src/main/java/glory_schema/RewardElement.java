package glory_schema;

import java.util.HashMap;
import java.util.Map;



public class RewardElement extends GloryElement{

    
     private final String word;
     String rev = "";
     int totalValue;
     
    
     RewardElement(String word){
       this.word=word;
     } 
     

    @Override
    public void validityWord() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     @Override
    public void score() {
         Map<Character, Integer> lettersMap = new HashMap<>();
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
    
    
    public int checkPalindrome(){
        return totalValue;
    }
    
    
    public void findRewardLetter(){
         
        int res = 0;
       
        for (int i=0; i<word.length(); i++)
        {
            char z;
            // checking character in string
            if (word.charAt(i) == 'z')
            res++;
        } 
        
        if(res<2){
            System.out.println("Your word does not contain 2 Z's");
        }
        else{
            System.out.println("Your word have 2 Z's,CONGRATS. You have reward Type2 and got bonus 10 points");
            totalValue = totalValue + 10;
            System.out.println("Final score after the reward Type 02 : "+totalValue+ "\n");
        }    
    }
        
     public void findRewardLength(){
         
          
        if(word.length()>8){
            System.out.println("Your word exceeds 8. You have reward Type3 and got bonus 10 points");
            totalValue = totalValue + 10;
            System.out.println("Final score after the reward Type 03 : "+totalValue+ "\n");
        }
     
      
            
          
    }   
    

 
    
}
