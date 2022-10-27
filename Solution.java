import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Solution {
    

    public static void main(String args[]) {
        Game game = new Game();     // Creating Game instance

        // Creating Player
        game.addPlayer(new Player("Player 1"));
        game.addPlayer(new Player("Player 2"));
        try {
            FileReader fileReader = new FileReader("./poker.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int handNumber = 0;
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                for (int j = 0, playerNumber = 0; j < line.length(); j+=15, playerNumber++) {
                    game.addCard(line.substring(j, j+14), game.getPlayer(playerNumber), handNumber);
                }
                game.playHand(handNumber);
                handNumber++;
            }
            
            game.displayResult();

            bufferedReader.close();  
        } catch(IOException ioException) {
            System.out.println("Unable to read file");
        } 
    }
}
