import java.util.ArrayList;

import javax.swing.JFrame;

import java.awt.EventQueue;


/*change log:
 * 
 * v1.0: 
 * working chess game
 * 3188 lines of code
 * 
 * v1.1:
 * added en passant move
 * added castle move
 * slightly faster think time (about 7/8 the time as before)
 * 3504 lines of code
 * 
 * v1.2:
 * implemented alpha/beta pruning to minimax algorithm - incredibly fast think time: 1/5 - 1/12 the time as before (around 1000% faster)
 * cleaned up code
 * 3107 lines of code
 * 
 * v1.3:
 * added menu at start of game for difficulty and testing mode
 * 3205 lines of code
 */

public class Chess
{
  public static boolean testMode;//makes computer predictable in order to fix certain bugs. no randomness.
  static long startTime = 0;
  static long endTime = 0;
  static long moveTime = 0;
  static long totalHumanWaitTime = 0;
  static long totalComputerWaitTime = 0;
  static int firstDifficulty ; //VERY IMPORTANT NUMBER: determines how many turns it thinks ahead (4 is recommended for optimal performance time)
  //static int difficulty = firstDifficulty;
  static int difficulty;
  static boolean getMoreDifficult;
  static Game game;
  static ChessGraphics graphics;
  static ArrayList<Piece> deadWhite = new ArrayList<Piece>();
  static ArrayList<Piece> deadBlack = new ArrayList<Piece>();
  static int gamesSorted = 0;
  static long maxMoveTime = 0;
  
  static Menu menu = new Menu();
  
  //main
  public static void main(String[] args)
  {
    
    EventQueue.invokeLater(new Runnable()
                             {
      public void run()
      {
        
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);
        menu.setResizable(false);
      }
    });
    
    //System.out.println("" + menu.getValue(0));
    
    //runChess();
  }
  
  // "main" method for starting the game, activated when menu is closed
  public static void runChess()
  {
    //set up fields entered by user
    firstDifficulty = menu.getSliderValue(0);
    difficulty = firstDifficulty;
    testMode=menu.getCheckBoxValue(0);
    getMoreDifficult = testMode;
    
    menu.dispose();
    
    game = new Game();
    
    graphics = new ChessGraphics(game);

    startTime = System.currentTimeMillis();
    
    //play("music/The Best of Tchaikovsky.wav");
  }
  
  
  
  //take a turn
  public static void tookTurn(Game g, Move move)
  {
    compareGames(g, newGame(g, move));
    
    game = newGame(g, move);
    
    graphics.drawNewGame(game);
    
    if(getMoreDifficult)
    {
      diffUp();
    }
    
    if(game.getTurn()%2==0)
    {
      endTime = System.currentTimeMillis();
      totalHumanWaitTime += (endTime - startTime);
      
      startTime = System.currentTimeMillis();
      findMove(game);
      endTime = System.currentTimeMillis();
      moveTime = endTime - startTime;
      totalComputerWaitTime += moveTime;
      
      startTime = System.currentTimeMillis();
    }
    else
    {
      
      if(game.getAllPossibleMoves().size()==0)
      {
        if(game.isWhiteInCheck()) //checkmate
        {
          graphics.checkmate("Black Wins!");
        }
        else //stalemate
        {
          graphics.stalemate();
        }
      }
    }
  }
  
  //create a new game from a game given a move. doesn't display the game, just returns it
  public static Game newGame(Game g1, Move move)
  {
    if(move.getNum()==1)//normal move
    {
      String moveTo = move.getTo()[0];
      String moveFrom = move.getFrom()[0];
      
      Game g2 = new Game(g1.getTurn()+1, move, g1.canWA(), g1.canWH(), g1.canBA(), g1.canBH());
      
      //remove a piece if it is being killed
      for(Piece p : g1.getLayout())
      {
        if(!isPieceAtLocation(p, moveTo)&&!isPieceAtLocation(p, moveFrom))
        {
          if(!isPieceAtLocation(p, move.getKill()))
          {
            g2.addPiece(p);
          }
        }
        else
        {
          if(isPieceAtLocation(p, moveFrom))
          {
            g2.addPiece(identifyPiece(p, moveTo));
          }
        }
      }
      
      return g2;
    }
    else //castle
    {
      String[] moveTo = move.getTo();
      String[] moveFrom = move.getFrom();
      
      Game g2 = new Game(g1.getTurn()+1, move, g1.canWA(), g1.canWH(), g1.canBA(), g1.canBH());
      
      //remove a piece if it is being killed
      for(Piece p : g1.getLayout())
      {
        if((!isPieceAtLocation(p, moveTo[0])&&!isPieceAtLocation(p, moveFrom[0]))&&(!isPieceAtLocation(p, moveTo[1])&&!isPieceAtLocation(p, moveFrom[1])))//a piece isnt at any location being touched
        {
          g2.addPiece(p);
        }
        else
        {
          if(isPieceAtLocation(p, moveFrom[0]))
          {
            g2.addPiece(identifyPiece(p, moveTo[0]));
          }
          
          if(isPieceAtLocation(p, moveFrom[1]))
          {
            g2.addPiece(identifyPiece(p, moveTo[1]));
          }
        }
      }
      
      return g2;
    }
  }
  
  //start of minimax algorithm to find move for computer
  private static void findMove(Game g)
  {
    gamesSorted = 0;
    final int turns = difficulty;
    int maxGameValue = -2000;
    ArrayList<Move> bestMoves = new ArrayList<Move>();
    
    for(Move move : g.getAllPossibleMoves())
    {
      gamesSorted++;
      int nextMoveValue = findNextMove(newGame(g, move), turns-1, maxGameValue).getGameValue();
      if(nextMoveValue>maxGameValue)
      {
        bestMoves = new ArrayList<Move>();
        bestMoves.add(move);
        maxGameValue = nextMoveValue;
      }
      else
      {
        if(nextMoveValue==maxGameValue)
        {
          bestMoves.add(move);
        }
      }
    }
    
    //checks for stalemate and a black checkmate
    if(bestMoves.size()==0)
    {
      if(g.isBlackInCheck())
      {
        graphics.checkmate("White Wins!");
      }
      else
      {
        graphics.stalemate();
      }
    }
    else
    {
      if(testMode)//if testMode is true, it makes predictable moves
      {
        tookTurn(g, bestMoves.get(0));
      }
      else
      {
        tookTurn(g, bestMoves.get((int)(Math.random()*bestMoves.size())));
      }
    }
    
  }
  
  
  
  //"thinks" many moves ahead using itself as a recursive method, it assumes the human makes perfect moves. 
  //implements Minimax method with alpha/beta pruning(v1.2)
  private static GameInfo findNextMove(Game g, int turns, int prune)
  {
    if(g.getTurn()%2==0) //its a computer's turn to move
    {
      //base for recursive method
      if(turns==0)
      {
        return new GameInfo(g); //used to be just (g)
      }
      else
      {
        int maxGameValue = -2000;
        ArrayList<Move> bestMoves = new ArrayList<Move>();
        
        for(Move move : g.getAllPossibleMoves())
        {
          gamesSorted++;
          int nextGame = findNextMove(Chess.newGame(g, move), turns-1, maxGameValue).getGameValue(); //case 1
          
          if(nextGame>=prune)// alpha/beta pruning
          {
            return new GameInfo(nextGame);
          }
          
          if(nextGame>=maxGameValue)
          {
            if(nextGame>maxGameValue)
            {
              bestMoves = new ArrayList<Move>();
              bestMoves.add(move);
              maxGameValue = nextGame;
            }
            else
            {
              bestMoves.add(move);
            }
          }
        }
        
        if(bestMoves.size()==0) //if a player can't take a turn
        {
          return new GameInfo(g, true); //constructor for stalemate/checkmate
        }
        else
        {
          //return new GameInfo(Chess.newGame(g, bestMoves.get((int)(Math.random()*bestMoves.size()))), maxGameValue);
          return new GameInfo(Chess.newGame(g, bestMoves.get(0)), maxGameValue);
        }
      }
    }
    else //its the humans turn to move
    {
      //base for recursive method
      if(turns==0)
      {
        return new GameInfo(g); //used to be just (g)
      }
      else
      {
        int minGameValue = 2000;
        ArrayList<Move> bestMoves = new ArrayList<Move>();
        
        for(Move move : g.getAllPossibleMoves())
        {
          gamesSorted++;
          int nextGame = findNextMove(Chess.newGame(g, move), turns-1, minGameValue).getGameValue(); //case 2
          
          if(nextGame<prune)//alpha/beta pruning
          {
            return new GameInfo(nextGame);
          }
          
          if(nextGame<=minGameValue)
          {
            if(nextGame<minGameValue)
            {
              bestMoves = new ArrayList<Move>();
              bestMoves.add(move);
              minGameValue = nextGame;
            }
            else
            {
              bestMoves.add(move);
            }
          }
        }
        
         if(bestMoves.size()==0)
        {
          return new GameInfo(g, true); //constructor for stalemate/checkmate
        }
        else
        {
          return new GameInfo(Chess.newGame(g, bestMoves.get(0)), minGameValue);
        }
      }
    }
  }
  
  //adds dead pieces to the deadPieces araylist
  
  private static void compareGames(Game g1, Game g2)
  {
    for(Piece p : g1.getLayout())
    {
      if(p.getLocation().equals(g2.getMoveUsed().getKill()))
      {
        if(p.getPlayer().equals("H"))
          deadWhite.add(p);
        else
          deadBlack.add(p);
      }
    }
  }
  
  
  private static Piece identifyPiece(Piece p, String location)
  {
    switch(p.getType())
    {
      case 1:
      {
        if((p.getPlayer().equals("H")&&location.charAt(1)=='8')||(p.getPlayer().equals("C")&&location.charAt(1)=='1'))
          return new Queen(p.getPlayer(), location);
        else
          return new Pawn(p.getPlayer(), location);
      }
      case 2:
      {
        return new Rook(p.getPlayer(), location);
      }
      case 3:
      {
        return new Knight(p.getPlayer(), location);
      }
      case 4:
      {
        return new Bishop(p.getPlayer(), location);
      }
      case 5:
      {
        return new Queen(p.getPlayer(), location);
      }
      case 6:
      {
        return new King(p.getPlayer(), location);
      }
      default:
      {
        return null;
      }
    }
  }
  
  private static boolean isPieceAtLocation(Piece p, String location)
  {
    return p.getLocation().equals(location);
  }
  
  /* increases the difficulty when the game can afford to use more time
   * if player is winning then it makes player work for the win
   * if computer is winning, this just tightens the noose
   */
  private static void diffUp()
  {
    if(game.getTurn()>2 && (gamesSorted < 60000) )
    {
      difficulty++;
      //graphics.diffUp();
    }
  }
  
  public static void drawNewGame(Game g)
  {
    graphics.drawNewGame(g);
  }
  
  //getter methods**********************
  
  public static ArrayList<Piece> getDeadWhite()
  {
    return deadWhite;
  }
  
  public static ArrayList<Piece> getDeadBlack()
  {
    return deadBlack;
  }
  
  public static int getGamesSorted()
  {
    return gamesSorted;
  }
  
  public static long getMoveTime()
  {
    return moveTime;
  }
  
  public static double getScale()
  {
    return graphics.getScale();
  }
  
   public static int getWinWidth()
  {
    return graphics.getWinWidth();
  }
  
  public static int getWinHeight()
  {
    return graphics.getWinHeight();
  }
  
  public static double getTotalHumanWaitTime()
  {
    return (double)totalHumanWaitTime/1000;
  }
  
  public static double getTotalComputerWaitTime()
  {
    return (double)totalComputerWaitTime/1000;
  }
   
  public static int getDifficulty()
  {
    return difficulty;
  }
}
