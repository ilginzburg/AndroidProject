public class King  extends ChessPiece{

    public King(String color){
        super(color);
    }

    @Override
    String getColor() {
        return color;
    }

    @Override
    String getSymbol() {
        return "K";
    }

    boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        if((line == toLine)  && (column == toColumn))
            return false;
        else{
            if(checkPos(line) && checkPos(column) && checkPos(toLine) && checkPos(toColumn)){
                return ((Math.abs(toColumn - column) == 1) && (line == toLine)) ||
                        ((Math.abs(toLine - line) == 1) && (column == toColumn)) ||
                        (((toLine - line) == 1) && (toColumn - column) == 1)||
                        (((toLine - line) == 1) && (column - toColumn) == 1)||
                        (((line - toLine) == 1) && (toColumn - column) == 1)||
                        (((line - toLine) == 1) && (column - toColumn) == 1) ;
            }
        }
        return false;
    }

    boolean isUnderAttack(ChessBoard board, int line, int column){
       for (int i = 0; i < 7; ++i){
        for (int j = 0; j < 7; ++j) {
            if(board.board[i][j] != null) {
                if (board.board[i][j].canMoveToPosition(board, i, j, line, column) && !board.board[i][j].getColor().equals(color))
                    return true;
            }
        }
        }
       return false;
    }

}


