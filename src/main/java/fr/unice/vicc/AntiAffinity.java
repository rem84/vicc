package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostStateHistoryEntry;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntiAffinity extends VmAllocationPolicy {

    //To track the Host for each Vm. The string is the unique Vm identifier, composed by its id and its userId
    private Map<String, Host> vmTable;

    public AntiAffinity(List<? extends Host> list) {
        super(list);
        vmTable = new HashMap<>();
    }

    public Host getHost(Vm vm) {
        // We must recover the Host which hosting Vm
        return this.vmTable.get(vm.getUid());
    }

    public Host getHost(int vmId, int userId) {
        // We must recover the Host which hosting Vm
        return this.vmTable.get(Vm.getUid(userId, vmId));
    }

    public boolean allocateHostForVm(Vm vm, Host host) {
        if (host.vmCreate(vm)) {
            //the host is appropriate, we track it
            vmTable.put(vm.getUid(), host);
            return true;
        }
        return false;
    }

    public boolean allocateHostForVm(Vm vm) {
        //First fit algorithm, run on the first suitable node
    	//System.out.println("vm.getUid() : "+vm.getUid()+" plage : "+getPlageByVmId(vm.getId()));
    	
    	int plageVm = getPlageByVmId(vm.getId());
        for (Host h : getHostList()) {
        	if(!checkPresenceVm(h,plageVm))
        	{
				if (h.vmCreate(vm)) 
				{
	                //track the host
					vmTable.put(vm.getUid(), h);
					System.out.println("vm.getUid() : "+vm.getUid()+" host : "+h.getId());
	                return true;
	            }
        	}
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
    
    public boolean checkPresenceVm(Host h, int plage)
    {
    	boolean ret =false;
    	for(int i=0;i<h.getVmList().size();i++)
    	{
    		if(h.getVmList().get(i).getId()>=plage && h.getVmList().get(i).getId()<(plage+100))
    			ret = true;
    		else
    			ret = false;
    	}
		return ret;
    }

    public void deallocateHostForVm(Vm vm,Host host) {
        vmTable.remove(vm.getUid());
        host.vmDestroy(vm);
    }

    @Override
    public void deallocateHostForVm(Vm v) {
        //get the host and remove the vm
        vmTable.get(v.getUid()).vmDestroy(v);
    }

    public static Object optimizeAllocation() {
        return null;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> arg0) {
        //Static scheduling, no migration, return null;
        return null;
    }
}
