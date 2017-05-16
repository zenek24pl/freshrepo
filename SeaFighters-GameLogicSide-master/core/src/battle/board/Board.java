package battle.board;

public class Board {

	private int boardSize;
	private float[][] board;
	
	public Board(){
		this.boardSize=10;
		this.board=new float[10][10];
		this.initBoard();
	}

	public void initBoard(){
		
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++)
				board[i][j] = -8;
		
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardElement(int x, int y, float value){
		this.board[x][y] = value;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public float[][] getBoard() {
		return board;
	}

	public void setBoard(float[][] board) {
		this.board = board;
	}
	
	public void printBoard(){
		for(int i=0; i < this.getBoardSize(); i++){
			for (int j=0; j < this.getBoardSize(); j++)
				System.out.print(this.getBoard()[j][i]+ " ");
			System.out.println();
		}
	}

}
