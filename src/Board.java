import drawing.Point;

public class Board {

    public int rows;
    public int columns;
    public BoardField[][] board;

    public Board(int newRows, int newColumns) {
        this.rows = newRows;
        this.columns = newColumns;
        generateNewBoard();
    }

    public void generateNewBoard() {
        this.board = new BoardField[rows+1][columns+1];
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= columns; j++) {
                this.board[i][j] = new BoardField(new Point(i, j));
            }
        }
    }

    public void incrementField(int x, int y) {
        this.board[x][y].incrementQuant();
    }

    public boolean isFieldUsed(int x, int y) {
        return this.board[x][y].usedQuant > 0;
    }

    public void resetBoard() {
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= columns; j++) {
                this.board[i][j].resetField();
            }
        }
    }

    public Point nextPoint(int x, int y){
        if (x<0) return this.board[0][y].point;
        if (x>rows) return this.board[rows][y].point;
        if(y<0) return this.board[x][0].point;
        if(y>columns) return this.board[x][columns].point;
        return this.board[x][y].point;

    }
}