# Glory
Scrabble Game
FAQ
1.	How do I Run the game on a Local server?(can play multiple users at the same time)

You need JDK version 8.0 and Glassfish version 4.1 or tomcat server (we added tomcat to the game folder) installed to run the Game Server. 
After installed tomcat (If havent) , First Open GloryServer in Game folder in your IDE and run the project. Then Open GloryClient. Then follow the given steps: 



	Go to the:
	GloryClient -> glory_schema -> VariableElement.java. Make changes to the following   
        Lines.
	
	Change the port(your port instead of 8080)		
           (public final static String IP = "http://localhost:8080/ glorygame /WebResources/";)
            Then run the Game Client.  
            
            
            
	Go to the:
GloryServer -> Restful Web Services -> PlayerService

Find registerPlayer() and addPlayer() Methods, Change port to your local like above And change the partition if you haven’t D drive.
Finally copy paste the dictionary text file into your above partition.




