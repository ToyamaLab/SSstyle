package supersql.codegenerator.VR;

import supersql.codegenerator.Connector;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class VRC3 extends Connector {

	private VREnv vrEnv;
	private VREnv vrEnv2;

	public VRC3(Manager manager, VREnv henv, VREnv henv2) {
		this.vrEnv = henv;
		this.vrEnv2 = henv2;
	}

	@Override
	public String getSymbol() {
		return "VRC3";
	}
	
	@Override
	public String work(ExtList data_info) {
		//C2から
		this.setDataList(data_info);


		//TODO: verify these 3 ifs are not needed
		if (decos.containsKey("insert")) {
			VREnv.setIDU("insert");
		}
		if (decos.containsKey("update")) {
			VREnv.setIDU("update");
		}
		if (decos.containsKey("delete")) {
			VREnv.setIDU("delete");
		}
		vrEnv.append_css_def_td(vrEnv.getClassID(this), this.decos);
		
		
		int i = 0;

		while (this.hasMoreItems()) {


			this.worknextItem();

			Log.out("</TD>");////</TR>消した

			i++;
		}
		
		if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount) && VRManager.gindex.get(vrEnv.gLevel-2) == 1 && VRManager.gindex.get(vrEnv.gLevel-1) == 1){//これいらないかな？
			try {
				String l=VRManager.multiexh.get(VRManager.nest1count);
				VRManager.multiexh.set(VRManager.nest1count,l+"%");//gindex[]++
			} catch (Exception e) {
				VRManager.multiexh.add("%");
			}
		}
	
		
		Log.out("TFEId = " + VREnv.getClassID(this));
		return null;

	}

}
