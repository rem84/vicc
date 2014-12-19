package fr.unice.vicc;

import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.power.PowerHost;

/**
 * @author Baazaoui Adonis / Duran Rémy
 */

public class LoadBalancerObserver extends SimEntity {
	/** The custom event id, must be unique. */
	public static final int OBSERVE = 17;

	private List<PowerHost> hosts;
	private double mipsMax,mipsMin;

	private float delay;

	public static final float DEFAULT_DELAY = 1;

	public LoadBalancerObserver(List<PowerHost> hosts) {
		this(hosts, DEFAULT_DELAY);
	}

	public LoadBalancerObserver(List<PowerHost> hosts, float delay) {
		super("LoadBalancerObserver");
		this.hosts = hosts;
		this.delay = delay;
	}
	
	public void eval_balancer()
	{
		for(Host h : hosts){
    		double tmpMips = h.getAvailableMips();
    		if(mipsMax != 0 && mipsMin != 0){
    			if(tmpMips >= mipsMax){
    				mipsMax = tmpMips;
    			}
    			else{
    				mipsMin = tmpMips;
    			}
    		}
    		else {
    			mipsMax = tmpMips;
    			mipsMin = tmpMips;
    		}
    	}
		affiche_calcul_ratio();
	}

	public void affiche_calcul_ratio()
    {
    	Log.printLine("mips ratio = "+(mipsMax-mipsMin));
    }
	
	@Override
	public void processEvent(SimEvent ev) {
		// I received an event
		switch (ev.getTag()) {
		case OBSERVE: 
			eval_balancer();
			send(this.getId(), delay, OBSERVE, null);
		}
	}


	@Override
	public void shutdownEntity() {
		Log.printLine(getName() + " is shutting down...");
	}

	@Override
	public void startEntity() {
		Log.printLine(getName() + " is starting...");
		send(this.getId(), delay, OBSERVE, null);
	}

	@Override
	public int getId() {
		return OBSERVE;
	}
}
