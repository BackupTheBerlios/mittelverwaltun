/*
 * Created on 30.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;

import dbObjects.Rolle;

/**
 * @author Mario
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ActivityRelatedElement {
	public void setActivityStatus(int[] validActivities);
	public void setActivityStatus(Rolle r);
}
