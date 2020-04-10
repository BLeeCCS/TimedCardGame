import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Assig6_1 extends JFrame
{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		new BuildModel();
	}
}


class BuildModel
{

	public static int NUM_PLAYERS = 2;
	   public static int NUM_CARDS_PER_HAND = 7;
	   static int LOW_CARD_INDEX = 0;
	   static Card AI_LOW_CARD = new Card();
	   static int AI_SCORE = 0;
	   static int PLAYER_SCORE = 0;
	   static int numPacksPerDeck = 1;
	   static int numJokersPerPack = 0;
	   static int numUnusedCardsPerPack = 0;
	   static int UNUSED_CARDS_PER_PACK_SIZE = numPacksPerDeck *
               (56 + 4*numJokersPerPack);
	   static Card[] unusedCardsPerPack = new Card[UNUSED_CARDS_PER_PACK_SIZE];
	   static boolean playedIndex[] = new boolean[NUM_CARDS_PER_HAND];
	   
	   // Create an instance of a game
	   static CardGameFramework lowCardGame = new CardGameFramework( 
	         numPacksPerDeck, numJokersPerPack,  
	         numUnusedCardsPerPack, unusedCardsPerPack, 
	         NUM_PLAYERS, NUM_CARDS_PER_HAND);
	   
	   BuildModel()
	   {
		   BuildView buildView = new BuildView();

		   lowCardGame.newGame();
		   // Deal cards to computer and player
		   lowCardGame.deal();
		      
		   AI_LOW_CARD.set(lowCardGame.getHand(0).inspectCard(0).getValue(), 
	       lowCardGame.getHand(0).inspectCard(0).getSuit());
		   lowCardGame.getHand(0).sort();
		   new BuildController(lowCardGame, buildView);
	       
	   }
	   
	   //Determines the next lowest card for the Computer to play
	   //If no low cards are available, selects the highest card
	   public static void generateCard(Hand computerHand, int playerChoice)
	   {
	      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)     
	      {
	         if (Card.lowCard(lowCardGame.getHand(0).inspectCard(i), 
	                          lowCardGame.getHand(1).inspectCard(playerChoice)) 
	                        == false && playedIndex[i] != true)
	         {
	            AI_LOW_CARD.set(lowCardGame.getHand(0).inspectCard(i).getValue(), 
	                            lowCardGame.getHand(0).inspectCard(i).getSuit());
	            LOW_CARD_INDEX = i;
	            break;
	         }
	      }

	      if (playedIndex[LOW_CARD_INDEX] == true)
	      {
	         int i = 0;

	         do 
	         {
	            LOW_CARD_INDEX = i;
	         } while (playedIndex[i++] == true);
	      }

	      playedIndex[LOW_CARD_INDEX] = true;
	   }
	   
}

class BuildView extends JFrame
{

	private static final long serialVersionUID = 1L;
	static CardTable myCardTable = new CardTable("CardTable", BuildModel.NUM_CARDS_PER_HAND, BuildModel.NUM_PLAYERS);
	static JLabel playedCard[] = new JLabel[BuildModel.NUM_PLAYERS];
	static JLabel compHand[] = new JLabel[BuildModel.NUM_CARDS_PER_HAND];
	static JLabel labelTitle[] = new JLabel[BuildModel.NUM_PLAYERS];
	   
	   BuildView()
	   {
		      // Load interface
		      GUICard.loadCardIcons();
		      // Create window
		      myCardTable.setSize(800, 800);
		      myCardTable.setLocationRelativeTo(null);
		      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		      // Create panels 
		      myCardTable.computerHand.setBorder(
		            BorderFactory.createTitledBorder("Computer Hand"));
		      myCardTable.playerHand.setBorder(
		            BorderFactory.createTitledBorder("Your Hand"));
		      myCardTable.playing.setBorder(
		            BorderFactory.createTitledBorder("Playing Area"));
		      myCardTable.playing.setLayout(new GridLayout(3, 2));
		      
		      // Create labels within computerHand panel
		      for (int i = 0; i < BuildModel.NUM_CARDS_PER_HAND; i++)
		      {
		         compHand[i] = new JLabel(GUICard.getBackCardIcon());
		         myCardTable.computerHand.add(compHand[i]);
		      }
		      //Display left and right
		      labelTitle[0] = new JLabel("Computer Score: " + BuildModel.AI_SCORE, JLabel.CENTER);
		      myCardTable.playing.add(labelTitle[0]);

		      labelTitle[1] = new JLabel("Your Score: " + BuildModel.PLAYER_SCORE, JLabel.CENTER);
		      myCardTable.playing.add(labelTitle[1]);

		      //Place holders for playing area
		      playedCard[0] = new JLabel(GUICard.getBackCardIcon());
		      playedCard[1] = new JLabel(GUICard.getBackCardIcon());
		      myCardTable.playing.add(playedCard[0]);
		      myCardTable.playing.add(playedCard[1]);

		      // Show window
		      myCardTable.setVisible(true); 
		      
	   }
	   public static void playCards(Card playerCard, Hand computerHand, int x,
	    		CardGameFramework lowCardGame, CardTable myCardTable )
	    {  
	       //Determines when to wipe the cards clean
	       if(BuildView.playedCard[0] != null)
	       {
	          myCardTable.playing.remove(BuildView.playedCard[0]);
	          myCardTable.playing.remove(BuildView.playedCard[1]);
	          myCardTable.playing.remove(BuildView.labelTitle[0]);
	          myCardTable.playing.remove(BuildView.labelTitle[1]);
	       }

	       // Find lowest AI card.
	       // If none available select the highest card.
	       BuildModel.generateCard(lowCardGame.getHand(0), x);

	       // Remove card from Computer's hand
	       myCardTable.computerHand.remove(BuildView.compHand[BuildModel.LOW_CARD_INDEX]);

	       // Who won the round?
	       // true for player wins; false for player loss.
	       if(Card.lowCard(lowCardGame.getHand(0).inspectCard(BuildModel.LOW_CARD_INDEX), 
	                       lowCardGame.getHand(1).inspectCard(x)))   
	       {
	          BuildModel.PLAYER_SCORE++;
	       }
	       else
	       {
	          BuildModel.AI_SCORE++;
	       }

	       // Updates scores to player titles
	       BuildView.labelTitle[0] = new JLabel("Computer Score: " + BuildModel.AI_SCORE, JLabel.CENTER);
	       myCardTable.playing.add(BuildView.labelTitle[0]);

	       BuildView.labelTitle[1] = new JLabel("Your Score: " +  BuildModel.PLAYER_SCORE, JLabel.CENTER);
	       myCardTable.playing.add(BuildView.labelTitle[1]);

	       //Display new cards
	       BuildView.playedCard[0] = new JLabel(GUICard.getIcon(
	                                  lowCardGame.getHand(0).inspectCard(
	                                  BuildModel.LOW_CARD_INDEX)));
	       myCardTable.playing.add(BuildView.playedCard[0]);

	       BuildView.playedCard[1] = new JLabel(GUICard.getIcon(
	                                  lowCardGame.getHand(1).inspectCard(x)));
	       myCardTable.playing.add(BuildView.playedCard[1]);

	       // if all cards have been used then display the winner.
	       if (BuildModel.playedIndex[0] == true && 
	    	   BuildModel.playedIndex[1] == true && 
  			   BuildModel. playedIndex[2] == true && 
  			   BuildModel.playedIndex[3] == true && 
  			   BuildModel.playedIndex[4] == true && 
  			   BuildModel.playedIndex[5] == true && 
  			   BuildModel.playedIndex[6] == true)
	       {
	          myCardTable.playing.remove(BuildView.playedCard[0]);
	          myCardTable.playing.remove(BuildView.playedCard[1]);

	          JLabel winStatement;

	          if (BuildModel.PLAYER_SCORE > BuildModel.AI_SCORE)
	          {
	             winStatement = new JLabel("YOU WON!!!!!", JLabel.CENTER);
	             myCardTable.playing.add(winStatement);
	          }
	          else if (BuildModel.PLAYER_SCORE < BuildModel.AI_SCORE)
	          {
	             winStatement = new JLabel("YOU LOST!!!!!", JLabel.CENTER);
	             myCardTable.playing.add(winStatement);
	          }
	          else
	          {
	             winStatement = new JLabel("DRAW!!!!!", JLabel.CENTER);
	          }
	       }
	    }
   
}
 
class BuildController
{
	static JButton[] humanCardButtons = new JButton[BuildModel.NUM_CARDS_PER_HAND];
    // Create buttons within playerHand panel
    int playerNum = 1;
    
    BuildController(CardGameFramework lowCardGame, BuildView buildView)
    {
    for (int i = 0; i < BuildModel.NUM_CARDS_PER_HAND; i++)
    {
       humanCardButtons[i] = new JButton("", GUICard.getIcon(
       lowCardGame.getHand(playerNum).inspectCard(i)));
       BuildView.myCardTable.playerHand.add(humanCardButtons[i]);
    }
    for (int k = 0; k < BuildModel.NUM_CARDS_PER_HAND; k++)
    {
       humanCardButtons[k].addActionListener(new ActionListener()
       {  
          public void actionPerformed(ActionEvent e)
          {
             BuildView.myCardTable.playerHand.remove((JButton) e.getSource());
             loop:
                for (int x = 0; x < BuildModel.NUM_CARDS_PER_HAND; x++)
                {
                   if ((JButton) e.getSource() == humanCardButtons[x])
                   {
                      BuildView.playCards(lowCardGame.getHand(playerNum).inspectCard(x), 
                                lowCardGame.getHand(0), x, lowCardGame, BuildView.myCardTable);
                      break loop;
                   }
                }
             //Refresh hand to remove buttons
             BuildView.myCardTable.playerHand.setVisible(false);
             BuildView.myCardTable.playerHand.setVisible(true);
          }
       });
    };
    }

}


//This class will control the positioning of the panels and cards of the GUI. 
class CardTable extends JFrame {

   // need this for extending JFrame so we dont get a warning.
   private static final long serialVersionUID = 1L;

   // Class variables: 
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;

   // Instance Variables: 
   private int numCardsPerHand;
   private int numPlayers;

   public JPanel playerHand, computerHand, playing;

   // Constructor: filters input, adds any panels to the JFrame, 
   // and establishes layouts
   public CardTable(String title, int numCardsPerHand, int numPlayers)  
   {

      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;

      // Creating new JPanels and using gridLayout to place components 
      // in a grid of cells : (row, col, hor gap, vert gap)
      playerHand = new JPanel(new GridLayout(1,1,10,10));
      computerHand = new JPanel(new GridLayout(1,1,10,10));
      playing = new JPanel (new GridLayout (2,2,20,20));

      // Setting layout manager: 
      setLayout (new BorderLayout(10,10));

      // Adding components: 
      add(computerHand,BorderLayout.NORTH);
      add(playing,BorderLayout.CENTER);
      add(playerHand,BorderLayout.SOUTH);

      // Adding arbitrary borders with a String titles: 
      playerHand.setBorder(new TitledBorder("Player"));
      computerHand.setBorder(new TitledBorder("Computer"));
      playing.setBorder(new TitledBorder("Playing"));

   }

   // ACCESSORS ---------------------------------------------------------------
   public int getNumPlayers() 
   {
      return numPlayers;
   }

   public int getNumCardsPerHand() 
   {
      return numCardsPerHand;
   }
}

class GUICard
{
   // Holds values for icons
   private static Icon[][] iconCards = new ImageIcon[14][4];  
   // Holds value for back of card icon
   private static Icon iconBack;     
   static public void loadCardIcons()
   {
      iconBack = new ImageIcon("images/BK.gif");

      for(int col = 0; col < 14; col++)
      {
         for(int row = 0; row < 4; row++)
         {
            iconCards[col][row] = new ImageIcon("images/" + 
                                          getStrValue(col) + 
                                          getCharSuit(row) + 
                                          ".gif");
         }
      }
   }

   static private String getStrValue(int col)
   {
      switch (col)
      {
      case 0: return "A";
      case 1: return "2";
      case 2: return "3";
      case 3: return "4";
      case 4: return "5";
      case 5: return "6";
      case 6: return "7";
      case 7: return "8";
      case 8: return "9";
      case 9: return "T";
      case 10: return "J";
      case 11: return "Q";
      case 12: return "K";
      case 13: return "X";
      default : return "A";
      }
   }

   static private char getCharSuit(int row)
   {
      switch(row)
      {
      case 0: return 'C';
      case 1: return 'D';
      case 2: return 'H';
      case 3: return 'S';
      default: return 'S';
      }
   }
   static public Icon getIcon(Card card) 
   {
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   static public Icon getBackCardIcon() 
   {
      return iconBack;
   }

   static public int valueAsInt(Card card)
   {
      char val = card.getValue();

      switch(val)
      {
      case 'A': return 0;
      case '2': return 1;
      case '3': return 2;
      case '4': return 3;
      case '5': return 4;
      case '6': return 5;
      case '7': return 6;
      case '8': return 7;
      case '9': return 8;
      case 'T': return 9;
      case 'J': return 10;
      case 'Q': return 11;
      case 'K': return 12;
      case 'X': return 13;
      default: return 0;   
      }
   }

   static public int suitAsInt(Card card)
   {
      int posVal = 0; 

      String suit = card.getSuit().toString();

      if(suit == "CLUBS"){ posVal = 0; }
      else if (suit == "DIAMONDS"){ posVal = 1; }
      else if (suit == "HEARTS"){ posVal = 2; }
      else if (suit == "SPADES"){ posVal = 3; }

      return posVal;
   }
}
//End GUICard Class 

// ///////////////// //
// CARDGAMEFRAMEWORK //
// ///////////////// //
class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;            // # standard 56-card packs per deck
   // ignoring jokers or unused cards
   private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack;  // # cards removed from each pack
   private int numCardsPerHand;        // # cards to deal each player
   private Deck deck;               // holds the initial full deck and gets
   // smaller (usually) during play
   private Hand[] hand;             // one Hand for each player
   private Card[] unusedCardsPerPack;   // an array holding the cards not used
   // in the game.  e.g. pinochle does not
   // use cards 2-8 of any suit

   public CardGameFramework( int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if  (numCardsPerHand < 1 ||
            numCardsPerHand >  numPacks * (56 - numUnusedCardsPerPack)
            / numPlayers )
         numCardsPerHand = numPacks * (56 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameFramework()
   {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck() { return deck.dealCard(); }

   public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard( unusedCardsPerPack[k] );

      // add jokers
      for (k = 0; k < numPacks; k++)
         for ( j = 0; j < numJokersPerPack; j++)
            deck.addCard( new Card('X', Card.Suit.values()[j]) );

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards ; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard( deck.dealCard() );
            else
            {
               enoughCards = false;
               break;
            }
      }
      return enoughCards;

   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
      {
    	 System.out.println(hand[0]);
         hand[k].sort();
      }
   }

   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
            cardIndex < 0 || cardIndex > numCardsPerHand - 1)
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);      
      }

      // return the card played
      return hand[playerIndex].playCard(cardIndex); //(()cardIndex);

   }


   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hand[playerIndex].takeCard(deck.dealCard());
   }
}

/***************************************************************************
 *                        Code From Previous Assignments                   *         
 ***************************************************************************/

class Card
{
   public static enum Suit {CLUBS, DIAMONDS, HEARTS, SPADES};
   public static final String[] SUIT_STRING = {"Clubs", "Diamonds",
         "Hearts", "Spades"};

   // Added Joker as "X"
   public static final char[] VALUES = {'A', '2', '3', '4', '5', '6', '7',
         '8', '9', 'T', 'J', 'Q', 'K', 'X'};                                   
   private char value;
   private Suit suit;
   private boolean errorFlag;

   // the array to put the order of the card values in here 
   // with the smallest first, include 'X' for a joker
   public static char[] valuRanks = {'A', '2', '3', '4', '5', '6', '7', '8', 
         '9', 'T', 'J', 'K', 'Q', 'K', 'X'};

   public Card(char val, Suit s)
   {
      set(val, s);
   }

   public Card()
   {
      value = 0;
      suit = Suit.CLUBS;
      errorFlag = true;
   }

   public String toString()
   {
      if(errorFlag)
      {
         return "invalid";
      }
      else
      {
         String retString = "";
         switch(suit)
         {
         case CLUBS:
            retString = value + " of Clubs";
            break;
         case DIAMONDS:
            retString = value + " of Diamonds";
            break;
         case SPADES:
            retString = value + " of Spades";
            break;
         case HEARTS:
            retString = value + " of Hearts";
            break;
         default: 
            retString = "Unknown suit type";
         }
         return retString;
      }
   }

   public static boolean lowCard( Card aiCard, Card playerCard )
   {
      boolean playerWon = false;

      int playerCardValue = -1;
      int playerCardSuit = -1;
      int aiCardValue = -1;
      int aiCardSuit = -1;

      for(int i = 0; i < 14; i++)
      {
         if(aiCard.getValue() == Card.valuRanks[i])
            aiCardValue = i;

         if(playerCard.getValue() == Card.valuRanks[i])
            playerCardValue = i;
      }

      if( aiCardValue > playerCardValue)
      {
         playerWon = true;
      }
      else if (aiCardValue <= playerCardValue)
      {
         playerWon = false;
      }
      else if (aiCardValue == playerCardValue)
      {
         for(int j = 0; j<4; j++)
         {
            if(aiCard.getSuit() == Card.Suit.values()[j])
               aiCardSuit = j;
            if(playerCard.getSuit() == Card.Suit.values()[j])
               playerCardSuit = j;
         }
         if(aiCardSuit <= playerCardSuit)
         {
            playerWon = false;
         }
         else
         {
            playerWon = true;
         }
      }
      return playerWon;
   }

   public boolean set(char val, Suit s)
   {

      if(isValid(val, s))
      {
         value = val;
         suit = s;
         errorFlag = false;
         return true; 
      }
      else
      {
         errorFlag = true;
         return false;
      }
   }

   public char getValue() { return value; }
   public Suit getSuit() { return suit; }
   public boolean getErrorFlag() { return errorFlag; }

   public boolean equals(Card card)
   {
      char otherValue = card.getValue();
      Suit otherSuit = card.getSuit();
      boolean otherError = card.getErrorFlag();

      if(value == otherValue && 
         suit == otherSuit && 
         otherError == errorFlag)
         return true;
      return false;
   }

   private boolean isValid(char value, Suit suit)
   {
      boolean found = false;

      for (char c : VALUES)
      {
         if(value == c)
            found = true;
      }

      if(!found)
      {
         return false;
      }

      if(suit != Suit.CLUBS && 
            suit != Suit.DIAMONDS && 
            suit != Suit.SPADES && 
            suit != Suit.HEARTS)
      {
         return false;
      }

      return true;
   }

   // This method will sort the incoming array of cards 
   // using a bubble sort routine.
   static void arraySort(Card[] cardArray, int arraySize)
   {
      for (int i = 0; i < arraySize - 1; i++)
      {
         for (int j = 0; j < arraySize - i - 1; j++)
         {
            if (cardArray[j].getValue() > cardArray[j+1].getValue())
            {
               char tempVal = cardArray[j].getValue();

               cardArray[j].set(cardArray[j+1].getValue(),cardArray[j+1].getSuit());
               cardArray[j+1].set(tempVal, cardArray[j].getSuit());
            }
         }
      }
   }

   // Returns value of card
   public int valueOfCard(Card card)
   {
      for (int i = 0; i < valuRanks.length; i++)
      {
         if (card.getValue() == valuRanks[i])
            return i;
      }
      return -1;
   }
}


class Hand
{
   public static int MAX_CARDS;
   private Card[] myCards;
   private int myCardsIndex;

   Hand()
   {
      MAX_CARDS = 100;
      myCardsIndex = -1;
      myCards = new Card[MAX_CARDS];
   }

   void resetHand()
   {
      myCardsIndex = -1;
   }

   boolean takeCard(Card card)
   {
      if(card.getErrorFlag())
         return false;
      if (!isFull())
      {
         myCardsIndex++;
         myCards[myCardsIndex] = card;

         return true;
      }
      return false;
   }

   Card playCard(int index)
   {
      if(index < 0 || index > myCardsIndex)
      {
         Card badCard = new Card('W', Card.Suit.SPADES);
         return badCard;
      }

      System.out.println(this.toString());

      Card retCard = new Card(myCards[index].getValue(), 
                              myCards[index].getSuit());

      for(int i = index; i < myCardsIndex; i++)
      {
         myCards[i] = myCards[i+1];
      }

      System.out.println(this.toString());
      
      myCardsIndex--;

      return retCard;
   }

   public String toString()
   {
      String cardsInHandStr = "";
      for (Card i : myCards)
      {
         if(i != null)
         {
            cardsInHandStr += i.toString() + ", ";
         }
         else
         {
            return cardsInHandStr;
         }
      }   
      return cardsInHandStr;
   }

   public int getNumCards()
   {
      return myCardsIndex + 1;
   }

   Card inspectCard(int k)
   {
      if (k > myCardsIndex) 
      {  
         Card tempCard = new Card();
         tempCard.set('z', null); 
         return tempCard;
      }
      return myCards[k];
   }

   private boolean isFull()
   {
      return myCardsIndex == MAX_CARDS;
   }

   // This method will sort the hand 
   // by calling the arraySort() method in the Card class.
   public void sort()
   {
      Card.arraySort(myCards, myCardsIndex);
   }

   /* In order for playCard() to work in the cardGameFramework class, 
      add the following to the Hand class. 
      This will remove the card at a location 
      and slide all of the cards down one spot in the myCards array.  
      (We will use this next week) */
   //   public Card playCard(int cardIndex)
   //   {
   //      if ( numCards == 0 ) //error
   //      {
   //         //Creates a card that does not work
   //         return new Card('M', Card.Suit.spades);
   //      }
   //      //Decreases numCards.
   //      Card card = myCards[cardIndex];
   //      
   //      numCards--;
   //      for(int i = cardIndex; i < numCards; i++)
   //      {
   //         myCards[i] = myCards[i+1];
   //      }
   //      
   //      myCards[numCards] = null;
   //      
   //      return card;
   //    }
}

class Deck
{
   public final int MAX_CARDS = 6*56;
   private static Card[] masterPack = new Card[56];
   private Card[] cards;
   private int topCard;    

   Deck()
   {     
      allocateMasterPack();
      int numCards = 56;
      topCard = numCards - 1;
      cards = new Card[numCards];
      for(int i = 0; i < numCards; i++)
      {
         cards[i] = masterPack[i];
      }
   }

   Deck(int numPacks)
   {     
      allocateMasterPack();
      int numCards = numPacks * 56;
      topCard = numCards - 1;
      cards = new Card[numCards];

      for(int i = 0; i < numPacks; i++)
      {
         for(int j = 0; j < 56; j++)
         {
            cards[56*i+j] = masterPack[j];
         }
      }
   }

   public void init(int numPacks)
   {
      if (numPacks > 6)
      {
         numPacks = 6;
      }
      int numOfCards = numPacks * 56;
      topCard = numOfCards - 1;

      for (int i = 0; i < numPacks; i++)
      {
         int k = i * 56;
         for (int j = 0; j < 56; j++)
         {
            cards[k] = masterPack[j];
            k++;
         }
      }
   }

   public void shuffle() 
   {
      int numOfCards = cards.length;
      for (int i = 0; i < numOfCards; i++)
      {
         int random = i + (int)(Math.random() * (numOfCards - i));
         Card temp = cards[random];
         cards[random] = cards[i];
         cards[i] = temp;
      }
   }

   public Card dealCard() 
   {
      if(topCard < 0)
      {
         Card empty = new Card();
         return empty;
      }

      Card tempCard = new Card();
      tempCard = cards[topCard];
      cards[topCard] = null;
      topCard--;
      return tempCard;
   }

   public final Card topCardAccessor() 
   {
      if(topCard < 0)
      {
         Card empty = new Card();
         return empty;
      }
      return cards[topCard];
   }

   public final Card inspectCard(int k) 
   {
      int numOfCards = cards.length;
      if (k > numOfCards | --k < 0)
      {
         Card empty = new Card();
         return empty;
      }
      return cards[k];
   }

   public static void allocateMasterPack()
   {
      if(masterPack[0] != null)
         return;
      Card.Suit[] placeHolder = Card.Suit.values();
      int k = 0;
      for(int i = 0;i < 4; i++)
      {
         for(int j = 0; j < 14; j++)
         {
            masterPack[k] = new Card(Card.VALUES[j], placeHolder[i]);
            k++;
         }
      }
   }

   // This method makes sure that there are not too many instances of the 
   // card in the deck if you add it.  
   // Return false if there will be too many.  It should put the card 
   // on the top of the deck.
   boolean addCard(Card card)
   {
      if (topCard == MAX_CARDS)
         return false;

      cards[topCard + 1] = card;

      return true;
   }

   // This method removes a specific card from the deck.
   // Put the current top card into its place.
   // Be sure the card you need is actually still in the deck, 
   // if not return false.
   boolean removeCard(Card card)
   {
      for (int i = 0; i < cards.length; i++)
      {
         if (cards[i].equals(card)) 
         {
            cards[i] = cards[topCard];
            cards[topCard] = card;
            return true;
         }
      }

      return false;
   }

   // This method put all of the cards in the deck back into the 
   // right order according to their values.
   public void sort()
   {
      Card.arraySort(cards, cards.length);
   }

   // This method returns the number of cards remaining in the deck.
   public final int getNumCards()
   {
      return cards.length;
   }
}


