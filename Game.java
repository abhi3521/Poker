import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Game{

    private List<Player> playerList;    
    Map<Integer, List<Hand>> handMap = new HashMap<Integer, List<Hand>>();    //To store cards in hands
    Map<Player, Integer> resultMap = new HashMap<Player, Integer>(); // To store players with number of hand won

    // Constructor
    Game() {
        playerList = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public Player getPlayer(int index) {
        return playerList.get(index);
    }

    void addCard(String cards, Player player, int handNumber) {
        List<Card> cardList = getCards(cards);
        handMap.putIfAbsent(handNumber, new ArrayList<Hand>());
        handMap.get(handNumber).add(new Hand(handNumber, player, cardList));
    }

    private List<Card> getCards(String cards) {
        List<Card> cardList = new ArrayList<>();
        String[] cardArray = cards.split(" ");
        for (String cardString : cardArray) {
            int value = Integer.valueOf(cardString.charAt(0));
            String suit = String.valueOf(cardString.charAt(1));
            Card card = new Card(value, suit);
            cardList.add(card);
        }
        return cardList;
    }

    void playHand(int handNumber) {
        List<Hand> handList = handMap.get(handNumber);
        //get the highest rank
        Hand highestHand = handList.get(0);
        for (int i = 1; i < handList.size(); i++) {
            highestHand = compareHand(highestHand, handList.get(i));
        }
        resultMap.put(highestHand.getPlayer(), resultMap.getOrDefault(highestHand.getPlayer(), 0) + 1);
        //System.out.println(highestHand.getPlayer().getName() + " wins round " + handNumber);
    }

    // Display result
    void displayResult(){

        for(Map.Entry<Player, Integer> rm : resultMap.entrySet()){
            System.out.println(rm.getKey().getName() + ": " + rm.getValue());
        }
    }

    //returns the highest hand
    private Hand compareHand(Hand hand1, Hand hand2) {
        //rank comparision
        List<Card> cardHand1 = hand1.getCardList();
        List<Card> cardHand2 = hand2.getCardList();
        //System.out.println(getRank(cardHand1) + " " + getRank(cardHand2));
        if (getRank(cardHand1) > getRank(cardHand2)) {
            return hand1;
        } else if (getRank(cardHand1) < getRank(cardHand2)) {
            return hand2;
        } else {
            //equal condition

            if(getRank(cardHand1) == 7 && getRank(cardHand2) == 7) {
                return getHighestHandOnFullHouse(cardHand1, cardHand2, hand1, hand2);
            }
            if(getRank(cardHand1) == 2 && getRank(cardHand2) == 2) {
                return getHighestHandOnPair(cardHand1, cardHand2, hand1, hand2);
            }

            return getHighestHand(cardHand1, cardHand2, hand1, hand2);
        }
    }

    // Getting highest value card
    private Hand getHighestHand(List<Card> cardHand1, List<Card> cardHand2, Hand hand1, Hand hand2) {
        List<Integer> cardHandValue1 = new ArrayList<>();
        List<Integer> cardHandValue2 = new ArrayList<>();
        for(int index = 0; index < cardHand1.size(); index++){
            cardHandValue1.add(cardHand1.get(index).getValue());
            cardHandValue2.add(cardHand2.get(index).getValue());
        }
        
        Collections.sort(cardHandValue1);
        Collections.sort(cardHandValue2);

        for(int index = cardHandValue1.size()-1; index >= 0; index--) {
            
            if(cardHandValue1.get(index) > cardHandValue2.get(index)) {
                return hand1;
            } else if(cardHandValue1.get(index) < cardHandValue2.get(index)) {
                return hand2;
            } else {
                continue;
            }
        }
        return null;
    }

    // Handling Pair case with same rank
    private Hand getHighestHandOnPair(List<Card> cardHand1, List<Card> cardHand2, Hand hand1, Hand hand2) {
        List<Integer> cardHandValue1 = new ArrayList<>();
        List<Integer> cardHandValue2 = new ArrayList<>();
        for(int index = 0; index < cardHand1.size(); index++){
            cardHandValue1.add(cardHand1.get(index).getValue());
            cardHandValue2.add(cardHand2.get(index).getValue());
        }
        
        Collections.sort(cardHandValue1);
        Collections.sort(cardHandValue2);
        
        
        if(getRank(cardHand1) == 2 && getRank(cardHand2) == 2) {
            int maxCardValue1 = cardHandValue1.get(0);
            int maxCardValue2 = cardHandValue2.get(0);

            Map<Integer, Integer> pairMap1 = new HashMap<>();
            Map<Integer, Integer> pairMap2 = new HashMap<>();

            for(int val : cardHandValue1){
                pairMap1.put(val, pairMap1.getOrDefault(val, 0) + 1);
                if(pairMap1.get(val) == 2) {
                    maxCardValue1 = val;
                    break;
                }
            }

            for(int val : cardHandValue2){
                pairMap2.put(val, pairMap2.getOrDefault(val, 0) + 1);
                if(pairMap2.get(val) == 2) {
                    maxCardValue2 = val;
                    break;
                }
            }

            if(maxCardValue1 < maxCardValue2){
                return hand2;
            } else if(maxCardValue1 > maxCardValue2) {
                return hand1;
            } 
                
        }
        return getHighestHand(cardHand1, cardHand2, hand1, hand2);
    }

    // Handling fullhouse case with Rank
    private Hand getHighestHandOnFullHouse(List<Card> cardHand1, List<Card> cardHand2, Hand hand1, Hand hand2) {
        List<Integer> cardHandValue1 = new ArrayList<>();
        List<Integer> cardHandValue2 = new ArrayList<>();
        for(int index = 0; index < cardHand1.size(); index++){
            cardHandValue1.add(cardHand1.get(index).getValue());
            cardHandValue2.add(cardHand2.get(index).getValue());
        }
        
        Collections.sort(cardHandValue1);
        Collections.sort(cardHandValue2);
        
        if(getRank(cardHand1) == 7 && getRank(cardHand2) == 7) {
            int maxCardValue1 = cardHandValue1.get(0);
            int maxCardValue2 = cardHandValue2.get(0);

            if(maxCardValue1 != cardHandValue1.get(2)){
                maxCardValue1 = cardHandValue1.get(4);
            }
            if(maxCardValue2 != cardHandValue2.get(2)){
                maxCardValue2 = cardHandValue2.get(4);
            }

            if(maxCardValue1 < maxCardValue2){
                return hand2;
            } else if(maxCardValue1 > maxCardValue2) {
                return hand1;
            }
                
        }
        return getHighestHand(cardHand1, cardHand2, hand1, hand2);
    }

    // Method to evaluate Rank 
    private int getRank(List<Card> cardHand) {
        List<Integer> cardHandValue = new ArrayList<>();
        for(Card card : cardHand){
            cardHandValue.add(card.getValue());
        }
        Collections.sort(cardHandValue);
        
        //Royal Flush
        if(isSameSuit(cardHand) && isConsecutive(cardHandValue) && cardHandValue.get(0) == 10) {
            return 10;
        }

        // Straight flush
        if(isSameSuit(cardHand) && isConsecutive(cardHandValue)) {
            return 9;
        }

        // Four of a kind
        if(cardHandValue.get(0) == cardHandValue.get(3) || cardHandValue.get(1) == cardHandValue.get(4)) {
            return 8;
        }

        // Full house
        if((cardHandValue.get(0) == cardHandValue.get(2) && cardHandValue.get(3) == cardHandValue.get(4)) || (cardHandValue.get(0) == cardHandValue.get(1) && cardHandValue.get(2) == cardHandValue.get(4))) {
            return 7;
        }

        // Flush
        if(isSameSuit(cardHand)) {
            return 6;
        }

        // Straight
        if(isConsecutive(cardHandValue)) {
            return 5;
        }

        // Three of a kind
        if(cardHandValue.get(0) == cardHandValue.get(2) || cardHandValue.get(1) == cardHandValue.get(3) || cardHandValue.get(2) == cardHandValue.get(4)) {
            return 4;
        }

        // Two pairs
        if((cardHandValue.get(0) == cardHandValue.get(1)) && (cardHandValue.get(2) == cardHandValue.get(3)) || (cardHandValue.get(1) == cardHandValue.get(2)) && (cardHandValue.get(3) == cardHandValue.get(4)) || (cardHandValue.get(0) == cardHandValue.get(1)) && (cardHandValue.get(3) == cardHandValue.get(4))) {
            return 3;
        }

        // Pair
        if(cardHandValue.get(0) == cardHandValue.get(1) || cardHandValue.get(1) == cardHandValue.get(2) || cardHandValue.get(2) == cardHandValue.get(3) || cardHandValue.get(3) == cardHandValue.get(4)) {
            return 2;
        }
        
        return 1;
    }

    // checking consecutive order cards
    private boolean isConsecutive(List<Integer> cardHandValue) {
        int currentCardValue = cardHandValue.get(0);
        for(int cardIndex = 1; cardIndex < cardHandValue. size(); cardIndex++) {
            if(cardHandValue.get(cardIndex) != (currentCardValue + 1)){
                return false;
            }
            currentCardValue += 1;
        }
        return true;
    }

    // Checking cards are of same suits
    private boolean isSameSuit(List<Card> cardHand) {
        
        for(int cardIndex = 1; cardIndex < cardHand.size(); cardIndex++) {
            if(!cardHand.get(0).getSuit().equals(cardHand.get(cardIndex).getSuit())){
                return false;
            }
        }
        return true;
    }
}