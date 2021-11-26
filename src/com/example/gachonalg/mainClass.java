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
        timeTable cdInputTimeTable = new timeTable();
        timeTable doInputTimeTable = new timeTable();

        lesson[] lessonData = new lesson[200]; //Array to hold data from input.txt
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
                if(cdInputTimeTable.addLesson(lessonData[key]) && doInputTimeTable.addLesson(lessonData[key]))
                    j++;

        }
        int reqCredit = 0;
        while (reqCredit<12||reqCredit>21) { //Maximum and Minimum Credits
            System.out.println("원하는 학점을 입력하세요");
            reqCredit = input.nextInt();
        }

        System.out.println("closeDistance 시간표\n");
        timeTable cdOffTimeTable = closeDistance(cdInputTimeTable,i,lessonData,reqCredit);
        cdOffTimeTable.print();

        System.out.println("daysOff 시간표\n");
        timeTable doTimeTable = daysOff(doInputTimeTable,lessonData,reqCredit);
        doTimeTable.print();

        input.close();

    }

    private static timeTable closeDistance(timeTable inputTT,int max,lesson[] lessonsData,int reqCredit){ //  max value is lessonsData value
        distance Distance = new distance();// context for search
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
            for (int i = 0; i < 25; i++) {  // check the data inside Table
                if (inputTT.getTotalCredit() < reqCredit) {//If more than the required grades
                    if (tableData[i] != null) {
                        char[] reqDate = new char[2];
                        date = tableData[i].getDate().toCharArray();
                        for (int cp = 0; cp < date.length; cp += 2) {
                            date[0] = date[cp];
                            reqDate[0] = date[0];
                            index = tableData[i].dateToIndex(date);
                            switch (index % 5) {
                                case 0: // putMdLesson: Find the closest class and add it to the table
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, 1);//If it is a lesson in the first lesson, only the time after that is considered
                                    break;
                                case 4:
                                    putMdLesson(distance, inputTT, tableData, lessonsData, date, index, i, reqDate,reqCredit, max, -1);//In the case of lesson 5 in lesson 5, only the previous time is considered.
                                    break;
                                default:// Consider both before and after times for 2 3 4
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
        if(tableData[index+mode]==null) { // If there is a place to put a lesson at any time you want
            reqDate[1] = (char) ((int) date[1] +mode);//Calculate reqDate[1]
            tableIndex = queryMinDistance(distance, inputTT, tableData[i], lessonsData, reqDate,reqCredit, max); //find the nearest
            if (tableIndex != -1) inputTT.addLesson(lessonsData[tableIndex]);//Add if valid
        }
    }

    //method daysOff with input timetable and max number of lessons and lessonData array
    private static timeTable daysOff(timeTable inputTT, lesson[] lessonsData, int reqCredit) {
        // add maximum lesson when same days.
        for (int i = 0; i < 5; i++) {
            if (inputTT.getDays()[i]) {
                addLessonSameDay(i, reqCredit, inputTT, lessonsData);
            }
        }
        // if not enough num of lesson, add lessons when another days.
        if ( inputTT.getTotalCredit() < reqCredit) {
            for (int i = 0; i < 5; i++) {
                if (!inputTT.getDays()[i]) {
                    addLessonSameDay(i, reqCredit, inputTT, lessonsData);
                }
            }
        }
        return inputTT; //return result table
    }

    private static void addLessonSameDay(int day, int reqCredit, timeTable inputTT, lesson[] lessonsData) {
        int j = 0;

        while (j < 157 && inputTT.getTotalCredit() < reqCredit) {
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
            if (lessonData[k].getDate().contains(sReqDate)&&inputTT.getTotalCredit()+lessonData[k].getCredit()<=reqCredit) { //If the class is held during reqDate, Also, if adding this lesson does not exceed reqCredit
                disInfo = Distance.getDistance(userLs.getBuilding(), lessonData[k].getBuilding(), userLs.getBuilding_num(), lessonData[k].getBuilding_num());//check the distance
                    if (minDis > disInfo && inputTT.isCanPutIn(lessonData[k])) {//If the distance is less compared to the minimum distance, change the minimum distance.
                        minIndex = k;
                        minDis = disInfo;
                    }
            }
        }
        if(minDis == 10000)
            System.out.println("Cannot find lesson for: "+sReqDate);

        return  minIndex;
    }

    private static int queryAsNum(int userWant,lesson[] lessonData,int dataSize){ //Find lesson index by class number
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
