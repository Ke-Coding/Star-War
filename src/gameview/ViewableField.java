package gameview;

import java.util.ArrayList;

import gamefield.Position;
import star.StarID;

public interface ViewableField {
	
	/**
	 * ��ѯField�ڵ� @param pos λ���Ƿ�Ϊ��
	 */
	public boolean isEmptyAt(Position pos);
	
	/**
	 * ��ѯField�ڵ� @param pos λ��������ID
	 */
	public StarID getIDAt(Position pos);

	/**
	 * ��ѯField�ڵ� @param pos λ�õ�ֵ
	 */
	public int getValueAt(Position pos);
	
	/**
	 * ���� @return һ��Position��������ʾ�Ӹ���λ�� @param pos �������ӵ������յ��Position�ļ���
	 */
	public void fetchDestinationsFrom(Position src, ArrayList<Position> dests);
}
