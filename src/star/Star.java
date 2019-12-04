package star;

import java.util.ArrayList;

import gamefield.Position;
import randomutil.RandomUtil;

public class Star {
	
	private StarID ID;
	private Position pos;
	private int currentForceVal;
	private int threshold;
	private int growSpeed;
	private int emitSpeed;
	private int maxGoalCount;
	private ArrayList<Star> goals;
	
	// ������
	public Star(StarID ID, Position pos, int initialForceVal, int growSpeed, int emitSpeed, int maxGoalCount) {
		this.ID = ID;
		this.pos = pos;
		this.currentForceVal = initialForceVal;
		this.threshold = initialForceVal + RandomUtil.randomInRange(StarConstants.DEFAULT_MIN_EXTEND_THRESHOLD, StarConstants.DEFAULT_MAX_EXTEND_THRESHOLD);
		this.growSpeed = growSpeed;
		this.emitSpeed = emitSpeed;
		this.maxGoalCount = maxGoalCount;
		this.goals = new ArrayList<>();
	}

	// �ӿڣ�ֻ��gameField���ţ�����ȡID��Position������ֵ
	public StarID getID() {
		return this.ID;
	}
	public Position getPos() {
		return this.pos;
	}
	public int getForceVal() {
		return this.currentForceVal;
	}

	// �ӿڣ�ֻ��gameField���ţ���Goal��ɾ��
	public boolean addGoal(Star newGoal) {
		if (this.goals.size() < this.maxGoalCount) {
//		if (this.goals.size() <= this.currentForceVal / 40) {
			this.goals.add(newGoal);
			return true;
		}
		return false;
	}
	public boolean removeGoal(Star givenGoal) {
		for (int i=goals.size()-1; i>=0; --i) {
			Star goal = goals.get(i);
			if (goal.pos.equals(givenGoal.pos)) {
				goals.remove(i);
				return true;
			}
		}
		/*
		Iterator<Star> it = this.goals.iterator();
		while (it.hasNext()) {
			if (it.next().pos.equals(givenGoal.pos)) {
				it.remove();
				return true;
			}
		}
		 */
		return false;
	}
	
	public void fetchDestinations(ArrayList<Position> dests) {
		for (Star goal : goals) {
			dests.add(goal.getPos());
		}
	}

	// ���ߣ�������������ֵ��ɾ
	private int incForceVal(int v) {
		int increased = Math.min(v, StarConstants.DEFAULT_MAX_FORCE_VALUE - this.currentForceVal);
		this.currentForceVal += increased;
		return increased;
	}
	private int decForceVal(int v) {
		int decreased = Math.min(v, this.currentForceVal);
		this.currentForceVal -= decreased;
		return decreased;
	}

	// ���ߣ�����չ������֮��Ĵ����߼���֮��ɿ��ǲ���Handlerģʽ��
	private int support(Star allyStar, int sentVal) {
		return allyStar.incForceVal(sentVal);
	}
	private int extend(Star neutralStar, int sentVal) {
		int receivedVal = neutralStar.incForceVal(sentVal);
		if (neutralStar.currentForceVal >= threshold) {
			neutralStar.ID = this.ID;
			this.removeGoal(neutralStar);
		}
		return receivedVal;
	}
	private int invade(Star enemyStar, int sentVal) {
		int receivedVal = enemyStar.decForceVal(sentVal);
		if (enemyStar.currentForceVal == 0) {
			enemyStar.ID = this.ID;
			this.removeGoal(enemyStar);
			enemyStar.goals.clear();
		}
		return receivedVal;
	}
	
	// �ӿڣ��������Ϊ�������ʹ��䣩
	public void grow() {
		if (this.ID != StarID.NEUTRAL)
			this.incForceVal(this.growSpeed);
	}
	public void emit() {
		int sentVal, receivedVal;
		for (int i=goals.size()-1; i>=0; --i) {
			Star goal = goals.get(i);
			sentVal = this.decForceVal(this.emitSpeed);
			if (goal.ID == this.ID)
				receivedVal = this.support(goal, sentVal);
			else if (goal.ID == StarID.NEUTRAL)
				receivedVal = this.extend(goal, sentVal);
			else
				receivedVal = this.invade(goal, sentVal);
			this.incForceVal(sentVal - receivedVal);		// sentVal != receivedValʱ��˵��û����ȫ���գ�������or���ޣ�����Ҫ����
		}
	}
}
