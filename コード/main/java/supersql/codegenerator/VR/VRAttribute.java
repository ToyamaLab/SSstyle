package supersql.codegenerator.VR;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Manager;
import supersql.codegenerator.HTML.HTMLEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class VRAttribute extends Attribute {

	private VREnv vrEnv;
	private VREnv vrEnv2;

	public static String genre = "";
	public static String Ngenre = "";//N=3以上のxmlタグに付加する用のgenre
	public static ArrayList<Integer> exharray = new ArrayList<Integer>();///部屋の並べ方(G1,G2,G3)が1部屋につき一つ代入される 4部屋あったら4つ代入
	public static ArrayList<Integer> floorarray = new ArrayList<Integer>();//部屋が[],、[]!、[]%のどの方向に並んでいるか 1グループにつき1個 2次目だけ
	public static int[][] Nfloorarray = new int[100][500];//N次元 それぞれのglevelのTFE(部屋のなねラベ方)=[グループの数][glevel] glevelが[0]->[5]で0,2,1,3,1,3
	public static ArrayList<String> genrearray2 = new ArrayList<String>();///カテゴリーごとのタイトルだす、Red,Whiteとか 2次目だけ
	public static ArrayList<Integer> genrearray22 = new ArrayList<Integer>();//0,2,6ってgroupごとのカテゴリーの数を累積で入れていく 2次目だけ
	public static int genrecount = 0;//genrearray22に累積で入れて行ってるgenreの数 2次目だけ
	public static int gjoinflag = 0;
	public static int cjoinflag = 0;
	public static int groupcount = 0;
	public static int groupcount1 = 0;//建物(ビル)の数 >=1 filecreateで代入される
	public static int grouptag = 0;
	public static ArrayList<String> cjoinarray = new ArrayList<String>();////建物間の結合子 建物同士を結合させる時分岐に使う
	public static int elearraySeq = 0;
	public static ArrayList<Element> elearrayXML = new ArrayList<Element>();//elearrayは展示物同士の結合 [id,id]
	public static int[] compx = new int[500];///複合反復子に使う
	public static int[] compy = new int[500];
	public static int[] compz = new int[500];
	public static int[] compflag = new int[100];//複合反復子で、一番最初にくるTFE
	public static int cgcount = 0;//comp group count
	public static boolean componexflag = false;///compx,flagに無駄に値を代入しないよう、１ビルに一回だけ
	public static boolean componeyflag = false;///compy,flagに無駄に値を代入しないよう、１ビルに一回だけ
	public static boolean componezflag = false;///compz,flagに無駄に値を代入しないよう、１ビルに一回だけ

	public static ArrayList<String> multiexhary = new ArrayList<>();////展示物を複数くっつけて並べる、グループごとにTFEを格納
	public static ArrayList<Integer> multiexhcount = new ArrayList<>();////展示物を複数くっつけて並べる時の展示物の数
	
	public static int[] arbitraryarray = new int[500];//グループ(ビル(建物))ごとにarbitraryが行われいるかどうかflag代わり 0はfalse,1はtrue//arbitrary kotani180415
	public static int[][] picturearray = new int[100][500];//グループ(ビル(建物))ごとにpictureが行われいるかどうかflag代わり 0はfalse,1はtrue//picture kotani180415
	public static int[][] wallarray = new int[100][500];//グループ(ビル(建物))ごとにwallが行われいるかどうかflag代わり 0はfalse,1はtrue 0or1=[groupccount][idcount] groupcountは1から//picture kotani180723
	public static int genrecountcompa = 0;//picture,wall {}内の２個目のidに入っているか見るため　genrecountと比較
	public static int idcount = 0;//picture,wall genreの数*{}内のidの合計数　{}内のidの合計数を導くため
	public static ArrayList<Integer> idcountarray = new ArrayList<>();//グループごとのidの数*category 0,8,8みたいな 0から始まる
	
	public static int[] lightflagarray = new int[100];//グループ(ビル(建物))ごとにlight-colorが行われいるかどうかflag代わり 0はfalse,1はtrue//light-color kotani180415
	public static ArrayList<String> lightcolorarray = new ArrayList<>();//lightcolorの値全部代入
	public static int[] exhcountarray = new int[100];//light-color kotani180521 1部屋ごとの展示物の数をカウント
	public static int exhcountnum = 0;//light-color kotani180521　exhcountarrayの要素数
	
	public static int[][] Nopen = new int[100][500];//N次元 分類部屋がopenかseparateか[groupcount][glevel]
	
	public static int[][] detail = new int[100][500];//[group数][1groupに複数]

	public VRAttribute(Manager manager, VREnv henv, VREnv henv2) {
		super();
		this.vrEnv = henv;
		this.vrEnv2 = henv2;
	}

	public VRAttribute(Manager manager, VREnv henv, VREnv henv2, boolean b) {
		super(b);
		this.vrEnv = henv;
		this.vrEnv2 = henv2;
	}

	// Attribute鐃緒申work鐃潤ソ鐃獣ワ申
	@Override
	public String work(ExtList data_info) {
		
		vrEnv.append_css_def_td(HTMLEnv.getClassID(this), this.decos);

		//Process the decorations attached to the attribute if the attribute is not a decoration itself
		if (vrEnv.decorationStartFlag.size() > 0 
				&& ((vrEnv.decorationStartFlag.get(0) || decos.size()>0) 
						&& !vrEnv.decorationEndFlag.get(0))) {
			for (String key : decos.keySet()) {
				String value = decos.get(key).toString();
				//if the decoration value is an attribute, register its name to decorationProperty to process it later
				if (!(value.startsWith("\"") && value.endsWith("\"")) 
						&& !(value.startsWith("\'") && value.endsWith("\'")) 
						&& !supersql.codegenerator.CodeGenerator.isNumber(value)
						) {
					vrEnv.decorationProperty.get(0).add(0, key);
				}
			}
		}
		//if this attribute is a decoration
		if(vrEnv.decorationEndFlag.size() > 0 && vrEnv.decorationEndFlag.get(0)){//ここで装飾子ある時、nameとかの中を指定された属性に変えてる
			//get the property name from decorationProperty
			String property = vrEnv.decorationProperty.get(0).get(0);
			//if the property is name, change the name of the last element entered
			if(property.equals("name")){//name
				for(int i = 0; i < elearrayXML.get(elearraySeq-1).getLastChild().getChildNodes().getLength(); i++){
					Node n = elearrayXML.get(elearraySeq-1).getLastChild().getChildNodes().item(i);
					if (n.getNodeName().equals("name")){
						n.setTextContent(this.getStr(data_info));
					}
				}
			}
			if(property.equals("placement")){//placement kotani180421
				for(int i = 0; i < elearrayXML.get(elearraySeq-1).getLastChild().getChildNodes().getLength(); i++){
					Node n = elearrayXML.get(elearraySeq-1).getLastChild().getChildNodes().item(i);
					if (n.getNodeName().equals("placement")){
						n.setTextContent(this.getStr(data_info));
					}
				}
			}
			if(property.equals("detail")){//detail kotani181114
				for(int i = 0; i < elearrayXML.get(elearraySeq-1).getLastChild().getChildNodes().getLength(); i++){
					Node n = elearrayXML.get(elearraySeq-1).getLastChild().getChildNodes().item(i);
					if (n.getNodeName().equals("detail")){
						n.setTextContent(this.getStr(data_info));
					}
				}
			}
			if(property.equals("light-color")){//light-color kotani180521
				for(int i = 0; i < elearrayXML.get(elearraySeq-1).getLastChild().getChildNodes().getLength(); i++){
					Node n = elearrayXML.get(elearraySeq-1).getLastChild().getChildNodes().item(i);
					//1部屋ごとの展示物の数だして、それ配列に代入
					if(i == 0)
						VRAttribute.lightcolorarray.add(this.getStr(data_info));
					if (n.getNodeName().equals("light-color")){
						n.setTextContent(this.getStr(data_info));
					}
				}
			}
		} else {
			String classname;
			if (this.decos.containsKey("class")) {
				classname = this.decos.getStr("class");
			} else {
				classname = VREnv.getClassID(this);
			}
			if(vrEnv.gLevel <= VRcjoinarray.gLemaxlist.get(groupcount)-1){// kotani 16/10/04//タグのレベルが１(1個目のcategoryが0で、二個目のcategoryは1)だったら、ジャンルの名前持ってくる
				if((vrEnv.gLevel == VRcjoinarray.gLemaxlist.get(groupcount)-1) && (!VRC1.N3flag)){
					genre = this.getStr(data_info);// kotani 16/10/04
				}
				Ngenre = this.getStr(data_info);// kotani N次元 181014
				if(exhcountarray[0] != 0 && lightflagarray[groupcount] == 1)//light-color kotani180521
					exhcountnum++;
			}else{
				if(elearrayXML.size() > elearraySeq){ 
					//Check if the elearray already contains something for this n2 grouper　elementあるからゲットできる 初めの一塊以降
					Element n2 = elearrayXML.get(elearraySeq);
					Element elem = vrEnv.xml.createElement("element");
					Element name = vrEnv.xml.createElement("name");
					name.setTextContent(this.getStr(data_info));//装飾子ないときはこのまま
					elem.appendChild(name);
					
//					Element detail = vrEnv.xml.createElement("detail");
//					detail.setTextContent("detail");
//					elem.appendChild(detail);
					
					if(picturearray[groupcount][idcount-1] == 1){//picture kotani180421
						Element picture = vrEnv.xml.createElement("placement");
						picture.setTextContent("picture");
						elem.appendChild(picture);
					}else if(wallarray[groupcount][idcount-1] == 1){//wall kotani180724
						Element wall = vrEnv.xml.createElement("placement");
						wall.setTextContent("wall");
						elem.appendChild(wall);
					}else{
						Element floor = vrEnv.xml.createElement("placement");
						floor.setTextContent("floor");
						elem.appendChild(floor);
					}
					if(lightflagarray[groupcount] == 1){//light-color kotani180521
						Element lightcolor = vrEnv.xml.createElement("light-color");
						lightcolor.setTextContent(this.getStr(data_info));
//						elem.appendChild(lightcolor);
//						lightcolorarray.add(this.getStr(data_info));
					}
					Element id = vrEnv.xml.createElement("id");
					id.setTextContent(this.getStr(data_info));
					elem.appendChild(id);
					n2.appendChild(elem);
				}else{//if not add a new n2　elementないからaddする　初めの1塊
					idcount++;//picture,wall 
					
					Element n2 = vrEnv.xml.createElement("n2");
					n2.setAttribute("seq", Integer.toString(elearraySeq));
					Element elem = vrEnv.xml.createElement("element");
					Element name = vrEnv.xml.createElement("name");
					name.setTextContent(this.getStr(data_info));
					elem.appendChild(name);
					
					
//					Element detail = vrEnv.xml.createElement("detail");
//					detail.setTextContent("detail");
//					elem.appendChild(detail);
					
					if(picturearray[groupcount][idcount-1] == 1){//picture kotani180421 -1してるのはVREnvとidcountの数が合わなくなるから
						Element picture = vrEnv.xml.createElement("placement");
						picture.setTextContent("picture");
						elem.appendChild(picture);				
					}else if(wallarray[groupcount][idcount-1] == 1){//wall kotani180724
						Element wall = vrEnv.xml.createElement("placement");
						wall.setTextContent("wall");
						elem.appendChild(wall);
					}else{
						Element floor = vrEnv.xml.createElement("placement");
						floor.setTextContent("floor");
						elem.appendChild(floor);
					}
					if(lightflagarray[groupcount] == 1){//light-color kotani180521
						Element lightcolor = vrEnv.xml.createElement("light-color");
						lightcolor.setTextContent(this.getStr(data_info));
//						elem.appendChild(lightcolor);
					}
					Element id = vrEnv.xml.createElement("id");
					id.setTextContent(this.getStr(data_info));
					elem.appendChild(id);
					
					n2.appendChild(elem);
					elearrayXML.add(elearraySeq,n2);
					
					

//					genrecountcompa++;//picture,wall もし同じカテゴリー内でelearraySeqがまた0だったら、{}の２個目に突入してる。
//					if(genrecountcompa != genrecount && elearraySeq == 0){
//						genrecountcompa = genrecount;
//					}
				}
				elearraySeq++;
				if(lightflagarray[groupcount] == 1)
					exhcountarray[exhcountnum]++;//light-color kotani180521
			}

			Log.out("TFEId = " + VREnv.getClassID(this));
			//		}
		}
		return null;
	}

	// optimizer
	//TODO: write a real optimizer?
	public void work_opt(ExtList data_info) {}

}
