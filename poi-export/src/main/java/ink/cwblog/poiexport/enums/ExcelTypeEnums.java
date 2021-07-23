package ink.cwblog.poiexport.enums;

/**
 * @author chenw
 * @date 2021/7/23 14:22
 *
 * excel 类型枚举
 */
public enum  ExcelTypeEnums {

    HSSF("org.apache.poi.hssf.usermodel.HSSFWorkbook","xls"),
    XSSF("org.apache.poi.xssf.usermodel.XSSFWorkbook","xlsx"),
    SXSSF("org.apache.poi.xssf.streaming.SXSSFWorkbook","xlsx");

    private String className;

    private String suffix;

    ExcelTypeEnums(String className,String suffix){
        this.className = className;
        this.suffix = suffix;
    }

    public String getClassName() {
        return className;
    }

    public String getSuffix() {
        return suffix;
    }
}
