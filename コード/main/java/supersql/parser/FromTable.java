package supersql.parser;

public class FromTable {
    private String alias;
    private String tableName;
    private String line;

    public FromTable(String name){
        line = name;
        if(name.split(" ").length == 1){
            alias = name;
            tableName = name;
        }else{
            alias = name.split(" ")[1];
            tableName = name.split(" ")[0];
        }
    }

    public String getAlias() {
        return alias;
    }

    public String getTableName() {
        return tableName;
    }

    public String getLine() {
        return line;
    }
}
