import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ChessGraphics implements MouseListener
{
  final int windowStartWidth = 1020;
  final int windowStartHeight = 950;
  static Draw draw = new Draw();
  JFrame window;
  JPanel area;
  String name = "John's Chess";
  String version = "v1.3";
  
  public ChessGraphics(Game g)
  {
    name = name+" "+version+" /d"+Chess.firstDifficulty;
    if(Chess.testMode)
    {
      name = name + " TEST MODE";
    }
    
    window = new JFrame(name);
    window.setVisible(true);
    window.setSize(windowStartWidth, windowStartHeight);
    
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //window.setResizable(false);
    
    area = new JPanel(new BorderLayout());
    area.setSize(1020, 950);
    window.add(area);
    area.add(draw);
    
    draw.addMouseListener(this);
    
    draw.drawing(g);
  }
  
  public void drawNewGame(Game g)
  {
    draw.drawing(g);
  }
  
  /*
  public void diffUp()
  {
    name = ("John's Chess " + version + " /d" + Chess.firstDifficulty);
    if(Chess.testMode)
    {
      name = name + " TEST MODE";
    }
    window.setName(name);
  }
  */
  
  //mouse listener methods***********
  
  public void mousePressed(MouseEvent e)
  {
    
  }
  
  public void mouseReleased(MouseEvent e)
  {
    
  }
  
  public void mouseEntered(MouseEvent e)
  {
    
  }
  
  public void mouseExited(MouseEvent e)
  {
    
  }
  
  public void mouseClicked(MouseEvent e)
  {
    draw.mouseClicked(e.getX(), e.getY());
  }
  
  public void stalemate()
  {
    draw.stalemate();
  }
  
  public void checkmate(String wins)
  {
    draw.checkmate(wins);
  }
  
  public void doRepaint()
  {
    draw.repaint();
  }
  
  public int getWinWidth()
  {
     return window.getWidth();
  }
  
  public int getWinHeight()
  {
    return window.getHeight();
  }
  
  public double getScale()
  {
    return Math.min(((double)window.getWidth()/windowStartWidth), ((double)window.getHeight()/windowStartHeight));
  }
  
}