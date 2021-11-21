package com.example.gachonalg;

import java.io.*;
import java.util.Scanner;

public class mainClass {

    public static void main(String[] args) throws IOException {

        InputStreamReader file_input = new InputStreamReader(new FileInputStream("input.txt"), "UTF-8");
        BufferedReader fileReader = new BufferedReader(file_input);

        String data;//lessonName, professorName, date, building,building_num, lesson_num;
        int[] userWant = new int[3];
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
            userWant[j] = input.nextInt();
            //여기에 그 파일에서 학수번호로 lesson 찾아서 inputTimeTable에 넣는 코드 구현하시면 돼요. addLesson 하면 True,False 나오니까 true 나올 때까지 받게 하면 중복피할 수 있음
            j++;
        }

        input.close();
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
