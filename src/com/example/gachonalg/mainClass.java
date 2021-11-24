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
            lessonData[i++] = new lesson(dataSplit[0], dataSplit[1], dataSplit[2], dataSplit[3], Integer.parseInt(dataSplit[4]), Integer.parseInt(dataSplit[5]));
        }
        fileReader.close();

        Scanner input = new Scanner(System.in);

        int j = 0;
        while ( j < 3) {
            int key;
            userWant = input.nextInt();
            key = queryAsNum(userWant,lessonData,i);
            if(key!= -1)
                if(inputTimeTable.addLesson(lessonData[key]));
                    j++;
                    //여기에 그 파일에서 학수번호로 lesson 찾아서 inputTimes 나오니까 true 나올 때까지 받게 하면 중복피할 수 있음(완료)
        }
        inputTimeTable = closeDistance(inputTimeTable,i,lessonData);
        inputTimeTable.print();
        input.close();

    } // main 함수의 끝 부분 괄호

    private static timeTable closeDistance(timeTable inputTT,int max,lesson[] lessonsData){
                // 그냥 바로 다음 수업만 추가하겠습니다
        distance Distance = new distance();// 검색을 위한 context,queryMinDistance 에서 만들면 낭비일거같아 여기서 만듭니다
        lesson[] tableData = inputTT.getTable();
        char[] date;
        int index;
        // 추가예정 사항 및 문제점들
        //while 여러번 시키는 코드 추가예정
        // 지금은 it대학의 경우 가장 짧은 길이 수업2개인 it 대학밖에 없으니 추가 실패할 가능성이 커진다 그래서 차선책들도 insert 해야 함
        //수업 두개있는 강의인 경우 뒷 수업은 고려하지 않는다


        for(int i = 0;i<25;i++) {  // 안에 들어있는 데이터를 검사

            if(tableData[i]!=null){//찾으면
                char[]reqDate = new char[2];
                index = tableData[i].dateToIndex();
                date= tableData[i].getDate().toCharArray();
                reqDate[0] = date[0];

                switch (index%5){
                    case 0: // 다음 수업 하나를 찿고 그중에서 min 값을 찾으면 데이터를 삽입한다.
                        reqDate[1] = (char)((int)date[1]+1);
                        index = queryMinDistance(Distance,tableData[i],lessonsData,reqDate,max);
                        if(index != -1) inputTT.addLesson(lessonsData[index]);
                        break;

                    case 4://앞으로만 이어붙이기
                        reqDate[1] = (char)((int)date[1]-1);
                        index = queryMinDistance(Distance,tableData[i],lessonsData,reqDate,max);
                        if(index != -1) inputTT.addLesson(lessonsData[index]);
                        break;


                    default:// 앞 뒤로 이어붙이기 for 1 2 3
                        reqDate[1] = (char)((int)date[1]-1);
                        index = queryMinDistance(Distance,tableData[i],lessonsData,reqDate,max);
                        if(index != -1) inputTT.addLesson(lessonsData[index]);

                        reqDate[1] = (char)((int)date[1]+1);
                        index = queryMinDistance(Distance,tableData[i],lessonsData,reqDate,max);
                        if(index != -1) inputTT.addLesson(lessonsData[index]);
                        break;
                }

            }
        }

        return inputTT;
    }
    //method daysOff with input timetable and max number of lessons and lessonData array
    private timeTable daysOff(timeTable inputTT, int max, lesson[] lessonsData) {
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

    private void addLessonSameDay(int day, int max, timeTable inputTT, lesson[] lessonsData) {
        int j = 0;
        while (j <= 200 && inputTT.getNumOfLesson() < max - 1) {
            switch (day) {
                case 0:
                    if (lessonsData[j].dateToIndex() <= 0 && lessonsData[j].dateToIndex() < 5) {
                        inputTT.addLesson(lessonsData[j]);
                    } else j++;
                case 1:
                    if (lessonsData[j].dateToIndex() <= 5 && lessonsData[j].dateToIndex() < 10) {
                        inputTT.addLesson(lessonsData[j]);
                    } else j++;
                case 2:
                    if (lessonsData[j].dateToIndex() <= 10 && lessonsData[j].dateToIndex() < 15) {
                        inputTT.addLesson(lessonsData[j]);
                    } else j++;
                case 3:
                    if (lessonsData[j].dateToIndex() <= 15 && lessonsData[j].dateToIndex() < 20) {
                        inputTT.addLesson(lessonsData[j]);
                    } else j++;
                case 4:
                    if (lessonsData[j].dateToIndex() <= 20 && lessonsData[j].dateToIndex() < 25) {
                        inputTT.addLesson(lessonsData[j]);
                    } else j++;
                default:
                    System.out.println("Wrong index");
                    break;
            }
        }
    }


    private static int queryMinDistance(distance Distance,lesson userLs,lesson[] lessonData,char[] reqDate ,int dataSize){
        int minIndex = -1;
        double minDis = 10000;
        double disInfo;
        String sReqDate = new String(reqDate);

        for(int k = 0;k<dataSize;k++) {
            // getDistance(String src, String des,double srcFloor, double desFloor)
            if (lessonData[k].getDate().contains(sReqDate)) {
                if(lessonData[k].dateToIndex()!=userLs.getLesson_num()) {
                    disInfo = Distance.getDistance(userLs.getBuilding(), lessonData[k].getBuilding(), userLs.getBuilding_num(), lessonData[k].getBuilding_num());
                    if (minDis > disInfo) {
                        minIndex = k;
                        minDis = disInfo;
                    }
                }
            }

        }
        if(minDis == 10000)
            System.out.println("Cannot find lesson");


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
