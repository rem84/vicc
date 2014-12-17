package fr.unice.vicc;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.power.PowerHost;

public class AntiAffinityObserver extends SimEntity {
	/** The custom event id, must be unique. */
	public static final int OBSERVE = 18;

	private List<PowerHost> hosts;
    private ArrayList<Host>plageListError;

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

	public void checkAntiAffinity()
    {
    	this.plageListError = new ArrayList<Host>();
    	boolean presence = true;
    	for(Host host : hosts){
    		if(!host.getVmList().isEmpty()) {
		    	for(int i=0;i<host.getVmList().size();i++)
		    	{
		    		Vm vm1 = host.getVmList().get(i);
		    		for(Vm vm2 : host.getVmList())
		    		{
	   					if(!(vm1.equals(vm2)) && (!checkPresenceVm(vm1,vm2))) {
	   						System.out.println("Comparaison - vm1 : " + vm1.getId() + " - vm2 : " + vm2.getId());
	   						presence = false;
	   					}
	   				}
				}
				if(!presence) {
					plageListError.add(host);
						TraiteListError(plageListError);
						System.out.println("Erreur");
				}
    		}
    		else {
				System.out.println("Host sans vm (hostId : "+ host.getId()+")");
			}
    	}
    }
	
	public boolean checkPresenceVm(Vm vm1, Vm vm2)
    {
    		if(getPlageByVmId(vm1.getId()) == (getPlageByVmId(vm2.getId())))
    			return true;
    		else 
    			return false;
    }

	public int getPlageByVmId(int idVm) {
		int plage;
		plage = idVm / 100;
		plage *= 100;
		return plage;
	}
	
	private void TraiteListError(ArrayList<Host> list) {
		for(Host host : list) {
			Log.printLine("Error host : " + host.getId());
			for(Vm vm : host.getVmList()) {
				Log.printLine("Detail erreur : vm : " + vm.getId() + " host : " + host.getId());
			}
		}
	}

	
	@Override
	public void processEvent(SimEvent ev) {
		// I received an event
		switch (ev.getTag()) {
		case OBSERVE: 
			checkAntiAffinity();
		//}
			send(this.getId(), delay, OBSERVE, null);}
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
