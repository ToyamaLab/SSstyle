package supersql.codegenerator.HTML;

import supersql.codegenerator.Connector;
import supersql.codegenerator.Manager;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;


//tbt add 180806
//for join string
public class HTMLCONCAT extends Connector {

    private HTMLEnv htmlEnv;
    private HTMLEnv htmlEnv2;

    public HTMLCONCAT(Manager manager, HTMLEnv htmlEnv, HTMLEnv htmlEnv2) {
        this.htmlEnv = htmlEnv;
        this.htmlEnv2 = htmlEnv2;
    }

    @Override
    public String getSymbol() {
        return "HTMLCONCAT";
    }

    @Override
    public String work(ExtList data_info) {
        Log.out("------- CONCAT -------");
        Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
        Log.out("tfes.size=" + tfes.size());
        Log.out("countconnetitem=" + countconnectitem());
        this.setDataList(data_info);
        String buffer = new String();
        int i = 0;
        GlobalEnv.joinFlag = true;
        while (this.hasMoreItems()) {
            htmlEnv.cNum++;
            htmlEnv.xmlDepth++;
            i++;
            buffer += this.worknextItem();
            Log.out("String is "+buffer);
            htmlEnv.cNum--;
            htmlEnv.xmlDepth--;
        }
        Log.out("result is "+buffer);
        Log.out("TFEId = " + HTMLEnv.getClassID(this));
        GlobalEnv.joinFlag = false;
//        htmlEnv.code.append("<td class=\""+HTMLEnv.getClassID(this)+"\">");
        htmlEnv.code.append(buffer);
//        htmlEnv.code.append("</td>");
        Log.out("+++++++ JOIN +++++++");
        return null;
    }
}
