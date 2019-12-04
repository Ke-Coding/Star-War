package gameview;

import gamefield.Position;
import star.StarID;

public interface ViewablePlayer {

	/**
	 * ��ѯ��Player������ID
	 */
	public StarID getID();
	
	/**
	 * ��ȡ��Player��ǰ�۽���λ�� @return
	 */
	public Position getFocusedPos();
	
	/**
	 * ��ȡ��Player��ʷѡ�е�λ�� @return���������򷵻�null
	 */
	public Position getSelectedPos();
}
