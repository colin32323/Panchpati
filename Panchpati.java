import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Panchpati {
    ArrayList<Integer> Deck, Played_Cards, In_Play;
    ArrayList<ArrayList<Integer>> Players;
    int Power_Card, Win_Order[], scores[];
    boolean drawcard, win;

    Panchpati(int num, int n_players) {
        Deck = new ArrayList<>(52);
        Win_Order = new int[n_players];
        scores = new int[n_players];
        Played_Cards = new ArrayList<>();
        In_Play = new ArrayList<>();
        In_Play.add(0);
        Players = new ArrayList<>();
        int n = 0;
        for (int i = 0; i < n_players; i++) {
            Players.add(new ArrayList<>());
        }
        for (int i = 0; i < 52 * num; i++) {

            n = i % 12;
            if (i > 51)
                n -= 52;
            Deck.add(n + 1);
        }

    }

    // To check validity of the chosen set of cards
    public boolean[] isValid(int a[], int player) {

        boolean[] validity = { true, true };

        for (int i = 03; i < a.length - 1; i++) {
            if (Players.get(player).get(a[i] - 1) + 1 != Players.get(player).get(a[i + 1] -1))
                validity[0] = false;
            if (Players.get(player).get(a[i] - 1) != Players.get(player).get(a[i + 1] - 1))
                validity[1] = false;
        }
        return validity;
    }

    // Shuffle the deck
    public void shuffleDeck() {
        Collections.shuffle(Deck);
    }

    // Pick a power card, value = 0;
    public int pickPowerCard() {
        Power_Card = Deck.get(0);
        Deck.remove(0);
        return Power_Card;
    }

    int sumHand(int player) {
        int sum = 0, cardval;
        for (int i = 0; i < Players.get(player).size(); i++) {

            cardval = Players.get(player).get(i);
            if (cardval == Power_Card)
                continue;
            cardval = (cardval == 0) ? 0 : cardval + 1;
            if (cardval == Power_Card)
                continue;
            if (cardval > 10) {
                sum += 10; // Value of K,Q,J is 10
                continue;
            }
            sum += cardval;
        }
        return sum;
    }

    // To play a card
    boolean playCard(int cardno[], int player, int pick) {
        for(int i=0;i<cardno.length;i++){
            if(cardno[i]-1>=Players.get(player).size()||cardno[i]<1)
                return false;
        }
        drawcard = true;
        int highest = In_Play.get(In_Play.size() - 1);
        boolean validity[] = isValid(cardno, player);

        if (cardno.length > 1 && !validity[0] && !validity[1])
            return false;

        if ((validity[1] || (cardno.length == 1)) && Players.get(player).get(cardno[cardno.length - 1] - 1) == highest
                && Players.get(player).size() > 1) {
            drawcard = false;
        }

        if (drawcard) {
            if (pick != 0) {
                Players.get(player).add(In_Play.get(pick - 1));
                In_Play.remove(pick - 1);
            } else {
                Players.get(player).add(Deck.get(0));
                Deck.remove(0);
            }

        }

        Played_Cards.addAll(In_Play); // Adding remaining cards In Play of previous round to played cards
        In_Play.clear();

        for (int i = 0; i < cardno.length; i++) {
            In_Play.add(Players.get(player).get(cardno[i] - 1 - i));
            Players.get(player).remove(cardno[i] - 1 - i);
        }
        Collections.sort(In_Play);
        Collections.sort(Players.get(player));

        return true;
    }

    // To distribute cards
    public boolean distribute() {
        int n = Players.size();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 5; j++) {
                Players.get(i).add(Deck.get(0));
                Deck.remove(0);
                shuffleDeck();
            }
            Collections.sort(Players.get(i));
        }

        return true;
    }

    boolean dropCards(int player) {
        win = true;
        int player_score = sumHand(player);
        if (player_score > 5)
            return false;

        // Sum all

        for (int i = 0; i < scores.length; i++) {
            scores[i] = sumHand(i);
            if (player_score >= scores[i] && i != player) {
                win = false;
            }
            Win_Order[i] = i + 1;
        }

        // for(int i = 0 ; i<scores.length-1;i++){
        // int small =i;
        // for(int j=i+1;j<scores.length;j++){
        // if(scores[small]>scores[j]){
        // small=j;
        // }
        // }
        // int temp = scores[small];
        // scores[small] = scores[i];
        // scores[i] = temp;
        // temp = Win_Order[small];
        // Win_Order[small]=Win_Order[i];
        // Win_Order[i]=temp;
        // }
        // for(int i =0;i<scores.length;i++){
        // if(players)

        // }
        return true;

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter number of players:");
        int n = in.nextInt();
        Panchpati ob = new Panchpati(1, n);
        ob.shuffleDeck();
        int turn = 0;
        System.out.println("Distributing Cards:");
        boolean m = ob.distribute();
        int f = 0;
        ArrayList<Integer> cardno_list = new ArrayList<>()       ;//Card no in array list

        System.out.println("Power card : " + (ob.pickPowerCard()-1));
        while (m) {
            turn %= n;

            //Printing Players
            for (int i = 0; i < n; i++) {
                System.out.print("Player " + (i + 1) + " : ");
                for (int j = 0; j < ob.Players.get(i).size(); j++) {
                    System.out.print(ob.Players.get(i).get(j) + ", ");
                }
                System.out.println();

            }
            System.out.println("Player "+(turn+1)+" :\nChoose cards(-1 to finish selection) to drop or 0 to drop hand:" );
            cardno_list.clear();
            for(int i = 0;i<5;i++){
                int d = in.nextInt();
                if(d==-1 && i>0)break;
                if(d==-1 && i==0){i--;continue;}
                cardno_list.add(d);
            }
            int[] cardno = cardno_list.stream().mapToInt(i -> i).toArray();
            int i = 0;

            if (cardno[0] == 0) {
                if (!ob.dropCards(turn)) {

                    System.out.println("Score not below 5, cannot drop hand, pick cards......");
                    continue;
                }
                if (ob.win == false) {
                    System.out.println("Player " + (turn + 1) + "Lost");

                }
                else System.out.println("Player "+(turn + 1) + " Wins!!");
                System.out.println("Results : ");
                for (; i < ob.scores.length; i++) {
                    System.out.println("Player " + ob.Win_Order[i] + " : " + ob.scores[i]);
                }
                break;

            }
            int pick;
            if (f > 0) {
                System.out.println("Choose In Play Card to pick:");
                pick = in.nextInt();
            }
            else
               pick = 0;
            System.out.println(pick);
            boolean played = ob.playCard(cardno, turn, pick);
            if (!played) {
                System.out.println("Invalid Set");
                continue;
            }

            turn++;
            f++;

        }
        in.close();

    }

}
