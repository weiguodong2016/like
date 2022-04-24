package test;

import com.alibaba.excel.EasyExcel;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.*;

public class PersonUtils {

    public void dealData(List<PiPeiExcel> piPeiExcels, String from, String to, int turnNum,Map<String, PiPeiExcel> piPeiMap) {
        PiPeiExcel piPeiExcel = new PiPeiExcel();
        if (turnNum == 0 && !piPeiMap.containsKey(from)) { //新增数据
            piPeiExcel.setSt(from);
            setTo(piPeiExcel, to, turnNum);
            piPeiExcels.add(piPeiExcel);
            piPeiMap.put(from, piPeiExcel);
        } else { //改数据
            piPeiExcel = piPeiMap.get(from);
            setTo(piPeiExcel, to, turnNum);
        }
    }

    public void setPiPeiExcels(List<PiPeiExcel> piPeiExcels, String from, String to, int turnNum, Map<String, PiPeiExcel> piPeiMap) {
        dealData(piPeiExcels, from, to, turnNum, piPeiMap);
        dealData(piPeiExcels, to, from, turnNum, piPeiMap);
    }

    private void setTo(PiPeiExcel piPeiExcel, String to, int turnNum) {
        switch (turnNum) {
            case 0:
                piPeiExcel.setRound1(to);
                break;
            case 1:
                piPeiExcel.setRound2(to);
                break;
            case 2:
                piPeiExcel.setRound3(to);
                break;
            case 3:
                piPeiExcel.setRound4(to);
                break;
            case 4:
                piPeiExcel.setRound5(to);
                break;
            case 5:
                piPeiExcel.setRound6(to);
                break;
            case 6:
                piPeiExcel.setRound7(to);
                break;
            case 7:
                piPeiExcel.setRound8(to);
                break;
            case 8:
                piPeiExcel.setRound9(to);
                break;
            case 9:
                piPeiExcel.setRound10(to);
                break;
            default:
                System.out.println("非法输入");
        }
    }


    public List<PiPeiExcel> getString(List<Person> personList) {
        if (personList == null || personList.size() ==0) {
            return null;
        }
        int turnNum = 0; //第几轮
        Map<String, PiPeiExcel> piPeiMap = new HashMap<>(personList.size());
        PiPeiExcel jiLu = new PiPeiExcel(); //记录每轮匹配次数
        jiLu.setSt("每轮匹配队数");
        int num = personList.size();
        List<PiPeiExcel> piPeiExcels = new ArrayList<>(personList.size() + 1);
        for (; turnNum < 10; turnNum++) {
            int piPeiNum = 0;
            Map<String, Person> personMap = new HashMap<>(num);
            //根据id放进map
            for (Person p :  personList) {
                personMap.put(p.getId(), p);
            }

            Person cPerson = null;
            Person likePerson = null;
            for (int i = 0; i < num; i++) {
                cPerson = personList.get(i);
                if (cPerson.isMatched()) {
                    continue;
                }
                List likes = cPerson.getLikeIds();
                if (likes != null && likes.size() > 0) {
                    for (int j = 0; j < likes.size(); j++) {
                        likePerson = personMap.get(likes.get(j));
                        if (likePerson != null) {
                            if (new Random().nextInt(8) > 5 && !likePerson.isMatched() && likePerson.getLikeIds().contains(cPerson.getId())) { //二个都喜欢
                                setPiPeiExcels(piPeiExcels, cPerson.getId(), likePerson.getId(), turnNum, piPeiMap);
                                personMap.remove(cPerson.getId());
                                personMap.remove(likePerson.getId());
                                cPerson.setMatched(true);
                                likePerson.setMatched(true);
                                piPeiNum++;
                                break;
                            }
                        }
                    }
                }

            }

            //剩下对personMap 打乱匹配
            int mapNum = personMap.size();
            if (mapNum != 0) {
                String from = null;
                String to = null;
                int size = personMap.size();
                for (int l = 0; l < size; l++) {
                    String randomValue = getRandomValue(personMap);
                    if (randomValue == null) {
                        break;
                    }
                    if (l % 2 == 0) {
                        from = randomValue;
                    } else {
                        to = randomValue;
                        setPiPeiExcels(piPeiExcels, from, to, turnNum, piPeiMap);
                    }

                }
            }

            setTo(jiLu, String.valueOf(piPeiNum), turnNum);
            for (Person p :  personList) {
                p.setMatched(false);
            }
        }
        piPeiExcels.sort(Comparator.comparing(PiPeiExcel::getSt));
        piPeiExcels.add(jiLu);
        return piPeiExcels;
    }

    public String getRandomValue(Map map) {
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
        String filePath =  ResourceUtils.getURL("classpath:").getPath().toString() +"交友匹配.xlsx";
        List<PersonExcel> list = EasyExcel.read(filePath).head(PersonExcel.class).sheet().doReadSync();
        if (list == null && list.size() == 0) {
            return;
        }

        List<Person> personList = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Person person = new Person();
            PersonExcel personExcel = list.get(i);
            person.setId(personExcel.getSt());
            List<String> likes = new ArrayList<>(10);
            likes.add(personExcel.getLike1());
            likes.add(personExcel.getLike2());
            likes.add(personExcel.getLike3());
            likes.add(personExcel.getLike4());
            likes.add(personExcel.getLike5());
            likes.add(personExcel.getLike6());
            likes.add(personExcel.getLike7());
            likes.add(personExcel.getLike9());
            likes.add(personExcel.getLike8());
            likes.add(personExcel.getLike10());
            person.setLikeIds(likes);
            personList.add(person);
        }

        PersonUtils personUtils = new PersonUtils();
        List<PiPeiExcel> piPeiExcels = personUtils.getString(personList);
        System.out.println(piPeiExcels);
        filePath =  ResourceUtils.getURL("classpath:").getPath().toString() +"交友匹配1.xlsx";
        EasyExcel.write(filePath, PiPeiExcel.class).sheet(2, "输出").doWrite(piPeiExcels);
    }

}
