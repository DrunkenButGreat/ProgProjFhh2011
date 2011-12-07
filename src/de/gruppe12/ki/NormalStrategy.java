package de.gruppe12.ki;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;
import de.gruppe12.shared.*;


/**
* Die Klasse NormalStrategy ist die normale Spielstrategie <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 0.6.0 03.12.2011
* ï¿½nderungen: 03.12. Fehler beseitigt, javadoc erweitert
*/

public class NormalStrategy implements MoveStrategy {
	private int grpNr;
	private String name;
	//private Node<Move> verlauf;
	private Board b;
	
	public NormalStrategy(){
		grpNr = 12;
		name = "normal"; 
		b = new Board();
	}
	
	@Override
	public int getGroupNr() {
		return grpNr;
	}

	@Override
	public String getStrategyName() {
		return name;
	}	
	
	/**
	 * 
	 * GenerateMoves()
	 * erzeutgt ein Array mit den nï¿½chsten mï¿½glichen Schritten
	 * jeder Spielfigur 
	 * 
	 * @return ein Array mit allen mï¿½glichen Moves
	 */
	private Move[] GenerateMoves(BoardContent type) {
		Move[] mList = new Move[500]; 
		Cell[] cList = new Cell[24];
		int m1=0;
		int c1=0;
		
		//prÃ¼ft wo Steine sind
		for(int i=0;i<13;i++){
			for(int j=0;j<13;j++){
				//speichert nur wenn Typ gleich ist
				if(b.getCell(i,j).getContent() == type){
					cList[c1]=new Cell(i,j, type);
					c1++;
				}
			}
		}
		for(int i=0;i<c1;i++){
			int r=12-cList[i].getCol();
			int l=12-cList[i].getRow();
			for(int j=0;j<r;j++){
				mList[m1]=new Move(cList[i],new Cell(cList[i].getCol()+j,cList[i].getRow(), cList[i].getContent()));
				m1++;
			}
			for(int h=0;h<l;h++){
				mList[m1]=new Move(cList[i],new Cell(cList[i].getCol(),cList[i].getRow()+h, cList[i].getContent()));
				m1++;		
			}
		}		
		return mList;
	}

	/**
	 * calculateValue
	 * Bewertet den ï¿½bergebenen Move.
	 * 
	 * @return Bewertung des Moves
	 */
	   private int calculateValue(Move m, boolean isDefTurn) {
		   	 
		   	 if (m == null){
		   		 return -1;
		   	 }
		   	 //checkt ob Move zulï¿½ssig
		      if(!MoveCheck.check(m,b,isDefTurn)){
		    	  return -1;
		      } 
		   
		      int value=0;
		      //Figur die Bewegt wird
		      BoardContent p = m.getFromCell().getContent();
		      //Felder in umgebung
			  BoardContent c1 = b.getCell(m.getToCell().getCol()+1,m.getToCell().getRow()).getContent();
			  BoardContent c2 = b.getCell(m.getToCell().getCol()+2,m.getToCell().getRow()).getContent();
			  BoardContent c3 = b.getCell(m.getToCell().getCol(),m.getToCell().getRow()+1).getContent();
			  BoardContent c4 = b.getCell(m.getToCell().getCol(),m.getToCell().getRow()+2).getContent();
		      
			  
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
		      
		      
		      //extra Berechnungen beginnen hier:
		      int row=m.getToCell().getRow();
		      int col=m.getToCell().getCol();
		      
		      //extra Berechnung für Attacker
		      //   1  2  3  4  5  6  7  8  9  10 11 12 13
		      // 1|xx|10|08|xx|xx|xx|xx|xx|xx|xx|08|10|xx
		      // 2|10|10|08|xx|xx|xx|xx|xx|xx|xx|08|10|10
		      // 3|08|08|08|xx|xx|xx|xx|xx|xx|xx|08|08|08
		      // 4|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 5|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 6|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 7|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 8|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 9|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      //10|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      //11|08|08|08|xx|xx|xx|xx|xx|xx|xx|08|08|08
		      //12|10|10|08|xx|xx|xx|xx|xx|xx|xx|08|10|10
		      //13|xx|10|08|xx|xx|xx|xx|xx|xx|xx|08|10|xx
		      
		      if(p==BoardContent.ATTACKER){
		    	  //Bewertung 10
		    	  if(((row==2)&&(col==2))|
						     ((row==2)&&(col==3))|
						     ((row==1)&&(col==2))|
						     ((row==12)&&(col==1))|
						     ((row==13)&&(col==2))|
						     ((row==1)&&(col==12))|
						     ((row==2)&&(col==12))|
						     ((row==2)&&(col==13))|
						     ((row==12)&&(col==13))|
						     ((row==12)&&(col==12))|
						     ((row==13)&&(col==12)))
						    {
						    		  value=value+10;	  
						    }
		    	  //Bewertung 8 
		    	  if(((row==1)&&(col==3))|
		           			 ((row==2)&&(col==3))|
		           			 ((row==3)&&(col==3))|	    	  
		           			 ((row==3)&&(col==1))|
		           			 ((row==3)&&(col==2))|
		           			 ((row==11)&&(col==1))|
		           			 ((row==11)&&(col==2))|
		           			 ((row==11)&&(col==3))|		    	  
		           			 ((row==12)&&(col==3))|		    	  
		           			 ((row==13)&&(col==3))|		    	  
		           			 ((row==1)&&(col==1))|		    	  
		           			 ((row==2)&&(col==2))|		    	  
		           			 ((row==3)&&(col==3))|
		           			 ((row==3)&&(col==12))|          			 
		           			 ((row==3)&&(col==13))|		    	  
		           			 ((row==11)&&(col==11))|		    	  
		           			 ((row==11)&&(col==12))|		    	  
		           			 ((row==11)&&(col==13))|		    	  
		           			 ((row==12)&&(col==11))|		    	  
		           			 ((row==13)&&(col==11))
		           			 ){
				    		  value=value+8;
				    	  }
		    	  
		    	  //Wenn König in der Nähe, dann ziehe dahin!
		    	  if((c1 ==BoardContent.KING)){
			    	  value=value+10;
			      }
		    	  if((c1 ==BoardContent.KING)){
			    	  value=value+10;
			      }
		    	  if((c1 ==BoardContent.KING)){
			    	  value=value+10;
			      }
		    	  if((c1 ==BoardContent.KING)){
			    	  value=value+10;
			      }
		    	  
		      }
		      
		      
		      
		      
		      
		      //extra Berechnung für König
		      //   1  2  3  4  5  6  7  8  9  10 11 12 13
		      // 1|10|08|06|04|03|02|01|02|03|04|06|08|10
		      // 2|08|08|06|04|03|02|01|02|03|04|06|08|08
		      // 3|06|06|06|04|03|02|01|02|03|04|06|06|06
		      // 4|04|04|04|04|03|02|01|02|03|04|04|04|04
		      // 5|03|03|03|03|03|02|01|02|03|03|03|03|03
		      // 6|02|02|02|02|02|02|01|02|02|02|02|02|02
		      // 7|01|01|01|01|01|01|01|01|01|01|01|01|01
		      // 8|02|02|02|02|02|02|01|02|02|02|02|02|02
		      // 9|03|03|03|03|03|02|01|02|03|03|03|03|03
		      //10|04|04|04|04|03|02|01|02|03|04|04|04|04
		      //11|06|06|06|04|03|02|01|02|03|04|06|06|06
		      //12|08|08|06|04|03|02|01|02|03|04|06|08|08
		      //13|10|08|06|04|03|02|01|02|03|04|06|08|10
		       
		      if(p==BoardContent.KING){
		    	  //Felder mit Bewertung 10: LO:1,1 RO:1,13 LU:13,1 RU:13,13
		    	  if(((row==1)&&(col==1))|
		    		 ((row==1)&&(col==13))|
		    		 ((row==13)&&(col==1))|
		    		 ((row==13)&&(col==13))
		    		 ){
		    		  value=value+10;
		    	  }
		    	  //Felder mit Bewertung 8: LO:2,2;2,3;1,2 LU:12,1;12,2;13,2 RO:1,12;2,12;2,13 RU:12,13;12,12;13,12
		    	  if(((row==2)&&(col==2))|
				     ((row==2)&&(col==3))|
				     ((row==1)&&(col==2))|
				     ((row==12)&&(col==1))|
				     ((row==13)&&(col==2))|
				     ((row==1)&&(col==12))|
				     ((row==2)&&(col==12))|
				     ((row==2)&&(col==13))|
				     ((row==12)&&(col==13))|
				     ((row==12)&&(col==12))|
				     ((row==13)&&(col==12)))
				    {
				    		  value=value+8;
				    	  }
		    	//Felder mit Bewertung 6: LO:1,3;2,3;3,3;3,1;3,2 LU:11,1;11,2;11,3;12,3;13,3 RO:1,11;2,11,3;3,12;3,13 RU:11,11;11,12;11,13;12,11;13,11 
		    	  if(((row==1)&&(col==3))|
           			 ((row==2)&&(col==3))|
           			 ((row==3)&&(col==3))|	    	  
           			 ((row==3)&&(col==1))|
           			 ((row==3)&&(col==2))|
           			 ((row==11)&&(col==1))|
           			 ((row==11)&&(col==2))|
           			 ((row==11)&&(col==3))|		    	  
           			 ((row==12)&&(col==3))|		    	  
           			 ((row==13)&&(col==3))|		    	  
           			 ((row==1)&&(col==1))|		    	  
           			 ((row==2)&&(col==2))|		    	  
           			 ((row==3)&&(col==3))|
           			 ((row==3)&&(col==12))|          			 
           			 ((row==3)&&(col==13))|		    	  
           			 ((row==11)&&(col==11))|		    	  
           			 ((row==11)&&(col==12))|		    	  
           			 ((row==11)&&(col==13))|		    	  
           			 ((row==12)&&(col==11))|		    	  
           			 ((row==13)&&(col==11))
           			 ){
		    		  value=value+6;
		    	  }
		    	  //Felder mit Bewertung 4: LO:4,1;4,2;4,3;4,4;3,4;2,4;1,4 LU:10,1;10,2;10,3;10,4;11,4;12,4;13,4 
			      // RO:1,10;2,10;3,10;4,10;4,11;4,12;4,13 RU:10,10;10,11;10,12;10,13;11,10;12,10;13,10 
		    	  if(((row==4)&&(col==1))|
			         ((row==4)&&(col==2))|
			         ((row==4)&&(col==3))|
			         ((row==4)&&(col==4))|
			         ((row==3)&&(col==4))|
			         ((row==2)&&(col==4))|
			         ((row==1)&&(col==4))|
			         ((row==10)&&(col==1))|
			         ((row==10)&&(col==2))|
			         ((row==10)&&(col==3))|
			         ((row==10)&&(col==4))|
			         ((row==11)&&(col==4))|
			         ((row==12)&&(col==4))|
			         ((row==13)&&(col==4))|
			         ((row==1)&&(col==10))|
			         ((row==2)&&(col==10))|
			         ((row==3)&&(col==10))|
			         ((row==4)&&(col==10))|
			         ((row==4)&&(col==11))|
			         ((row==4)&&(col==12))|
			         ((row==4)&&(col==13))|
			         ((row==10)&&(col==10))|
			         ((row==11)&&(col==10))|
			         ((row==12)&&(col==10))|
			         ((row==13)&&(col==10))|
			         ((row==10)&&(col==11))|
			         ((row==10)&&(col==12))|
			         ((row==10)&&(col==13))
				     ){
		    		  value=value+4;
		    	  }
		    	  
		    	//Felder mit Bewertung 3: LO: 51;52;53;54;55;15;25;35;45 LU: 91,92,93,94,95;105;115;125;135
			      //RO: 19;29;39;49;59;510;511;512;513 RU:99;910;911;912;913;109;119;129;139 
		    	  if(((row==5)&&(col==1))|
			         ((row==5)&&(col==2))|
			         ((row==5)&&(col==3))|
			         ((row==5)&&(col==4))|
			         ((row==5)&&(col==5))|
			         ((row==1)&&(col==5))|
			         ((row==2)&&(col==5))|
			         ((row==3)&&(col==5))|
			         ((row==4)&&(col==5))|

			         ((row==9)&&(col==1))|
			         ((row==9)&&(col==2))|
			         ((row==9)&&(col==3))|
			         ((row==9)&&(col==4))|
			         ((row==9)&&(col==5))|
			         ((row==10)&&(col==5))|
			         ((row==11)&&(col==5))|
			         ((row==12)&&(col==5))|
			         ((row==13)&&(col==5))|

			         ((row==1)&&(col==9))|
			         ((row==2)&&(col==9))|
			         ((row==3)&&(col==9))|
			         ((row==4)&&(col==9))|
			         ((row==5)&&(col==9))|
			         ((row==5)&&(col==10))|
			         ((row==5)&&(col==11))|
			         ((row==5)&&(col==12))|
			         ((row==5)&&(col==13))|

			         ((row==9)&&(col==9))|
			         ((row==9)&&(col==13))|
			         ((row==9)&&(col==12))|
			         ((row==9)&&(col==11))|
			         ((row==9)&&(col==10))|
			         ((row==10)&&(col==9))|
			         ((row==11)&&(col==9))|
			         ((row==12)&&(col==9))|
			         ((row==13)&&(col==9))
		    	  	 ){
		    		  value=value+3;
		    	  }
		       //Felder mit Bewertung 1: 7,1;7,2;7,3;7,4;7,5;7,6 1,7;2,7;3,7;4,7;5,7;6,7  
			   // 						 7,8;7,9;7,10;7,11;7,12;7,13 8,7;9,7;10,7;11,7;12,7;13,7 
		    	  if(((row==7)&&(col==1))|
	           		 ((row==7)&&(col==2))|		    	  
	           		 ((row==7)&&(col==3))|		    	  
	           		 ((row==7)&&(col==4))|	           			 
	         		 ((row==7)&&(col==5))|	           			 
	         		 ((row==7)&&(col==6))|	           			 
	           		 ((row==7)&&(col==8))|	           			 
	           		 ((row==7)&&(col==9))|	           			 
	           		 ((row==7)&&(col==10))|	           			 
	           		 ((row==7)&&(col==11))|	           			 
	           		 ((row==7)&&(col==12))|	           			 
	           		 ((row==7)&&(col==13))|	           			 
	           		 ((row==1)&&(col==7))|	           			 
	           		 ((row==2)&&(col==7))|	           			 
	           		 ((row==3)&&(col==7))|	           			 
	           		 ((row==4)&&(col==7))|
	           		 ((row==5)&&(col==7))|
	           		 ((row==6)&&(col==7))|	           			 
	           		 ((row==8)&&(col==7))|	           			 
	           		 ((row==9)&&(col==7))|	           			 
	           		 ((row==10)&&(col==7))|	           			 
	           		 ((row==11)&&(col==7))|
	           		 ((row==12)&&(col==7))|
	           		 ((row==13)&&(col==7))
	           		 ){ 
		    		  value=value+1;
		    	  } else {
				    	//Felder mit Bewertung REST 
			    	  
			    		  value=value+2;
			    	  }
		    	  }
		    	  
		    	  
		      
		      
		      //Berechnung für Verteidiger?
		    //extra Berechnung für Deffender
		      //   1  2  3  4  5  6  7  8  9  10 11 12 13
		      // 1|xx|xx|10|xx|xx|xx|xx|xx|xx|xx|10|xx|xx
		      // 2|xx|10|xx|xx|xx|xx|xx|xx|xx|xx|xx|10|xx
		      // 3|10|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|10
		      // 4|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 5|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 6|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 7|xx|xx|xx|xx|xx|xx|KK|xx|xx|xx|xx|xx|xx
		      // 8|xx|xx|xx|xx|xx|xx|xx|08|xx|xx|xx|xx|xx
		      // 9|xx|xx|xx|xx|xx|xx|08|xx|08|xx|xx|xx|xx
		      //10|xx|xx|xx|xx|xx|08|xx|xx|xx|08|xx|xx|xx
		      //11|10|xx|xx|xx|08|xx|xx|xx|xx|xx|08|xx|xx
		      //12|xx|10|xx|08|xx|xx|xx|xx|xx|xx|xx|08|xx
		      //13|xx|xx|10|xx|xx|xx|xx|xx|xx|xx|08|08|xx
		      if(p==BoardContent.DEFENDER){
		    	  //links oben 31;22;13 
		    	  if(((row==3)&&(col==1))|
		    		 ((row==2)&&(col==2))|
		    		 ((row==1)&&(col==3))
		    		 ){
		    		  value=value+10;
		    	  }
		      //links unten 111;122;131 
		    	  if(((row==11)&&(col==1))|
				     ((row==12)&&(col==2))|
				     ((row==13)&&(col==1))
				      ){
				    		  value=value+10;
				      }
		    //rechtes oben 111;212;313
		    	  if(((row==1)&&(col==11))|
				   	 ((row==2)&&(col==12))|
				   	 ((row==3)&&(col==13))
				   	 ){
				    	  value=value+10;
				     } 
		    	  
		    //rest 124;115;106;97;88;99;1010;1111;1212;1312;1311
		    	  if(((row==12)&&(col==4))|
				     ((row==11)&&(col==5))|
				     ((row==10)&&(col==6))|
				     ((row==9)&&(col==57))|
				     ((row==8)&&(col==8))|
				     ((row==9)&&(col==9))|
				     ((row==10)&&(col==10))|
				     ((row==11)&&(col==11))|
				     ((row==12)&&(col==12))|
				     ((row==13)&&(col==12))|				     				     
				     ((row==13)&&(col==11))
				     ){
				    		  value=value+10;
				     }
		      }
		      return value;
		   }
	
	public String toString(){
		return name+" Strategy, Gruppe"+grpNr;	
	}
	
	private Board doMove(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board){
		if (currentMove2 != null){
			return RemoveCheck.checkForRemove(currentMove2, board);
		}		
		return board;		
	}

	@Override
	public de.fhhannover.inform.hnefatafl.vorgaben.Move calculateAttackerMove(
			de.fhhannover.inform.hnefatafl.vorgaben.Move arg0, int arg1) {		
		// Move Ã¼bernehmen	
				b = doMove(arg0,b);
				
		//Speichern in Verlaufbaum, muss noch durchdacht werden
				//verlauf.setLeft(new Node<Move>(lastMove));
				
				//Abbruchbedingung Zeit muss eingefï¿½gt werden
				//if(time>= thinktimeInSeconds){
				//zeit abgelaufen -> Spiel vorbei ?
				//} else {
				//Hauptcode zur Berechnung nï¿½chster Schritt
			    Move[] moves = GenerateMoves(BoardContent.ATTACKER);
			    Move move;
			    Move best_move=moves[0];
			    Move last_bm=best_move;
			    for(int i=1; i<moves.length; i++) {
			       move = moves[i];
			       if (calculateValue(move, false) > calculateValue(best_move, false)) {
			          last_bm = best_move;
			    	  best_move = move;
			       }
			    }
			    if(best_move!=arg0){
			    	return best_move;
			    } else { 
			    	return last_bm;
			    }
				//}	
	}

	@Override
	public de.fhhannover.inform.hnefatafl.vorgaben.Move calculateDefenderMove(
			de.fhhannover.inform.hnefatafl.vorgaben.Move arg0, int arg1) {
		// Move Ã¼bernehmen	
		b = doMove(arg0,b);
		
		//Speichern in Verlaufbaum, muss noch durchdacht werden
				//verlauf.setRight(new Node<Move>(lastMove));
				
				//Abbruchbedingung Zeit muss eingefï¿½gt werden
				//if(time>= thinktimeInSeconds){
				//zeit abgelaufen -> Spiel vorbei ?
				//} else {
				//Hauptcode zur Berechnung nï¿½chster Schritt
			    Move[] moves = GenerateMoves(BoardContent.DEFENDER);
			    Move move;
			    Move best_move=moves[0];
			    Move last_bm=best_move;
			    for(int i=1; i<moves.length; i++) {
			       move = moves[i];
			       if (calculateValue(move, true) > calculateValue(best_move, true)) {
			          last_bm = best_move;
			    	   best_move = move;
			       }
			    }
			    if(best_move!=arg0){
			    	return best_move;
			    } else { 
			    	return last_bm;
			    }
				//}	
	}

//	//Methoden mï¿½ssen importiert werden, vlt in vorgaben lï¿½schen ?
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