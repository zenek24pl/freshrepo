package battle.logic;

import java.util.ArrayList;
import java.util.Random;

public class Player {

	private Board ownBoard;
	private int points;
	private boolean play;
	private boolean lost;
	private String name;
	private ArrayList<Ship> ships;

	public Player() {
		this.ownBoard=new Board();
		this.points = 0;
		this.play = false;
		this.lost = false;
		this.ships = new ArrayList<Ship>();
	}

	public ArrayList<Ship> getShips() {
		return ships;
	}

	public void eraseShips(){
			ships.clear();
		
		System.out.print("N SHIPS: "+ships.size()+ " ");
	}
	
	public void randomBoard() {
		
		Random r1 = new Random();

		Ship[] createShips = {
				new AircraftCarrier(r1.nextInt(10), r1.nextInt(10),
						r1.nextInt(2)),
				new BattleShip(r1.nextInt(10), r1.nextInt(10), r1.nextInt(2)),
				new Cruiser(r1.nextInt(10), r1.nextInt(10), r1.nextInt(2)),
				new Submarine(r1.nextInt(10), r1.nextInt(10), r1.nextInt(2)),
				new Destroyer(r1.nextInt(10), r1.nextInt(10), r1.nextInt(2))
				 };

		for (int i = 0; i < 5; i++) {
			ships.add(createShips[i]);
			// fillBoardShip(i);
			// System.out.println(ships.get(i).getDirection()+" "+ships.get(i).getPosX()+" "+ships.get(i).getPosY());
			// this.getOwnBoard().printBoard();

			while (!validShip(i) || !fillBoardShip(i)) {

				Ship s1 = ships.get(i);
				ships.remove(i);

				s1.setPosX(r1.nextInt(10));
				s1.setPosY(r1.nextInt(10));
				s1.setDirection(r1.nextInt(2));

				ships.add(i, s1);
				// fillBoardShip(i);
			}
		}

	}

	
	public boolean fillBoardShip(int i) {
		Ship ship = ships.get(i);

		if (ship.getDirection() == 0) {
			if (ship.getPosX() + ship.getSize() -1 > 9)
				return false;

			for (int j = 0; j < ship.getSize(); j++)
				if (this.getOwnBoard().getBoard()[ship.getPosX() + j][ship
						.getPosY()] != -8)
					return false;

			for (int j = 0; j < ship.getSize(); j++)
				this.getOwnBoard().getBoard()[ship.getPosX() + j][ship
						.getPosY()] = i;
		} else {

			if (ship.getPosY() + ship.getSize() -1 > 9)
				return false;

			for (int j = 0; j < ship.getSize(); j++)
				if (this.getOwnBoard().getBoard()[ship.getPosX()][ship
						.getPosY() + j] != -8)
					return false;

			for (int j = 0; j < ship.getSize(); j++)
				this.getOwnBoard().getBoard()[ship.getPosX()][ship.getPosY()
						+ j] = i;
		}


		return true;
	}

	public Board getOwnBoard() {
		return ownBoard;
	}

	public void setOwnBoard(Board ownBoard) {
		this.ownBoard = ownBoard;
	}


	public void play(int posX, int posY, Player p2) {

		if (posX < 0 || posX > 9 || posY < 0 || posY > 9)
			return;

		for (int i = 0; i < ships.size(); i++)
			if (p2.getOwnBoard().getBoard()[posX][posY] == -8)
				p2.getOwnBoard().getBoard()[posX][posY] = -9;
			else if (p2.getOwnBoard().getBoard()[posX][posY] == -9)
				return;
			else if (p2.getOwnBoard().getBoard()[posX][posY] == i) {

				p2.getOwnBoard().getBoard()[posX][posY] = (float) (i + 0.5);

				Ship ship = p2.getShips().get(i);

				for (int j = 0; j < ship.getSize(); j++) {
					if (ship.getDirection() == 0){
						if (ship.getPosX()+ship.getSize()-1 > 9){
							return;
						}
						if (p2.getOwnBoard().getBoard()[ship.getPosX() + j][ship.getPosY()] == i){
							return;
						}
					}else {
							if (ship.getPosY()+ship.getSize()-1 > 9)
								return;
							if (p2.getOwnBoard().getBoard()[ship.getPosX()][ship.getPosY() + j] == i)
								return;
						}
				}

				p2.getShips().get(i).setSink(true);
				p2.surrondSinkedShip(i);

			} else if (p2.getOwnBoard().getBoard()[posX][posY] == (i + 0.5))
				return;
	}

	public void surrondSinkedShip(int i) {
		
		Ship ship = this.getShips().get(i);

		if (ship.getDirection() == 0) {
			if (ship.getPosX() - 1 >= 0)
				this.getOwnBoard().getBoard()[ship.getPosX() - 1][ship
						.getPosY()] = -9;
			if (ship.getPosX() + ship.getSize() <= 9)
				this.getOwnBoard().getBoard()[ship.getPosX() + ship.getSize()][ship.getPosY()] = -9;

			int j = 0;
			int size = ship.getSize();
			int c = 0;
			int term = 0;

			if (ship.getPosY() - 1 >= 0 && ship.getPosY() + 1 <= 9) {
				c = -1;
				term = 2;
			} else if (ship.getPosY() - 1 >= 0) {
				c = -1;
				term = 0;
			} else {
				c = 1;
				term = 2;
			}

			if (ship.getPosX() - 1 >= 0) {
				j = -1;
			//	size++;
			}

			if (ship.getPosX() + ship.getSize() <= 9)
				size++;

			int jBak = j;

			for (; c < term; c += 2) {
				j = jBak;
				for (; j < size; j++)
					this.getOwnBoard().getBoard()[ship.getPosX() + j][ship
							.getPosY() + c] = -9;
			}

		}else {
			
			if (ship.getPosY() - 1 >= 0)
				this.getOwnBoard().getBoard()[ship.getPosX()][ship
						.getPosY()-1] = -9;
			if (ship.getPosY() + ship.getSize() <= 9)
				this.getOwnBoard().getBoard()[ship.getPosX()][ship.getPosY() + ship.getSize()] = -9;

			int j = 0;
			int size = ship.getSize();
			int c = 0;
			int term = 0;

			if (ship.getPosX() - 1 >= 0 && ship.getPosX() + 1 <= 9) {
				c = -1;
				term = 2;
			} else if (ship.getPosX() - 1 >= 0) {
				c = -1;
				term = 0;
			} else {
				c = 1;
				term = 2;
			}

			if (ship.getPosY() - 1 >= 0) {
				j = -1;
			//	size++;
			}

			if (ship.getPosY() + ship.getSize() <= 9)
				size++;

			int jBak = j;

			for (; c < term; c += 2) {
				j = jBak;
				for (; j < size; j++)
					this.getOwnBoard().getBoard()[ship.getPosX() + c][ship
							.getPosY() + j] = -9;
			}

		}


	}

	public boolean validShipPosition(int posX, int posY, int direction, int size) {

		if (posX < 0 || posX > 9 || posY < 0 || posY > 9)
			return false;

		if (direction == 0) {
			if (posX + size > 9)
				return false;
		} else {
			if (posY + size > 9)
				return false;
		}

		return true;
	}

	public boolean validShip(int i) {

		Ship ship = ships.get(i);

		if (ship.getDirection() == 0) {
			// horizontal

			if (ship.getPosX() + ship.getSize()-1 > 9)
				return false;

			if (ship.getPosX() + ship.getSize() <= 9)
				if (this.getOwnBoard().getBoard()[ship.getPosX()
						+ ship.getSize()][ship.getPosY()] != -8)
					return false;
			if (ship.getPosX() - 1 >= 0)
				if (this.getOwnBoard().getBoard()[ship.getPosX() - 1][ship
						.getPosY()] != -8)
					return false;

			int j = 0;
			int size = ship.getSize();
			int c = 0;
			int term = 0;

			if (ship.getPosY() - 1 >= 0 && ship.getPosY() + 1 >= 9) {
				c = -1;
				term = 2;
			} else if (ship.getPosY() - 1 >= 0) {
				c = -1;
				term = 0;
			} else {
				c = 1;
				term = 2;
			}

			if (ship.getPosX() - 1 >= 0) {
				j = -1;
				//size++;
			}

			if (ship.getPosX() + ship.getSize() <= 9)
				size++;

			int jBak = j;

			for (; c < term; c += 2) {
				j = jBak;
				for (; j < size; j++)
					if (this.getOwnBoard().getBoard()[ship.getPosX() + j][ship
							.getPosY() + c] != -8)
						return false;

			}

		} else {
			// vertical
			if (ship.getPosY() + ship.getSize() -1 > 9)
				return false;

			if (ship.getPosY() + ship.getSize() <= 9)
				if (this.getOwnBoard().getBoard()[ship.getPosX()][ship
						.getPosY() + ship.getSize()] != -8)
					return false;
			if (ship.getPosY() - 1 >= 0)
				if (this.getOwnBoard().getBoard()[ship.getPosX()][ship
						.getPosY() - 1] != -8)
					return false;

			int j = 0;
			int size = ship.getSize();
			int c = 0;
			int term = 0;

			if (ship.getPosX() - 1 >= 0 && ship.getPosX() + 1 <= 9) {
				c = -1;
				term = 2;
			} else if (ship.getPosX() - 1 >= 0) {
				c = -1;
				term = 0;
			} else {
				c = 1;
				term = 2;
			}

			if (ship.getPosY() - 1 >= 0) {
				j = -1;
				//size++;
			}
			if (ship.getPosY() + ship.getSize() <= 9)
				size++;

			int jBak = j;

			for (; c < term; c += 2) {
				j = jBak;
				for (; j < size; j++)
					if (this.getOwnBoard().getBoard()[ship.getPosX() + c][ship
							.getPosY() + j] != -8)
						return false;
			}
		}
		return true;
	}

	public boolean validBoard(boolean total) {

		for (int i = 0; i < ships.size(); i++)
			if (validShip(i) == false)
				return false;

		if (total == true)
			if (ships.size() != 10)
				return false;

		return true;
	}


	public boolean getLost() {
		
		return lost;
	}
	
	public boolean checkLost(){
		
		for (int i=0; i < ships.size(); i++)
			if (!ships.get(i).isSink()){
				lost=false;
				return false;
			}
		
		lost=true;
		return true;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}

}
