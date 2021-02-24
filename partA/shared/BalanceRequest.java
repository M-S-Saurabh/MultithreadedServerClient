/*******************************************************************************
 * Authors:
 * ---------
 * Saurabh Mylavaram (mylav008@umn.edu)
 * Edwin Nellickal (nelli053@umn.edu)
 ******************************************************************************/
package partA.shared;

public class BalanceRequest extends Request {

	private int uid;

	public BalanceRequest(int uid) {
		super(Constants.BALANCE_REQUEST);
		this.setUid(uid);
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
}
