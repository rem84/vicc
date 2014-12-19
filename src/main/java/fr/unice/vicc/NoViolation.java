package fr.unice.vicc;

import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

public class NoViolation extends VmAllocationPolicy {
	private Host host = null;	

	public NoViolation(List<? extends Host> list) {
		super(list);
	}

	@Override
	public boolean allocateHostForVm(Vm vm) {
		for(Host h : getHostList()){
			if(vm.getMips()<h.getAvailableMips())
			{
				host = h;
				return allocateVm(vm);
			}
		}
		return allocateVm(vm);
	}
	
	private boolean allocateVm(Vm vm)
	{
		if(host.vmCreate(vm)){
			host.setPeStatus(host.getPeList().get(0).getId(),0);
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