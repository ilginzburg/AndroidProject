abstract class ChessPiece {

    String color;
    boolean check = true;

    public ChessPiece(String color) {
        this.color = color;
    }

    abstract String getColor();

    abstract String getSymbol();

    abstract boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn);

    protected boolean checkPos(int pos) {
        return pos >= 0 && pos <= 7;
    }


    protected ChessPiece findOnDiagonal(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        if ((toLine - line) == 1)
            return null;
        if((toLine >  line) && (toColumn > column)) { // up right
            for (int i = line + 1; i < toLine; ++i) {
                if (chessBoard.board[i][column + i - line] != null) {
                    return chessBoard.board[i][column + i - line];
                }
            }
        }
        if((toLine >  line) && (toColumn < column)) { // up left
            for (int i = line + 1; i < toLine; ++i) {
                //  System.out.println("line = " + line + ",column = " + column + ",toLine = " + toLine + ",toColumn = " + toColumn);


                if (chessBoard.board[i][column - i + line] != null) {
                    System.out.println("line = " + i + "col = " + (column - i + line));

                return chessBoard.board[i][column - i + line];
            }
            }
        }
        if((toLine <  line) && (toColumn > column)) { // down right
            for (int i = line - 1; i > toLine; --i) {
                if (chessBoard.board[i][column - i + line] != null) {
                    return chessBoard.board[i][column - i + line];
                }
            }
        }
        if((toLine <  line) && (toColumn < column)) { // down left
            for (int i = line - 1; i > toLine; --i) {
                if (chessBoard.board[i][column + i - line] != null)
                    return chessBoard.board[i][column + i - line];
            }
        }
        return null;
    }


    protected ChessPiece findOnLine(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        if ((toLine - line) == 1)
            return null;
        if(toLine >  line) { //up
            for (int i = line + 1; i < toLine; ++i) {
                if (chessBoard.board[i][column] != null)
                    return chessBoard.board[i][column];
            }
        }
        if(toLine <  line) { //down
            for (int i = line - 1; i > toLine; --i) {
                if (chessBoard.board[i][column] != null)
                    return chessBoard.board[i][column];
            }
        }
        if(toColumn > column) { //right
            for (int i = column + 1; i < toColumn; ++i) {
                if (chessBoard.board[line][i] != null)
                    return chessBoard.board[line][i];
            }
        }
        if(toColumn < column) { //left
            for (int i = column - 1; i > toColumn; --i) {
                if (chessBoard.board[line][i] != null)
                    return chessBoard.board[line][i];
            }
        }
        return null;
    }



    private boolean isTileFree(ChessBoard chessBoard, int line, int col) {
        return (chessBoard.board[line][col] == null);
    }

    protected boolean isFriendOccupied(ChessBoard chessBoard, int line, int col) {
        if (isTileFree(chessBoard, line, col))
            return false;
        return (chessBoard.board[line][col].getColor().equals(color));
    }

    protected boolean isEnemyOccupied(ChessBoard chessBoard, int line, int col) {
        if (isTileFree(chessBoard, line, col))
            return false;
        return (!chessBoard.board[line][col].getColor().equals(color));
    }
}
