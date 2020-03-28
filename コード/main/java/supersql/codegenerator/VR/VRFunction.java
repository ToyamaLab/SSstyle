package supersql.codegenerator.VR;

import supersql.codegenerator.Function;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class VRFunction extends Function {

	protected static String updateFile;

	
	public static String opt(String s) {
		if (s.contains("\"")) {
			s = s.replaceAll("\"", "");
		}
		if (s.startsWith("./")) {
			s = s.substring(2, s.length());
		}
		if (s.startsWith("/")) {
			s = s.substring(1, s.length());
		}
		return s;
	}

	private VREnv htmlEnv;
	private VREnv htmlEnv2;

	public VRFunction() {

	}

	public VRFunction(Manager manager, VREnv henv, VREnv henv2) {
		super();
		this.htmlEnv = henv;
		this.htmlEnv2 = henv2;
	}

	protected void Func_null() {
		return;
	}

	protected String className() { // added 20130703
		if (decos.containsKey("class"))
			return " class=\"" + decos.getStr("class") + "\" ";
		return "";
	}

	// Function��work�᥽�å�
	@Override
	public String work(ExtList data_info) {
		this.setDataList(data_info);
//		String FuncName = this.getFuncName();
		//No functions supported in VR yet

		Log.out("TFEId = " + VREnv.getClassID(this));
//		htmlEnv.append_css_def_td(VREnv.getClassID(this), this.decos);
		return null;

	}

}
