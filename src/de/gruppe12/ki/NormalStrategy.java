package de.gruppe12.ki;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;
import de.gruppe12.shared.*;


/**
* Die Klasse NormalStrategy ist die normale Spielstrategie <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 1.0.1 10.12.2011
* �nderungen: 10.12. KI verbessert!
*/

public class NormalStrategy implements MoveStrategy {
	private int grpNr;
	private String name;
	//private Node<Move> verlauf;
	private Board b;
	Random r;
	
	public NormalStrategy(){
		grpNr = 12;
		name = "normal"; 
		b = new Board();
		r = new Random();
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
	 * erzeutgt ein Array mit den n�chsten m�glichen Schritten
	 * jeder Spielfigur 
	 * 
	 * @return ein Array mit allen m�glichen Moves
	 */
	private Move[] generateMoves(BoardContent type) {
		Move[] mList = new Move[500]; 
		Cell[] cList = new Cell[26];
		int m1=0;
		int c1=0;
		
		//prüft wo Steine sind
		for(int i=0;i<=12;i++){
			for(int j=0;j<=12;j++){
				//speichert nur wenn Typ gleich ist
				if(b.getCell(i,j).getContent() == type){
					cList[c1]=new Cell(i,j, type);
					c1++;
				}
			}
		}
		for(int i=0;i<c1;i++){
			int r=13-cList[i].getCol();
			int l=13-cList[i].getRow();
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
	 * Bewertet den �bergebenen Move.
	 * 
	 * @return Bewertung des Moves
	 */
	   private int calculateValue(Move m, boolean isDefTurn) {
		   	 
		   	 if (m == null){
		   		 return -1;
		   	 }
		   	 //checkt ob Move zul�ssig
		      if(!MoveCheck.check(m,this.b,isDefTurn,false)){
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
			  if(p==BoardContent.ATTACKER){
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
			  }
		      
		      //extra Berechnungen beginnen hier:
		      int row=m.getToCell().getRow();
		      int col=m.getToCell().getCol();
		      
		      //extra Berechnung f�r Attacker
		      //   0   1  2  3  4  5  6  7  8  9 10 11 12
		      // 0|xx|10|08|xx|xx|xx|xx|xx|xx|xx|08|10|xx
		      // 1|10|10|08|xx|xx|xx|xx|xx|xx|xx|08|10|10
		      // 2|08|08|08|xx|xx|xx|xx|xx|xx|xx|08|08|08
		      // 3|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 4|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 5|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 6|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 7|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 8|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 9|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      //10|08|08|08|xx|xx|xx|xx|xx|xx|xx|08|08|08
		      //11|10|10|08|xx|xx|xx|xx|xx|xx|xx|08|10|10
		      //12|xx|10|08|xx|xx|xx|xx|xx|xx|xx|08|10|xx
		      
		      if(p==BoardContent.ATTACKER){
		    	  //Bewertung 10
		    	  if(((row==1)&&(col==1))|
						     ((row==1)&&(col==2))||
						     ((row==0)&&(col==1))||
						     ((row==11)&&(col==0))||
						     ((row==12)&&(col==1))||
						     ((row==0)&&(col==11))||
						     ((row==1)&&(col==11))||
						     ((row==1)&&(col==12))||
						     ((row==11)&&(col==12))||
						     ((row==11)&&(col==11))||
						     ((row==12)&&(col==11)))
						    {
						    		  value+=10;	  
						    }
		    	  //Bewertung 8 
		    	  if(((row==0)&&(col==2))|
		           			 ((row==1)&&(col==2))||
		           			 ((row==2)&&(col==2))||	    	  
		           			 ((row==2)&&(col==0))||
		           			 ((row==2)&&(col==1))||
		           			 ((row==10)&&(col==0))||
		           			 ((row==10)&&(col==2))||
		           			 ((row==10)&&(col==2))||	    	  
		           			 ((row==11)&&(col==2))||		    	  
		           			 ((row==12)&&(col==2))||		    	  
		           			 ((row==0)&&(col==2))||		    	  
		           			 ((row==1)&&(col==2))||		    	  
		           			 ((row==2)&&(col==2))||
		           			 ((row==2)&&(col==11))||          			 
		           			 ((row==2)&&(col==12))||	    	  
		           			 ((row==10)&&(col==10))||		    	  
		           			 ((row==10)&&(col==11))||		    	  
		           			 ((row==10)&&(col==12))||		    	  
		           			 ((row==11)&&(col==10))||		    	  
		           			 ((row==12)&&(col==10))
		           			 ){
				    		  value+=8;
				    	  }
		    	  
		    	  //Wenn K�nig in der N�he, dann ziehe dahin!
		    	  if((c1 ==BoardContent.KING)){
			    	  value+=100;
			      }
		    	  if((c2 ==BoardContent.KING)){
			    	  value+=100;
			      }
		    	  if((c3 ==BoardContent.KING)){
			    	  value+=100;
			      }
		    	  if((c4 ==BoardContent.KING)){
			    	  value+=100;
			      }
		    	  
		      }
		      
		      
		      
		      
		      
		      //extra Berechnung f�r K�nig
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
		    	  if(((row==0)&&(col==0))||
		    		 ((row==0)&&(col==12))||
		    		 ((row==12)&&(col==0))||
		    		 ((row==12)&&(col==12))
		    		 ){
		    		  value=10000;
		    	  }
		    	  //Felder mit Bewertung 8
		    	  if(((row==1)&&(col==1))||
				     ((row==1)&&(col==2))||
				     ((row==0)&&(col==1))||
				     ((row==11)&&(col==0))||
				     ((row==12)&&(col==1))||
				     ((row==0)&&(col==11))||
				     ((row==1)&&(col==11))||
				     ((row==1)&&(col==12))||
				     ((row==11)&&(col==12))||
				     ((row==11)&&(col==11))||
				     ((row==12)&&(col==11)))
				    {
				    		  value=1000;
				    }
		    	//Felder mit Bewertung 6: 
		    	  if(((row==0)&&(col==2))||
           			 ((row==1)&&(col==2))||
           			 ((row==2)&&(col==2))||	    	  
           			 ((row==2)&&(col==0))||
           			 ((row==2)&&(col==1))||
           			 ((row==10)&&(col==0))||
           			 ((row==10)&&(col==1))||
           			 ((row==10)&&(col==2))||		    	  
           			 ((row==11)&&(col==2))||		    	  
           			 ((row==12)&&(col==2))||		    	  
           			 ((row==0)&&(col==0))||		    	  
           			 ((row==1)&&(col==1))||		    	  
           			 ((row==2)&&(col==2))||
           			 ((row==2)&&(col==11))||         			 
           			 ((row==2)&&(col==12))||		    	  
           			 ((row==10)&&(col==10))||		    	  
           			 ((row==10)&&(col==11))||		    	  
           			 ((row==10)&&(col==12))||		    	  
           			 ((row==11)&&(col==10))||		    	  
           			 ((row==12)&&(col==10))
           			 ){
		    		  value=800;
		    	  }
		    	  //Felder mit Bewertung 4
		    	  if(((row==3)&&(col==0))||
			         ((row==3)&&(col==1))||
			         ((row==3)&&(col==2))||
			         ((row==3)&&(col==3))||
			         ((row==2)&&(col==3))||
			         ((row==1)&&(col==3))||
			         ((row==0)&&(col==3))||
			         ((row==9)&&(col==0))||
			         ((row==9)&&(col==1))||
			         ((row==9)&&(col==2))||
			         ((row==9)&&(col==3))||
			         ((row==10)&&(col==3))||
			         ((row==11)&&(col==3))||
			         ((row==12)&&(col==3))||
			         ((row==0)&&(col==9))||
			         ((row==1)&&(col==9))||
			         ((row==2)&&(col==9))||
			         ((row==3)&&(col==9))||
			         ((row==3)&&(col==10))||
			         ((row==3)&&(col==11))||
			         ((row==3)&&(col==12))||
			         ((row==9)&&(col==9))||
			         ((row==10)&&(col==9))||
			         ((row==11)&&(col==9))||
			         ((row==12)&&(col==9))||
			         ((row==9)&&(col==10))||
			         ((row==9)&&(col==11))||
			         ((row==9)&&(col==12))
				     ){
		    		  value=400;
		    	  }
		    	  
		    	//Felder mit Bewertung 3: LO:
		    	  if(((row==4)&&(col==0))||
			         ((row==4)&&(col==1))||
			         ((row==4)&&(col==2))||
			         ((row==4)&&(col==3))||
			         ((row==4)&&(col==4))||
			         ((row==0)&&(col==4))||
			         ((row==1)&&(col==4))||
			         ((row==2)&&(col==4))||
			         ((row==3)&&(col==4))||

			         ((row==8)&&(col==0))||
			         ((row==8)&&(col==1))||
			         ((row==8)&&(col==2))||
			         ((row==8)&&(col==3))||
			         ((row==8)&&(col==4))||
			         ((row==9)&&(col==4))||
			         ((row==10)&&(col==4))||
			         ((row==11)&&(col==4))||
			         ((row==12)&&(col==4))||

			         ((row==0)&&(col==8))||
			         ((row==1)&&(col==8))||
			         ((row==2)&&(col==8))||
			         ((row==3)&&(col==8))||
			         ((row==4)&&(col==8))||
			         ((row==4)&&(col==9))||
			         ((row==4)&&(col==10))||
			         ((row==4)&&(col==11))||
			         ((row==4)&&(col==12))||

			         ((row==8)&&(col==8))||
			         ((row==8)&&(col==12))||
			         ((row==8)&&(col==11))||
			         ((row==8)&&(col==10))||
			         ((row==8)&&(col==9))||
			         ((row==9)&&(col==8))||
			         ((row==10)&&(col==8))||
			         ((row==11)&&(col==8))||
			         ((row==12)&&(col==8))
		    	  	 ){
		    		  value=300;
		    	  }		//}	
		       //Felder mit Bewertung 1:
		    	  if(((row==6)&&(col==0))||
	           		 ((row==6)&&(col==1))||		    	  
	           		 ((row==6)&&(col==2))||		    	  
	           		 ((row==6)&&(col==3))||	           			 
	         		 ((row==6)&&(col==4))||	           			 
	         		 ((row==6)&&(col==5))||	           			 
	           		 ((row==6)&&(col==6))||	           			 
	           		 ((row==6)&&(col==8))||	           			 
	           		 ((row==6)&&(col==9))||	           			 
	           		 ((row==6)&&(col==10))||	           			 
	           		 ((row==6)&&(col==11))||           			 
	           		 ((row==6)&&(col==12))||	           			 
	           		 ((row==0)&&(col==6))||	           			 
	           		 ((row==1)&&(col==6))||	           			 
	           		 ((row==2)&&(col==6))||	           			 
	           		 ((row==3)&&(col==6))||
	           		 ((row==4)&&(col==6))||
	           		 ((row==5)&&(col==6))||           			 
	           		 ((row==7)&&(col==6))||           			 
	           		 ((row==8)&&(col==6))||           			 
	           		 ((row==9)&&(col==6))||           			 
	           		 ((row==10)&&(col==6))||
	           		 ((row==11)&&(col==6))||
	           		 ((row==12)&&(col==6))
	           		 ){ 
		    		  value=200;
		    	  } else {
				    	//Felder mit Bewertung REST 
			    	  
			    		  value+=2;
			    	  }
		    	  }
		      
		      //Berechnung f�r Verteidiger?
		    //extra Berechnung f�r Deffender
		      //    0 1  2  3  4  5  6  7  8  9  10 11 12
		      // 0|xx|xx|10|xx|xx|xx|xx|xx|xx|xx|10|xx|xx
		      // 1|xx|10|xx|xx|xx|xx|xx|xx|xx|xx|xx|10|xx
		      // 2|10|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|10
		      // 3|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 4|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 5|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx|xx
		      // 6|xx|xx|xx|xx|xx|xx|KK|xx|xx|xx|xx|xx|xx
		      // 7|xx|xx|xx|xx|xx|xx|xx|08|xx|xx|xx|xx|xx
		      // 8|xx|xx|xx|xx|xx|xx|08|xx|08|xx|xx|xx|xx
		      // 9|xx|xx|xx|xx|xx|08|xx|xx|xx|08|xx|xx|xx
		      //10|10|xx|xx|xx|08|xx|xx|xx|xx|xx|08|xx|xx
		      //11|xx|10|xx|08|xx|xx|xx|xx|xx|xx|xx|08|xx
		      //12|xx|xx|10|xx|xx|xx|xx|xx|xx|xx|08|08|xx
		      if(p==BoardContent.ATTACKER){
		    	  //links oben  
		    	  if(((row==2)&&(col==0))||
		    		 ((row==1)&&(col==1))||
		    		 ((row==0)&&(col==2))
		    		 ){
		    		  value+=100;
		    	  }
		      //links unten 
		    	  if(((row==10)&&(col==0))||
				     ((row==11)&&(col==1))||
				     ((row==12)&&(col==0))
				      ){
				    		  value+=100;
				      }
		    //rechts oben
		    	  if(((row==0)&&(col==10))||
				   	 ((row==1)&&(col==11))||
				   	 ((row==2)&&(col==12))
				   	 ){
				    	  value+=100;
				     } 
		    	  
		    	  //rechts unten
		    	  if(((row==10)&&(col==12))||
		    		((row==11)&&(col==11))||
					((row==12)&&(col==10))
		    			 ){
				    	  value+=100;
				     } 
		    	  
		    //rest 
		    	  if(((row==11)&&(col==3))||
				     ((row==10)&&(col==4))||
				     ((row==9)&&(col==5))||
				     ((row==8)&&(col==6))||
				     ((row==7)&&(col==7))||
				     ((row==8)&&(col==8))||
				     ((row==9)&&(col==9))||
				     ((row==10)&&(col==10))||
				     ((row==11)&&(col==11))||
				     ((row==12)&&(col==11))||				     				     
				     ((row==12)&&(col==10))
				     ){
				    		  value+=8;
				     }
		      }
		      System.out.println(m.toString() + "Value: " + value);
		      return value;
		   }
	@Override
	public String toString(){
		return name+" Strategy, Gruppe"+grpNr;	
	}
	
	private Board doMove(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board){
		if (currentMove2 != null){
			return RemoveCheck.checkForRemove(currentMove2, board, false);
		}		
		return board;		
	}

	@Override
	public de.fhhannover.inform.hnefatafl.vorgaben.Move calculateAttackerMove(
			de.fhhannover.inform.hnefatafl.vorgaben.Move arg0, int arg1) {	
		long t1 = System.nanoTime();
		// Move übernehmen	
				this.b = doMove(arg0,this.b);
				
				
		//Speichern in Verlaufbaum, muss noch durchdacht werden
				//verlauf.setLeft(new Node<Move>(lastMove));
				
				//Abbruchbedingung Zeit muss eingef�gt werden
				//if(time>= thinktimeInSeconds){
				//zeit abgelaufen -> Spiel vorbei ?
				//} else {
				//Hauptcode zur Berechnung n�chster Schritt
			    Move[] generatedMoves = generateMoves(BoardContent.ATTACKER);
			    TreeMap<Integer, ArrayList<Integer>> ratedMoves = new TreeMap<Integer, ArrayList<Integer>>();
			    int rating;
			    
			    // Moves nach Rating sortiert in TreeMap speichern
			    
			    for(int i=0; i<generatedMoves.length; i++) {
			    	rating=calculateValue(generatedMoves[i], false);
			    	if (rating == -1) continue;
			    	
			    	ArrayList<Integer> possibleMoves;	
			    	
			    	if (!ratedMoves.containsKey(rating)){
			    		possibleMoves = new ArrayList<Integer>();			    		
			    	}
			    	else
			    	{
			    		possibleMoves = ratedMoves.get(rating);			    			    		
			    	}
			    	possibleMoves.add(i);
		    		ratedMoves.put(rating, possibleMoves);			      
			    } 
			    
			    //Aus den Moves mit dem Besten Rating einen per Zufall auswählen
			    ArrayList<Integer> bestMoves = ratedMoves.get(ratedMoves.lastKey());
			    int selectedMove = r.nextInt(bestMoves.size());			    
		
		    	// Move übernehmen	
				this.b = doMove(generatedMoves[bestMoves.get(selectedMove)],this.b);
				
				long t2 = (System.nanoTime() - t1 )/1000;
				System.out.println("Time für Attackermove: " + t2 + "ms");
		    	return generatedMoves[bestMoves.get(selectedMove)];
				//}	
	}

	@Override
	public de.fhhannover.inform.hnefatafl.vorgaben.Move calculateDefenderMove(
			de.fhhannover.inform.hnefatafl.vorgaben.Move arg0, int arg1) {
		long t1 = System.nanoTime();
		// Move übernehmen	
		this.b = doMove(arg0,this.b);
		
				
		//Speichern in Verlaufbaum, muss noch durchdacht werden
				//verlauf.setRight(new Node<Move>(lastMove));
				
				//Abbruchbedingung Zeit muss eingef�gt werden
				//if(time>= thinktimeInSeconds){
				//zeit abgelaufen -> Spiel vorbei ?
				//} else {
				//Hauptcode zur Berechnung n�chster Schritt
		
	 		Move[] generatedMoves = generateMoves(BoardContent.DEFENDER);
	 		Move[] generatedKingMoves = generateMoves(BoardContent.KING);
	 		Move[] allGeneratedMoves = new Move[1000];
	 		for (int i = 0; i<500; i++){
	 			allGeneratedMoves[i] = generatedKingMoves[i];
	 		}
	 		for (int i = 500; i<1000; i++){
	 			allGeneratedMoves[i] = generatedMoves[i - 500];
	 		}
	 		
		    TreeMap<Integer, ArrayList<Integer>> ratedMoves = new TreeMap<Integer, ArrayList<Integer>>();
		    int rating;
		    
		    
		 // Moves nach Rating sortiert in TreeMap speichern
		    
		    for(int i=0; i<allGeneratedMoves.length; i++) {
		    	rating=calculateValue(allGeneratedMoves[i], true);
		    	if (rating == -1) continue;
		    	
		    	ArrayList<Integer> possibleMoves;	
		    	
		    	if (!ratedMoves.containsKey(rating)){
		    		possibleMoves = new ArrayList<Integer>();			    		
		    	}
		    	else
		    	{
		    		possibleMoves = ratedMoves.get(rating);			    			    		
		    	}
		    	possibleMoves.add(i);
	    		ratedMoves.put(rating, possibleMoves);			      
		    } 		   
		    
		    //Aus den Moves mit dem Besten Rating einen per Zufall auswählen
		    ArrayList<Integer> bestMoves = ratedMoves.get(ratedMoves.lastKey());
		    int selectedMove = r.nextInt(bestMoves.size());
		    
	
	    	// Move übernehmen	
			this.b = doMove(allGeneratedMoves[bestMoves.get(selectedMove)],this.b);
			long t2 = (System.nanoTime() - t1)/1000;
			
			System.out.println("Time für Defendermove: " + t2 + "ms");
	    	return allGeneratedMoves[bestMoves.get(selectedMove)];
				//}	
	}
}