package test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class StudentUtils {

    public void dealData(List<MatchingExcel> matchingExcels, String from, String to, int round, Map<String, MatchingExcel> matchingExcelMap) {
        MatchingExcel matchingExcel = new MatchingExcel();
        if (round == 0 && !matchingExcelMap.containsKey(from)) { //新增数据
            matchingExcel.setSt(from);
            dealRound(matchingExcel, to, round);
            matchingExcels.add(matchingExcel);
            matchingExcelMap.put(from, matchingExcel);
        } else { //改数据
            matchingExcel = matchingExcelMap.get(from);
            dealRound(matchingExcel, to, round);
        }
    }

    public void addToMatchingExcels(List<MatchingExcel> matchingExcels, String from, String to, int round, Map<String, MatchingExcel> matchingExcelMap, List<String> matchingResultList) {
        dealData(matchingExcels, from, to, round, matchingExcelMap);
        dealData(matchingExcels, to, from, round, matchingExcelMap);
        if (from.compareTo(to) > 0) {
            matchingResultList.add(to + "_" + from);
        } else {
            matchingResultList.add(from + "_" + to);
        }
    }

    private void dealRound(MatchingExcel matchingExcel, String to, int round) {
        switch (round) {
            case 0:
                matchingExcel.setRound1(to);
                break;
            case 1:
                matchingExcel.setRound2(to);
                break;
            case 2:
                matchingExcel.setRound3(to);
                break;
            case 3:
                matchingExcel.setRound4(to);
                break;
            case 4:
                matchingExcel.setRound5(to);
                break;
            case 5:
                matchingExcel.setRound6(to);
                break;
            case 6:
                matchingExcel.setRound7(to);
                break;
            case 7:
                matchingExcel.setRound8(to);
                break;
            case 8:
                matchingExcel.setRound9(to);
                break;
            case 9:
                matchingExcel.setRound10(to);
                break;
            default:
                System.out.println("非法输入");
        }
    }


    public List<MatchingExcel> getMatchingResults(List<Student> students, int maxRound) {
        if (students == null || students.size() ==0) {
            return null;
        }
        int round = 0; //第几轮
        Map<String, MatchingExcel> matchingExcelMap = new HashMap<>(students.size());
        MatchingExcel bestMatchingNum = new MatchingExcel(); //记录每轮最佳的匹配次数
        bestMatchingNum.setSt("每轮最佳的匹配次数");
        int studentNum = students.size();
        List<String> matchingResultList = new ArrayList<>(maxRound); //maxRound次匹配结果 用于判断没有重复结果
        List<MatchingExcel> matchingExcels = new ArrayList<>(students.size() + 1);
        Map<String, Student> studentMap = new HashMap<>(studentNum);

        for (; round < maxRound; round++) {
            //根据id放进map
            for (Student s :  students) {
                studentMap.put(s.getId(), s);
            }
            List<String> matchingList = new ArrayList<>(students.size());
            int matchingNum = 0; //当前轮的最佳匹配次数
            Student currentStudent = null; //当前学生
            Student likePerson = null; //与当前学生匹配的学生
            for (int i = 0; i < studentNum; i++) {
                currentStudent = students.get(i);
                if (currentStudent.isMatched()) {
                    continue;
                }
                List likes = currentStudent.getLikeIds();
                if (likes != null && likes.size() > 0) {
                    for (int j = 0; j < likes.size(); j++) {
                        likePerson = studentMap.get(likes.get(j));
                        if (likePerson != null) {
                            if (new Random().nextInt(8) > 5 && !likePerson.isMatched() && likePerson.getLikeIds().contains(currentStudent.getId())) { //二个都喜欢
                                addToMatchingExcels(matchingExcels, currentStudent.getId(), likePerson.getId(), round, matchingExcelMap, matchingList);
                                studentMap.remove(currentStudent.getId());
                                studentMap.remove(likePerson.getId());
                                currentStudent.setMatched(true);
                                likePerson.setMatched(true);
                                matchingNum++;
                                break;
                            }
                        }
                    }
                }
            }

            //对studentMap再匹配
            Iterator iterator = studentMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Student student = (Student)entry.getValue();
                if (student.isMatched()) {
                    continue;
                }
                List likes = student.getLikeIds();
                if (likes != null && likes.size() > 0) {
                    for (int j = 0; j < likes.size(); j++) {
                        likePerson = studentMap.get(likes.get(j));
                        if (likePerson != null) {
                            if (!likePerson.isMatched() && likePerson.getLikeIds().contains(student.getId())) { //二个都喜欢
                                addToMatchingExcels(matchingExcels, student.getId(), likePerson.getId(), round, matchingExcelMap, matchingList);
                                student.setMatched(true);
                                likePerson.setMatched(true);
                                matchingNum++;
                                break;
                            }
                        }
                    }
                }
            }

            //移除再匹配成功的学生
            for (Iterator<Map.Entry<String, Student>> iter = studentMap.entrySet().iterator(); iter.hasNext();){
                Map.Entry<String, Student> item = iter.next();
                if (item.getValue().isMatched()) {
                    iter.remove();
                }
            }

            //剩下对studentMap 打乱匹配
            int mapNum = studentMap.size();
            if (mapNum != 0) {
                String from = null;
                String to = null;
                for (int l = 0; l < mapNum; l++) {
                    String randomValue = getRandomFromMap(studentMap);
                    if (randomValue == null) {
                        break;
                    }
                    if (l % 2 == 0) {
                        from = randomValue;
                    } else {
                        to = randomValue;
                        addToMatchingExcels(matchingExcels, from, to, round, matchingExcelMap, matchingList);
                    }

                }
            }

            dealRound(bestMatchingNum, String.valueOf(matchingNum), round);

            matchingList.sort(Comparator.naturalOrder()); //排序

            String matchingListString = matchingList.toString();

            if (matchingResultList.contains(matchingListString)) {
                System.out.println("重复结果了");
                round --; //这轮不算
                continue;
            } else  {
                matchingResultList.add(matchingListString);
                matchingResultList.contains(matchingListString);
            }

            for (Student p :  students) {
                p.setMatched(false);
            }
        }
        matchingExcels.sort(Comparator.comparing(MatchingExcel::getSt));
        matchingExcels.add(bestMatchingNum);
        return matchingExcels;
    }

    public String getRandomFromMap(Map map) {
        Random generator = new Random();
        Object[] values = map.keySet().toArray();
        if (values == null || values.length ==0) {
            return null;
        }
        String id = (String) values[generator.nextInt(values.length)];
        map.remove(id);
        return id;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filePath =  ResourceUtils.getURL("classpath:").getPath().toString() +"交友匹配模板.xlsx";
        List<StudentExcel> studentExcels = EasyExcel.read(filePath).head(StudentExcel.class).sheet().doReadSync();
        if (studentExcels == null && studentExcels.size() == 0) {
            return;
        }

        List<Student> students = new ArrayList<>(studentExcels.size());
        for (int i = 0; i < studentExcels.size(); i++) {
            Student student = new Student();
            StudentExcel studentExcel = studentExcels.get(i);
            student.setId(studentExcel.getSt());
            List<String> likes = new ArrayList<>(10);
            likes.add(studentExcel.getLike1());
            likes.add(studentExcel.getLike2());
            likes.add(studentExcel.getLike3());
            likes.add(studentExcel.getLike4());
            likes.add(studentExcel.getLike5());
            likes.add(studentExcel.getLike6());
            likes.add(studentExcel.getLike7());
            likes.add(studentExcel.getLike9());
            likes.add(studentExcel.getLike8());
            likes.add(studentExcel.getLike10());
            student.setLikeIds(likes);
            students.add(student);
        }

        StudentUtils studentUtils = new StudentUtils();
        List<MatchingExcel> matchingResults = studentUtils.getMatchingResults(students, 10);

        String destPath =  ResourceUtils.getURL("classpath:").getPath().toString() + "交友匹配.xlsx";

        // 生成工作簿对象
        ExcelWriterBuilder workBookWriter = EasyExcel.write(new File(destPath)).withTemplate(filePath);
        // 创建工作表对象
        ExcelWriterSheetBuilder sheet = workBookWriter.sheet("输出");
        sheet.doFill(matchingResults);
    }

}
