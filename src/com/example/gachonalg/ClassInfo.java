package com.example.gachonalg;

class lesson {
    String lessonName;
    String professorName;
    String date;
    String building;
    int building_num;
    int lesson_num;

    public  lesson(String lessonName, String professorName, String date, String building, int building_num, int lesson_num) {
        this.lessonName = lessonName;
        this.professorName = professorName;
        this.date = date;
        this.building = building;
        this.building_num = building_num;
        this.lesson_num = lesson_num;
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
}