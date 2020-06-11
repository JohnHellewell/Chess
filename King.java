import java.util.ArrayList;

public class King extends Piece
{
  //fields
  int value = 1000;
  int type = 6; 
  String player, location;
  
  //constructor
  public King(String player, String location)
  {
    this.player = player;
    this.location = location;
  }
  
  //getter methods*********
  
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
  
  //methods*************
  
  public ArrayList<Move> getPossibleMoves(Game game)
  {
    ArrayList<Piece> pieces = game.getLayout();
    ArrayList<Move> moves = new ArrayList<Move>();
    
    //creates possible 8 moves for king to test together rather than one at a time
    ArrayList<String> tests = new ArrayList<String>();
    tests.add("" + (char)(location.charAt(0)+0) + (char)(location.charAt(1)+1));
    tests.add("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)+1));
    tests.add("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)+0));
    tests.add("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)-1));
    tests.add("" + (char)(location.charAt(0)+0) + (char)(location.charAt(1)-1));
    tests.add("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)-1));
    tests.add("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)+0));
    tests.add("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)+1));
    
    for(String str : tests)
    {
      if(landsOn(str, pieces)==0||landsOn(str, pieces)==2)
      {
        moves.add(newMove(str));
      }
    }
    moves = castles(moves, game);
    
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
  
  //if it can castle, it adds the moves
  private ArrayList<Move> castles(ArrayList<Move> moves, Game game)
  {
    ArrayList<Piece> pieces = game.getLayout();
    if(player.equals("H"))
    {
      if(game.canWA())
      {
        if(landsOn("B1", pieces)==0&&landsOn("C1", pieces)==0&&landsOn("D1", pieces)==0)
        {
          moves.add(new Move(new String[]{"E1", "A1"}, new String[]{"B1", "C1"}, type, player));
        }
      }
      if(game.canWH())
      {
        if(landsOn("F1", pieces)==0&&landsOn("G1", pieces)==0)
        {
          moves.add(new Move(new String[]{"E1", "H1"}, new String[]{"G1", "F1"}, type, player));
        }
      }
    }
    else
    {
      if(game.canBA())
      {
        if(landsOn("B8", pieces)==0&&landsOn("C8", pieces)==0&&landsOn("D8", pieces)==0)
        {
          moves.add(new Move(new String[]{"E8", "A8"}, new String[]{"B8", "C8"}, type, player));
        }
      }
      if(game.canBH())
      {
        if(landsOn("F8", pieces)==0&&landsOn("G8", pieces)==0)
        {
          moves.add(new Move(new String[]{"E8", "H8"}, new String[]{"G8", "F8"}, type, player));
        }
      }
    }
    return moves;
  }
  
  //sees if a piece can attack it
  public boolean checked(ArrayList<Piece> pieces)
  {
    for(Piece p : pieces)
    {
      if(!p.getPlayer().equals(player))
      {
        switch(p.getType())
        {
          case 1:
          {
            if(canPawnAttack(p))
              return true;
            break;
          }
          case 2:
          {
            if(canRookAttack(p, pieces))
              return true;
            break;
          }
          case 3:
          {
            if(canKnightAttack(p))
              return true;
            break;
          }
          case 4:
          {
            if(canBishopAttack(p, pieces))
              return true;
            break;
          }
          case 5:
          {
            if(canQueenAttack(p, pieces))
              return true;
            break;
          }
          case 6:
          {
            if(canKingAttack(p))
              return true;
            break;
          }
          default: 
          {
            System.out.println("error: reaching default in King");
            return false;
          }
        }
      }
    }
    return false;
  }
  
  // checks if a pawn can attack king
  private boolean canPawnAttack(Piece p)
  {
    //we already know that the piece belongs to the other team
    if(p.getPlayer().equals("H"))
    {
      return ((p.getLocation().equals("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)-1)))||(p.getLocation().equals("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)-1))));
    }
    else
    {
      return ((p.getLocation().equals("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)+1)))||(p.getLocation().equals("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)+1))));
    }
  }
  
  private boolean canKingAttack(Piece p)
  {
    return ((Math.abs((p.getLocation().charAt(0))-(location.charAt(0)))<=1)&&(Math.abs((p.getLocation().charAt(1))-(location.charAt(1)))<=1));
  }
  
  
  //checks if rook can attack king
  private boolean canRookAttack(Piece p, ArrayList<Piece> pieces)
  {
    if((p.getLocation().charAt(0)==location.charAt(0))||(p.getLocation().charAt(1)==location.charAt(1)))
    {
      ArrayList<String> locations = new ArrayList<String>();
      
      if(p.getLocation().charAt(0)==location.charAt(0)) //on the same letter (up and down)
      {
        if(Math.abs(p.getLocation().charAt(1)-location.charAt(1))==1)
        {
          return true;
        }
        else
        {
          //puts together list of locations between rook and king to check if they are empty
          for(int i=Math.min(p.getLocation().charAt(1), location.charAt(1))+1; i< Math.max(p.getLocation().charAt(1), location.charAt(1)); i++)
          {
            locations.add("" + location.charAt(0) + (char)(i));
          }
          return (isRowEmpty(locations, pieces));
        }
      }
      else //on the same number (side to side)
      {
        if(Math.abs(p.getLocation().charAt(0)-location.charAt(0))==1)
        {
          return true;
        }
        else
        {
          //puts together list of locations between rook and king to check if they are empty
          for(int i=Math.min(p.getLocation().charAt(0), location.charAt(0))+1; i< Math.max(p.getLocation().charAt(0), location.charAt(0)); i++)
          {
            locations.add(""  + (char)(i) + location.charAt(1));
          }
          return (isRowEmpty(locations, pieces));
        }
      }
    }
    else
    {
      return false;
    }
  }
  
  //checks if a knight can attack king
  private boolean canKnightAttack(Piece p)
  {
    ArrayList<String> locs = new ArrayList<String>();
    
    locs.add("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)+2));
    locs.add("" + (char)(location.charAt(0)+2) + (char)(location.charAt(1)+1));
    locs.add("" + (char)(location.charAt(0)+2) + (char)(location.charAt(1)-1));
    locs.add("" + (char)(location.charAt(0)+1) + (char)(location.charAt(1)-2));
    locs.add("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)-2));
    locs.add("" + (char)(location.charAt(0)-2) + (char)(location.charAt(1)-1));
    locs.add("" + (char)(location.charAt(0)-2) + (char)(location.charAt(1)+1));
    locs.add("" + (char)(location.charAt(0)-1) + (char)(location.charAt(1)+2));
    
    for(String loc : locs)
    {
      if(p.getLocation().equals(loc))
        return true;
    }
    return false;
  }
  
  //checks if bishop can attack king
  private boolean canBishopAttack(Piece p, ArrayList<Piece> pieces)
  {
    ArrayList<String> locations = new ArrayList<String>();
    
    if(Math.abs(p.getLocation().charAt(0)-location.charAt(0))==Math.abs(p.getLocation().charAt(1)-location.charAt(1))) //if it is diagonal from king
    {
      if(Math.abs(p.getLocation().charAt(0)-location.charAt(0))==1)//if it is one tile away
      {
        return true;
      }
      else //2 or more tiles away
      {
        //each of the four diagonal directions
        if(p.getLocation().charAt(1)<location.charAt(1)) //bishop is lower than king
        {
          if(p.getLocation().charAt(0)<location.charAt(0)) //bishop is to the left of king
          {
            for(int i = p.getLocation().charAt(0)+1; i < location.charAt(0); i++)
            {
              locations.add("" + (char)(i) + (char)((i-p.getLocation().charAt(0))+p.getLocation().charAt(1)) );
            }
          }
          else //bishop is to the right of king
          {
            for(int i = location.charAt(0)+1; i < p.getLocation().charAt(0); i++)
            {
              locations.add("" + (char)(i) + (char)(location.charAt(0)-i+location.charAt(1)) );
            }
          }
        }
        else //bishop is higher than king
        {
          if(p.getLocation().charAt(0)<location.charAt(0)) //bishop is to the left of king
          {
            for(int i = p.getLocation().charAt(0)+1; i < location.charAt(0); i++)
            {
              locations.add("" + (char)(i) + (char)((p.getLocation().charAt(0)-i)+p.getLocation().charAt(1)) );
            }
          }
          else //bishop is to the right of king
          {
            for(int i = location.charAt(0)+1; i < p.getLocation().charAt(0); i++)
            {
              locations.add("" + (char)(i) + (char)((i-location.charAt(0))+location.charAt(1)) );
            }
          }
        }
      }
      return isRowEmpty(locations, pieces);
    }
    else
    {
      return false;
    }
  }
  
  private boolean canQueenAttack(Piece p, ArrayList<Piece> pieces)
  {
    return (canRookAttack(p, pieces)||canBishopAttack(p, pieces));
  }
  
  //helps check for rook, bishop and queen
  private boolean isRowEmpty(ArrayList<String> locations, ArrayList<Piece> pieces)
  {
    for(String loc : locations)
    {
      for(Piece p : pieces)
      {
        if(loc.equals(p.getLocation()))
          return false;
      }
    }
    return true;
  }
  
  public String toString()
  {
    return player+"X"+location;
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
    return new Move(new String[]{location}, new String[]{to}, type, player);
  }
  
}