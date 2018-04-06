package com.example.dev.gymassistantv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dev on 07.02.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Logcat tag
     */
    private static final String log = "DatabaseHelper";

    /**
     * Database version
     */
    private static final int databaseVersion = 1;

    /**
     * Database name
     */
    private static final String databaseName = "gymAssistantDB";

    /**
     * Table names
     */
    private static final String tableCompletedWorkouts = "completedWorkouts";
    private static final String tablePlannedWorkouts = "plannedWorkouts";
    private static final String tableSegments = "segments";
    private static final String tableExercises = "exercises";
    private static final String tableMuscleGroups = "muscleGroups";
    private static final String tableSets = "sets";
    private static final String tableCompletedWorkoutSegment = "completedWorkoutSegment";
    private static final String tablePlannedWorkoutSegment = "plannedWorkoutSegment";
    private static final String tableExerciseMuscleGroups = "exerciseMuscleGroup";

    /**
     * Common column names and foreign keys
     */
    private static final String keyID = "id";
    private static final String createdAt = "createdAt";
    private static final String description = "description";
    private static final String segmentID = "segmentID";
    private static final String workoutID = "workoutID";
    private static final String exerciseID = "exerciseID";
    private static final String muscleGroupID = "muscleGroupID";

    /**
     * PlannedWorkouts table column names
     */
    private static final String workoutDate = "workoutDate";

    /**
     * Exercises table column names
     */
    private static final String exerciseName = "exerciseName";
    private static final String exerciseIsAvailable = "exerciseIsAvailable";

    /**
     * MuscleGroups table column names
     */
    private static final String muscleGroupName = "muscleGroupName";

    /**
     * Sets table column names
     */
    private static final String repCount = "repCount";
    private static final String weight = "weight";

    /**
     * Create CompletedWorkout table statements
     */
    private static final String createTableCompletedWorkouts = "CREATE TABLE "
            + tableCompletedWorkouts + "("
            + keyID + " INTEGER PRIMARY KEY,"
            + description + " VARCHAR(1000),"
            + createdAt + " DATETIME" + ")";

    /**
     * Create PlannedWorkout table statement
     */
    private static final String createTablePlannedWorkouts = "CREATE TABLE "
            + tablePlannedWorkouts + "("
            + keyID + " INTEGER PRIMARY KEY,"
            + description + " VARCHAR(1000),"
            + workoutDate + "DATETIME,"
            + createdAt + " DATETIME" + ")";

    /**
     * Create Segments table statement
     */
    private static final String createTableSegments = "CREATE TABLE "
            + tableSegments + "("
            + keyID + " INTEGER PRIMARY KEY,"
            + exerciseID + " INTEGER,"
            + workoutID + " INTEGER,"
            + createdAt + " DATETIME" + ")";

    /**
     * Create Exercises table statement
     */
    private static final String createTableExercises = "CREATE TABLE "
            + tableExercises + "("
            + keyID + " INTEGER PRIMARY KEY,"
            + muscleGroupID + " INTEGER,"
            + exerciseName + " VARCHAR(50),"
            + exerciseIsAvailable + " INTEGER,"
            + createdAt + " DATETIME" + ")";

    /**
     * Create MuscleGroups table statement
     */
    private static final String createTableMuscleGroups = "CREATE TABLE "
            + tableMuscleGroups + "("
            + keyID + " INTEGER PRIMARY KEY,"
            + muscleGroupName + " VARCHAR(50),"
            + createdAt + " DATETIME" + ")";

    /**
     * Create Sets table statement
     */
    private static final String createTableSets = "CREATE TABLE "
            + tableSets + "("
            + keyID + " INTEGER PRIMARY KEY,"
            + segmentID + " INTEGER,"
            + repCount + " INTEGER,"
            + weight + " INTEGER,"
            + createdAt + " DATETIME)";

    /**
     * Create exercise muscle groups table statement
     */
    private static final String createTableExerciseMuscleGroups = "CREATE TABLE "
            + tableExerciseMuscleGroups + "("
            + keyID + " INTEGER PRIMARY KEY,"
            + exerciseID + " INTEGER,"
            + muscleGroupID + " INTEGER,"
            + createdAt + " DATETIME)";

    /**********************************************************************************************/

    /**
     * Class constructor
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    /**
     * OnCreate method
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create required tables
        db.execSQL(createTableCompletedWorkouts);
        db.execSQL(createTablePlannedWorkouts);
        db.execSQL(createTableSegments);
        db.execSQL(createTableExercises);
        db.execSQL(createTableMuscleGroups);
        db.execSQL(createTableSets);
        db.execSQL(createTableExerciseMuscleGroups);
    }

    /**
     * OnUpgrade method
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //On upgrade drop old tables and create new ones
        db.execSQL("DROP TABLE IF EXISTS " + tableCompletedWorkouts);
        db.execSQL("DROP TABLE IF EXISTS " + tablePlannedWorkouts);
        db.execSQL("DROP TABLE IF EXISTS " + tableSegments);
        db.execSQL("DROP TABLE IF EXISTS " + tableExercises);
        db.execSQL("DROP TABLE IF EXISTS " + tableMuscleGroups);
        db.execSQL("DROP TABLE IF EXISTS " + tableSets);
        db.execSQL("DROP TABLE IF EXISTS " + tableCompletedWorkoutSegment);
        db.execSQL("DROP TABLE IF EXISTS " + tablePlannedWorkoutSegment);
        db.execSQL("DROP TABLE IF EXISTS " + tableExerciseMuscleGroups);

        onCreate(db);
    }

    /**
     * Create completed workout
     * @param workout
     * @return
     */
    public long createCompletedWorkout(CompletedWorkout workout) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(description, workout.getDescription());
        values.put(createdAt, getCurrentDate());

        long workoutID = db.insert(tableCompletedWorkouts, null, values);

        return workoutID;
    }

    /**
     * Create planned workout
     * @param workout
     * @return
     */
    public long createPlannedWorkout(PlannedWorkout workout) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(description, workout.getDescription());
        values.put(createdAt, getCurrentDate());

        long workoutID = db.insert(tablePlannedWorkouts, null, values);

        return workoutID;
    }

    /**
     * Create segment
     * @param segment
     * @return
     */
    public long createSegment(Segment segment) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(exerciseID, segment.getExerciseID());
        values.put(workoutID, segment.getWorkoutID());
        values.put(createdAt, getCurrentDate());

        long segmentID = db.insert(tableSegments, null, values);

        return segmentID;
    }

    /**
     * Create exerciseSet
     * @param exerciseSet
     * @return
     */
    public long createSet(ExerciseSet exerciseSet) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(segmentID, exerciseSet.getSegmentID());
        values.put(repCount, exerciseSet.getRepCount());
        values.put(weight, exerciseSet.getWeight());
        values.put(createdAt, getCurrentDate());

        long setID = db.insert(tableSets, null, values);

        return setID;
    }

    /**
     * Create exercise muscle group
     * @param exerciseID
     * @param muscleGroupID
     */
    public long createExerciseMuscleGroup(long exerciseID, long muscleGroupID) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(this.exerciseID, exerciseID);
        values.put(this.muscleGroupID, muscleGroupID);
        values.put(createdAt, getCurrentDate());

        long exerciseMuscleGroupID = db.insert(tableExerciseMuscleGroups, null, values);

        return exerciseMuscleGroupID;
    }

    /**
     * Get completed workout by ID
     * @param ID
     * @return
     */
    public CompletedWorkout getCompletedWorkout(long ID) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tableCompletedWorkouts
                + " WHERE " + keyID + " = " + ID;

        Log.e(log, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        CompletedWorkout workout = new CompletedWorkout();
        workout.setID(c.getInt(c.getColumnIndex(keyID)));
        workout.setDescription(c.getString(c.getColumnIndex(description)));
        workout.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));

        return workout;
    }

    /**
     * Get planned workout
     * @param ID
     * @return
     */
    public PlannedWorkout getPlannedWorkout(long ID) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tablePlannedWorkouts
                + " WHERE " + keyID + " = " + ID;

        Log.e(log, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        PlannedWorkout workout = new PlannedWorkout();
        workout.setID(c.getInt(c.getColumnIndex(keyID)));
        workout.setDescription(c.getString(c.getColumnIndex(description)));
        workout.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));

        return workout;
    }

    /**
     * Get segment
     * @param ID
     * @return
     */
    public Segment getSegment(long ID) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tableSegments
                + " WHERE " + keyID + " = " + ID;

        Log.e(log, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        Segment segment = new Segment();
        segment.setID(c.getInt(c.getColumnIndex(keyID)));
        segment.setExerciseID(c.getInt(c.getColumnIndex(exerciseID)));
        segment.setWorkoutID(c.getLong(c.getColumnIndex(workoutID)));
        segment.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));

        return segment;
    }

    /**
     * Get exercise by ID
     * @param ID
     * @return
     */
    public Exercise getExerciseByID(long ID) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tableExercises
                + " WHERE " + keyID + " = " + ID;

        Log.e(log, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        Exercise exercise = new Exercise();
        exercise.setID(c.getInt(c.getColumnIndex(keyID)));
        exercise.setName(c.getString(c.getColumnIndex(exerciseName)));
        exercise.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
        exercise.setIsAvailable(c.getInt(c.getColumnIndex(exerciseIsAvailable)) != 0);

        return exercise;
    }

    /**
     * Get exercise by name
     * @param name
     * @return
     */
    public Exercise getExerciseByName(String name) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tableExercises
                + " WHERE " + exerciseName + " = '" + name + "'";

        Log.e(log, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        Exercise exercise = new Exercise();
        exercise.setID(c.getInt(c.getColumnIndex(keyID)));
        exercise.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));

        return exercise;
    }

    /**
     * Get muscle group by ID
     * @param ID
     * @return
     */
    public MuscleGroup getMuscleGroupByID(long ID) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tableMuscleGroups
                + " WHERE " + keyID + " = " + ID;

        Log.e(log, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        MuscleGroup muscleGroup = new MuscleGroup();
        muscleGroup.setID(c.getInt(c.getColumnIndex(keyID)));
        muscleGroup.setName(c.getString(c.getColumnIndex(muscleGroupName)));
        muscleGroup.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));

        return muscleGroup;
    }

    /**
     * Get muscle group by it's name
     * @param name
     * @return
     */
    public MuscleGroup getMuscleGroupByName(String name) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tableMuscleGroups
                + " WHERE " + muscleGroupName + " = '" + name + "'";

        Log.e(log, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        MuscleGroup muscleGroup = new MuscleGroup();
        muscleGroup.setID(c.getInt(c.getColumnIndex(keyID)));
        muscleGroup.setName(c.getString(c.getColumnIndex(muscleGroupName)));
        muscleGroup.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));

        return muscleGroup;
    }

    /**
     * Get muscle group by name
     * @param name
     * @return
     */
    public MuscleGroup getMuscleGroupByID(String name) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tableMuscleGroups
                + " WHERE " + muscleGroupName + " = '" + name + "'";

        Log.e(log, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        MuscleGroup muscleGroup = new MuscleGroup();
        muscleGroup.setID(c.getInt(c.getColumnIndex(keyID)));
        muscleGroup.setName(c.getString(c.getColumnIndex(muscleGroupName)));
        muscleGroup.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));

        return muscleGroup;
    }

    /**
     * Get all completed workouts
     * @return
     */
    public List<CompletedWorkout> getAllCompletedWorkouts() {

        List<CompletedWorkout> workouts = new ArrayList<CompletedWorkout>();
        String selectQuery = "SELECT * FROM " + tableCompletedWorkouts;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                CompletedWorkout workout = new CompletedWorkout();
                workout.setID(c.getInt(c.getColumnIndex(keyID)));
                workout.setDescription(c.getString(c.getColumnIndex(description)));
                workout.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
                workouts.add(workout);
            } while(c.moveToNext());
        }

        return workouts;
    }

    /**
     * Get all planned workouts
     * @return
     */
    public List<PlannedWorkout> getAllPlannedWorkouts() {

        List<PlannedWorkout> workouts = new ArrayList<PlannedWorkout>();
        String selectQuery = "SELECT * FROM " + tablePlannedWorkouts;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                PlannedWorkout workout = new PlannedWorkout();
                workout.setID(c.getInt(c.getColumnIndex(keyID)));
                workout.setDescription(c.getString(c.getColumnIndex(description)));
                workout.setWorkoutDate(c.getString(c.getColumnIndex(workoutDate)));
                workout.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
                workouts.add(workout);
            } while(c.moveToNext());
        }

        return workouts;
    }

    /**
     * Get all segments
     * @return
     */
    public List<Segment> getAllSegments() {

        List<Segment> segments = new ArrayList<Segment>();
        String selectQuery = "SELECT * FROM " + tableSegments;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Segment segment = new Segment();
                segment.setID(c.getInt(c.getColumnIndex(keyID)));
                segment.setExerciseID(c.getLong(c.getColumnIndex(exerciseID)));
                segment.setWorkoutID(c.getLong(c.getColumnIndex(workoutID)));
                segment.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
                segments.add(segment);
            } while(c.moveToNext());
        }

        return segments;
    }

    /**
     * Get all segments with specified workout ID
     * @return
     */
    public List<Segment> getSegmentsByWorkoutID(long workoutID) {

        List<Segment> segments = new ArrayList<Segment>();
        String selectQuery = "SELECT * FROM " + tableSegments + " WHERE " + this.workoutID + " = " + workoutID;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Segment segment = new Segment();
                segment.setID(c.getInt(c.getColumnIndex(keyID)));
                segment.setExerciseID(c.getInt(c.getColumnIndex(exerciseID)));
                segment.setWorkoutID(c.getLong(c.getColumnIndex(this.workoutID)));
                segment.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
                segments.add(segment);
            } while(c.moveToNext());
        }

        return segments;
    }

    /**
     * Get all sets
     * @return
     */
    public List<ExerciseSet> getAllSets() {

        List<ExerciseSet> exerciseSets = new ArrayList<ExerciseSet>();
        String selectQuery = "SELECT * FROM " + tableSets;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                ExerciseSet set = new ExerciseSet();
                set.setID(c.getInt(c.getColumnIndex(keyID)));
                set.setSegmentID(c.getInt(c.getColumnIndex(segmentID)));
                set.setRepCount(c.getInt(c.getColumnIndex(repCount)));
                set.setWeight((c.getInt(c.getColumnIndex(weight))));
                set.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
                exerciseSets.add(set);
            } while(c.moveToNext());
        }

        return exerciseSets;
    }

    /**
     * Get all sets with specified segment ID
     * @return
     */
    public List<ExerciseSet> getSetsBySegmentID(long segID) {

        List<ExerciseSet> exerciseSets = new ArrayList<ExerciseSet>();
        String selectQuery = "SELECT * FROM " + tableSets + " WHERE " + segmentID + "=" + segID;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                ExerciseSet set = new ExerciseSet();
                set.setID(c.getInt(c.getColumnIndex(keyID)));
                set.setSegmentID(c.getInt(c.getColumnIndex(segmentID)));
                set.setRepCount(c.getInt(c.getColumnIndex(repCount)));
                set.setWeight((c.getInt(c.getColumnIndex(weight))));
                set.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
                exerciseSets.add(set);
            } while(c.moveToNext());
        }

        return exerciseSets;
    }

    /**
     * Get all exercises
     * @return
     */
    public List<Exercise> getAllExercises() {

        List<Exercise> exercises = new ArrayList<Exercise>();
        String selectQuery = "SELECT * FROM " + tableExercises;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                int test = c.getInt(c.getColumnIndex(exerciseIsAvailable));
                Exercise exercise = new Exercise();
                exercise.setID(c.getInt(c.getColumnIndex(keyID)));
                exercise.setName(c.getString(c.getColumnIndex(exerciseName)));
                exercise.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
                exercise.setIsAvailable(c.getInt(c.getColumnIndex(exerciseIsAvailable)) != 0);
                exercises.add(exercise);
            } while(c.moveToNext());
        }

        Collections.sort(exercises);
        return exercises;
    }

    /**
     * Get all muscle groups
     * @return
     */
    public List<MuscleGroup> getAllMuscleGroups() {

        List<MuscleGroup> muscleGroups = new ArrayList<MuscleGroup>();
        String selectQuery = "SELECT * FROM " + tableMuscleGroups;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                MuscleGroup muscleGroup = new MuscleGroup();
                muscleGroup.setID(c.getInt(c.getColumnIndex(keyID)));
                muscleGroup.setName(c.getString(c.getColumnIndex(muscleGroupName)));
                muscleGroup.setCreatedAt(c.getString(c.getColumnIndex(createdAt)));
                muscleGroups.add(muscleGroup);
            } while(c.moveToNext());
        }

        return muscleGroups;
    }

    public List<MuscleGroup> getMuscleGroupsByExerciseID(long exerciseID) {

        String selectQuery = "SELECT " + muscleGroupID + " FROM " + tableExerciseMuscleGroups
                + " WHERE " + this.exerciseID + " = " + exerciseID;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        List<Long> muscleGroupIDs = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                muscleGroupIDs.add(c.getLong(c.getColumnIndex(muscleGroupID)));
            } while(c.moveToNext());
        }

        List<MuscleGroup> muscleGroups = new ArrayList<>();
        for(Long muscleGroupID : muscleGroupIDs) {
            muscleGroups.add(getMuscleGroupByID(muscleGroupID));
        }

        return muscleGroups;
    }

    public List<Exercise> getExercisesByMuscleGroupID(long groupID) {

        String selectQuery = "SELECT " + exerciseID + " FROM " + tableExerciseMuscleGroups
                + " WHERE " + muscleGroupID + " = " + groupID;

        Log.e(log, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        List<Long> exerciseIDs = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                exerciseIDs.add(c.getLong(c.getColumnIndex(exerciseID)));
            } while(c.moveToNext());
        }

        List<Exercise> exercises = new ArrayList<>();
        for(Long exerciseID : exerciseIDs) {
            exercises.add(getExerciseByID(exerciseID));
        }

        Collections.sort(exercises);
        return exercises;
    }

    /**
     * Delete completed workout
     * @param ID
     */
    public void deleteCompletedWorkout(long ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<Segment> segments = getSegmentsByWorkoutID(ID);
        for(Segment segment : segments) {
            db.execSQL("DELETE FROM " + tableSets + " WHERE " + segmentID + " = " + segment.getID());
        }
        db.execSQL("DELETE FROM " + tableSegments + " WHERE " + workoutID + " = " + ID);
        db.execSQL("DELETE FROM " + tableCompletedWorkouts + " WHERE " + keyID + " = " + ID);
    }

    /**
     * Delete planned workout
     * @param ID
     */
    public void deletePlannedWorkout(long ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablePlannedWorkouts, keyID + " = ?",
                new String[] { String.valueOf(ID) });
    }

    /**
     * Delete segment
     * @param ID
     */
    public void deleteSegment(long ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableSets + " WHERE " + segmentID + " = " + ID);
        db.execSQL("DELETE FROM " + tableSegments + " WHERE " + keyID + " = " + ID);
    }

    /**
     * Delete exercise
     * @param ID
     */
    public void deleteExercise(long ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableExercises, keyID + " = ?",
                new String[] { String.valueOf(ID) });
    }

    /**
     * Delete muscle group
     * @param ID
     */
    public void deleteMuscleGroup(long ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableMuscleGroups, keyID + " = ?",
                new String[] { String.valueOf(ID) });
    }

    public void truncateExerciseMuscleGroups()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tableExerciseMuscleGroups
                + ";VACUUM";
        db.execSQL(query);
        Log.e(log, query);
    }

    /**
     * Delete all completed workouts
     */
    public void truncateCompletedWorkouts() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tableCompletedWorkouts
                + ";VACUUM";
        db.execSQL(query);
        Log.e(log, query);
    }

    /**
     * Update record in Exercise table
     * @param exercise
     */
    public void updateExercise(Exercise exercise) {

        int isAvailable;
        if(exercise.getIsAvailable()) {
            isAvailable = 1;
        }
        else {
            isAvailable = 0;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + tableExercises
                + " SET " + exerciseIsAvailable + "=" + isAvailable
                + " WHERE " + keyID + "=" + exercise.getID();
        db.execSQL(query);
        Log.e(log, query);
    }

    /**
     * Populate muscle group table from text file
     * @param context
     */
    public void populateMuscleGroupsTable(Context context) {

        //TODO: check if launching app for the first time in if
        //
        List<MuscleGroup> muscleGroups = getAllMuscleGroups();
        if(true) {
            for(MuscleGroup muscleGroup : muscleGroups) {
                deleteMuscleGroup(muscleGroup.getID());
            }

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open("muscleGroups.txt")));
                String mLine;
                while ((mLine = reader.readLine()) != null) {
                    values.put(muscleGroupName, mLine);
                    values.put(createdAt, getCurrentDate());
                    db.insert(tableMuscleGroups, null, values);
                }
            } catch (IOException e) {
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    /**
     * Populate exercises table from text file
     * @param context
     */
    public void populateExercisesTable(Context context) {

        //TODO: check if launching app for the first time in if
        List<Exercise> exercises = getAllExercises();
        if(true) {
            for(Exercise exercise : exercises) {
                deleteExercise(exercise.getID());
                truncateExerciseMuscleGroups();
            }

            SQLiteDatabase db = this.getWritableDatabase();

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open("exercises.txt")));
                String mLine;
                while ((mLine = reader.readLine()) != null) {
                    String[] lineElements = mLine.split(",");

                    //Create exercise
                    ContentValues exerciseValues = new ContentValues();
                    exerciseValues.put(exerciseName, lineElements[0]);
                    exerciseValues.put(createdAt, getCurrentDate());
                    exerciseValues.put(exerciseIsAvailable, 1);
                    long exerciseID = db.insert(tableExercises, null, exerciseValues);

                    //Create exercise muscle group
                    ContentValues emgValues = new ContentValues();
                    for(int i = 1; i < lineElements.length; i++) {
                        emgValues.put(this.exerciseID, exerciseID);
                        emgValues.put(muscleGroupID, lineElements[i]);
                        emgValues.put(createdAt, getCurrentDate());
                        db.insert(tableExerciseMuscleGroups, null, emgValues);
                    }
                }
            } catch (IOException e) {
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    /**
     * Drop all tables in database
     */
    public void dropAllTables() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + tableCompletedWorkouts);
        db.execSQL("DROP TABLE IF EXISTS " + tablePlannedWorkouts);
        db.execSQL("DROP TABLE IF EXISTS " + tableSegments);
        db.execSQL("DROP TABLE IF EXISTS " + tableExercises);
        db.execSQL("DROP TABLE IF EXISTS " + tableMuscleGroups);
        db.execSQL("DROP TABLE IF EXISTS " + tableSets);
        db.execSQL("DROP TABLE IF EXISTS " + tableCompletedWorkoutSegment);
        db.execSQL("DROP TABLE IF EXISTS " + tablePlannedWorkoutSegment);
    }

    /**
     * Close database
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * Get current date in format YYYY-MM-DD HH:MM:SS
     * @return String with current date
     */
    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        return date;
    }
}
