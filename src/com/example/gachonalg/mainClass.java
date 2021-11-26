package com.example.gachonalg;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class mainClass {

    public static void main(String[] args) throws IOException {

        InputStreamReader file_input = new InputStreamReader(new FileInputStream("input.txt"), "UTF-8");
        BufferedReader fileReader = new BufferedReader(file_input);

        String data;//lessonName, professorName, date, building,building_num, lesson_num;
        int userWant;
        timeTable inputTimeTable = new timeTable();

        lesson[] lessonData = new lesson[200]; // 데이터 배열로 담았어요 (매번 io 하긴 느릴 것 같아서)
        int i = 0;

        while ((data = fileReader.readLine()) != null) {
            String[] dataSplit = data.split(" ");
            dataSplit[0] = dataSplit[0].replace("!", " ");
            dataSplit[1] = dataSplit[1].replace("!", " ");
            lessonData[i++] = new lesson(dataSplit[0], dataSplit[1], dataSplit[2], dataSplit[3], Integer.parseInt(dataSplit[4]), Integer.parseInt(dataSplit[5]),Integer.parseInt(dataSplit[6]));
        }
        fileReader.close();

        Scanner input = new Scanner(System.in);

        int j = 0;
        while ( j < 3) {
            int key;
            userWant = input.nextInt();
            key = queryAsNum(userWant,lessonData,i);
            if(key!= -1)
                if(inputTimeTable.addLesson(lessonData[key]))
                    j++;
                    //여기에 그 파일에서 학수번호로 lesson 찾아서 inputTimes 나오니까 true 나올 때까지 받게 하면 중복피할 수 있음(완료)
        }
        int reqCredit = 0;
        while (reqCredit<12||reqCredit>21) { // 최대, 최소학점 (없으면 이상하게 input 했을 때 무한루프)
            System.out.println("원하는 학점을 입력하세요");
            reqCredit = input.nextInt();
        }
        inputTimeTable = closeDistance(inputTimeTable,i,lessonData,reqCredit);
        inputTimeTable = daysOff(inputTimeTable,i,lessonData);
        inputTimeTable.print();
        input.close();

    } // main 함수의 끝 부분 괄호

    private static timeTable closeDistance(timeTable inputTT,int max,lesson[] lessonsData,int reqCredit){ // 여기서 max 값은 lessonsData 값입니다
                // 그냥 바로 다음 수업만 추가하겠습니다
        distance Distance = new distance();// 검색을 위한 context,queryMinDistance 에서 만들면 낭비일거같아 여기서 만듭니다
        lesson[] tableData = inputTT.getTable();
        calculateCD(inputTT, lessonsData, tableData, Distance, max,reqCredit);

        while (inputTT.getTotalCredit()<reqCredit) {
            System.out.println("학점이 부족합니다 어느 요일을 추가하시겠습니까? 숫자로 입력해주세요");
            boolean[] Days = inputTT.getDays();
            char[] week = new char[]{'월', '화', '수', '목', '금'};
            for (int i = 0; i < 5; i++) {
                if (!Days[i]) System.out.print(i + ":" + week[i] + " ");
            }
            Scanner input = new Scanner(System.in);
            int wantDay = input.nextInt();
            boolean hasAdded = false;
            for (int i = 0; i < max && !hasAdded; i++) {
                if (lessonsData[i].getDate().contains(String.valueOf(week[wantDay])) && inputTT.isCanPutIn(lessonsData[i])) {
                    inputTT.addLesson(lessonsData[i]);
                    hasAdded = true;
                }
            }
            calculateCD(inputTT, lessonsData, tableData, Distance, max,reqCredit);
        }

        return inputTT;
    }

    private static void calculateCD(timeTable inputTT, lesson[] lessonsData, lesson[] tableData, distance distance, int max ,int reqCredit) {
        char[] date;
        int index;
        for(int chack = 0; chack<5; chack++) {
            for (int i = 0; i < 25; i++) {  // 안에 들어있는 데이터를 검사
                if (inputTT.getTotalCredit() < reqCredit) {// 요구 학점이상이라면
                    if (tableData[i] != null) {//찾으면
                        char[] reqDate = new char[2];
                        date = tableData[i].getDate().toCharArray();
                        for (int cp = 0; cp < date.length; cp += 2) {
                            date[0] = date[cp];
                            reqDate[0] = date[0];
                            index = tableData[i].dateToIndex(date);
                            switch (index % 5) {
                                case 0: // 다음 수업 하나를 찿고 그중에서 min 값을 찾으면 데이터를 삽입한다.
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, 1);
                                    break;
                                case 4://앞으로만 이어붙이기
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, -1);
                                    break;
                                default:// 앞 뒤로 이어붙이기 for 1 2 3
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, -1);
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, 1);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void putMdLesson(distance distance,timeTable inputTT, lesson[] tableData, lesson[] lessonsData,char[] date, int index, int i, char[] reqDate,int reqCredit, int max,int mode) {
        int tableIndex;
        if(tableData[index+mode]==null) {
            reqDate[1] = (char) ((int) date[1] +mode);
            tableIndex = queryMinDistance(distance, inputTT, tableData[i], lessonsData, reqDate,reqCredit, max);
            if (tableIndex != -1) inputTT.addLesson(lessonsData[tableIndex]);
        }
    }

    //method daysOff with input timetable and max number of lessons and lessonData array
    private static timeTable daysOff(timeTable inputTT, int max, lesson[] lessonsData) {
        // add maximum lesson when same days.
        for (int i = 0; i < 5; i++) {
            if (inputTT.getDays()[i]) {
                addLessonSameDay(i, max, inputTT, lessonsData);
            }
        }
        // if not enough num of lesson, add lessons when another days.
        if ( inputTT.getNumOfLesson() != max) {
            for (int i = 0; i < 5; i++) {
                if (!inputTT.getDays()[i]) {
                    addLessonSameDay(i, max, inputTT, lessonsData);
                }
            }
        }
        return inputTT; //return result table
    }

    private static void addLessonSameDay(int day, int max, timeTable inputTT, lesson[] lessonsData) {
        int j = 0;
        while (j <= 157 && inputTT.getNumOfLesson() < max - 1) {
            if(lessonsData[j].dateToIndex(lessonsData[j].getDate().toCharArray()) <= (day*5) && lessonsData[j].dateToIndex(lessonsData[j].getDate().toCharArray()) < (day+1)*5)inputTT.addLesson(lessonsData[j]);
            j++;
        }
    }

    private static int queryMinDistance(distance Distance,timeTable inputTT,lesson userLs,lesson[] lessonData,char[] reqDate ,int reqCredit,int dataSize){
        int minIndex = -1;
        double minDis = 10000;
        double disInfo;
        String sReqDate = new String(reqDate);

        for(int k = 0;k<dataSize;k++) {
            if (lessonData[k].getDate().contains(sReqDate)&&inputTT.getTotalCredit()+lessonData[k].getCredit()<=reqCredit) {
                disInfo = Distance.getDistance(userLs.getBuilding(), lessonData[k].getBuilding(), userLs.getBuilding_num(), lessonData[k].getBuilding_num());
                    if (minDis > disInfo && inputTT.isCanPutIn(lessonData[k])) {
                        minIndex = k;
                        minDis = disInfo;
                    }
            }
        }
        if(minDis == 10000)
            System.out.println("Cannot find lesson for: "+sReqDate);

        return  minIndex;
    }

    private static int queryAsNum(int userWant,lesson[] lessonData,int dataSize){
        for(int k = 0;k<dataSize;k++) {
            if (lessonData[k].getLesson_num() == userWant)
                return k;
        }
        System.out.println("Cannot find lesson");
        return  -1;
    }

}
/*데이터 set
인터페이스
shortest Travel Distance
days off
recommand
ppt 제작
input - 학수번호 3개 입력,
data set - 수업들 ( name, 교수이름, 강의 시간 ( 1~ 6 교시), 건물 ( 건물 번호 1~n ), 강의실(101 ~ 820) ), 학수번호(00000000) - file I/O 메모장 사용
output - 시간표 ( 수업들의 2차원 배열[5][8] ) - print 화면에 출력
시간표 - 수업 구조체[5][6]
main class {
input ( File I/O , 학수 번호 3개 입력)
시간표 객체화 3개
output
}
class distance { weight[n][n] -> 거리 객체에 하드코딩
}
class 수업 { } - 그냥 구조체
class 시간표 {
수업 2차원 배열 - instance로 소유
shortest Travel Distance  (input에서 학수번호 3개, weight[][] 배열 arguments, 시간표( 수업 2차원 배열 ) return)
days off (input에서 학수번호 3개 arguments, 시간표( 수업 2차원 배열 ) return)
recommand (input에서 학수번호 3개 arguments, 시간표( 수업 2차원 배열 ) return)
}*/
