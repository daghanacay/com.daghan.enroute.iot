package com.daghan.iot.utils.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents all the configuration in the system. Factory configurations
 * correspond to devices and child configurations correspond to the
 * configurations of the created instances
 * 
 * @author daghan
 *
 */
public class PidObject {
	private String pidStr;
	private List<String> childPids = new ArrayList<>();

	/**
	 * Constructor for Factory configuration Pid
	 * 
	 * @param pidString
	 *            pid of the component that the configuration is attached to
	 */
	public PidObject(String pidString) {
		this.pidStr = pidString;
	}

	/**
	 * Returns the name of this factory PID
	 * 
	 * @return
	 */
	public String getPidStr() {
		return pidStr;
	}

	/**
	 * Adds a child pid if this is the parent of the attempted child Pid. that
	 * is
	 * 
	 * childPidName.startsWith(getPidStr())
	 * 
	 * should be true before the child is added
	 * 
	 * @param childPidName
	 *            name of the child pid
	 * @return true if the child is added to the list
	 */

	protected boolean addChildPid(String childPidName) {
		return childPids.add(childPidName);

	}

	/**
	 * remove child PID for this factory PID
	 * 
	 * @param servicePID
	 */
	public void removeChildPid(String servicePID) {
		childPids.remove(servicePID);
	}

	/**
	 * The names of all the child PID objects created from using this factory
	 * PID as an unmodifiable list. See {@link #addChildPid(String)} if you need
	 * to add child
	 * 
	 * @return empty list if there is no child use
	 */
	public List<String> getChildPids() {
		return Collections.unmodifiableList(childPids);
	}

	@Override
	public String toString() {
		return "PidObject [pidStr=" + pidStr + ", childPids=" + childPids + "]";
	}
}
