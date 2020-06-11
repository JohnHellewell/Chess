public class GameInfo
{
  int games, value, numPossibleMoves;
  boolean savedValue;
  Move move;
  Game game;
  
  public GameInfo(Game g)
  {
    move = g.getMoveUsed();
    game = g;
    numPossibleMoves = 1;
    savedValue = false;
  }
  
  public GameInfo(Game g, int i)
  {
    move = g.getMoveUsed();
    game = g;
    value = i;
    savedValue = true;
  }
  
  public GameInfo(Game g, boolean b)
  {
    move = g.getMoveUsed();
    game = g;
    
    if(b)
      numPossibleMoves = 0;
    else
      numPossibleMoves = 1;
    
    savedValue = false;
  }
  
  public GameInfo(int i)
  {
    value=i;
    savedValue=true;
  }
  
  //getter methods***********
  
  public Game getGame()
  {
    return game;
  }
  
  public Move getMove()
  {
    return move;
  }
  
  public int getGamesCalculated()
  {
    return games;
  }
  
  //always returns the game value unless there is a checkmate/stalemate. 
  //I had problems with the computer not putting the player in checkmate because a chackmate isn't a "valuable" move
  public int getGameValue()
  {
    if(numPossibleMoves>0||savedValue) //if there is no checkmate/stalemate
    {
      if(savedValue)
        return value;
      else
        return game.getGameValue();
    }
    else //if there is a checkmate/stalemate
    {
      if(game.getTurn()%2==0) //computer's turn (computer cannot make a move)
      {
        if(game.isBlackChecked()) //if black is in checkmate (can't make any moves and is in check)
        {
          return -1000; //computer will value this outcome as very poor
        }
        else //black is in stalemate (can't make any moves but isn't in check)
        {
          //wants to see if a stalemate is a good option (whether or not the computer is winning)
          if(game.getGameValue()<0)
          {
            return 100; //wants a stalemate because it is losing
          }
          else
          {
            return -100; //doesn't want a stalemate because it is winning
          }
        }
      }
      else //player's turn (player cannot make a move)
      {
        if(game.isWhiteChecked()) //if white is in checkmate (can't make any moves and is in check)
        {
          return 1000 - game.getTurn(); //computer will value this outcome as very valuable (because it wants to put white in checkmate)
        }
        else //black is in stalemate (can't make any moves but isn't in check)
        {
          //wants to see if a stalemate is a good option (whether or not the computer is winning)
          if(game.getGameValue()<0)
          {
            return 100; //wants a stalemate because it is losing
          }
          else
          {
            return -100; //doesn't want a stalemate because it is winning
          }
        }
      }
    }
  }
  
}