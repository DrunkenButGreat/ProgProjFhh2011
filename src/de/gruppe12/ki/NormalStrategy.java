package de.gruppe12.ki;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.gruppe12.shared.*;


/**
* Die Klasse NormalStrategy ist die normale Spielstrategie <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 0.5.0 28.11.2011
* �nderungen: 28.11. erste implementierung von calcMoves und calcValue
*/

public class NormalStrategy implements MoveStrategy {
	private int grpNr;
	private String name;
	//private Node<Move> verlauf;
	private Board b;	
	
	public NormalStrategy(){
		grpNr = 12;
		name = "normal";
	}
	
	public int getGroupNr() {
		return grpNr;
	}

	public String getStrategyName() {
		return name;
	}

	//ein erster Versuch, noch nicht ganz der MinMax Algo. M�sste aber f�rs Erste reichen.
	public Move calculateDefenderMove(Move lastMove, int thinktimeInSeconds) {
		//Speichern in Verlaufbaum, muss noch durchdacht werden
		//verlauf.setLeft(new Node<Move>(lastMove));
		
		//Abbruchbedingung Zeit muss eingef�gt werden
		//if(time>= thinktimeInSeconds){
		//zeit abgelaufen -> Spiel vorbei ?
		//} else {
		//Hauptcode zur Berechnung n�chster Schritt
	    Move[] moves = GenerateMoves(BoardContent.DEFENDER);
	    Move move;
	    Move best_move=moves[0];
	    Move last_bm=best_move;
	    for(int i=1; i<=moves.length; i++) {
	       move = moves[i];
	       if (calculateValue(move) > calculateValue(best_move)) {
	          last_bm = best_move;
	    	   best_move = move;
	       }
	    }
	    if(best_move!=lastMove){
	    return best_move;
	    } else { 
	    	return last_bm;
	    }
		//}	
		}
	

	
	public Move calculateAttackerMove(Move lastMove, int thinktimeInSeconds) {
		//Speichern in Verlaufbaum, muss noch durchdacht werden
		//verlauf.setRight(new Node<Move>(lastMove));
		
		//Abbruchbedingung Zeit muss eingef�gt werden
		//if(time>= thinktimeInSeconds){
		//zeit abgelaufen -> Spiel vorbei ?
		//} else {
		//Hauptcode zur Berechnung n�chster Schritt
	    Move[] moves = GenerateMoves(BoardContent.ATTACKER);
	    Move move;
	    Move best_move=moves[0];
	    Move last_bm=best_move;
	    for(int i=1; i<=moves.length; i++) {
	       move = moves[i];
	       if (calculateValue(move) > calculateValue(best_move)) {
	          last_bm = best_move;
	    	   best_move = move;
	       }
	    }
	    if(best_move!=lastMove){
	    return best_move;
	    } else { 
	    	return last_bm;
	    }
		//}	
		}
	
	
	/**
	 * 
	 * GenerateMoves()
	 * 
	 * @return ein Array mit allen m�glichen Moves
	 */
	private Move[] GenerateMoves(BoardContent type) {
		Move[] mList = new Move[500]; 
		Cell[] cList = new Cell[24];
		Cell c = null;
		int m1=0;
		int c1=0;
		
		//pr�ft wo Steine sind
		for(int i=0;i<=13;i++){
			for(int j=0;j<=13;j++){
				//speichert nur wenn Typ gleich ist
				if(b.get(i, j) == type)
				cList[c1]=new Cell(i,j, type);
				c1++;
			}
		}
		for(int i=0;i<=cList.length;i++){
			int r=13-cList[i].getCol();
			int l=13-cList[i].getRow();
			for(int j=0;j<=r;j++){
				mList[m1]=new Move(cList[i],new Cell(cList[i].getCol()+1,cList[i].getRow(), cList[i].getContent()));
				m1++;
			}
			for(int h=0;h<=l;h++){
				mList[m1]=new Move(cList[i],new Cell(cList[i].getCol(),cList[i].getRow()+1, cList[i].getContent()));
				m1++;		
			}
		}
		
		return mList;
	}

	/**
	 * calculateValue
	 * 
	 * @return Bewertung des Moves
	 */
	   private int calculateValue(Move m) {
		      int value=0;
		      //Figur die Bewegt wird
		      BoardContent p = m.getFromCell().getContent();
		      //Felder in umgebung
		      BoardContent c1 = b.get(m.getToCell().getCol()+1,m.getToCell().getRow());
			  BoardContent c2 = b.get(m.getToCell().getCol()+2,m.getToCell().getRow());
			  BoardContent c3 = b.get(m.getToCell().getCol(),m.getToCell().getRow()+1);
			  BoardContent c4 = b.get(m.getToCell().getCol(),m.getToCell().getRow()+2);
		      
			  
			  //testet, wenn Gegner und kein leeres Feld steigt Value
		      if((c1 != p)&&(c1!=BoardContent.EMPTY)){
		    	  value++;
		      }
		      if((c2 != p)&&(c1!=BoardContent.EMPTY)){
		    	  value++;
		      }
		      if((c3 != p)&&(c1!=BoardContent.EMPTY)){
		    	  value++;
		      }
		      if((c4 != p)&&(c1!=BoardContent.EMPTY)){
		    	  value++;
		      }
		      //checkt ob Move zul�ssig
		      if(MoveCheck.check(m,b)){
		    	  return -1;
		      }
		      return value;
		   }
	
	public String toString(){
	return name+" Strategy, Gruppe"+grpNr;	
	}

//	//Methoden m�ssen importiert werden, vlt in vorgaben l�schen ?
//	@Override
//	public de.fhhannover.inform.hnefatafl.vorgaben.Move calculateDefenderMove(
//			de.fhhannover.inform.hnefatafl.vorgaben.Move lastMove,
//			int thinktimeInSeconds) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public de.fhhannover.inform.hnefatafl.vorgaben.Move calculateAttackerMove(
//			de.fhhannover.inform.hnefatafl.vorgaben.Move lastMove,
//			int thinktimeInSeconds) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}