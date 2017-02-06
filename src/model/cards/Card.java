package model.cards;

import java.io.Serializable;

public class Card implements Serializable {
	
	private static final long serialVersionUID = 4659238855265416272L;

	private boolean isClickable = false;
	private Suit suit;
	private CardValue cardValue;
	 
	public Card (CardValue cardValue, Suit suit){
		this.cardValue = cardValue;
		this.suit = suit;
	}
	 
	public Suit getSuit(){
		return suit;
	}
	 
	public void setSuit(Suit suit){
		this.suit = suit;
	}
	 
	public CardValue getCardValue(){
		return cardValue;
	}
	 
	public void setCardValue(CardValue cardValue){
		this.cardValue = cardValue;
	}
	public boolean getClickable() {
		return isClickable;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}
}
