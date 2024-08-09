package com.my.voenmeh.Schedule;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Schedule {
    ArrayList<Day> EvenWeek;
    ArrayList<Day> OddWeek;

    public class Day {
        public ArrayList<String> time;
        public ArrayList<String> subject;
        public ArrayList<String> teacher;
        public ArrayList<String> place;
        public String dayName;

        private Day(String DayName) {
            dayName = DayName;
            time = new ArrayList<>();
            subject = new ArrayList<>();
            teacher = new ArrayList<>();
            place = new ArrayList<>();
        }

        public void Append(String _time, String _subject, String _teacher, String _place) {
            time.add(_time);
            subject.add(_subject);
            teacher.add(_teacher);
            place.add(_place);
        }

        public ArrayList<String> Get(String InfoType){
            switch(InfoType){
                case "time":
                    return time;
                case "teacher":
                    return teacher;
                case "subject":
                    return subject;
                case "place":
                    return place;
                default:
                    return null;
            }
        }

        @Override
        @NonNull
        public String toString() {
            String result = dayName + "\n";
            for (int i = 0, n = time.size(); i < n; i++) {
                result += time.get(i) + " " + subject.get(i) + " " + teacher.get(i) + " " + place.get(i) + "\n";
            }
            result += "\n";
            return result;
        }
    }

    public Schedule(){
        EvenWeek = new ArrayList<>();
        OddWeek = new ArrayList<>();
    }

    public void PullSchedule(String Group) {
        Document WebSchedule;
        try {
            WebSchedule = Jsoup.connect("http://www.voenmeh.com/schedule_green.php").get();
            Elements semesters = WebSchedule.getElementsByTag("option");
            String group_id = "", semester_id = semesters.get(0).val();
            String url = "http://www.voenmeh.com/schedule_green.php?semestr_id=" + semester_id + "&page_mode=group";
            WebSchedule = Jsoup.connect(url).get();
            Elements groups = WebSchedule.getElementsByTag("option");
            for (Element el : groups) {
                if (el.text().equals(Group)) {
                    group_id = el.val();
                }
            }
            url = "http://www.voenmeh.com/schedule_green.php?group_id=" + group_id + "&semestr_id=" + semester_id;
            WebSchedule = Jsoup.connect(url).get();
            Elements dayNames = WebSchedule.getElementsByClass("day");
            Elements dayInfo = WebSchedule.getElementsByClass("inner_table");
            Day DayForEven, DayForOdd;
            String[] oneLineData = new String[4];
            int k = 0;
            for (int i = 0, n = dayNames.size(); i < n; i++) {
                DayForEven = new Day(dayNames.get(i).text());
                DayForOdd = new Day(dayNames.get(i).text());
                Elements CurrentDayInfo = dayInfo.get(i).getElementsByTag("td");
                for (int j = 0, m = CurrentDayInfo.size(); j < m; j++) {
                    if (k == 4) {
                        switch (CurrentDayInfo.get(j).text()) {
                            case "чёт":
                                DayForEven.Append(oneLineData[0], oneLineData[1], oneLineData[2], oneLineData[3]);
                                break;
                            case "нечет":
                                DayForOdd.Append(oneLineData[0], oneLineData[1], oneLineData[2], oneLineData[3]);
                                break;
                            case "все":
                                DayForEven.Append(oneLineData[0], oneLineData[1], oneLineData[2], oneLineData[3]);
                                DayForOdd.Append(oneLineData[0], oneLineData[1], oneLineData[2], oneLineData[3]);
                                break;
                        }
                        k = 0;
                    } else {
                        oneLineData[k++] = CurrentDayInfo.get(j).text();
                    }
                }
                EvenWeek.add(DayForEven);
                OddWeek.add(DayForOdd);
            }
        }
        catch (IOException e) {
            Log.d("Exceptions", e.toString());
        }
    }

    public ArrayList<Day> GetWeek(boolean Even){
        if(Even){
            return EvenWeek;
        }
        else{
            return OddWeek;
        }
    }
}
