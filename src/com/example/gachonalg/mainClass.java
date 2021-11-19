package com.example.gachonalg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class mainClass {

    public static void main(String[] args) throws IOException{
        FileInputStream file_input = new FileInputStream("C:/Users/ADMIN/Desktop/2021_2Study/algorithm/algo_java/src/input.txt");


        Scanner scanner = new Scanner(file_input);
        String data ;//lessonName, professorName, date, building,building_num, lesson_num;
        int[] userWant = new int[3];

        lesson[] lessonData = new lesson[200]; // 데이터 배열로 담았어요 (매번 io 하긴 느릴 것 같아서)
        int i = 0;

        while (scanner.hasNext()) {
            data = scanner.nextLine();
            String[]dataSplit = data.split(" ");
            dataSplit[0] =dataSplit[0].replace("!", " ");
            dataSplit[1] = dataSplit[1].replace("!"," ");
            lessonData[i++] = new lesson(dataSplit[0],dataSplit[1], dataSplit[2], dataSplit[3],Integer.parseInt(dataSplit[4]),Integer.parseInt(dataSplit[5]));
        }
        Scanner input = new Scanner(System.in);
        userWant[0] = input.nextInt();
        userWant[1] = input.nextInt();
        userWant[2] = input.nextInt();


        //    input ( File I/O , 학수 번호 3개 입력) 완료
//
//    시간표 객체화 3개
//
//            output


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