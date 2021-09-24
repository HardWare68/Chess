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

  /*
  * printBoard() prints the current board state.
  */
  public void printBoard(){
    for(int row=0; row < chessBoard.length; row++){
      for(int column=0; column < chessBoard[row].length; column++){
        System.out.print(chessBoard[row][column]);
      }
      System.out.println();
    }
  }


  /*
  * performMove() performs the move the user inputs.
  *
  * Parameters:
  * String paramNextMove: The move the user has submitted. It should be in PGN form.
  */
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

  /*
  * isLegalMove() checks to see if the move is legal or not.
  *
  * Parameters:
  * String paramNextMove: The move the user has submitted. It should be in PGN form.
  *
  * Returns: True if the move is legal. Otherwise, false.
  */
  private boolean isLegalMove(String paramNextMove){
    //first, lets find out what piece we're dealing with.

    //if the PGN is 2 characters long (ex: e4, d7, g6), then it is the code for a pawn move. Otherwise, the PGN will be for a special piece.
    if(paramNextMove.length() == 2){
      pawnMoves(paramNextMove);
    } else {
      switch (paramNextMove.charAt(0)){
        case 'R':
          //piece is a rook, lets find out if its a legal move
          return rookMoves(paramNextMove, 'R');

        case 'B':
          //piece is a bishop. lets find out if its a legal move
          return bishopMoves(paramNextMove, 'B');

        case 'Q':
          //piece is a queen.
          //note that if the user sumbits a valid rook or bishop move, then they have also submitted a valid queen move.
          //also note that this will make me want to cry.
          if(rookMoves(paramNextMove, 'Q') == false){
            return bishopMoves(paramNextMove, 'Q');
          } else {
            return true;
          }
        
        case 'K':
          //piece is a king. let's find out if it is a legal move.
          kingMoves(paramNextMove);
          break;

        case 'N':
          //piece is a knight. let's find out if it is a legal move
          knightMoves(paramNextMove);
          break;
      }
    }

  }

  /*
  * rookMoves() checks to see if the ROOK move is legal or not.
  *
  * Parameters:
  * String paramNextMove: The move the user has submitted. It should be in PGN form.
  * char paramPiece: The piece we are checking for.
  *
  * Returns: True if the move is legal. Otherwise, false.
  */
  private boolean rookMoves(String paramNextMove, char paramPiece){
    //first, lets find the rooks on the board
    //there will likely be two, so we have to be prepared for both of them
    for(int row=0; row < chessBoard.length; row++){
      for(int column=0; column < chessBoard[row].length; column++){
        if(chessBoard[row][column].toUpperCase().charAt(0) == paramPiece){ //this "charAt" workaround is because im lazy. and don't want to cry.
          //we found a rook. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = paramNextMove.charAt(2) - 1;

          //if the row they wish to move on is the same as the row the rook is on, then it must be a valid move!
          if(file == row){return true;}
          //same holds for if the column is the same. If either of the numbers are the same, then it is a valid move.
          if(rank == column){return true;}
        }
      }
    }

    //if we have scanned the entire board, and there are either no rooks found or it was not a valid move, then it must be an illegal rook move.
    return false;
  }

  /*
  * bishopMoves() checks to see if the BISHOP move is legal or not.
  *
  * Parameters:
  * String paramNextMove: The move the user has submitted. It should be in PGN form.
  * char paramPiece: The piece we are checking for.
  *
  * Returns: True if the move is legal. Otherwise, false.
  */
  private boolean bishopMoves(String paramNextMove, char paramPiece){
    //first, lets find the bishops on the board
    //there will likely be two, so we have to be prepared for both of them
    for(int row=0; row < chessBoard.length; row++){
      for(int column=0; column < chessBoard[row].length; column++){
        if(chessBoard[row][column].toUpperCase().charAt(0) == paramPiece){
          //we found a bishop. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = paramNextMove.charAt(2) - 1;

          //okay, so bishops are a bit more complicated. the method behind them though is that they must change their rank AND file BY THE SAME NUMBER.
          //so if the piece is on 3,3 on the array, then moving the row AND column by 2 would be valid.
          //realize that means 2 in either direction, either up or down and left or right.
          //this is a statement from hell. Enjoy~!
          if(Math.abs(file-row) == Math.abs(rank-column)){return true;}
        }
      }
    }

    //if we have scanned the entire board, and there are either no bishops found or it was not a valid move, then it must be an illegal bishop move.
    return false;
  }

  /*
  * kingMoves() checks to see if the KING move is legal or not.
  *
  * Parameters:
  * String paramNextMove: The move the user has submitted. It should be in PGN form.
  * char paramPiece: The piece we are checking for.
  *
  * Returns: True if the move is legal. Otherwise, false.
  */
  private boolean kingMoves(String paramNextMove){
    //first, lets find the kings on the board
    for(int row=0; row < chessBoard.length; row++){
      for(int column=0; column < chessBoard[row].length; column++){
        if(chessBoard[row][column].toUpperCase().charAt(0) == 'K'){
          //we found a king. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = paramNextMove.charAt(2) - 1;

          //kings sounds easy in theory: they only move on square away! however, i am stupid.
          //hi, HardWare a couple days into the future here. This is actually super easy. Past me is extra stupid.
          if(Math.abs(file-row) == 1 || Math.abs(rank-column) == 1){
            return true;
          }
        }
      }
    }

    //if we have scanned the entire board, and there are either no kings found or it was not a valid move, then it must be an illegal king move.
    return false;
  }

  /*
  * pawnMoves() checks to see if the pawn move is legal or not.
  *
  * Parameters:
  * String paramNextMove: The move the user has submitted. It should be in PGN form.
  * char paramPiece: The piece we are checking for.
  *
  * Returns: True if the move is legal. Otherwise, false.
  */
  private boolean pawnMoves(String paramNextMove){
    //first, lets find the pawns on the board
    for(int row=0; row < chessBoard.length; row++){
      for(int column=0; column < chessBoard[row].length; column++){
        if(chessBoard[row][column].toUpperCase().charAt(0) == 'P'){
          //we found a pawn. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = paramNextMove.charAt(2) - 1;

          //alright, pawns. If they are on the 7th or 2nd file, they can move two squares forward. And if there is a piece diagonally forwards from them, they can attack it. Otherwise, they move forwards.
          //hi, HardWare a couple days into the future here. I HATE PAWNS!
          if(row == 1 || row == 6){
            if(Math.abs(rank-column) == 1 || Math.abs(rank-column) == 2){return true;}
          } else{
             if (Math.abs(rank-column) == 1 || pawnDiagonalCheck(paramNextMove, file, row, rank, column)){return true;}
          }
        }
      }
    }

    //if we have scanned the entire board, and there are either no pawns found or it was not a valid move, then it must be an illegal king move.
    return false;
  }

  /*
  * knightMoves() checks to see if the KNIGHT move is legal or not.
  *
  * Parameters:
  * String paramNextMove: The move the user has submitted. It should be in PGN form.
  *
  * Returns: True if the move is legal. Otherwise, false.
  */
  private boolean knightMoves(String paramNextMove){
    //first, lets find the rooks on the board
    //there will likely be two, so we have to be prepared for both of them
    for(int row=0; row < chessBoard.length; row++){
      for(int column=0; column < chessBoard[row].length; column++){
        if(chessBoard[row][column].toUpperCase().charAt(0) == 'K'){ //this "charAt" workaround is because im lazy. and don't want to cry.
          //we found a rook. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = paramNextMove.charAt(2) - 1;

          //knights seem hard, but are actually pretty easy if you use that absolute value math I've been doing.
          if((Math.abs(row-file) == 2 && Math.abs(rank-column) == 1) || (Math.abs(row-file) == 1 && Math.abs(rank-column) == 2)){
            return true;
          }
        }
      }
    }

    //if we have scanned the entire board, and there are either no knights found or it was not a valid move, then it must be an illegal knight move.
    return false;
  }

  /*
  * fileToNumber() converts the file to an integer.
  *
  * Parameters:
  * char ParamFile: The file we wish to convert.
  *
  * Returns: An integer that describes where on the array the file is.
  */
  private int fileToNumber(char paramFile){
    //im so sorry.
    if(Character.toUpperCase(paramFile) == 'A'){return 0;}
    if(Character.toUpperCase(paramFile) == 'B'){return 1;}
    if(Character.toUpperCase(paramFile) == 'C'){return 2;}
    if(Character.toUpperCase(paramFile) == 'D'){return 3;}
    if(Character.toUpperCase(paramFile) == 'E'){return 4;}
    if(Character.toUpperCase(paramFile) == 'F'){return 5;}
    if(Character.toUpperCase(paramFile) == 'G'){return 6;}
    if(Character.toUpperCase(paramFile) == 'H'){return 7;}
    else{return -1;} //-1 means it's not an actual chess file. this'll probably break stuff later on down the line, so yeah.
  }

  /*
  * pawnDiagonalCheck checks all of the pawns diagonals to see if they are valid moves
  *
  *
  */
  private boolean pawnDiagonalCheck(String paramNextMove, int paramFile, int paramRow, int paramRank, int paramColumn){
    //I HATE! Pawns!
    if(chessBoard[paramFile - paramRow][paramRank - paramColumn] != " "){return true;}
    if(chessBoard[paramFile - paramRow][paramRank + paramColumn] != " "){return true;}
    if(chessBoard[paramFile + paramRow][paramRank - paramColumn] != " "){return true;}
    if(chessBoard[paramFile + paramRow][paramRank + paramColumn] != " "){return true;}
    return false;
  }
}