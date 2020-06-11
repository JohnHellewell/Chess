import java.util.ArrayList;

public class Game
{
  //fields
  public ArrayList<Piece> layout;
  int turn;
  Move moveUsed;
  boolean canWhiteCastleH, canWhiteCastleA, canBlackCastleH, canBlackCastleA;
  
  public Game(int i, Move moveUsed, boolean WA, boolean WH, boolean BA, boolean BH)
  {
    this.moveUsed = moveUsed;
    turn = i;
    layout = new ArrayList<Piece>();
    canWhiteCastleA=WA;
    canWhiteCastleH=WH;
    canBlackCastleA=BA;
    canBlackCastleH=BH;
    castleCheck();
  }
  
  public Game()//first game
  {
    turn = 1;
    layout = new ArrayList<Piece>();
    setDefaultGame();
    canWhiteCastleA=true;
    canWhiteCastleH=true;
    canBlackCastleA=true;
    canBlackCastleH=true;
  }
  
  //goes through old moves and sees if the players can check or not
  private void castleCheck()
  {
    if(moveUsed.getType()==6||moveUsed.isCastle())//a player castled or moved the king
    {
      if(moveUsed.getPlayer().equals("H"))
      {
        canWhiteCastleA=false;
        canWhiteCastleH=false;
      }
      else
      {
        canBlackCastleA=false;
        canBlackCastleH=false;
      }
    }
    
    if(moveUsed.getFrom()[0].equals("A1")||moveUsed.getTo()[0].equals("A1"))
    {
      canWhiteCastleA=false;
    }
    if(moveUsed.getFrom()[0].equals("H1")||moveUsed.getTo()[0].equals("H1"))
    {
      canWhiteCastleH=false;
    }
    if(moveUsed.getFrom()[0].equals("A8")||moveUsed.getTo()[0].equals("A8"))
    {
      canBlackCastleA=false;
    }
    if(moveUsed.getFrom()[0].equals("H8")||moveUsed.getTo()[0].equals("H8"))
    {
      canBlackCastleH=false;
    }
  }
  
  public Move getMoveUsed()
  {
    return moveUsed;
  }
  
  public int getGameValue()
  {
    int sum = 0;
    for(Piece p : layout)
    {
      if(p.getPlayer().equals("H"))
        sum-=p.getValue();
      else
        sum+=p.getValue();
    }
    return sum;
  }
  
  public int getTurn()
  {
    return turn;
  }
  
  public void addPiece(Piece p)
  {
    layout.add(p);
  }
  
  public boolean canWA()
  {
    return canWhiteCastleA;
  }
  
  public boolean canWH()
  {
    return canWhiteCastleH;
  }
  
  public boolean canBA()
  {
    return canBlackCastleA;
  }
  
  public boolean canBH()
  {
    return canBlackCastleH;
  }
  
  public boolean isWhiteChecked()
  {
    return isWhiteInCheck();
  }
  
  public boolean isBlackChecked()
  {
    return isBlackInCheck();
  }
  
  //sets up the pieces for a starting game
  private void setDefaultGame()
  {
    //human pieces
    layout.add(new Rook("H", "A1"));
    layout.add(new Knight("H", "B1"));
    layout.add(new Bishop("H", "C1"));
    layout.add(new Queen("H", "D1"));
    layout.add(new King("H", "E1"));
    layout.add(new Bishop("H", "F1"));
    layout.add(new Knight("H", "G1"));
    layout.add(new Rook("H", "H1"));
    layout.add(new Pawn("H", "A2"));
    layout.add(new Pawn("H", "B2"));
    layout.add(new Pawn("H", "C2"));
    layout.add(new Pawn("H", "D2"));
    layout.add(new Pawn("H", "E2"));
    layout.add(new Pawn("H", "F2"));
    layout.add(new Pawn("H", "G2"));
    layout.add(new Pawn("H", "H2"));
    //computer pieces
    layout.add(new Rook("C", "A8"));
    layout.add(new Knight("C", "B8"));
    layout.add(new Bishop("C", "C8"));
    layout.add(new Queen("C", "D8"));
    layout.add(new King("C", "E8"));
    layout.add(new Bishop("C", "F8"));
    layout.add(new Knight("C", "G8"));
    layout.add(new Rook("C", "H8"));
    layout.add(new Pawn("C", "A7"));
    layout.add(new Pawn("C", "B7"));
    layout.add(new Pawn("C", "C7"));
    layout.add(new Pawn("C", "D7"));
    layout.add(new Pawn("C", "E7"));
    layout.add(new Pawn("C", "F7"));
    layout.add(new Pawn("C", "G7"));
    layout.add(new Pawn("C", "H7"));
  }
  
  //is a piece's turn
  private boolean turnToMove(Piece p)
  {
    return ((p.getPlayer().equals("H")&&turn%2==1)||(p.getPlayer().equals("C")&&turn%2==0));
  }
  
  //returns all possoble moves as strings
  public ArrayList<Move> getAllPossibleMoves()
  {
    ArrayList<Move> moves = new ArrayList<Move>();
    
    for(Piece p : layout)
    {
      if(turnToMove(p))
      {
        for(Move move : p.getPossibleMoves(this))
          moves.add(move);
      }
    }
    return moves;
  }
  
  public void printPieces()
  {
    for(Piece p : layout)
    {
      System.out.println(p);
    }
  }
  
  public ArrayList<Piece> getLayout()
  {
    return layout;
  }
  
  public boolean isWhiteInCheck()
  {
    for(Piece p : layout)
    {
      if(p.getType()==6&&p.getPlayer().equals("H"))
      {
        return p.checked(layout);
      }
    }
    return true;
  }
  
  public boolean isBlackInCheck()
  {
    for(Piece p : layout)
    {
      if(p.getType()==6&&p.getPlayer().equals("C"))
      {
        return p.checked(layout);
      }
    }
    return true;
  }
}