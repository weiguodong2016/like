package test;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                //生成getter,setter ,toString等函数
@NoArgsConstructor   //生成无参构造函数
@AllArgsConstructor //生成全参数构造函数
public class StudentExcel {

    @ExcelProperty(value = "学生")
    private String st;

    @ExcelProperty(value = "心动1")
    private String like1;

    @ExcelProperty(value = "心动2")
    private String like2;


    @ExcelProperty(value = "心动3")
    private String like3;

    @ExcelProperty(value = "心动4")
    private String like4;


    @ExcelProperty(value = "心动5")
    private String like5;


    @ExcelProperty(value = "心动6")
    private String like6;


    @ExcelProperty(value = "心动7")
    private String like7;

    @ExcelProperty(value = "心动8")
    private String like8;

    @ExcelProperty(value = "心动9")
    private String like9;

    @ExcelProperty(value = "心动10")
    private String like10;

}
