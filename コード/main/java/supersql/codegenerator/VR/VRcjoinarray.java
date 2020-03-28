package supersql.codegenerator.VR;

import java.util.ArrayList;

import org.datanucleus.store.rdbms.exceptions.NullValueException;
import org.stringtemplate.v4.compiler.STParser.ifstat_return;

import supersql.common.GlobalEnv;

public class VRcjoinarray {
	public static String query;
	public static ArrayList<Integer> gLemaxlist = new ArrayList<>();//1からglevelmax入れていく
	
	


	private static String removeComment(){////クエリからコメントアウト除去
		StringBuffer tmp = new StringBuffer();
		String commentOutLetters = ""+GlobalEnv.COMMENT_OUT_LETTER+GlobalEnv.COMMENT_OUT_LETTER;
		try{
			String[] lines = query.split("[\\n\\r]");
					
			for (int lineNumber = 0; lineNumber < lines.length; lineNumber++){	
				String line = lines[lineNumber];
				while (line.contains("/*")){
					String line1 = line.substring(0, line.indexOf("/*"));
					while (!line.contains("*/")){
						if(lineNumber < lines.length) {
							line = lines[lineNumber];
							lineNumber++;
						} 
					}
					line = line1 + line.substring(line.indexOf("*/") + 2);
				}		
						
				if (line.contains(commentOutLetters) || line.contains("\\\"") || line.contains("\"\"")){	//commentOutLetters = "--"
					boolean dqFlg = false;
					int i = 0;
					for (i=0; i < line.length(); i++){
						if (!dqFlg && line.charAt(i) == '"' && i>0 && i<line.length()-1
						&& (line.charAt(i-1) != '\\' && line.charAt(i-1) != '"' && line.charAt(i+1) != '"'))	//omit \" and ""
							dqFlg = true;
						else if (dqFlg && line.charAt(i) == '"' && i>0 && i<line.length()-1
						&& (line.charAt(i-1) != '\\' && line.charAt(i-1) != '"' && line.charAt(i+1) != '"'))	//omit \" and ""
							dqFlg = false;
		
						if(dqFlg && i>0 && (line.charAt(i-1)=='\\' || line.charAt(i-1) == '"') && line.charAt(i)=='"'){	//if \" or ""
									line = line.substring(0,i-1)+"&quot;"+line.substring(i+1,line.length());
						} else if (!dqFlg && i < line.length()-1 && line.charAt(i)==GlobalEnv.COMMENT_OUT_LETTER && line.charAt(i+1)==GlobalEnv.COMMENT_OUT_LETTER){
							break;
						}
					}
					line = line.substring(0, i);
				}
					
				tmp.append(" " + line);
			}
			
					
		} catch (Exception e) {
		}
		
		
		String query1 = tmp.toString().trim();
		return query1;
	}
	
	
	public static String getTFE() {///クエリからGENERATE VR(unity)とFROM〜除去
		int fromIndex = 0;///fromが始める位置
		String query2 = removeComment();///コメントアウト除去したクエリ代入
		query2 = query2.replaceAll("  "," ");
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("[\\W]from[\\W]");
		java.util.regex.Matcher m = p.matcher(query2.toLowerCase());
		if (m.find()){
			fromIndex = m.start();	
		}
		String tfe = query2.substring("generate unity_".length(), fromIndex);
		tfe = tfe.replaceAll("[\\n\\r]", "");
		return tfe;
	}
	
	public static void getJoin(){///ビルの繋げ方の記号を取って、配列に格納
		String c ="";
		String c1 ="";
		int count = 0;
		int gLevelmax= 0;//クエリが何次元か 初めに測定
		int countstart = 0;
		int countend = 0;
		int cgroupcount = 1;//ここだけで使うgroupcount
		boolean prevBrack = false;//Previous character is a closing bracket.
		String tfe = getTFE();
		String join = tfe.replaceAll(" ","");
		for(int i=0; i<join.length(); i++){
			c = join.substring(i,i+1);
			if(c.equals("[")) {
		       count++;
		       countstart++;
		       gLevelmax = count;
		    }
			if(count == 0){
		    	if(!prevBrack && (c.equals(",") || c.equals("!") || c.equals("%")))
		    	{
		    		VRAttribute.cjoinarray.add(c);//ビルの間の結合子をcjoinarrayにadd
		    	}
		    }
			
			if(c.equals("]")) {
		       count--;
		       countend++;
		       prevBrack = true;
		       
		       
		   	//floorarrayにglevelが0,1,2,3,4(gelvelmaxではない)を取得
		   	//filecereateの下の方のNのとこで使う
		       if(join.substring(i+1,i+2).equals(",")){
		    	   VRAttribute.Nfloorarray[cgroupcount][count+1] = 1;/////////////////////
		    	   if((count+1) == gLevelmax-1)
		    		   VRAttribute.floorarray.add(1);
		       }else if(join.substring(i+1,i+2).equals("!")){
		    	   VRAttribute.Nfloorarray[cgroupcount][count+1] = 2;
		    	   if((count+1) == gLevelmax-1)
		    		   VRAttribute.floorarray.add(2);
		       }else if(join.substring(i+1,i+2).equals("%")){
		    	   VRAttribute.Nfloorarray[cgroupcount][count+1] = 3;
		    	   if((count+1) == gLevelmax-1)
		    		   VRAttribute.floorarray.add(3);
		       }
		       if(countstart == countend){//ビル一個分終わったら
		    	   cgroupcount++;
		    	   gLemaxlist.add(gLevelmax);
		       }
		       
			}else{
				prevBrack = false;
			}		    
		}
	}
	
	public static void getexhJoin(){///展示物の繋げ方の記号を取って、配列に格納　[id,id]のやつ
		String c ="";
		String s ="";
		int count = 0;
		int gLearrayint = 0;
		int exhcount = 0;
		boolean gLeflag = true;
		boolean prevBrack = false;//Previous character is a closing bracket bracket.
		String tfe = getTFE();
		String join = tfe.replaceAll(" ","");
		for(int i=0; i<join.length();i++){
			c = join.substring(i,i+1);
			if(c.equals("[")) {
		       count++;
		       if(gLeflag){
		    	   gLearrayint++;//建物に入った最初の一回だけ
		    	   gLeflag = false;
		       }
		    }
			
			if(count == gLemaxlist.get(gLearrayint)){//countがmaxにいて(一番真ん中にいて)
		    	if(!prevBrack && (c.equals(",") || c.equals("!") || c.equals("%"))){
		    		s += c;
		    		exhcount++;
		    	}
		    }
			
			if(c.equals("]")) {
				count--;
				prevBrack = true;
				if(count == 0){
					gLeflag = true;
		    		VRAttribute.multiexhary.add(s);//groupごとにTFEを格納		    		
		    		VRAttribute.multiexhcount.add(exhcount+1);///groupごとに何個idがあるか
		    		s ="";
		    		exhcount = 0;
				}
			}else{
				prevBrack = false;
			}
			
		}
	}	
}
