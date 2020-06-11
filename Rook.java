import java.util.ArrayList;

public class Rook extends Piece
{
  //fields
  int value = 5;
  int type = 2; 
  String player, location;
  
  //constructor
  public Rook(String player, String location)
  {
    this.player = player;
    this.location = location;
  }
  
  //getter methods**************
  
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
  
  //methods**************
  
  public ArrayList<Move> getPossibleMoves(Game game)
  {
    ArrayList<Piece> pieces = game.getLayout();
    ArrayList<Move> moves = new ArrayList<Move>();
    
    //up
    moves = getUp(moves, pieces, location);
    
    //down
    moves = getDown(moves, pieces, location);
    
    //right
    moves = getRight(moves, pieces, location);
    
    //left
    moves = getLeft(moves, pieces, location);
    
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
    return player+"R"+location;
  }
  
  //private methods************
  
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
  
  //up
  private ArrayList<Move> getUp(ArrayList<Move> moves, ArrayList<Piece> pieces, String start)
  {
    //set new test location
    String test = start.substring(0, 1) + (char)(start.charAt(1)+1);
    
    //base
    if(landsOn(test, pieces)==-1||landsOn(test, pieces)==1)
    {
      return moves;
    }
    else
    {
      if(landsOn(test, pieces)==0)
      {
        moves.add(newMove(test));
        return getUp(moves, pieces, test);
      }
      else //2
      {
        moves.add(newMove(test));
        return moves;
      }
    }
  }
  
  //down
  private ArrayList<Move> getDown(ArrayList<Move> moves, ArrayList<Piece> pieces, String start)
  {
    //set new test location
    String test = start.substring(0, 1) + (char)(start.charAt(1)-1);
    
    //base
    if(landsOn(test, pieces)==-1||landsOn(test, pieces)==1)
    {
      return moves;
    }
    else
    {
      if(landsOn(test, pieces)==0)
      {
        moves.add(newMove(test));
        return getDown(moves, pieces, test);
      }
      else //2
      {
        moves.add(newMove(test));
        return moves;
      }
    }
  }
  
  
  //right
  private ArrayList<Move> getRight(ArrayList<Move> moves, ArrayList<Piece> pieces, String start)
  {
    //set new test location
    String test = "" + (char)(start.charAt(0)+1) + start.substring(1, 2);
    
    //base
    if(landsOn(test, pieces)==-1||landsOn(test, pieces)==1)
    {
      return moves;
    }
    else
    {
      if(landsOn(test, pieces)==0)
      {
        moves.add(newMove(test));
        return getRight(moves, pieces, test);
      }
      else //2
      {
        moves.add(newMove(test));
        return moves;
      }
    }
  }
  
  //left
  private ArrayList<Move> getLeft(ArrayList<Move> moves, ArrayList<Piece> pieces, String start)
  {
    //set new test location
    String test = "" + (char)(start.charAt(0)-1) + start.substring(1, 2);
    
    //base
    if(landsOn(test, pieces)==-1||landsOn(test, pieces)==1)
    {
      return moves;
    }
    else
    {
      if(landsOn(test, pieces)==0)
      {
        moves.add(newMove(test));
        return getLeft(moves, pieces, test);
      }
      else //2
      {
        moves.add(newMove(test));
        return moves;
      }
    }
  }
  
  private Move newMove(String to)
  {
    return new Move(new String[]{location}, new String[]{to}, type, player);
  }
  
}