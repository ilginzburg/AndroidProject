public class Queen extends ChessPiece{

    public Queen(String color){
        super(color);
    }

    @Override
    String getColor() {
        return color;
    }

    @Override
    String getSymbol() {
        return "Q";
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        if ((line == toLine) && (column == toColumn))
            return false;
        if (isFriendOccupied(chessBoard, toLine, toColumn))
            return false;
        if (checkPos(line) && checkPos(column) && checkPos(toLine) && checkPos(toColumn)) {
            if ((line != toLine) && (column != toColumn)) { // diagonal
                if (findOnDiagonal(chessBoard, line, column, toLine, toColumn) != null)
                    return false;
                return (Math.abs(toColumn - column)) == (Math.abs(toLine - line));
            }
            if ((line == toLine) || (column == toColumn)) {
                return findOnLine(chessBoard, line, column, toLine, toColumn) == null;
            }
        }
        return false;
    }

}

