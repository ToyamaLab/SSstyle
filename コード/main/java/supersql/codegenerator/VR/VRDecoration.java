package supersql.codegenerator.VR;

import java.util.ArrayList;

import supersql.codegenerator.Decorator;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

//tk

public class VRDecoration extends Decorator {

	private VREnv htmlEnv;
	private VREnv htmlEnv2;
	
	public static ArrayList<StringBuffer> fronts = new ArrayList<StringBuffer>();
	public static ArrayList<StringBuffer> classes = new ArrayList<StringBuffer>();
	public static ArrayList<StringBuffer> styles = new ArrayList<StringBuffer>();
	public static ArrayList<StringBuffer> ends = new ArrayList<StringBuffer>();
	
	public VRDecoration(Manager manager, VREnv henv, VREnv henv2) {
		this.htmlEnv = henv;
		this.htmlEnv2 = henv2;
	}

	@Override
	public String getSymbol() {
		return "HTMLDecoration";
	}

	@Override
	public String work(ExtList data_info) {
		Log.out("------- Decoration -------");
		Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
		Log.out("tfes.size=" + tfes.size());
		Log.out("countconnetitem=" + countconnectitem());
		StringBuffer Front = new StringBuffer();
		StringBuffer classname = new StringBuffer();
		StringBuffer Style = new StringBuffer();
		StringBuffer End = new StringBuffer();
		fronts.add(0, Front);
		classes.add(0, classname);
		styles.add(0, Style);
		ends.add(0, End);
		ArrayList<String> decoproperty = new ArrayList<String>();
		htmlEnv.decorationProperty.add(0, decoproperty);
		htmlEnv.decorationStartFlag.add(0, false);
		htmlEnv.decorationEndFlag.add(0, false);

		this.setDataList(data_info);//d.floor
		

		int i = 0;

		while (this.hasMoreItems()) {
			//ITFE tfe = tfes.get(i);
			//String classid = HTMLEnv.getClassID(tfe);
			htmlEnv.decorationStartFlag.set(0, true);
			this.worknextItem();
			htmlEnv.decorationEndFlag.set(0, true);
		}
		
		fronts.remove(0);
		classes.remove(0);
		styles.remove(0);
		ends.remove(0);
		htmlEnv.decorationProperty.remove(0);
		htmlEnv.decorationStartFlag.remove(0);
		htmlEnv.decorationEndFlag.remove(0);
		
		Log.out("+++++++ Decoration +++++++");
		return null;
	}

}



//package supersql.codegenerator.VR;
//
//import java.util.ArrayList;
//
//import supersql.codegenerator.Connector;
//import supersql.codegenerator.Decorator;
//import supersql.codegenerator.ITFE;
//import supersql.codegenerator.Manager;
//import supersql.common.GlobalEnv;
//import supersql.common.Log;
//import supersql.extendclass.ExtList;
//
////tk
//
//public class VRDecoration extends Decorator {
//
//	private VREnv vrEnv;
//	private VREnv vrEnv2;
//	
//	public static ArrayList<StringBuffer> fronts = new ArrayList<StringBuffer>();
//	public static ArrayList<StringBuffer> classes = new ArrayList<StringBuffer>();
//	public static ArrayList<StringBuffer> styles = new ArrayList<StringBuffer>();
//	public static ArrayList<StringBuffer> ends = new ArrayList<StringBuffer>();
//	
//	// ���󥹥ȥ饯��
//	public VRDecoration(Manager manager, VREnv henv, VREnv henv2) {
//		this.vrEnv = henv;
//		this.vrEnv2 = henv2;
//	}
//
//	@Override
//	public String getSymbol() {
//		return "VRDecoration";
//	}
//
//	// Decoration��work�᥽�å�
//	@Override
//	public String work(ExtList data_info) {
//		Log.out("------- Decoration -------");
//		Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
//		Log.out("tfes.size=" + tfes.size());
//		Log.out("countconnetitem=" + countconnectitem());
//		
//		StringBuffer Front = new StringBuffer();
//		StringBuffer classname = new StringBuffer();
//		StringBuffer Style = new StringBuffer();
//		StringBuffer End = new StringBuffer();
//		fronts.add(0, Front);
//		classes.add(0, classname);
//		styles.add(0, Style);
//		ends.add(0, End);
//		ArrayList<String> decoproperty = new ArrayList<String>();
//		vrEnv.decorationProperty.add(0, decoproperty);
//		vrEnv.decorationStartFlag.add(0, false);
//		vrEnv.decorationEndFlag.add(0, false);
//
//		this.setDataList(data_info);
//		
//		// tk
//		// start///////////////////////////////////////////////////////////////////////
////		vrEnv.append_css_def_td(VREnv.getClassID(this), this.decos);
//		
////		if (!GlobalEnv.isOpt()) {
////			vrEnv.code
////					.append("<TABLE cellSpacing=\"0\" cellPadding=\"0\" border=\"");
////			vrEnv.code.append(vrEnv.tableBorder + "\"");
////			vrEnv.code.append(vrEnv.getOutlineMode());
////			/*
////			 * if(decos.containsKey("outborder")){
////			 * vr_env.code.append(" noborder ");
////			 * vr_env2.code.append(" noborder "); }
////			 */
////			// classid������Ȥ��ˤ�������
////			if (vrEnv.writtenClassId.contains(VREnv.getClassID(this))) {
////				vrEnv.code.append(" class=\"");
////				vrEnv.code.append(VREnv.getClassID(this));
////			}
////
////			if (decos.containsKey("class")) {
////				if (!vrEnv.writtenClassId.contains(VREnv.getClassID(this))) {
////					vrEnv.code.append(" class=\"");
////				} else {
////					vrEnv.code.append(" ");
////				}
////				vrEnv.code.append(decos.getStr("class") + "\" ");
////			} else if (vrEnv.writtenClassId
////					.contains(VREnv.getClassID(this))) {
////				vrEnv.code.append("\" ");
////			}
////			vrEnv.code.append("><TR>");
////		}
//
//		// xml
////		if (GlobalEnv.isOpt()) {
////			vrEnv2.code.append("<tfe type=\"connect\" dimension =\"1\"");
////			if (decos.containsKey("tablealign"))
////				vrEnv2.code.append(" align=\"" + decos.getStr("tablealign")
////						+ "\"");
////			if (decos.containsKey("tablevalign"))
////				vrEnv2.code.append(" valign=\"" + decos.getStr("tablevalign")
////						+ "\"");
////			if (decos.containsKey("tabletype")) {
////				vrEnv2.code.append(" tabletype=\""
////						+ decos.getStr("tabletype") + "\"");
////				if (decos.containsKey("cellspacing")) {
////					vrEnv2.code.append(" cellspacing=\""
////							+ decos.getStr("cellspacing") + "\"");
////				}
////				if (decos.containsKey("cellpadding")) {
////					vrEnv2.code.append(" cellpadding=\""
////							+ decos.getStr("cellpadding") + "\"");
////				}
////				if (decos.containsKey("border")) {
////					vrEnv2.code.append(" border=\""
////							+ decos.getStr("border").replace("\"", "") + "\"");
////				}
////				if (decos.containsKey("tableborder")) {
////					vrEnv2.code.append(" tableborder=\""
////							+ decos.getStr("tableborder").replace("\"", "")
////							+ "\"");
////				}
////			} else {
////				if (decos.containsKey("border")) {
////					vrEnv2.code.append(" border=\""
////							+ decos.getStr("border").replace("\"", "") + "\"");
////				} else {
////					vrEnv2.code.append(" border=\""
////							+ vrEnv.tableBorder.replace("\"", "") + "\"");
////				}
////				if (decos.containsKey("tableborder")) {
////					vrEnv2.code.append(" tableborder=\""
////							+ decos.getStr("tableborder").replace("\"", "")
////							+ "\"");
////				}
////			}
////			if (vrEnv.writtenClassId.contains(VREnv.getClassID(this))) {
////				vrEnv2.code.append(" class=\"");
////				vrEnv2.code.append(VREnv.getClassID(this));
////			}
////			if (decos.containsKey("class")) {
////				if (!vrEnv.writtenClassId.contains(VREnv.getClassID(this))) {
////					vrEnv2.code.append(" class=\"");
////				} else {
////					vrEnv2.code.append(" ");
////				}
////				vrEnv2.code.append(decos.getStr("class") + "\" ");
////			} else if (vrEnv.writtenClassId
////					.contains(VREnv.getClassID(this))) {
////				vrEnv2.code.append("\" ");
////			}
////
////			if (decos.containsKey("form")) {
////				vrEnv2.code.append(" form=\"" + VREnv.getFormNumber()
////						+ "\" ");
////			}
////			vrEnv2.code.append(">");
////		}
//
//		// tk
//		// end////////////////////////////////////////////////////////////////////
//
//		// Log.out("<TABLE class=\""+VREnv.getClassID(this) + "\"><TR>");
//		int i = 0;
//
//		if (decos.containsKey("form")) {
//			vrEnv.code.append(VRFunction.createForm(decos));
//			VREnv.setFormItemFlg(true, null);
//		}
//
//		while (this.hasMoreItems()) {
//			ITFE tfe = tfes.get(i);
//			String classid = VREnv.getClassID(tfe);
//
//			vrEnv.decorationStartFlag.set(0, true);
//			
//			this.worknextItem();
//
//			vrEnv.decorationEndFlag.set(0, true);
//
//			i++;
//		}
//
////		vrEnv2.code.append("</tfe>");
////		if (decos.containsKey("form")) {
////			vrEnv2.code.append("<form" + VREnv.getFormNumber() + "end />");
////			Log.out("<form" + VREnv.getFormNumber() + "end />");
////			vrEnv.code.append(VREnv.exFormNameCreate());
////			vrEnv.code.append("</form>");
////			VREnv.setFormItemFlg(false, null);
////			VREnv.incrementFormNumber();
////			if (decos.getStr("form").toLowerCase().equals("search"))
////				VREnv.setSearch(false);
////		}
////		vrEnv.code.append("</TR></TABLE>\n");
//		
//		if (vrEnv.decorationStartFlag.size() > 1) {
////			ends.get(1).append(fronts.get(0));
//			if (!styles.get(0).equals("")) {
////				ends.get(1).append(" style=\"");
////				ends.get(1).append(styles.get(0));
////				ends.get(1).append("\"");
//			}
////			ends.get(1).append(classes.get(0));
////			ends.get(1).append(ends.get(0));
//		} else {
////			vrEnv.code.append(fronts.get(0));
//			if (!styles.get(0).equals("")) {
////				vrEnv.code.append(" style=\"");
////				vrEnv.code.append(styles.get(0));
////				vrEnv.code.append("\"");
//			}
////			vrEnv.code.append(classes.get(0));
////			vrEnv.code.append(ends.get(0));
//		}
//		
//		fronts.remove(0);
//		classes.remove(0);
//		styles.remove(0);
//		ends.remove(0);
//		vrEnv.decorationProperty.remove(0);
//		vrEnv.decorationStartFlag.remove(0);
//		vrEnv.decorationEndFlag.remove(0);
//		
//		Log.out("+++++++ Decoration +++++++");
//		return null;
//	}
//
//}
