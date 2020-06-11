import java.util.ArrayList;

public abstract class Piece
{
  //fields
  int value;
  String location, type, player;
  
  // constructor
  public Piece()
  {
    
  }
  
  //abstract methoods
  public abstract int getValue();
  
  public abstract String getPlayer();
  
  public abstract String getLocation();
  
  public abstract String toString();
  
  public abstract int getType(); //pawn 1, rook 2, knight 3, bishop 4, queen 5, king 6
  
  public abstract int landsOn(String loc, ArrayList<Piece> pieces);
  
  public abstract ArrayList<Move> getPossibleMoves(Game game);
  
  public abstract boolean checked(ArrayList<Piece> pieces);
  
}