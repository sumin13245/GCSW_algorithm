package timeTable;

import java.io.*;
import java.util.Scanner;

public class mainClass {

    public static void main(String[] args) throws IOException{
    	
        InputStreamReader file_input = new InputStreamReader(new FileInputStream("input.txt"),"UTF-8");
        BufferedReader fileReader = new BufferedReader(file_input);
        
        String data ;//lessonName, professorName, date, building,building_num, lesson_num;
        int[] userWant = new int[3];
        
        lesson[] lessonData = new lesson[200]; // 데이터 배열로 담았어요 (매번 io 하긴 느릴 것 같아서)
        int i = 0;

        while ((data = fileReader.readLine())!=null) {
            String[]dataSplit = data.split(" ");
            dataSplit[0] = dataSplit[0].replace("!", " ");
            dataSplit[1] = dataSplit[1].replace("!"," ");
            lessonData[i++] = new lesson(dataSplit[0],dataSplit[1], dataSplit[2], dataSplit[3],Integer.parseInt(dataSplit[4]),Integer.parseInt(dataSplit[5]));
        }
        fileReader.close();
        
        Scanner input = new Scanner(System.in);
        userWant[0] = input.nextInt();
        userWant[1] = input.nextInt();
        userWant[2] = input.nextInt();
        
        input.close();
    }
}