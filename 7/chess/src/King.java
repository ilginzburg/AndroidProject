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
        boolean underAttack = false;
        if( color.equals("White")) {
            underAttack |= isUnderAttackDiagonal(board, line, column, column, 0);
            underAttack |= isUnderAttackDiagonal(board, line, column, (7-column), 7);
            underAttack |= isUnderAttackLine(board, line, column, 7, column);
        } else {
            underAttack |= isUnderAttackDiagonal(board, line, column, column, 7);
            underAttack |= isUnderAttackDiagonal(board, line, column, (7-column), 0);
            underAttack |= isUnderAttackLine(board, line, column, 0, column);
        }
        underAttack |= isUnderAttackLine(board, line, column, line, 0);
        underAttack |= isUnderAttackLine(board, line, column, line, 7);
        underAttack |= isUnderAttackPawn(board, line, column);
        return underAttack;
    }



    private boolean isUnderAttackDiagonal(ChessBoard board, int line, int column, int toLine, int toColumn){
        boolean underAttack = false;
        ChessPiece p1,p2;
        p1 = findOnDiagonal(board, line, column, toLine, toColumn);
        p2 = board.board[toLine][toColumn];
        if(p1 != null) {
            underAttack |= (!p1.getColor().equals(color)) && (p1.getSymbol().equals("B") || p1.getSymbol().equals("Q"));

            System.out.println("Color = " + p1.getColor() + "name = " + p1.getSymbol());
        }
        if(p2 != null)
            underAttack |= (!p2.getColor().equals(color)) && (p2.getSymbol().equals("B") || p2.getSymbol().equals("Q"));
        return underAttack;
    }


    private boolean isUnderAttackLine(ChessBoard board, int line, int column, int toLine, int toColumn){
        boolean underAttack = false;
        ChessPiece p1,p2;
        p1 = findOnLine(board, line, column, toLine, toColumn);
        p2 = board.board[toLine][toColumn];
        if(p1 != null)
            underAttack |= (!p1.getColor().equals(color)) && (p1.getSymbol().equals("R") || p1.getSymbol().equals("Q"));
        if(p2 != null)
            underAttack |= (!p2.getColor().equals(color)) && (p2.getSymbol().equals("R") || p2.getSymbol().equals("Q"));
        return underAttack;
    }

    private boolean isUnderAttackPawn(ChessBoard board, int line, int column){
        boolean underAttack = false;
        ChessPiece p1,p2;
        if(color.equals("White")){
            p1 = board.board[line + 1][column - 1];
            p2 = board.board[line + 1][column + 1];
            if(p1 != null)
                underAttack |= p1.getColor().equals("Black") && p1.getSymbol().equals("P");
            if(p2 != null)
                underAttack |= p2.getColor().equals("Black") && p2.getSymbol().equals("P");
        }else{
            p1 = board.board[line - 1][column - 1];
            p2 = board.board[line - 1][column + 1];
            if(p1 != null)
                underAttack |= p1.getColor().equals("White") && p1.getSymbol().equals("P");
            if(p2 != null)
                underAttack |= p2.getColor().equals("White") && p2.getSymbol().equals("P");
        }
        return underAttack;
    }

}


