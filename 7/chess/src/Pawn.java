public class Pawn extends ChessPiece{

    public Pawn(String color){
        super(color);
    }

    @Override
    String getColor() {
        return color;
    }

    @Override
    String getSymbol() {
        return "P";
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        if ((line == toLine) && (column == toColumn))
            return false;
        if ((isFriendOccupied(chessBoard, toLine,toColumn)) && (column == toColumn))
            return false;
        if (checkPos(line) && checkPos(column) && checkPos(toLine) && checkPos(toColumn)) {
             if (color.equals("White")) {
                if (line == 1) {
                   if ((toLine - line) == 2)
                        return true;
                    }
                    if ((toLine - line) == 1) {
                        if(isEnemyOccupied(chessBoard, toLine, toColumn) && (Math.abs(toColumn - column) == 1))
                            return true;
                        else
                            return column == toColumn;
                    }
                } else {
                    if (line == 6) {
                        if ((line - toLine) == 2)
                            return true;
                    }
                    if ((line - toLine) == 1) {
                        if(isEnemyOccupied(chessBoard, toLine, toColumn) && (Math.abs(column - toColumn) == 1))
                            return true;
                        else
                            return column == toColumn;
                    }
                }
            }
        return false;
    }
}


