public class Move
{
  String[] from, to;
  String kill, player;
  int type;
  boolean enPassant = false;
  boolean castle = false;
  
  public Move(String[] from, String[] to, int type, String player)
  {
    this.from = from;
    this.to = to;
    kill = to[0];
    this.type = type;
    this.player = player;
    if(from.length>1)
    {
      castle=true;
    }
  }
  
  public Move(String[] from, String[] to, int type, String player, String kill)
  {
    this.from = from;
    this.to = to;
    this.type=type;
    this.player = player;
    this.kill = kill;
    enPassant = true;
  }
  
  public boolean isCastle()
  {
    return castle;
  }
  
  public boolean isEnPassant()
  {
    return enPassant;
  }
  
  public int getNum()
  {
    return from.length;
  }
  
  public String getPlayer()
  {
    return player;
  }
  
  public int getType()
  {
    return type;
  }
  
  public String[] getFrom()
  {
    return from;
  }
  
  public String[] getTo()
  {
    return to;
  }
  
  public String getKill()
  {
    return kill;
  }
  
}