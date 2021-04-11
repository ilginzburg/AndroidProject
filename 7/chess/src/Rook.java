public class Rook  extends ChessPiece{

    public Rook(String color){
        super(color);
    }

    @Override
    String getColor() {
        return color;
    }

    @Override
    String getSymbol() {
        return "R";
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        if (findOnLine(chessBoard, line, column, toLine,toColumn) != null)
            return false;
        if (isFriendOccupied(chessBoard, toLine,toColumn))
            return false;
        if(checkPos(line) && checkPos(column) && checkPos(toLine) && checkPos(toColumn))
            return ((line == toLine) && (column != toColumn)) || ((line != toLine) && (column == toColumn));
        return false;
    }
}


