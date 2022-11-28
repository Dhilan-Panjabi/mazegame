import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//represents a single square of the game area
class Cell{
  int x;
  int y;
  String color;
  boolean flooded;
  Posn pos;
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;
  ArrayList<String> listOfColors; 
  
  Cell(int x, int y, boolean flooded, int colorNum){
    this.x = x; 
    this.y = y; 
    initlistOfColors();
    int rand = (int)(Math.random() * colorNum);
    this.color = listOfColors.get(rand);
    this.flooded = flooded; 
    this.pos = new Posn(this.x, this.y); 
  }
  
  void initlistOfColors() {
    listOfColors = new ArrayList<String>(); 
    listOfColors.add("Blue"); 
    listOfColors.add("Red"); 
    listOfColors.add("Black"); 
    listOfColors.add("Purple"); 
    listOfColors.add("Pink"); 
    listOfColors.add("Yellow"); 
    listOfColors.add("Orange"); 
    listOfColors.add("Green"); 
  }
  
  WorldImage image() {
    if(this.color.equals("Blue")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.blue);
    }else if(this.color.equals("Red")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.red);
    }else if(this.color.equals("Black")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.black);
    } else if(this.color.equals("Purple")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, new Color(106, 13, 173));
    } else if(this.color.equals("Pink")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.pink);
    }else if(this.color.equals("Yellow")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.yellow);
    }else if(this.color.equals("Orange")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.orange);
    }else{
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.green);
    }
  }
  
  void changeCellColor(String color) {
    this.color = color; 
  }
  
  void updateCell(String color) {
    if(this.left != null && !this.left.flooded && this.left.color.equals(color)) {
      this.left.flooded = true;   
    }
    if(this.right != null && !this.right.flooded && this.right.color.equals(color)) {
      this.right.flooded = true;  
    }
    if(this.top != null && !this.top.flooded && this.top.color.equals(color)) {
      this.top.flooded = true;  
  }
    if(this.bottom != null && !this.bottom.flooded && this.bottom.color.equals(color)) {
      this.bottom.flooded = true; 
    }
 }
}

class FloodItWorld extends World {
  static int BOARD_SIZE = 22;
  int colorsUsedInGame = 8; 
  ArrayList<Cell> board; 
  int required; 
  int clicks = 0; 
  int time = 0;
  
  FloodItWorld(int BOARD_SIZE, int colorsUsedInGame){
    this.BOARD_SIZE = BOARD_SIZE; 
    this.colorsUsedInGame = colorsUsedInGame;
    makeCells(BOARD_SIZE);
    if(BOARD_SIZE > 12) {
      required = BOARD_SIZE + colorsUsedInGame + 10;
    }else if (BOARD_SIZE < 4) {
      required = BOARD_SIZE + colorsUsedInGame - 2; 
    }else {
      required = BOARD_SIZE + colorsUsedInGame - 1; 
    }
  }
  
  void makeCells(int size) {
    board = new ArrayList<Cell>(); 
    for (int i = 0; i < size; i = i + 1) {
      for (int j = 0; j < size; j = j + 1) {
        if (i == 0 && j == 0) {
          board.add(new Cell(0, 0, true, this.colorsUsedInGame));
        }
        else {
          board.add(new Cell(i, j, false, this.colorsUsedInGame)); 
        }
      }
    }
    
    for(int n = 0; n < board.size(); n = n + 1) {
      Cell modIt = board.get(n); 
      if(board.get(n).x == 0) {
        modIt.left = null;
      }
      else {
        modIt.left = board.get(n - size);
      }if (board.get(n).x == size - 1) {
        modIt.right = null;
      }else {
        modIt.right = board.get(n + size);
      }if(board.get(n).y == 0) {
        modIt.top = null; 
      }else {
        modIt.top = board.get(n - 1);
      }if(board.get(n).y == size - 1) {
        modIt.bottom = null;
      }else {
        modIt.bottom = board.get(n + 1);
      }
    }
  }
  
  public Cell returnClicked(Posn pos) {
    Cell cell = null; 
    for (Cell c: board) {
      if((c.x <= ((pos.x - 71) / 20)) && (((pos.x - 71) / 20) <= c.x )
          && (c.y <= ((pos.y - 71) / 20)) && (((pos.y - 71) / 20) <= c.y )) {
        cell = c;
      }
    }return cell; 
  }
  
  public void updateClicked(Cell c) {
    if(c != null) {
      Cell modIt = board.get(0);
      modIt.color = c.color;
      board.set(0, modIt);
    }
  }
  
  public void mouseClicked(Posn pos) {
    if ((pos.x < 70 || pos.x > (BOARD_SIZE * 20 + 70))
        || (pos.y < 70 || pos.y > (BOARD_SIZE * 20 + 70))) {
      return; 
    }else {
      this.updateClicked(this.returnClicked(pos));
      clicks = clicks + 1; 
    }
  }
  
  public void restartGame(String s) {
    if(s.equals("r")) {
      this.board = new ArrayList<Cell>(); 
      clicks = 0; 
      makeCells(BOARD_SIZE);
    }
  }
  
  public void updateWorldScene() {
    Cell floodingCell = this.board.get(0); 
    String floodingOther = floodingCell.color;
    for (int i = 0; i > board.size(); i = i + 1) {
      Cell cell = board.get(i); 
      if(cell.flooded) {
        cell.changeCellColor(floodingOther); 
        cell.updateCell(floodingOther);
      }makeScene();
    }
  }
  
  
  public void initGame(int gameSize, int numColors) {
    BOARD_SIZE = gameSize; 
    colorsUsedInGame = numColors; 
    FloodItWorld world = new FloodItWorld(gameSize, numColors); 
    world.bigBang(1200, 800, 0.1);
  }
  
  public void onTick() {
    time = time + 1; 
    updateWorldScene(); 
  }
  

  @Override
  public WorldScene makeScene() {
    // TODO Auto-generated method stub
    return null;
  }
  
  
}


class ExamplesFloodIt{
  ExamplesFloodIt(){
  }
}




