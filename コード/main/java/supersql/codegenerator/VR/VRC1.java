package supersql.codegenerator.VR;

import java.io.Serializable;

import org.w3c.dom.Element;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;



public class VRC1 extends Connector implements Serializable {

	private VREnv vrEnv;
	private VREnv vrEnv2;
	
	public static boolean N3flag = false;
	public static int[][]  Nclassct1 = new int[10][500];//それぞれの次元の分類属性の数をカウント 処理前分類属性数=[group数][glevel] group数は1から、glevelは2から始まる
	public static int[][]  Nclassct2 = new int[10][500];//処理後分類属性数=[group数][glevel] group数は1から、glevelは2から始まる

	public VRC1(Manager manager, VREnv henv, VREnv henv2) {
		this.vrEnv = henv;
		this.vrEnv2 = henv2;
	}

	@Override
	public String getSymbol() {
		return "VRC1";
	}

	@Override
	public String work(ExtList data_info) {
		Log.out("------- C1 -------");
		Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
		Log.out("tfes.size=" + tfes.size());
		Log.out("countconnetitem=" + countconnectitem());
		this.setDataList(data_info);

		//TODO: check if the code between A and B is relevant to VR.
		//A
		if (decos.containsKey("insert")) {
			VREnv.setIDU("insert");
		}
		if (decos.containsKey("update")) {
			VREnv.setIDU("update");
		}
		if (decos.containsKey("delete")) {
			VREnv.setIDU("delete");
		}

		String classname;
		if (this.decos.containsKey("class")) {
			classname = this.decos.getStr("class");
		} else {
			classname = VREnv.getClassID(this);
		}
		
		vrEnv.append_css_def_td(VREnv.getClassID(this), this.decos);

		if (!GlobalEnv.isOpt()) {
			if (vrEnv.decorationStartFlag.size() > 0) {
				if (vrEnv.decorationStartFlag.get(0)) {
					VRDecoration.ends.get(0).append(classname);
					vrEnv.decorationStartFlag.set(0, false);
				} else {
					VRDecoration.ends.get(0).append(classname);
				}
			} else {
				if (vrEnv.writtenClassId.contains(VREnv.getClassID(this))) {
					vrEnv.code.append(VREnv.getClassID(this));
				}

				if (decos.containsKey("class")) {
					if (!vrEnv.writtenClassId.contains(VREnv
							.getClassID(this))) {
					} else {
						vrEnv.code.append(" ");
					}
				} else if (vrEnv.writtenClassId.contains(VREnv
						.getClassID(this))) {
				}
			}
		}

		// xml
		if (GlobalEnv.isOpt()) {
			vrEnv2.code.append("<tfe type=\"connect\" dimension =\"1\"");
			if (decos.containsKey("tablealign"))
				vrEnv2.code.append(" align=\""
						+ decos.getStr("tablealign") + "\"");
			if (decos.containsKey("tablevalign"))
				vrEnv2.code.append(" valign=\""
						+ decos.getStr("tablevalign") + "\"");
			if (decos.containsKey("tabletype")) {
				vrEnv2.code.append(" tabletype=\""
						+ decos.getStr("tabletype") + "\"");
				if (decos.containsKey("cellspacing")) {
					vrEnv2.code.append(" cellspacing=\""
							+ decos.getStr("cellspacing") + "\"");
				}
				if (decos.containsKey("cellpadding")) {
					vrEnv2.code.append(" cellpadding=\""
							+ decos.getStr("cellpadding") + "\"");
				}
				if (decos.containsKey("border")) {
					vrEnv2.code.append(" border=\""
							+ decos.getStr("border").replace("\"", "")
							+ "\"");
				}
				if (decos.containsKey("tableborder")) {
					vrEnv2.code.append(" tableborder=\""
							+ decos.getStr("tableborder").replace("\"", "")
							+ "\"");
				}
			} else {
				if (decos.containsKey("border")) {
					vrEnv2.code.append(" border=\""
							+ decos.getStr("border").replace("\"", "")
							+ "\"");
				} else {
					vrEnv2.code.append(" border=\""
							+ vrEnv.tableBorder.replace("\"", "") + "\"");
				}
				if (decos.containsKey("tableborder")) {
					vrEnv2.code.append(" tableborder=\""
							+ decos.getStr("tableborder").replace("\"", "")
							+ "\"");
				}
			}
			if (vrEnv.writtenClassId.contains(VREnv.getClassID(this))) {
				vrEnv2.code.append(" class=\"");
				vrEnv2.code.append(VREnv.getClassID(this));
			}
			if (decos.containsKey("class")) {
				if (!vrEnv.writtenClassId.contains(VREnv
						.getClassID(this))) {
					vrEnv2.code.append(" class=\"");
				} else {
					vrEnv2.code.append(" ");
				}
				vrEnv2.code.append(decos.getStr("class") + "\" ");
			} else if (vrEnv.writtenClassId.contains(VREnv
					.getClassID(this))) {
				vrEnv2.code.append("\" ");
			}

			if (decos.containsKey("form")) {
				vrEnv2.code.append(" form=\"" + VREnv.getFormNumber()
				+ "\" ");
			}
			vrEnv2.code.append(">");
		}
		//B
		
		int i = 0;
		while (this.hasMoreItems()) {

			vrEnv.cNum++;
			vrEnv.xmlDepth++;
			ITFE tfe = tfes.get(i);

			if(VRAttribute.Ngenre.equals("")){// kotani 16/10/04　まだ<category name=◯>って書いてない時
				if(vrEnv.gLevel == 0){//[category,[id]%]! []の外側の所
//					VRAttribute.groupcount++;//G1へ移動 C2とC3のもG2とG3へ移動
				}else if (vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-1){ //vrEnv.gLevel == 1　categoryの所
					vrEnv.currentNode = vrEnv.currentNode.appendChild(vrEnv.xml.createElement("category"));
				}
			}else{
				Element category;
				if (vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-1){
					category = vrEnv.xml.createElement("category2");
					category.setAttribute("name", VRAttribute.Ngenre);
					vrEnv.currentNode = vrEnv.currentNode.appendChild(category);
				}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-2){
					category = vrEnv.xml.createElement("category3");
					category.setAttribute("name", VRAttribute.Ngenre);
					vrEnv.currentNode = vrEnv.currentNode.appendChild(category);
				}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-3){
					category = vrEnv.xml.createElement("category4");
					category.setAttribute("name", VRAttribute.Ngenre);
					vrEnv.currentNode = vrEnv.currentNode.appendChild(category);
				}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-4){
					category = vrEnv.xml.createElement("category5");
					category.setAttribute("name", VRAttribute.Ngenre);
					vrEnv.currentNode = vrEnv.currentNode.appendChild(category);
				}
				
				if ((vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-1) && (!N3flag)){
					VRAttribute.genrecount++;
					VRAttribute.genrearray2.add("\"" + VRAttribute.genre + "\"");
				}

//				if(VRAttribute.genrecount == 0)//ここ問題////////////////
//					VRAttribute.genrearray22.add(0);
				
			}
			
			String classid = VREnv.getClassID(tfe);
			this.worknextItem();

			//TODO: check what this if does
			if (vrEnv.notWrittenClassId.contains(classid)) {
				if(vrEnv.code.indexOf(classid)>=0){
					vrEnv.code.delete(vrEnv.code.indexOf(classid),vrEnv.code.indexOf(classid) + classid.length() + 1);
				}
			}

			i++;
			vrEnv.cNum--;
			vrEnv.xmlDepth--;
		}


		vrEnv2.code.append("</tfe>");
//		if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-1){//オリジナル　N=2 ここで閉じるタグ付加　2-1=1 category
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode().getParentNode();//これでcategory2からglevel0の<group>に戻ってる
//		}
		
//		if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-1){//N=3 2=3-1 category
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode().getParentNode();//category2からcategory3へ
//		}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-2){//1=3-2 company
//			N3flag = true;
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode();//category3からgroupへ
//		}

//		if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-1){//N=4 3=4-1 
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode().getParentNode();//category2からcategory3へ
//		}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-2){//2=4-2
//			N3flag = true;
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode();//category3からcategory4へ
//		}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-3){//1=4-3
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode();//category4からgroupへ
//		}
		
		
		if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-1){
			vrEnv.currentNode = vrEnv.currentNode.getParentNode().getParentNode();
		}else{
			vrEnv.currentNode = vrEnv.currentNode.getParentNode();
		}
		if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-2)
			N3flag = true;
		Nclassct1[VRAttribute.groupcount][vrEnv.gLevel]++;
		
//		if(N=2){
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode().getParentNode();//category2からcategory3へ	
//		}else{
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode();
//		}
//		if(N=3){
//			N3flag = true;
//		}
//		Nclassct1[VRAttribute.groupcount][vrEnv.gLevel]++;
		
//		if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-1){//N=5 4=5-1
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode().getParentNode();//category2からcategory3へ	
//			Nclassct1[VRAttribute.groupcount][vrEnv.gLevel]++;
//		}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-2){//3=5-2
//			N3flag = true;
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode();	//category3からcategory4へ
//			Nclassct1[VRAttribute.groupcount][vrEnv.gLevel]++;
//		}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-3){//2=5-3
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode();	//category4からcategory5へ
//			Nclassct1[VRAttribute.groupcount][vrEnv.gLevel]++;
//		}else if(vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(VRAttribute.groupcount)-4){//1=5-4
//			vrEnv.currentNode = vrEnv.currentNode.getParentNode();	//category5からgroupへ
//			Nclassct1[VRAttribute.groupcount][vrEnv.gLevel]++;
//		}	

		Log.out("+++++++ C1 +++++++");
		return null;
	}
}