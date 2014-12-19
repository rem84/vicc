package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import java.util.List;
import java.util.Map;

/**
 * @author Baazaoui Adonis / Duran Rémy
 */

public class LoadBalancer extends VmAllocationPolicy {
	private double mips = 0;
	private Host host = null;
	
	
	public LoadBalancer(List<? extends Host> list) {
		super(list);
	}

	@Override
	public boolean allocateHostForVm(Vm vm) {
		
		for(Host h : getHostList()){
			double tmpMips = h.getAvailableMips();
			if(tmpMips > mips){
				mips = tmpMips;
				host = h;
			}
			return allocateVm(vm);
		}
			
		return false;
	}
	
	private boolean allocateVm(Vm vm)
	{
		if(host.vmCreate(vm)){
			System.out.println("host ID : " + host.getId() + " vm ID : " + vm.getId());
			return true;
		}
		return false;
	}

	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		return host.vmCreate(vm);
	}

	@Override
	public void deallocateHostForVm(Vm vm) {
		vm.getHost().vmDestroy(vm);
	}

	@Override
	public Host getHost(Vm vm) {
		return vm.getHost();
	}

	@Override
	public Host getHost(int vmId, int userId) {
		for(Host cHost : getHostList()){
			if(cHost.getVm(vmId, userId) != null){
				return cHost;
			}
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> vmList) {
		// TODO Auto-generated method stub
		return null;
	}

}
