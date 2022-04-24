package test;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                //生成getter,setter ,toString等函数
@NoArgsConstructor   //生成无参构造函数
@AllArgsConstructor //生成全参数构造函数
public class PiPeiExcel {

    @ExcelProperty(value = "学生")
    private String st;

    @ExcelProperty(value = "回合1")
    private String round1;

    @ExcelProperty(value = "回合2")
    private String round2;


    @ExcelProperty(value = "回合3")
    private String round3;

    @ExcelProperty(value = "心动4")
    private String round4;


    @ExcelProperty(value = "回合5")
    private String round5;


    @ExcelProperty(value = "回合6")
    private String round6;


    @ExcelProperty(value = "回合7")
    private String round7;

    @ExcelProperty(value = "回合8")
    private String round8;

    @ExcelProperty(value = "回合9")
    private String round9;

    @ExcelProperty(value = "回合10")
    private String round10;

    public boolean equals(Object anObject) {
        return (this.st == ((PiPeiExcel)anObject).st);
    }

    public int hashCode() {
        return this.st.hashCode();
    }

    public static void main(String[] args) {
        System.out.println(0 % 2);
        System.out.println(1 % 2);
        System.out.println(2 % 2);
        System.out.println(3 % 2);
        System.out.println(4 % 2);
        System.out.println(5 % 2);
    }
}
