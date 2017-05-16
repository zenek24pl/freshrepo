package battle.core;

import java.util.ArrayList;
import java.util.Random;

import battle.logic.Board;
import battle.ships.FivePlacesShip;
import battle.ships.FourPlacesShip;
import battle.ships.ThreePlacesShip;
import battle.ships.TwoPlacesShip;

public class Player {

	private battle.board.Board ownBoard;
	private boolean play;
	private boolean lost;
	private ArrayList<Ship> ships;
	private int blankCell=-8;
	private int missCell=-9;

	public Player() {
		this.ownBoard=new battle.board.Board();

		this.play = false;
		this.lost = false;
		this.ships = new ArrayList<Ship>();
	}

	public Player(ArrayList<battle.core.Ship> ships){
		this.ownBoard=new battle.board.Board();
		this.play = false;
		this.lost = false;
		this.ships = ships;
		for (int i = 0; i<ships.size(); i++){
			fillBoardShip(i);
		}
	}

	public ArrayList<Ship> getShips() {
		return ships;
	}

	//usuwanie statkow(listy)
	public void eraseShips(){
			ships.clear();
	}
	
	public void randomBoard() {
		//tworzymy randomowa tablice statkow
		Random r1 = new Random();
		Ship[] createShips = {
				new FivePlacesShip(r1.nextInt(8), r1.nextInt(8),
						r1.nextInt(2)),
				new FourPlacesShip(r1.nextInt(8), r1.nextInt(8), r1.nextInt(2)),
				new ThreePlacesShip(r1.nextInt(8), r1.nextInt(8), r1.nextInt(2)),
				new ThreePlacesShip(r1.nextInt(8), r1.nextInt(8), r1.nextInt(2)),
				new TwoPlacesShip(r1.nextInt(8), r1.nextInt(8), r1.nextInt(2))
				 };

		for (int i = 0; i < 5; i++) {
			ships.add(createShips[i]);
			while (!validShip(i) || !fillBoardShip(i)) {
				Ship s1 = ships.get(i);
				ships.remove(i);
				s1.setPosX(r1.nextInt(8));
				s1.setPosY(r1.nextInt(8));
				s1.setDirection(r1.nextInt(2));
				ships.add(i, s1);
			}
		}

	}

	
	public boolean fillBoardShip(int i) {
		Ship ship = ships.get(i);
		if (ship.getDirection() == 0) {//jezeli statek jest polozony vertykalnie
			if (ship.getPosX() + ship.getSize() -1 > 9)//jezeli statek + jego rozmiar wykracza poza rozmiar planszy
				return false;
			for (int j = 0; j < ship.getSize(); j++)//jezeli
				if (this.getOwnBoard().getBoard()[ship.getPosX() + j][ship
						.getPosY()] != blankCell)
					return false;

			for (int j = 0; j < ship.getSize(); j++)
				this.getOwnBoard().getBoard()[ship.getPosX() + j][ship
						.getPosY()] = i;
		} else {//jezeli statek jest polozony horyzontalnie
			if (ship.getPosY() + ship.getSize() -1 > 9)
				return false;
			for (int j = 0; j < ship.getSize(); j++)
				if (this.getOwnBoard().getBoard()[ship.getPosX()][ship
						.getPosY() + j] != blankCell)
					return false;

			for (int j = 0; j < ship.getSize(); j++)
				this.getOwnBoard().getBoard()[ship.getPosX()][ship.getPosY()
						+ j] = i;
		}


		return true;
	}

	public battle.board.Board getOwnBoard() {
		return ownBoard;
	}

	public void setOwnBoard(battle.board.Board ownBoard) {
		this.ownBoard = ownBoard;
	}


	public void play(int posX, int posY, Player p2) {

		if (posX < 0 || posX > 9 || posY < 0 || posY > 9)
			return;

		for (int i = 0; i < ships.size(); i++)
			if (p2.getOwnBoard().getBoard()[posX][posY] == blankCell)
				p2.getOwnBoard().getBoard()[posX][posY] = missCell;
			else if (p2.getOwnBoard().getBoard()[posX][posY] == missCell)
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


	//malowanie otoczonego statku
	public void surrondSinkedShip(int i) {
		
		Ship ship = this.getShips().get(i);
		int j,c,term,size;
		if (ship.getDirection() == 0) {//jezeli mamy vertykalnie ustawiony statek
			if (ship.getPosX() - 1 >= 0)
				this.getOwnBoard().getBoard()[ship.getPosX() - 1][ship
						.getPosY()] = missCell;
			if (ship.getPosX() + ship.getSize() <= 9)
				this.getOwnBoard().getBoard()[ship.getPosX() + ship.getSize()][ship.getPosY()] = missCell;

			 j = 0;size = ship.getSize();c = 0;term = 0;

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

			 j = 0;size = ship.getSize();c = 0;term = 0;

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
			}
			if (ship.getPosY() + ship.getSize() <= 9) size++;
			int jBak = j;
			for (; c < term; c += 2) {
				j = jBak;
				for (; j < size; j++)
					this.getOwnBoard().getBoard()[ship.getPosX() + c][ship
							.getPosY() + j] = -9;
			}

		}


	}

	public boolean validShip(int i) {
		Ship ship = ships.get(i);
		int j,c,term,size;
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

			 j = 0;size = ship.getSize();c = 0;term = 0;

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

			j= 0;size = ship.getSize();c = 0;term = 0;

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
			if (!validShip(i))
				return false;

		if (total)
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
