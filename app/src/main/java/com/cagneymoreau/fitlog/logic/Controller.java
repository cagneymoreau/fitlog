package com.cagneymoreau.fitlog.logic;


import android.app.Activity;
import android.util.Pair;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.cagneymoreau.fitlog.data.AppDataBase;
import com.cagneymoreau.fitlog.data.CheckLists;
import com.cagneymoreau.fitlog.data.CheckListsDao;
import com.cagneymoreau.fitlog.data.Splits;
import com.cagneymoreau.fitlog.data.SplitsDao;
import com.cagneymoreau.fitlog.data.UserProfile;
import com.cagneymoreau.fitlog.data.UserProfileDao;
import com.cagneymoreau.fitlog.data.WorkoutRecord;
import com.cagneymoreau.fitlog.data.WorkoutRecordDao;
import com.cagneymoreau.fitlog.views.MyFragment;
import com.cagneymoreau.fitlog.views.Workout_Holder_Fragment;
import com.cagneymoreau.fitlog.views.history_viewer.History_Chooser;
import com.cagneymoreau.fitlog.views.history_viewer.recycleview.HistoryItem;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import kotlin.Triple;

/**
 *
 * Fetch and organize data from storage
 *
 * None of these methods are available on UI thread
 *
 *     // TODO: 11/2/2021 are we working with database or lists held in controller class for all these
 *     // TODO: 11/2/2021 are we managing lists with title which could have duplicates? or with objects and uid
 *
 *
 *      // TODO: 5/27/2021 if sort is called while editing it would start editing wrong area
 *
 *
 *

 *
 */

public class Controller {
    
    
   AppDataBase dataBase;

  UserProfileDao userProfileDao;
  CheckListsDao checkListsDao;
  SplitsDao splitsDao;
  WorkoutRecordDao workoutRecordDao;

  Subscription subscription;


  private final int maxCheckLists = 25;

  //how many months of records to save
  private final int maxRecordPeriods = 12;

    boolean fullyLoaded;

    public Controller(Activity activity)
    {
            dataBase = Room.databaseBuilder( activity.getApplicationContext(),AppDataBase.class, "fitlogDatabase").build();

            userProfileDao = dataBase.userProfileDao();
            checkListsDao = dataBase.checkListsDao();
            splitsDao = dataBase.splitsDao();
            workoutRecordDao = dataBase.workoutRecordDao();

            getAppDisp();
            gatherCheckLists();
            gatherSplits();

            subscription = new Subscription(activity);

    }






    //region----------------------------------------  user /app settings
    
    //app internal
    private boolean firstLaunch = true;
    
    //user info display
    private String username = "";
    private String email = "";
    
    //features
    private boolean checklist = false;
    private boolean statistics = false;
    private boolean bodyMeasure = false;

    List<UserProfile> user;

    Set<String> movements;

    //called from non ui thread
    private void getAppDisp()
    {
        user = userProfileDao.getAll();
        if (user.size() == 0){
            user.add(new UserProfile());
            user.get(0).uid = 0;
            user.get(0). backups = new ArrayList<>();
            user.get(0).timers = new ArrayList<>();
            user.get(0).movements = new ArrayList<>();
            movements = new TreeSet<>();
            userProfileDao.insert(user.get(0));

        }
        else {
            firstLaunch = false;
            username = user.get(0).userName;
            email = user.get(0).email;
            checklist = user.get(0).checkList;
            statistics = user.get(0).statistics;
            bodyMeasure = user.get(0).bodyMeasure;
            movements = new TreeSet<>(user.get(0).movements);

            if (email == null){
                email = "";
            }
            FirebaseCrashlytics.getInstance().setCustomKey("email", email);
        }

    }


    public void saveUserToDB()
    {
        user.get(0).userName = username;
        user.get(0).email = email;
        user.get(0).firstLaunch = firstLaunch;
        user.get(0).bodyMeasure = bodyMeasure;
        user.get(0).checkList = checklist;
        user.get(0).statistics = statistics;
        user.get(0).movements =  new ArrayList<>(movements);


        new Thread(() -> {

            userProfileDao.update(user.get(0));

        }).start();
    }





    //getters
    public Set<String> getMovements()
    {

        if (movements == null){
            movements = new TreeSet<>();
        }

        return movements;
    }

    public void addMovement(String m)
    {
        movements.add(m);
    }

    public boolean getcheckListBool()
    {
        return checklist;
    }

    public boolean getStatisticsBool()
    {
        return statistics;
    }

    public boolean bodyMeasureBool()
    {
        return bodyMeasure;
    }

    public boolean isFirstLaunch()
    {
        return firstLaunch;
    }

    public String getUserName()
    {
        return username;
    }

    public String getEmail()
    {
        return email;
    }

    //setters
    public void setUsername(String s)
    {
        username = s;
    }

    public void setEmail(String s)
    {
        email = s;
    }

    public void setChecklist(boolean b)
    {
        checklist = b;
    }

    public void setBodyMeasure(boolean b)
    {
        bodyMeasure = b;
    }

    public void setStatistics(boolean b)
    {
        statistics = b;
    }


    //region-----------------trophy

    //when some new task is accomplished add a trophy
    //check if exists first
    public void addTrophy(int trophy)
    {
        if (user.get(0).trophies.contains(trophy)) return;

        user.get(0).trophies.add(trophy);

        new Thread(() -> {

            userProfileDao.update(user.get(0));

        }).start();
    }

    //Get a list of all the trophies by their number
    public ArrayList<Integer> getTrophies()
    {
        return new ArrayList<>(user.get(0).trophies);
    }


    //endregion

    //region---------------------------------------------------------------------------------------------timers
    /**
     * timers consist of a delay and count to construct interval alert for workout timing and tempo
     * Pair<delay, count>
     * All data stored in heap as user. relay on user table being saved elsewhere
     */

    public void addTimer(int delay, int count)
    {
        user.get(0).timers.add(new Pair<>(delay, count));
    }

    public void deleteTimer(int position)
    {
        user.get(0).timers.remove(position);

    }

    public ArrayList<Pair<Integer, Integer>> getTimers()
    {
        if (user.get(0).timers == null){
            user.get(0).timers = new ArrayList<>();
        }

        return new ArrayList<>(user.get(0).timers);
    }

    public void setAllTimers(ArrayList<Pair<Integer, Integer>> tList)
    {
        user.get(0).timers = new ArrayList<>(tList);
    }

    //endregion

    //region----------------------------------backups

    /**
     * Backups represent periods of time and a boolean flag signifying whether the data within has been backed up
     * time objects cant be used due to sql restriction?
     * this method needs to make sure there is a "backup" associated with that entry
     * if not create that "backup"
     * flag an existing true as false if being edited // TODO: 10/25/2021
     *
     * @param millis
     */

    public void placeWorkoutIntoBackups(Long millis)
    {
        //user.get(0).backups.clear(); //debuggin only

        // TODO: 11/3/2021 make this go back several months in case user want to back enter some data
        //first time user is using app, backdate it 60 seconds and create a new records
        if (user.get(0).backups.size() == 0) {

            long backlog = millis - (1000L*60L*60L*24L*90L); //last num is 60 days
            user.get(0).backups.add(generateNewMonth(backlog));

        }

        //we already have data. see if it fits into existing or create new month brackets until it does
        Triple<Long, Long, Boolean> mostRecent = user.get(0).backups.get(0);

        while (millis > mostRecent.getSecond()){

            user.get(0).backups.add(0, generateNewMonth(mostRecent.getSecond()));
            mostRecent = user.get(0).backups.get(0);

        }

        while (user.get(0).backups.size() > maxRecordPeriods){

            Triple<Long,Long,Boolean> trip = user.get(0).backups.get(user.get(0).backups.size()-1);
            user.get(0).backups.remove(user.get(0).backups.size()-1);

            ArrayList<WorkoutRecord> list = getMatchingWorkoutRecords(trip.getFirst(), trip.getSecond());

            new Thread(() -> {

                for (int i = 0; i < list.size(); i++) {

                    workoutRecordDao.delete(list.get(i).uid);

                }

            }).start();


        }






    }

    //create a new period from the incoming millis + 60001 minute to the end of the next
    //month (begin looking for next month 36 hours after start to avoid single day months
    //due to a time zone change
    private Triple<Long, Long, Boolean> generateNewMonth(Long prevMonthEnd)
    {

        LocalDate startSearch = Instant.ofEpochMilli(prevMonthEnd + (1000 * 60 *60*36)).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate lastDay = startSearch.withDayOfMonth(startSearch.lengthOfMonth());
        LocalDateTime end = LocalDateTime.of(lastDay.getYear(), lastDay.getMonth(), lastDay.getDayOfMonth(), 23, 59, 59);

        Long input = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Triple<Long, Long, Boolean> newMonth = new Triple<>(prevMonthEnd + 6001, input,false);



        return newMonth;
    }


    //returns list of backup periods and booleans for emailed already
    //triple<start, end, been emailed>
    public ArrayList<Triple<Long, Long, Boolean>> getBackups()
    {
        return new ArrayList<>(user.get(0).backups);
    }


    //endregion



    //endregion


    
    //region------------------------- checklist

    /**
     * Hold all the checklists in heap. The size is small. when we exit the chooser
     * we can save progress
     */

    List<CheckLists> checkListsList; //new and updated
    List<CheckLists> checkListsToDelete; //need to delete

    private int checkListToModify = -1;

    //pull from database and sort in order of last used date <- called from non ui thread
    private void gatherCheckLists()
    {
        checkListsList = checkListsDao.getAll();
        checkListsToDelete = new ArrayList<>();

       // Collections.sort(checkListsList, (o1, o2) -> Long.compare(o2.lastUsedMilli,  o1.lastUsedMilli));

    }


    // TODO: 11/4/2021 called when app is closed or checlist chooser exited
    public void updateCheckListInDB()
    {
        new Thread(() -> {

            for (int i = 0; i < checkListsList.size(); i++) {

                 int result =  checkListsDao.update(checkListsList.get(i));

                 if (result == 0){

                     checkListsDao.insert(checkListsList.get(i));

                 }

            }


            for (int i = 0; i < checkListsToDelete.size(); i++) {
                checkListsDao.delete(checkListsToDelete.get(i));
            }
            checkListsToDelete.clear();

        }).start();

    }



    /**
     *
     * @return titles with boolean
     */
    //return a list for the UI to show user in order of last used date with an active checklist highlighted
    //if checklists length = 25 dont give the option to add any more
    public Pair<List<String>, Boolean> getCheckListsChoice()
    {

        Collections.sort(checkListsList, (o1, o2) -> Long.compare(o2.lastUsedMilli,  o1.lastUsedMilli));


        List<String> titles = new ArrayList<>();
        Boolean active = false;

        if (checkListsList.size() > 0) {
            active = checkListsList.get(0).selected;

            for (int i = 0; i < checkListsList.size(); i++) {

                titles.add(checkListsList.get(i).checkListName);

            }
        }
        // TODO: 5/25/2021 this is a ui issue
            if (checkListsList.size() < maxCheckLists) {
                titles.add(" Create New CheckList");
            }

            return new Pair<>(titles, active);



    }



    public void setCheckListToModify(int i)
    {
        checkListToModify = i;
    }

    public int getCheckListToModify()
    {
        int out = checkListToModify;
        checkListToModify = -1;
        return out;
    }


    //switch which checklist is active and disable others
    public Pair<List<String>, Boolean> selectCheckList(int position)
    {
        if (position == 0){
            checkListsList.get(0).selected = ! checkListsList.get(0).selected;
            if (checkListsList.get(0).selected)checkListsList.get(0).lastUsedMilli = System.currentTimeMillis();
        }
        else {

            checkListsList.get(0).selected = false;
            checkListsList.get(position).selected = true;
            checkListsList.get(position).lastUsedMilli = System.currentTimeMillis();
        }
        return getCheckListsChoice();
    }


    //return checklist with title prepended
    public ArrayList<String> getSpecificCheckListEditable(int which)
    {
        //create new
        if (which == checkListsList.size()){
          return null;
        }

        ArrayList<String> theList = new ArrayList<>();
        for (int i = 0; i < checkListsList.get(which).checkList.size(); i++) {

            theList.add(checkListsList.get(which).checkList.get(i));
        }


        theList.add(0,checkListsList.get(which).checkListName);

        return theList;
    }



    public String getSpecificCheckListName(int position)
    {
            if (position == checkListsList.size()){
                return null;
            }

            return checkListsList.get(position).checkListName;
    }

    //Any time a checklist is worked on just update the heap list
    //This method needs the title as entry position 0
    //it will automatically select as active, record new last used time
    // TODO: 11/29/2021 when new checklist is created we use position in list to edit but the list order changes and we start editing wrong
    public void updateSpecificCheckListEdit(ArrayList<String> update, int position)
    {
        boolean newDBItem = false;

        if (position == checkListsList.size()){
            newDBItem = true;
            CheckLists checkLists = new CheckLists();
            if (checkListsList.size() > 0) {
                int max = 0;
                for (int i = 0; i < checkListsList.size(); i++) {
                    if (checkListsList.get(i).uid > max){
                        max = checkListsList.get(i).uid;
                    }
                }
                checkLists.uid = max + 1;
            }else {
                checkLists.uid = 1;
            }
            checkListsList.add(checkLists);
        }

        for (int i = 0; i < checkListsList.size(); i++) {
            checkListsList.get(position).selected = false;
        }

        String title = update.remove(0);
        checkListsList.get(position).checkListName = title;
        checkListsList.get(position).selected = true;
        checkListsList.get(position).lastUsedMilli = System.currentTimeMillis();
        checkListsList.get(position).checkList = update;

    }



    public void deleteChecklist( CheckLists checkLists)
    {
        checkListsToDelete.add(checkLists);

        checkListsList.remove(checkLists);
    }

    public void reverseDelete(CheckLists c)
    {
        checkListsToDelete.remove(c);
        checkListsList.add(c);
    }


    public CheckLists getspecificCheckList(int pos)
    {
        return checkListsList.get(pos);
    }



    //endregion



    //region------------------------- Splits

    List<Splits> splitsList;
    List<Splits> splitsToDelete;

    private int splitToModify = -1;

    //called from non ui thread
    private void gatherSplits()
    {
       splitsList = splitsDao.getAll();
       splitsToDelete = new ArrayList<>();

    }

    //called when exiting the split design menu
    public void updateSplitsInDB()
    {
        new Thread(() -> {

            for (int i = 0; i < splitsList.size(); i++) {

                int result =  splitsDao.update(splitsList.get(i));

                if (result == 0){

                    splitsDao.insert(splitsList.get(i));
                }
            }

            for (int i = 0; i < splitsToDelete.size(); i++) {
                splitsDao.delete(splitsToDelete.get(i));
            }
            splitsToDelete.clear();

        }).start();

    }



    public List<String> getSplitsList()
    {
        Collections.sort(splitsList, (o1, o2) -> Long.compare(o2.lastUsedMilli,  o1.lastUsedMilli));

        List<String> titles = new ArrayList<>();

        if (splitsList.size() > 0) {

            for (int i = 0; i < splitsList.size(); i++) {
                titles.add(splitsList.get(i).splitName);
            }
        }

        if (splitsList.size() < maxCheckLists) {
            titles.add(" Create New Split");
        }
        return titles;

    }

    public void setSplitToModify(int i){
        splitToModify = i;
    }

    public int getSplitToModify()
    {
        int out = splitToModify;
        splitToModify = -1;
        return out;
    }

    /**
     *
     * @param position the split you wish to activate
     * @return newly ordered list with activation changed
     */
    public List<String> selectSplit(int position)
    {
            splitsList.get(position).lastUsedMilli = System.currentTimeMillis();

            return getSplitsList();
    }

    /**
     * @return a list with each element containing a list of that days title followed by its movements
     */
    public ArrayList<ArrayList<String>> getSpecificSplitEditable(int position)
    {

        if (position == splitsList.size()){
            return null;
        }


        ArrayList<ArrayList<String>> thisSplit = new ArrayList<>();

        ArrayList<String> title = new ArrayList<>();
        title.add(splitsList.get(position).splitName);
        thisSplit.add(title);

        ArrayList<String> day = splitsList.get(position).dayOne;
        if (day == null) return thisSplit;
        ArrayList<String> day1 = new ArrayList<>(day);
        thisSplit.add(day1);

        day = splitsList.get(position).dayTwo;
        if (day == null) return thisSplit;
        ArrayList<String> day2 = new ArrayList<>(day);
        thisSplit.add(day2);

        day = splitsList.get(position).dayThree;
        if (day == null) return thisSplit;
        ArrayList<String> day3 = new ArrayList<>(day);
        thisSplit.add(day3);

        day = splitsList.get(position).dayFour;
        if (day == null) return thisSplit;
        ArrayList<String> day4 = new ArrayList<>(day);
        thisSplit.add(day4);

        day = splitsList.get(position).dayFive;
        if (day == null) return thisSplit;
        ArrayList<String> day5 = new ArrayList<>(day);
        thisSplit.add(day5);

        day = splitsList.get(position).daySix;
        if (day == null) return thisSplit;
        ArrayList<String> day6 = new ArrayList<>(day);
        thisSplit.add(day6);

        day = splitsList.get(position).daySeven;
        if (day == null) return thisSplit;
        ArrayList<String> day7 = new ArrayList<>(day);
        thisSplit.add(day7);

        day = splitsList.get(position).dayEight;
        if (day == null) return thisSplit;
        ArrayList<String> day8 = new ArrayList<>(day);
        thisSplit.add(day8);

        day = splitsList.get(position).dayNine;
        if (day == null) return thisSplit;
        ArrayList<String> day9 = new ArrayList<>(day);
        thisSplit.add(day9);

        day = splitsList.get(position).dayTen;
        if (day == null) return thisSplit;
        ArrayList<String> day10 = new ArrayList<>(day);
        thisSplit.add(day10);

        return thisSplit;

    }


    /**
     * Update the heap list
     * @param update 1) is name title and each after is the specific days starting with title and following with movements
     * @param position which split are we updating
     */
    public void updateSpecificSplitEdit(ArrayList<ArrayList<String>> update, int position)
    {
        boolean newDBItem = false;

        if (position == splitsList.size()){
            newDBItem = true;
            Splits splitsA = new Splits();
            if (splitsList.size() > 0) {
                int max = 0;
                for (int i = 0; i < splitsList.size(); i++) {
                    if (splitsList.get(i).uid > max){
                        max = splitsList.get(i).uid;
                    }
                }
                splitsA.uid = max + 1;
            }else {
                splitsA.uid = 1;
            }
            splitsList.add(splitsA);
        }


        String title = update.get(0).get(0);
        splitsList.get(position).splitName = title;

        splitsList.get(position).lastUsedMilli = System.currentTimeMillis();
        if (update.size() > 1) splitsList.get(position).dayOne = update.get(1);
        if (update.size() > 2) splitsList.get(position).dayTwo = update.get(2);
        if (update.size() > 3) splitsList.get(position).dayThree = update.get(3);
        if (update.size() > 4) splitsList.get(position).dayFour = update.get(4);
        if (update.size() > 5) splitsList.get(position).dayFive = update.get(5);
        if (update.size() > 6) splitsList.get(position).daySix = update.get(6);
        if (update.size() > 7) splitsList.get(position).daySeven = update.get(7);
        if (update.size() > 8) splitsList.get(position).dayEight = update.get(8);
        if (update.size() > 9) splitsList.get(position).dayNine = update.get(9);
        if (update.size() > 10) splitsList.get(position).dayTen = update.get(10);



    }


    public Splits getSpecificSplit(int pos)
    {
        return splitsList.get(pos);
    }

    public void deleteSplits(Splits s)
    {
         splitsList.remove(s);
         splitsToDelete.add(s);
    }

    public void restoreSplit(Splits s)
    {
        splitsList.add(s);
        splitsToDelete.remove(s);
    }

    //endregion


    /**
     * The workoutRecord Object is the only active version
     */
    //region-------------------- workout record


    int daySelection = -1;

    WorkoutRecord workoutRecord;

    ArrayList<Integer> toDelete = new ArrayList<>();


    public String getCurrentDayName()
    {
        return workoutRecord.dayName;
    }

    public String getCurrentSplitName()
    {
        return workoutRecord.splitName;
    }

    public int getCurrentUID()
    {
        return workoutRecord.uid;
    }

    public String getTimeDateDesc()
    {
        LocalDateTime date = Instant.ofEpochMilli(workoutRecord.datetime).atZone(ZoneId.systemDefault()).toLocalDateTime();

        return date.toString();
    }

    public void getRecentWorkoutDesc(MyFragment myFragment)
    {
        new Thread(() -> {

        List<WorkoutRecord> records = workoutRecordDao.getAll();

        ArrayList<String> desc = new ArrayList<>();

        String  gap = " ";
        StringBuilder sb;
        for (int i = 0; i < records.size(); i++) {

            sb = new StringBuilder();
            sb.append(records.get(i).dayName);
            sb.append(gap);
            sb.append(records.get(i).splitName);
            sb.append(gap);

            // how many entries did we make
            int entries = 0;
            for (int j = 0; j < records.get(i).workout.size(); j++) {
                entries += records.get(i).workout.get(j).size();
            }

            sb.append(entries);
            sb.append(gap);
            sb.append(records.get(i).notes);

            desc.add(sb.toString());
        }

        myFragment.getActivity().runOnUiThread(() -> {

            myFragment.sendSqlResults(desc);

        });


        }).start();
    }


    public void activateWorkout(int uid, Workout_Holder_Fragment fragment)
    {
        new Thread(() -> {

            //save existing
            if (workoutRecord != null) {

                int success = workoutRecordDao.update(workoutRecord);

                if (success == 0) {
                    workoutRecordDao.insert(workoutRecord);
                }
            }

            workoutRecord = workoutRecordDao.getRecordbyUID(uid);

            fragment.openHistory();

        }).start();



    }



    public WorkoutRecord openExistingWorkoutRecord(int uid)
    {
        return workoutRecordDao.getRecordbyUID(uid);
    }


    /**
     * @return a list of records in order of most recent with
     *          daynames and splits matching
     */
    public ArrayList<HistoryItem> getCuratedList()
    {
        ArrayList<WorkoutRecord> records = (ArrayList<WorkoutRecord>) workoutRecordDao.getCurated(workoutRecord.dayName, workoutRecord.splitName);

        ArrayList<HistoryItem> rList = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            HistoryItem h = new HistoryItem();
            h.uid = records.get(i).uid;
            h.dayName = records.get(i).dayName;
            h.splitName = records.get(i).splitName;
            h.date = LocalDate.ofEpochDay(records.get(i).uid);

            rList.add(h);
        }

        return rList;

    }

    /**
     * @return return workout from periods, -1 means last 30 days without period consideration
     */
    public void getRecentList(History_Chooser chooser, int period)
    {



        new Thread(() -> {

            long start = System.currentTimeMillis();
            long end = start - (1000L*60L*60L*24L*60L); //last num is 60 days

            if (period > -1){

                Triple<Long, Long, Boolean> v = user.get(0).backups.get(period);

                start = v.getSecond();
                end = v.getFirst();

            }


            ArrayList<WorkoutRecord> records = (ArrayList<WorkoutRecord>) workoutRecordDao.getSelection(start, end);

            Collections.sort(records, (o1, o2) -> Long.compare(o2.datetime,  o1.datetime));



            ArrayList<HistoryItem> rList = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            HistoryItem h = new HistoryItem();
            h.uid = records.get(i).uid;
            h.dayName = records.get(i).dayName;
            h.splitName = records.get(i).splitName;
            h.date = Instant.ofEpochMilli(records.get(i).datetime).atZone(ZoneId.systemDefault()).toLocalDate();

            rList.add(h);
        }

        chooser.setHistory(rList);

        }).start();
    }


    /**
     * Dont save to database here in case user backs up from a mistake
     * if time = -1 than its now
     */
    public void createNewWorkoutRecord(int dayPos, long time)
    {
        // TODO: 10/26/2021 save an existing record that is open? if not null

        workoutRecord = new WorkoutRecord();

        if (time == -1){

            workoutRecord.datetime = System.currentTimeMillis();

        }else{

            workoutRecord.datetime = time;

        }


        workoutRecord.splitName = splitsList.get(0).splitName;

        workoutRecord.dayName = getSpecificSplitEditable(0).get(dayPos).get(0);

        placeWorkoutIntoBackups(workoutRecord.datetime);

        ArrayList<ArrayList<String>> workouts = new ArrayList<>();

        for (int i = 0; i < getSpecificSplitEditable(0).get(dayPos).size(); i++) {
            ArrayList<String> n = new ArrayList<>();
            n.add(getSpecificSplitEditable(0).get(dayPos).get(i));
            workouts.add(n);
        }

        //checklist
        Pair<List<String>, Boolean> check = getCheckListsChoice();
        ArrayList<Pair<String, Boolean>>  savable = new ArrayList<>();

        if (check.second){

            ArrayList<String> thisList = getSpecificCheckListEditable(0);

            for (int i = 1; i < thisList.size(); i++) {

                savable.add(new Pair<>(thisList.get(i), true)); // TODO: 10/22/2021 auto uncheck, checked, repeat

            }

        }else{

            String s = "Checklist not enabled";
            savable.add(new Pair<>(s, true)); // TODO: 10/22/2021 auto uncheck, checked, repeat

        }

        workoutRecord.checkList = savable;

        workoutRecord.workout = workouts;


    }


    //checklist initial grab and update
    // TODO: 10/22/2021 prepend with title
    public ArrayList<Pair<String, Boolean>> getCheckList()
    {
        return workoutRecord.checkList;
    }

    public void setCheckList(ArrayList<Pair<String, Boolean>> input)
    {
        workoutRecord.checkList = input;
    }


    public String getNotes()
    {
        return workoutRecord.notes;
    }

    public void updateNotes(String note)
    {
        workoutRecord.notes = note;
    }

    public void updateCheckListInWorkout(ArrayList<Pair<String, Boolean>> checkList, String notes)
    {
        workoutRecord.checkList = checkList;
        workoutRecord.notes = notes;



    }

    //this is a convinience method used when creating new workouts
    //all other records are opened by milli time from a history log
    public ArrayList<ArrayList<String>> getCurrentWorkout()
    {
        return new ArrayList<>(workoutRecord.workout);
    }


    //called to save all data when workoutholder is exited
    public void saveWorkoutToDB()
    {
        new Thread(() -> {

            int success =  workoutRecordDao.update(workoutRecord);

            if (success == 0){
                workoutRecordDao.insert(workoutRecord);
                workoutRecord.uid = workoutRecordDao.getLastEntry();
            }


        }).start();

    }

    public void updatecurrentWorkout(ArrayList<ArrayList<String>> updates)
    {
        workoutRecord.workout = updates;
    }




    public void deleteWorkoutRecord( int uidVal)
    {
        new Thread(() -> {
        int success = workoutRecordDao.delete(uidVal);

        if (success != 1)
        {
            FirebaseCrashlytics.getInstance().log("failed delete workout");
            FirebaseCrashlytics.getInstance().recordException(new Exception());
        }

        }).start();

    }



    //endregion



    //region--------------------------backup/file building etc

    //return records between two dates
    public ArrayList<WorkoutRecord> getMatchingWorkoutRecords(long start, long end)
    {
        return (ArrayList<WorkoutRecord>) workoutRecordDao.getSelection(end, start);
    }



    //endregion


    public Subscription getSubscription()
    {
        return subscription;
    }

    public boolean showRating()
    {
        //return true;
        return  subscription.showRating();
    }

    public void ratingRecieved()
    {
        subscription.cancelRating();
    }

}
