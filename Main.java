import java.util.Scanner;

class Main {

  String[][] chessBoard = {
      {"r", "n", "b", "q", "k", "b", "n", "r"},
      {"p", "p", "p", "p", "p", "p", "p", "p"},
      {" ", " ", " ", " ", " ", " ", " ", " "},
      {" ", " ", " ", " ", " ", " ", " ", " "},
      {" ", " ", " ", " ", " ", " ", " ", " "},
      {" ", " ", " ", " ", " ", " ", " ", " "},
      {" ", " ", " ", " ", " ", " ", " ", " "},
      {"P", "P", "P", "P", "P", "P", "P", "P"},
      {"R", "N", "B", "Q", "K", "B", "N", "R"}
    };
    boolean boolIsGameWon = false;


  public static void main(String[] args) {
    System.out.println("Hello world!");
    boolean boolIsGameWon = false;
    

    while (boolIsGameWon == false){

    }
  }

  public void printBoard(){
    for(int row=0; row > chessBoard.length; row++){
      for(int column=0; column > chessBoard[row].length; column++){
        System.out.print(chessBoard[row][column]);
      }
      System.out.println();
    }
  }
}