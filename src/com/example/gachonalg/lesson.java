package com.example.gachonalg;

class lesson {
    private String lessonName;
    private String professorName;
    private String date;
    private String building;
    private int building_num;
    private int lesson_num;
    
    
    lesson(String lessonName, String professorName, String date, String building, int building_num, int lesson_num) {
        this.lessonName = lessonName;
        this.professorName = professorName;
        this.date = date;
        this.building = building;
        this.building_num = building_num;
        this.lesson_num = lesson_num;
    }

    public int dateToIndex(char[] temp) {//receive the value like 월1,화2... ,return the correct index
        int lessonIndex;
        //char[] temp = date.toCharArray();
        if (temp[0] == '월') lessonIndex = -1 + (temp[1] - '0');
            else if (temp[0] == '화') lessonIndex = 4 + (temp[1] - '0');
            else if (temp[0] == '수') lessonIndex = 9 + (temp[1] - '0');
            else if (temp[0] == '목') lessonIndex = 14 + (temp[1] - '0');
            else lessonIndex = 19 + (temp[1] - '0');

        return lessonIndex;
    }

    public String getLessonName() {
        return lessonName;
    }
    public String getProfessorName() {
        return professorName;
    }
    public String getDate() {
        return date;
    }
    public String getBuilding() {
        return building;
    }
    public int getBuilding_num() {
        return building_num;
    }
    public int getLesson_num() {
        return lesson_num;
    }
    public String toString() {
    	return  lessonName+" "+professorName+" "+date+" "+building+" "+building_num+" "+lesson_num;
    }
}
