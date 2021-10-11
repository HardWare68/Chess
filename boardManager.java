public class boardManager{
  char[][] chessBoard = {
      {'r', 'n', 'b', 'k', 'q', 'b', 'n', 'r'},
      {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
      {'R', 'N', 'B', 'K', 'Q', 'B', 'N', 'R'}
    };
    boolean enPassantFlag = false; //Google en passant!

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
  public void performMove(String paramNextMove, int paramPlayerMove){
    //first, the move is gonna be given to us in probably PGN form, so let's check to make sure it's in that. That'll probably help.
    if(!isPGN(paramNextMove)){
      System.out.println("That is not in PGN form. Please format your move in PGN form.");
    } else if(!isLegalMove(paramNextMove)) {
      System.out.println("That move is not legal. Please double check to make sure it is a legal move.");
    } else {
      int file, rank;
      if(paramNextMove.length() == 3){
        file = fileToNumber(paramNextMove.charAt(1));
        rank = rankToNumber(paramNextMove.charAt(2));
        try{
          if(paramPlayerMove == 1){
            chessBoard[file][rank] = paramNextMove.charAt(0);
          } else {
            chessBoard[file][rank] = Character.toUpperCase(paramNextMove.charAt(0));
          }
        } catch (Exception ArrayIndexOutOfBoundsException) {
          System.out.println("File is: " + file);
          System.out.println("Rank is: " + rank);
          System.out.println("Length of master array is: " + chessBoard.length);
          System.out.println("Length of inside array is: " + chessBoard[0].length);
          System.out.println("If you are seeing this something went horribly wrong. That is unfortunate.");
        }
      } else {
        file = fileToNumber(paramNextMove.charAt(0));
        rank = rankToNumber(paramNextMove.charAt(1));
        if(paramPlayerMove == 1){
            chessBoard[file][rank] = 'P';
          } else {
            chessBoard[file][rank] = 'p';
          }
      }
    }
  }

  private boolean isPGN(String paramNextMove){
    return paramNextMove.matches("([NKQRB]?[a-h][1-8])|(O-O)");//idk regex lol
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
      return pawnMoves(paramNextMove);
    } else {
      switch (paramNextMove.charAt(0)){
        case 'R':
          //piece is a rook, lets find out if its a legal move
          return (rookMoves(paramNextMove, 'R'));

        case 'B':
          //piece is a bishop. lets find out if its a legal move
          return bishopMoves(paramNextMove, 'B');

        case 'Q':
          //piece is a queen.
          //note that if the user submits a valid rook or bishop move, then they have also submitted a valid queen move.
          //also note that this will make me want to cry.
          if(rookMoves(paramNextMove, 'Q') == false){
            return bishopMoves(paramNextMove, 'Q');
          } else {
            return true;
          }
        
        case 'K':
          //piece is a king. let's find out if it is a legal move.
          return kingMoves(paramNextMove);

        case 'N':
          //piece is a knight. let's find out if it is a legal move
          return knightMoves(paramNextMove);
      }
    }
    //the compiler yells at me unless i put this here.
    return false;
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
        if(Character.toUpperCase(chessBoard[row][column]) == paramPiece){
          //we found a rook. lets find out where they want to place it, and if it is legal
          System.out.println("Found rook at: " + row + ", " + column);

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = rankToNumber(paramNextMove.charAt(2));

          //if the row they wish to move on is the same as the row the rook is on, then it must be a valid move!
          if(file == row){
            System.out.println("Rook is on the same row!");

            
            //first let's make sure there is no collision. then we can return true.
            return (rookCollision(file, row, rank, column));
            }
          //same holds for if the column is the same. If either of the numbers are the same, then it is a valid move.
          if(rank == column){
            System.out.println("Rook is on the same column!");
            return (rookCollision(file, row, rank, column));
            }
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
        if(Character.toUpperCase(chessBoard[row][column]) == paramPiece){
          //we found a bishop. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = rankToNumber(paramNextMove.charAt(2));

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
        if(Character.toUpperCase(chessBoard[row][column]) == 'K'){
          //we found a king. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = rankToNumber(paramNextMove.charAt(2));

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
        if(Character.toUpperCase(chessBoard[row][column]) == 'P'){
          //we found a pawn. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(0)); //the file is given to us as a letter. let's make that into a number!
          int rank = rankToNumber(paramNextMove.charAt(1));

          //alright, pawns. If they are on the 7th or 2nd file, they can move two squares forward. And if there is a piece diagonally forwards from them, they can attack it. Otherwise, they move forwards.
          //hi, HardWare a couple days into the future here. I HATE PAWNS!
          if(row == 1 || row == 6){
            if(Math.abs(rank-column) == 1 || Math.abs(rank-column) == 2){
              return true;
              }
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
        if(Character.toUpperCase(chessBoard[row][column]) == 'K'){ //this "charAt" workaround is because im lazy. and don't want to cry.
          //we found a rook. lets find out where they want to place it, and if it is legal

          //lets grab the second and third characters since thatll tell us where they want to go
          int file = fileToNumber(paramNextMove.charAt(1)); //the file is given to us as a letter. let's make that into a number!
          int rank = rankToNumber(paramNextMove.charAt(2));

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
  * rankToNumber() converts the rank to an integer.
  *
  * Parameters:
  * char paramRank: The rank we wish to convert.
  *
  * Returns: An integer that describes where on the array the rank is.
  */
  private int rankToNumber(char paramRank){
    //I HATE THIS! THIS IS STUPID!!! GRRRRRRRR!!!!!
    if(Character.toUpperCase(paramRank) == '1'){return 0;}
    if(Character.toUpperCase(paramRank) == '2'){return 1;}
    if(Character.toUpperCase(paramRank) == '3'){return 2;}
    if(Character.toUpperCase(paramRank) == '4'){return 3;}
    if(Character.toUpperCase(paramRank) == '5'){return 4;}
    if(Character.toUpperCase(paramRank) == '6'){return 5;}
    if(Character.toUpperCase(paramRank) == '7'){return 6;}
    if(Character.toUpperCase(paramRank) == '8'){return 7;}
    else{return -1;} //-1 means it's not an actual chess rank. this'll probably break stuff later on down the line, so yeah.
  }

  /*
  * pawnDiagonalCheck checks all of the pawns diagonals to see if they are valid moves
  *
  * Parameters:
  * String paramNextMove: the move the user has submitted. It should be in PGN form.
  * int paramFile: the file the user wishes to move to.
  * int paramRow: the row the user is currently on.
  * int paramRank: the rank the user wishes to move to.
  * int paramColumn: the column the user is currently on.
  *
  * Returns: True if the move is a legal move. Otherwise, false.
  */
  private boolean pawnDiagonalCheck(String paramNextMove, int paramFile, int paramRow, int paramRank, int paramColumn){
    //I HATE! Pawns!

    //En passant immediately overrides everything
    if(enPassantFlag){return true;}

    //check to see if the diagonals are open
    if(chessBoard[paramFile - paramRow][paramRank - paramColumn] != ' '){return true;}
    if(chessBoard[paramFile - paramRow][paramRank + paramColumn] != ' '){return true;}
    if(chessBoard[paramFile + paramRow][paramRank - paramColumn] != ' '){return true;}
    if(chessBoard[paramFile + paramRow][paramRank + paramColumn] != ' '){return true;}
    return false;
  }


  /*
  * rookCollision checks along the rows and columns to make sure we are not hopping over pieces.
  *
  * Parameters:
  * int paramFile: the file the user wishes to move to.
  * int paramRow: the row the user is currently on.
  * int paramRank: the rank the user wishes to move to.
  * int paramColumn: the column the user is currently on.
  *
  * Returns: True if the move the row/column is unobstructed. Otherwise, false.
  */
  private boolean rookCollision(int paramFile, int paramRow, int paramRank, int paramColumn){
    //first, lets find out if we are dealing with rows or columns
    if(paramFile == paramRow){
      //the rows are the same. let us find out the stuff with the columns then.
      // /!\ x represents the distance between the two. this might help later, so i'm commenting it here /!\
      for(int x=1; x > (Math.abs(paramRank - paramColumn)); x++){
        //hi there, me right now programming this. i want to cry.

        //the rank is bigger. we are gonna keep going left, hoping we don't hit a piece
        if(paramRank > paramColumn){
          System.out.print("Square at " + paramRow + ", " + (paramRank - x) + " is " + chessBoard[paramRow][paramRank - x]);
          if(chessBoard[paramRow][paramRank - x] != ' '){return false;} //the square is not empty, which means it is not good.
        } else {
          System.out.print("Square at " + paramRow + ", " + (paramColumn - x) + " is " + chessBoard[paramRow][paramColumn - x]);
          //if the rank is not greater, then the column they want to move to is. just keep going left, fellas.
          if(chessBoard[paramRow][paramColumn - x] != ' '){return false;}
        }
      } //so we should have scanned every square between the two columns, and each one should be empty by the time we have gotten to this point. thus, we should be good to return true.
      return true;
    } else {
      //if the rows are not equal, then the columns must be.
      for(int x=1; x > (Math.abs(paramFile - paramRow)); x++){

        //the file is bigger. we are gonna keep going left, hoping we don't hit a piece
        if(paramFile > paramRow){
          if(chessBoard[paramFile - x][paramColumn] != ' '){return false;} //the square is not empty, which means it is not good.
        } else {
          //if the rank is not greater, then the column they want to move to is. just keep going left, fellas.
          if(chessBoard[paramRow - x][paramColumn] != ' '){return false;}
        }
      } //so we should have scanned every square between the two columns, and each one should be empty by the time we have gotten to this point. thus, we should be good to return true.
      return true;
    }
  }
}