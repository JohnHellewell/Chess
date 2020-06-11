import java.util.ArrayList;
import javax.swing.*;

import com.sun.tools.javac.Main;

//import com.sun.tools.javac.Main;
import java.net.URL;

import java.awt.*;
import java.awt.Graphics;

public class Draw extends JPanel
{
  
	private static final long serialVersionUID = 1L;
	int turn;
  double scale = 1.0;
  Piece selected;
  ArrayList<Piece> pieces;
  
  Game game;
  boolean stalemate = false;
  boolean checkmate = false;
  String win = "";
  
  //constructor
  public Draw()
  {
    
  }
  
  //piece icon image files
  ImageIcon bp = new ImageIcon("icons/BlackPawn.png");
  ImageIcon wp = new ImageIcon("icons/WhitePawn.png");
  ImageIcon br = new ImageIcon("icons/BlackRook.png");
  ImageIcon wr = new ImageIcon("icons/WhiteRook.png");
  ImageIcon bk = new ImageIcon("icons/BlackHorse.png");
  ImageIcon wk = new ImageIcon("icons/WhiteHorse.png");
  ImageIcon bb = new ImageIcon("icons/BlackBishop.png");
  ImageIcon wb = new ImageIcon("icons/WhiteBishop.png");
  ImageIcon bx = new ImageIcon("icons/BlackKing.png");
  ImageIcon wx = new ImageIcon("icons/WhiteKing.png");
  ImageIcon bq = new ImageIcon("icons/BlackQueen.png");
  ImageIcon wq = new ImageIcon("icons/WhiteQueen.png");
  
  public void drawing(Game g)
  {
    game = g;
    turn = g.getTurn();
    pieces = g.getLayout();
    selected = null;
    
    if(turn%2==0)
      paintComponent(getGraphics());
    else
      repaint();
  }
  
  @Override 
  public void paintComponent(Graphics g)
  { 
    super.paintComponent(g);
    
    scale = Chess.getScale();
    //background
    g.setColor(Color.GRAY);
    g.fillRect(0, 0, Chess.getWinWidth(), Chess.getWinHeight());
    
    //board border (board-er haha)
    g.setColor(Color.BLACK);
    g.fillRect(toScale(45), toScale(20), toScale(810), toScale(810));
    
    drawTiles(g);
    
    // 1-8
    g.setColor(Color.BLACK);
    for(int i=1; i<=8; i++)
    {
      g.drawString("" + (9-i), toScale(30), toScale(i*100-25));
    }
    
    // A-H
    g.setColor(Color.BLACK);
    for(int i=0; i<8; i++)
    {
      g.drawString("" + (char)('A' + i), toScale(i*100+100), toScale(850));
    }
    
    //who's turn
    g.setColor(Color.BLACK);
    if(turn%2==0)
      g.drawString("Black's Turn", toScale(50), toScale(15));
    else
      g.drawString("White's Turn", toScale(50), toScale(15));
    
    //if a player is in check
    if(!stalemate&&!checkmate)
    {
      if(game.isWhiteChecked())
      {
        g.setColor(Color.RED);
        g.drawString("White Check!", toScale(650), toScale(875));
      }
      
      if(game.isBlackChecked())
      {
        g.setColor(Color.RED);
        g.drawString("Black Check", toScale(650), toScale(875));
      }
    }
    else
    {
      if(stalemate)
      {
        g.setColor(Color.RED);
        g.drawString("STALEMATE!", toScale(650), toScale(875));
      }
      else //checkmate
      {
        g.setColor(Color.RED);
        g.drawString("CHECKMATE! " + win, toScale(650), toScale(875));
      }
    }
    
    drawDeadPieces(g);
    
    if(turn%2==1&&turn!=1)
    {
      g.setColor(Color.BLACK);
      g.drawString(addCommas((int)Chess.getGamesSorted()) + " possible moves calculated in " + addCommas((int)Chess.getMoveTime()) + " miliseconds", toScale(50), toScale(875));
    }
    else
    {
      if(turn!=1)
      {
        g.setColor(Color.BLACK);
        g.drawString("thinking"+getDots(), toScale(50), toScale(875));
      }
    }
  }
  
  //painting methods
  
  private void drawDeadPieces(Graphics g)
  {
    //border
    g.setColor(Color.BLACK);
    g.fillRect(toScale(870), toScale(20), toScale(110), toScale(810));
    g.setColor(Color.GRAY);
    g.fillRect(toScale(875), toScale(25), toScale(100), toScale(800));
    
    //dead black pieces
    for(int i=0; i<Chess.getDeadBlack().size(); i++)
    {
      g.drawImage(getIcon(Chess.getDeadBlack().get(i)), toScale(getDeadX(Chess.getDeadBlack().get(i), i)), toScale(780 - (i%8)*50), toScale(getDeadXWidth(Chess.getDeadBlack().get(i))), toScale(40), null);
    }
    
    //dead white pieces
    for(int i=0; i<Chess.getDeadWhite().size(); i++)
    {
      g.drawImage(getIcon(Chess.getDeadWhite().get(i)), toScale(getDeadX(Chess.getDeadWhite().get(i), i)), toScale(30 + (i%8)*50), toScale(getDeadXWidth(Chess.getDeadWhite().get(i))), toScale(40), null);
    }
    
    //score
    g.setColor(Color.BLACK);
    g.drawString("Score: " + game.getGameValue()*-1, toScale(875), toScale(850));
  }
  
  //VERY IMPORTANT: is used to make the game shrink/grow to ratio based off window size
  private int toScale(int i)
  {
    return (int)(scale * i);
  }
  
  private int getDeadXWidth(Piece p)
  {
    if(p.getType()==1)
    {
      return 30;
    }
    else
    {
      return 40;
    }
  }
  
  private int getDeadX(Piece p, int i)
  {
    if(p.getType()==1)
    {
      return 885 + ((i/8)*50);
    }
    else
    {
      return 880 + ((i/8)*50);
    }
  }
  
  private void drawTiles(Graphics g)
  {
    //alternating squares
    for(int i=0; i<8; i++)
    {
      for(int j=0; j<8; j++)
      {
        if(i==1&&j==1)
          g.setColor(Color.WHITE);
        else
          g.setColor(changeColor(i, j));
        
        g.fillRect(toScale(i*100 + 50), toScale(j*100 + 25), toScale(100), toScale(100));
      }
    }
    
    if(selected!=null)
      drawSelectedTiles(selected, g);
    
    drawPieces(g);
  }
  
  private void drawPieces(Graphics g)
  {
    //draw pieces
    for(Piece p : pieces)
    {
      g.drawImage(getIcon(p), toScale(getPieceX(p)), toScale(getPieceY(p)), toScale(getPieceWidth(p)), toScale(80), null);
    }
  }
  
  private void drawSelectedTiles(Piece p, Graphics g)
  {
    for(int i=0; i<8; i++)
    {
      for(int j=0; j<8; j++)
      {
        //shows last move computer made
        if(turn>1)
        {
          for(int k=0; k<game.getMoveUsed().getNum(); k++)
          {
            if(mouseToLocation(toScale(i*100 + 50), toScale(j*100 + 25)).equals(game.getMoveUsed().getFrom()[k])||mouseToLocation(toScale(i*100 + 50), toScale(j*100 + 25)).equals(game.getMoveUsed().getTo()[k]))
            {
              g.setColor(Color.CYAN);
              g.fillRect(toScale(i*100 + 50), toScale(j*100 + 100), toScale(100), toScale(25));
            }
          }
        }
        
        if(mouseToLocation(toScale(i*100 + 50), toScale(j*100 + 25)).equals(p.getLocation()))
        {
          //set color
          g.setColor(Color.GREEN);
          g.fillRect(toScale(i*100 + 50), toScale(j*100 + 25), toScale(100), toScale(100));
          g.setColor(changeColor(i, j));
          g.fillRect(toScale(i*100 + 60), toScale(j*100 + 35), toScale(80), toScale(80));
        }
        
        for(Move m : p.getPossibleMoves(game))
        {
          String[] loc = m.getTo();
          for(int k=0; k<m.getNum(); k++)
          {
            if(mouseToLocation(toScale(i*100 + 50), toScale(j*100 + 25)).equals(loc[k]))
            {
              if(p.landsOn(mouseToLocation(toScale(i*100 + 50), toScale(j*100 + 25)), game.getLayout())==0)
              {
                if(m.isEnPassant())
                {
                  g.setColor(Color.RED);
                }
                else
                {
                  g.setColor(Color.BLUE);
                }
              }
              else
              {
                g.setColor(Color.RED);
              }
              
              g.fillRect(toScale(i*100 + 50), toScale(j*100 + 25), toScale(100), toScale(100));
              g.setColor(changeColor(i, j));
              g.fillRect(toScale(i*100 + 60),toScale( j*100 + 35), toScale(80), toScale(80));
              
              if(turn>1)
              {
                if(mouseToLocation(toScale(i*100 + 50), toScale(j*100 + 25)).equals(game.getMoveUsed().getFrom())||mouseToLocation(toScale(i*100 + 50), toScale(j*100 + 25)).equals(game.getMoveUsed().getFrom()))
                {
                  g.setColor(Color.CYAN);
                  g.fillRect(toScale(i*100 + 60), toScale(j*100 + 100), toScale(80), toScale(15));
                }
              }
            }
          }
        }
      }
    }
  }
  
  //private methods***********
  
  private int getPieceWidth(Piece p)
  {
    if(p.getType()==1)
    {
      return 60;
    }
    else
      return 80;
  }
  
  private int getPieceX(Piece p)
  {
    if(p.getType()==1)
      return 70 + ((p.getLocation().charAt(0)-'A')*100);
    else
      return 60 + ((p.getLocation().charAt(0)-'A')*100);
  }
  
  private int getPieceY(Piece p)
  {
    return 735 - ((p.getLocation().charAt(1)-'1')*100);
  }
  
  private Image getIcon(Piece p)
  {
    switch(p.getType())
    {
      case 1:
      {
        if(p.getPlayer().equals("H"))
          return wp.getImage();
        else
          return bp.getImage();
      }
      case 2:
      {
        if(p.getPlayer().equals("H"))
          return wr.getImage();
        else
          return br.getImage();
      }
      case 3:
      {
        if(p.getPlayer().equals("H"))
          return wk.getImage();
        else
          return bk.getImage();
      }
      case 4:
      {
        if(p.getPlayer().equals("H"))
          return wb.getImage();
        else
          return bb.getImage();
      }
      case 5:
      {
        if(p.getPlayer().equals("H"))
          return wq.getImage();
        else
          return bq.getImage();
      }
      case 6:
      {
        if(p.getPlayer().equals("H"))
          return wx.getImage();
        else
          return bx.getImage();
      }
      default:
      {
        return null;
      }
    }
  }
  
  
  //used to help draw checkerboard pattern
  private Color changeColor(int i, int j)
  {
    if(i%2==0)
    {
      if(j%2==0)
      {
        return Color.WHITE;
      }
      else
      {
        return Color.DARK_GRAY;
      }
    }
    else
    {
      if(j%2==0)
      {
        return Color.DARK_GRAY;
      }
      else
      {
        return Color.WHITE;
      }
    }
  }
  
  //mouse related methods************
  
  public void mouseClicked(int x, int y)
  {
    click(mouseToLocation(x, y));
  }
  
  //converts mouse coordinates to chess coordinates (such as x:124 y:334 to D4, etc)
  private String mouseToLocation(int x, int y)
  {
    String X;
    String Y;
    if(x>=toScale(50))
      X = "" + (char)((x-toScale(50))/toScale(100)+'A');
    else
      X = "" + (char)('A'-1);
    
    if(y>=toScale(25))
      Y = "" + ('9'-(char)((y-toScale(25))/toScale(100) + '1'));
    else
      Y = "" + '0';
    
    return X+Y;
  }
  
  //"brain" for picking up and moving pieces
  private void click(String loc)
  {
    if(selected==null)
      clickedOn(loc);
    else
    {
      if(canMoveTo(selected, loc))
      {
        Chess.tookTurn(game, moveTo(selected, loc));
      }
      else
      {
        clickedOn(loc);
      }
    }
  }
  
  //checks if a selected piece can move to squre that was just clicked on
  private boolean canMoveTo(Piece p, String loc)
  {
    for(Move s : p.getPossibleMoves(game))
    {
      if(loc.equals(s.getTo()[0]))
        return true;
    }
    return false;
  }
  
  private Move moveTo(Piece p, String loc)
  {
    for(Move s : p.getPossibleMoves(game))
    {
      if(loc.equals(s.getTo()[0]))
        return s;
    }
    return null;
  }
  
  //checks to see if it is a piece's turn to move
  private boolean turnToMove(Piece p)
  {
    return ((p.getPlayer().equals("H")&&turn%2==1)||(p.getPlayer().equals("C"))&&turn%2==0);
  }
  
  //gets piece from location clicked
  private void clickedOn(String loc)
  {
    for(Piece p : pieces)
    {
      if(p.getLocation().equals(loc)&&(turnToMove(p)&&p.getPlayer().equals("H")))
      {
        if(selected == null)
        {
          selected = p;
          repaint();
        }
        else
        {
          if(p.getLocation().equals(selected.getLocation()))
            selected = null;
          else
          {
            selected = p;
          }
          repaint();
        }
      }
    }
  }
  
  public void stalemate()
  {
    stalemate = true;
    repaint();
  }
  
  public void checkmate(String wins)
  {
    checkmate = true;
    win = wins;
    repaint();
  }
  
  public void doRepaint()
  {
    repaint();
  }
  
  //adds commas into long numbers (example, 123456789 -> "123,456,789") 
  //only works on positive numbers, but there will only be positive numbers used in this program
  private String addCommas(int i)
  {
    if(i<1000)
      return "" + i;
    else
    {
      return addCommas(i/1000) + "," + addZeros(i%1000);
    }
  }
  
  //helps addCommas()
  private String addZeros(int i)
  {
    if(i>=100)
      return "" + i;
    else
    {
      if(i>=10)
      {
        return "0" + i;
      }
      else
      {
        if(i==0)
        {
          return "000";
        }
        else
        {
          return "00" + i;
        }
      }
    }
  }
  
  //returns as many dots as there are difficulty levels. Is only used when computer is taking its turn and says "thinking....", that way I know what level difficulty it is on
  private String getDots()
  {
    String temp = "";
    for(int i=0; i<Chess.getDifficulty(); i++)
    {
      temp = temp + ". ";
    }
    return temp;
  }
  
}