import java.util.ArrayList;

public class Knight extends Piece
{
  //fields
  int value = 3;
  int type = 3; 
  String player, location;
  
  //constructor
  public Knight(String player, String location)
  {
    this.player = player;
    this.location = location;
  }
  
  //getter methods****************
  
  public boolean checked(ArrayList<Piece> pieces)
  {
    return false;
  }
  
  public int getValue()
  {
    return value;
  }
  
  public String getPlayer()
  {
    return player;
  }
  
  public String getLocation()
  {
    return location;
  }
  
  public int getType()
  {
    return type;
  }
  
  //methods************
  
  public ArrayList<Move> getPossibleMoves(Game game)
  {
    ArrayList<Piece> pieces = game.getLayout();
    ArrayList<Move> moves = new ArrayList<Move>();
    
    //create possible 8 moves for king to test together rather than one at a time
    ArrayList<String> tests = new ArrayList<String>();
    tests.add("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)+2));
    tests.add("" + (char)(location.charAt(0)+2) + (char)(location.charAt(1)+1));
    tests.add("" + (char)(location.charAt(0)+2) + (char)(location.charAt(1)-1));
    tests.add("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)-2));
    tests.add("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)-2));
    tests.add("" + (char)(location.charAt(0)-2) + (char)(location.charAt(1)-1));
    tests.add("" + (char)(location.charAt(0)-2) + (char)(location.charAt(1)+1));
    tests.add("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)+2));
    
    for(String str : tests)
    {
      if(landsOn(str, pieces)==0||landsOn(str, pieces)==2)
      {
        moves.add(newMove(str));
      }
    }
    
    ArrayList<Move> safeMoves = new ArrayList<Move>();
    
    for(Move move : moves)
    {
      if(player.equals("H")) //human piece
      {
        if(!Chess.newGame(game, move).isWhiteInCheck())
          safeMoves.add(move);
      }
      else //computer piece
      {
         if(!Chess.newGame(game, move).isBlackInCheck())
          safeMoves.add(move);
      }
    }
    return safeMoves;
  }
  
  public String toString()
  {
    return player+"K"+location;
  }
  
  //private methods****************
  
  //sees if location is "valid" (inside chess board)
  private boolean isValid(String loc)
  {
    if(loc.charAt(0)<'A'||loc.charAt(0)>'H')
    {
      return false;
    }
    if(loc.charAt(1)<'1'||loc.charAt(1)>'8')
    {
      return false;
    }
    return true;
  }
  
  //sees if a location is occupied by a blank tile (0), a friendly piece (1), an enemy piece (2), or is an invalid location (-1)
  public int landsOn(String loc, ArrayList<Piece> pieces)
  {
    if(!isValid(loc))
    {
      return -1;
    }
    
    for(Piece p : pieces)
    {
      if(p.getLocation().equals(loc))
      {
        if(p.getPlayer().equals(player))
        {
          return 1;
        }
        else
        {
          return 2;
        }
      }
    }
    return 0;
  }
  
  //creates string for location
  private Move newMove(String to)
  {
    return new Move(new String[]{location}, new String[]{to}, type, player);
  }
  
}