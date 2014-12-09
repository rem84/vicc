package fr.unice.vicc;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.power.PowerHost;

public class AntiAffinityObserver extends SimEntity {
	/** The custom event id, must be unique. */
	public static final int OBSERVE = 953248;

	private List<PowerHost> hosts;

	private boolean antiAffinity;

	private float delay;

	public static final float DEFAULT_DELAY = 1;

	public AntiAffinityObserver(List<PowerHost> hosts) {
		this(hosts, DEFAULT_DELAY);
	}

	public AntiAffinityObserver(List<PowerHost> hosts, float delay) {
		super("antiAffinityObserver");
		this.hosts = hosts;
		this.delay = delay;
	}

	public void checkPresenceVm()
    {
    	boolean ret =false;
    	List <Integer> plageList = new ArrayList<>();
    	
        for(int j=0;j<hosts.size();j++)
    	for(int i=0;i<hosts.get(j).getVmList().size();i++)
    	{
    		int plage = getPlageByVmId(hosts.get(j).getVmList().get(i).getId());
    		for(int a=0;a<plageList.size();a++)
			{
				if(plage==plageList.get(a).intValue())
				{
					System.err.println("Error :  anti-affinity detected / id vm : "+hosts.get(j).getVmList().get(i).getId()+", host id : "+hosts.get(j).getId());
					break;
				}
				else
				{
					plageList.add(plage);
				}
			}
    	}
    }

	public int getPlageByVmId(int idVm) {
		int plage;
		plage = idVm / 100;
		plage *= 100;
		return plage;
	}

	/*
	 * This is the central method to implement. CloudSim is event-based. This
	 * method is called when there is an event to deal in that object. In
	 * practice: create a custom event (here it is called OBSERVE) with a unique
	 * int value and deal with it.
	 */
	@Override
	public void processEvent(SimEvent ev) {
		// I received an event
		switch (ev.getTag()) {
		case OBSERVE: // It is my custom event
			// I must observe the datacenter
			/*antiAffinity = */checkPresenceVm();
			// Observation loop, re-observe in `delay` seconds
			send(this.getId(), delay, OBSERVE, null);
		}
	}

	/**
	 * Get the peak power consumption.
	 * 
	 * @return a number of Watts
	 */
	/*public double getPeak() {
		return peak;
	}*/

	@Override
	public void shutdownEntity() {
		Log.printLine(getName() + " is shutting down...");
	}

	@Override
	public void startEntity() {
		Log.printLine(getName() + " is starting...");
		// I send to myself an event that will be processed in `delay` second by
		// the method
		// `processEvent`
		send(this.getId(), delay, OBSERVE, null);
	}

	@Override
	public int getId() {
		return OBSERVE;
	}
}
