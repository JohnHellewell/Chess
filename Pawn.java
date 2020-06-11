import java.util.ArrayList;

public class Pawn extends Piece
{
  //fields
  int value = 1;
  int type = 1; 
  String player, location;
  
  //constructor
  public Pawn(String player, String location)
  {
    this.player = player;
    this.location = location;
  }
  
  //getter methods************
  
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
  
  //methods************8
  
  public ArrayList<Move> getPossibleMoves(Game game)
  {
    ArrayList<Piece> pieces = game.getLayout();
    ArrayList<Move> moves = new ArrayList<Move>();
    
    if(player.equals("H")) //human piece
    {
      //move one space forward
      if(landsOn("" + location.charAt(0) + (char)(location.charAt(1)+1), pieces)==0)
      {
        moves.add(newMove("" + location.charAt(0) + (char)(location.charAt(1)+1)));
        
        //move two spaces
        if(location.charAt(1)=='2')
        {
          if(landsOn("" + location.charAt(0) + (char)(location.charAt(1)+2), pieces)==0)
            moves.add(newMove("" + location.charAt(0) + (char)(location.charAt(1)+2)));
        }
      }
      //attack moves
      if(landsOn("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)+1), pieces)==2)
        moves.add(newMove("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)+1)));
      
      if(landsOn("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)+1), pieces)==2)
        moves.add(newMove("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)+1)));
      
      //En passant
      if(Chess.game.getTurn()>1)
      {
        if(location.charAt(1)=='5')
        {
          if((game.getMoveUsed().getPlayer().equals("C")&&game.getMoveUsed().getType()==1)&&(game.getMoveUsed().getFrom()[0].charAt(1)=='7'&&(game.getMoveUsed().getTo()[0].charAt(1)=='5'&&game.getMoveUsed().getTo()[0].charAt(0)==location.charAt(0)+1)))
            moves.add(specialMove("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)+1), "" + (char)(location.charAt(0)+1) + 5));
          
          if((game.getMoveUsed().getPlayer().equals("C")&&game.getMoveUsed().getType()==1)&&(game.getMoveUsed().getFrom()[0].charAt(1)=='7'&&(game.getMoveUsed().getTo()[0].charAt(1)=='5'&&game.getMoveUsed().getTo()[0].charAt(0)==location.charAt(0)-1)))
            moves.add(specialMove("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)+1), "" + (char)(location.charAt(0)-1) + 5));
        }
      }
    }
    else //computer piece
    {
      //move one space forward
      if(landsOn("" + location.charAt(0) + (char)(location.charAt(1)-1), pieces)==0)
      {
        moves.add(newMove("" + location.charAt(0) + (char)(location.charAt(1)-1)));
        
        //move two spaces
        if(location.charAt(1)=='7')
        {
          if(landsOn("" + location.charAt(0) + (char)(location.charAt(1)-2), pieces)==0)
            moves.add(newMove("" + location.charAt(0) + (char)(location.charAt(1)-2)));
        }
      }
      //attack moves
      if(landsOn("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)-1), pieces)==2)
        moves.add(newMove("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)-1)));
      
      if(landsOn("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)-1), pieces)==2)
        moves.add(newMove("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)-1)));
      
      if(Chess.game.getTurn()>1)
      {
        if(location.charAt(1)=='4')
        {
          if((game.getMoveUsed().getPlayer().equals("H")&&game.getMoveUsed().getType()==1)&&(game.getMoveUsed().getFrom()[0].charAt(1)=='2'&&(game.getMoveUsed().getTo()[0].charAt(1)=='4'&&game.getMoveUsed().getTo()[0].charAt(0)==location.charAt(0)+1)))
            moves.add(specialMove("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)-1), "" + (char)(location.charAt(0)+1) + 4));
          
          if((game.getMoveUsed().getPlayer().equals("H")&&game.getMoveUsed().getType()==1)&&(game.getMoveUsed().getFrom()[0].charAt(1)=='2'&&(game.getMoveUsed().getTo()[0].charAt(1)=='4'&&game.getMoveUsed().getTo()[0].charAt(0)==location.charAt(0)-1)))
            moves.add(specialMove("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)-1), "" + (char)(location.charAt(0)-1) + 4));
        }
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
    return player+"P"+location;
  }
  
  //private methods***************
  
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
    //return player + "P" + location + " to " + loc;
    return new Move(new String[]{location}, new String[]{to}, type, player);
  }
  
  //en passant
  private Move specialMove(String to, String kill)
  {
    //return player + "P" + location + " to " + loc;
    return new Move(new String[]{location}, new String[]{to}, type, player, kill);
  }
  
}