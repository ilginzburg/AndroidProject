public class Bishop extends ChessPiece{

    public Bishop(String color){
        super(color);
    }

    @Override
    String getColor() {
        return color;
    }

    @Override
    String getSymbol() {
        return "B";
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        if((line == toLine)  || (column == toColumn))
            return false;
        if (findOnDiagonal(chessBoard, line, column, toLine,toColumn) != null)
            return false;
        if (isFriendOccupied(chessBoard, toLine,toColumn))
            return false;
        if(checkPos(line) && checkPos(column) && checkPos(toLine) && checkPos(toColumn))
            return (Math.abs(toColumn - column)) == (Math.abs(toLine - line));
        return false;
    }


}


