package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Baazaoui Adonis / Duran Rémy
 */

public class AntiAffinity extends VmAllocationPolicy {
		
	public AntiAffinity(List<PowerHost> list) {
		super(list);
		new LinkedHashMap<Host, Vm>();
	}
	
	@Override
	public boolean allocateHostForVm(Vm vm) {
        boolean presence = true;
		for(Host h : getHostList()) {
			if(h.getVmList().size() > 0) {
				for(Vm vmTmp : h.getVmList()) {
					presence = checkPresenceVm(vmTmp, vm);
				}
				if(!presence) {
					return allocateVm(vm, h);
				}
			} else {
				return allocateVm(vm, h);
			}
		}
		return false;
    }
	
	private boolean allocateVm(Vm vm, Host h)
	{
		if(h.vmCreate(vm)){
			System.out.println("host ID : " + h.getId() + " vm ID : " + vm.getId());
			return true;
		}
		return false;
	}
	
	public int getPlageByVmId(int idVm)
    {
    	int plage;
    	plage = idVm/100;
    	plage *= 100;
    	return plage;
    }
    
    public boolean checkPresenceVm(Vm vm1, Vm vm2)
    {
    		if(getPlageByVmId(vm1.getId()) == (getPlageByVmId(vm2.getId())))
    			return true;
    		else 
    			return false;
    }

	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		if(host.vmCreate(vm)) {
			return true;
		}
		return false;
	}

	@Override
	public void deallocateHostForVm(Vm vm) {
		vm.getHost().deallocatePesForVm(vm);
	}

	@Override
	public Host getHost(Vm vm) {
		return vm.getHost();
	}

	@Override
	public Host getHost(int vmID, int userID) {
		for(Host host : getHostList()) {
			if(host.getVm(vmID, userID) != null) {
				return host;
			}
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
