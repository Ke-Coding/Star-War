package gamefield;

import java.util.ArrayList;

import gamemain.UpdateableField;
import gameview.ViewableField;
import player.ControllableField;
import randomutil.RandomUtil;
import star.Star;
import star.StarConstants;
import star.StarID;

public class GameField implements UpdateableField, ControllableField, ViewableField {

	private final int N_ROW;
	private final int N_COL;
	private Star[][] stars;

	// ������
	public GameField(int N_ROW, int N_COL) {
		this.N_ROW = N_ROW;
		this.N_COL = N_COL;
		this.stars = new Star[N_ROW][N_COL];
		this.randomInit();
	}

	
	// RunnableField�ӿ�ʵ��
	@Override
	public void updateFieldOneStep() {
		for (Star[] row : stars) {
			for (Star s : row) {
				if (s != null) {
					s.grow();
					s.emit();
				}
			}
		}
	}
	
	
	// ControllableField�ӿ�ʵ��
	@Override
	public boolean isFocusableAt(StarID ID, Position pos) {
		return pos.row >= 0 && pos.row < this.N_ROW
				&& pos.col >= 0 && pos.col < this.N_COL;
	}
	@Override
	public boolean isSelectableAt(StarID ID, Position pos) {
		return stars[pos.row][pos.col] != null && stars[pos.row][pos.col].getID() == ID;
	}
	@Override
	public Position getDefaultPos(StarID ID) {
		for (int r=0; r<N_ROW; ++r) {
			for (int c=0; c<N_COL; ++c) {
				if (stars[r][c] != null && stars[r][c].getID() == ID) {
					return new Position(r, c);
				}
			}
		}
		return null;
	}
	@Override
	public boolean operateConnection(StarID ID, Position src, Position dest) {
		if (!this.hasStarAt(src) || !this.hasStarAt(dest) || stars[src.row][src.col].getID() != ID)
			return false;
		Star sender = stars[src.row][src.col], receiver = stars[dest.row][dest.col];
		if (sender.removeGoal(receiver)) {
			return true;
		} else {
			return sender.addGoal(receiver);
		}
	}

	
	// ViewableField�ӿ�ʵ��
	@Override
	public boolean isEmptyAt(Position pos) {
		return stars[pos.row][pos.col] == null;
	}
	@Override
	public StarID getIDAt(Position pos) {
		return stars[pos.row][pos.col].getID();
	}
	@Override
	public int getValueAt(Position pos) {
		return stars[pos.row][pos.col].getForceVal();
	}
	@Override
	public void fetchDestinationsFrom(Position src, ArrayList<Position> dests) {
		stars[src.row][src.col].fetchDestinations(dests);
	}
	
	
	// ���ߣ�ĳ��λ���Ƿ�������
	private boolean hasStarAt(Position pos) {
		return stars[pos.row][pos.col] != null;
	}

	// ���ߣ���Field���������ָ��ID��count������
	private void randomCreateStars(StarID ID, int count) {
		while (count-->0) {
			int r, c;
			do {
				r = RandomUtil.randomInRange(0, this.N_ROW);
				c = RandomUtil.randomInRange(0, this.N_COL);
			} while (stars[r][c] != null);

			int initialForceVal;
			if (ID == StarID.NEUTRAL) {
				initialForceVal = RandomUtil.randomInRange(
						StarConstants.DEFAULT_MIN_INITIAL_NEUTRAL_FORCE_VALUE,
						StarConstants.DEFAULT_MAX_INITIAL_NEUTRAL_FORCE_VALUE);
			} else {
				initialForceVal = StarConstants.DEFAULT_INITIAL_PLAYERS_FORCE_VALUE;
			}
			stars[r][c] = new Star(ID, new Position(r, c), initialForceVal,
					StarConstants.DEFAULT_GROW_SPEED, StarConstants.DEFAULT_EMIT_SPEED, StarConstants.DEFAULT_MAX_GOAL_COUNT);
		}
	}
	// ���ߣ������ʼ������Field
	private void randomInit() {
		for (int r=0; r<N_ROW; ++r) {
			for (int c=0; c<N_COL; ++c) {
				stars[r][c] = null;
			}
		}
		
		for (StarID ID : StarID.values()) {
			if (ID == StarID.NEUTRAL)
				randomCreateStars(StarID.NEUTRAL,FieldConstants.DEFAULT_INITIAL_NEUTRAL_PLANET_COUNT);
			else
				randomCreateStars(ID,FieldConstants.DEFAULT_INITIAL_PLAYER_PLANET_COUNT);
		}
	}
	
	// ���ߣ�debug��
	public void println() {
		for (int r=0; r<N_ROW; ++r) {
			for (int c=0; c<N_COL; ++c) {
				if (stars[r][c] != null) {
					System.out.print(r + ", " + c + ": " + stars[r][c].getForceVal());
					System.out.print("(" + stars[r][c].getID() + ")" + "\t");
				}
			}
		}
		System.out.println("");
	}
	public static void main(String[] args) {
		GameField gf = new GameField(FieldConstants.DEFAULT_N_ROW, FieldConstants.DEFAULT_N_COL);
		gf.println();
		gf.updateFieldOneStep();
		gf.println();
	}
	
}

