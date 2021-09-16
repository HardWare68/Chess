public class boardManager{
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

  public void printBoard(){
    for(int row=0; row < chessBoard.length; row++){
      for(int column=0; column < chessBoard[row].length; column++){
        System.out.print(chessBoard[row][column]);
      }
      System.out.println();
    }
  }

  public void performMove(String paramNextMove){
    //first, the move is gonna be given to us in probably PGN form, so let's check to make sure it's in that. That'll probably help.
    isPGN(paramNextMove);
    

    //next, check to make sure the piece can actually move to that square
    //who's ready for some suffering?
    isLegalMove(paramNextMove);
  }

  private boolean isPGN(String paramNextMove){
      if(paramNextMove.matches("[NKQRB]?[A-H][1-8]")){
        return true;
      } else {
        return false;
      }
  }

  private boolean isLegalMove(String paramNextMove){
    //first, lets find out what piece we're dealing with.


    if(paramNextMove.charAt(0) == 'R'){
      //piece is a rook, lets find out if its a legal move
      rookMoves(paramNextMove);
    }

  }

  private boolean rookMoves(String paramNextMove){
    //first, lets find the rooks on the board
    //there will likely be two, so we have to be prepared for both of them
    for(int row=0; row < chessBoard.length; row++){
      for(int column=0; column < chessBoard[row].length; column++){
        if(chessBoard[row][column].toUpperCase() == "R"){
          //we found a rook. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go

          int file = paramNextMove.charAt(1); //the file is given to us as a letter. let's make that into a number!

          int rank = paramNextMove.charAt(2);
        }
      }
    }
  }

  private int fileToNumber(char paramFile){
    if(paramFile.toUpperCase == 'A'){return 0;}
  }
}