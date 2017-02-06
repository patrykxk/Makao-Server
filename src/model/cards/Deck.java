package model.cards;

import java.util.Stack;
import java.util.Collections;

 
public class Deck{
  private Stack<Card> deck;
 
  public Stack<Card> getDeck() {
	return deck;
  }

	public Deck (){
	    this.deck = new Stack<Card>();
	    for (int i=0; i<13; i++){
	      CardValue value = CardValue.values()[i];
	      for (int j=0; j<4; j++){
	        Card card = new Card(value, Suit.values()[j]);
	        this.deck.add(card);
	      }
	    }
	    Collections.shuffle(deck);
	  }
  
}