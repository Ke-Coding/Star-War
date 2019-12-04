package player;

import gamefield.Position;
import star.StarID;

public interface ControllableField {

	/**
	 * ��ѯField�ڵ� @param pos λ���Ƿ��ܱ� @param ID �۽�
	 */
	public boolean isFocusableAt(StarID ID, Position pos);

	/**
	 * ��ѯField�ڵ� @param pos λ���Ƿ��ܱ� @param ID ѡ��
	 */
	public boolean isSelectableAt(StarID ID, Position pos);

	/**
	 * ���� @return Field������һ������  @param ID ռ���λ��
	 */
	public Position getDefaultPos(StarID ID);
	
	/**
	 * �� @param ID ��ݳ��Զ� @param src �� @param dest ���в��������������ӻ�ɾ�����ӣ�
	 * ���� @return �Ա�ʾ�Ƿ�����ɹ�
	 */
	public boolean operateConnection(StarID ID, Position src, Position dest);
}
