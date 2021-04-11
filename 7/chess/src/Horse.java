public class Horse extends ChessPiece{

    public Horse(String color){
        super(color);
    }

    @Override
    String getColor() {
        return color;
    }

    @Override
    String getSymbol() {
        return "H";
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        if((line == toLine) && (column == toColumn))
            return false;
        if (isFriendOccupied(chessBoard, toLine,toColumn))
            return false;
            if(checkPos(line) && checkPos(column) && checkPos(toLine) && checkPos(toColumn))
                return ((Math.abs(toLine - line) == 1) && (Math.abs(toColumn - column) == 2)) ||
                       ((Math.abs(toLine - line) == 2) && (Math.abs(toColumn - column) == 1));
        return false;
    }
}
