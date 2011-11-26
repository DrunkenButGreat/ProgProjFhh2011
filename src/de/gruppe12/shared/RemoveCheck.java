/**
 * @author Julian Kipka
 * @version = 1.0
 * 
 */

package de.gruppe12.shared;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;

public class RemoveCheck {
	
	public static Board checkForRemove(Move move, Board board){
		
		Board temp = new Board();
		
		temp.set(checkSurround(move,board));
		temp = checkForEnd(move, board);
		
		return temp;
		
	}
	
	private static BoardContent[][] checkSurround(Move move, Board board){
		int x=move.getToCell().getCol(),y=move.getToCell().getRow();
		BoardContent me = move.getToCell().getContent();
		BoardContent tboard[][] = board.get();
		
		
		/* Ab hier Try-Catch weil es sein könnte das ein Stein sich an der Seite befindet 
		 * Es gäbe sonst eine Index out of Bound Exception
		 */
		
		// Links gucken und evtl entfernen
		try{
			if(tboard[x-1][y]==opposite(me)&&tboard[x-2][y]==me){
				tboard[x-1][y] = BoardContent.EMPTY;
			}
		} catch (Exception e){
			
		}
		
		// Rechts gucken und evtl entfernen
			try{
				if(tboard[x+1][y]==opposite(me)&&tboard[x+2][y]==me){
					tboard[x+1][y] = BoardContent.EMPTY;
				}
			} catch (Exception e){
				
			}
		
		// Unten gucken und evtl entfernen
			try{
				if(tboard[x][y+1]==opposite(me)&&tboard[x][y+2]==me){
					tboard[x][y+1] = BoardContent.EMPTY;
				}
			} catch (Exception e){
				
			}
			
			// Unten gucken und evtl entfernen
			try{
				if(tboard[x][y-1]==opposite(me)&&tboard[x][y-2]==me){
					tboard[x][y-1] = BoardContent.EMPTY;
				}
			} catch (Exception e){
				
			}
		
			return tboard;
	}
	
	private static Board checkForEnd(Move move, Board board){
		
		int x=move.getToCell().getCol(),y=move.getToCell().getRow();
		BoardContent tboard[][] = board.get();
		Board tb = board;
		
		
		// König zieht auf Burg beendet die Methode und setzt das Finish Flag
		if(move.getToCell().getContent()==BoardContent.KING &&
				((x==0&&y==0) || (x==0&&y==tboard[0].length-1) || (x==tboard.length-1&&y==0) || (x==tboard.length-1&&y==tboard[0].length-1))
		) {
			tb.setFinish();
			return tb;
		}
		
		
		/* Suche König
		 * JA ist ineffizient
		 */
		for(int i=0; i<tboard.length;i++){
			for(int j = 0; j < tboard[i].length; j++){
				if(tboard[i][j]==BoardContent.KING){
					x=i;
					y=j;
				}
			}
		}
		
		//Ist der König umzingelt wenn ja su = 4
		int sur = 0;
		
		//links
		try{
			if(tboard[x-1][y]==BoardContent.ATTACKER) sur++;			
		} catch (Exception e){
			sur++;
		}

		//Rechts
		try{
			if(tboard[x+1][y]==BoardContent.ATTACKER) sur++;			
		} catch (Exception e){
			sur++;
		}

		//oben
		try{
			if(tboard[x][y-1]==BoardContent.ATTACKER) sur++;			
		} catch (Exception e){
			sur++;
		}
		
		//unten
		try{
			if(tboard[x][y+1]==BoardContent.ATTACKER) sur++;			
		} catch (Exception e){
			sur++;
		}
		
		if(sur==4) tb.setFinish();
		
		return tb;		
		
	}
	
	
	/** opposite
	 * 
	 * Helfer zum ermitteln des Gegenspielers
	 * 
	 * @param bc
	 * @return
	 */
	private static BoardContent opposite(BoardContent bc){
		if(bc==BoardContent.ATTACKER)
			return BoardContent.DEFENDER;
		else 
			return BoardContent.ATTACKER;
	}
}
