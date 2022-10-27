import java.util.List;

public class Hand {

    private Player player;
    private List<Card> cardList;
    private int handNumber;

    public Hand(int handNumber, Player player, List<Card> cardList) {
        this.handNumber = handNumber;
        this.player = player;
        this.cardList = cardList;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Card> getCardList() {
        return cardList;
    }
    
    public int getHandNumber() {
        return handNumber;
    }
}
