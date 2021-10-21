import java.util.Scanner;

class Main {
  public static void main(String[] args) {
    //declaring variables
    boolean boolIsGameWon = false;
    byte playerTurn = 1;
    String nextMove;

    //importing local files
    boardManager boardManager = new boardManager();
    Scanner scan = new Scanner(System.in);

    System.out.println("Hello world!");

    
    while (boolIsGameWon == false){
      System.out.println();
      boardManager.printBoard();
      //get the next move from the user
      if(playerTurn == 1){
        System.out.println("It's player 1's turn!");
      } else {
        System.out.println("It's player 2's turn!");
      }
      System.out.println("Enter your move! Format it in PGN format. (PGN formation is like so: (letter of piece)(file you wish to move to)(rank you wish to move to).)");
      nextMove = scan.nextLine();


    boardManager.performMove(nextMove, playerTurn);
    if(playerTurn == 1 && boardManager.performedMoveFlag){
        playerTurn = 2;
      } else {
        playerTurn = 1;
      }

    }






    //close up the scanner
    scan.close();
  }
}