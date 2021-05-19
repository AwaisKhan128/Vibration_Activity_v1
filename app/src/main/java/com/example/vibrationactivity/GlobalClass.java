package com.example.vibrationactivity;

import android.app.Application;

import java.util.ArrayList;


public class GlobalClass extends Application {

    public static float delay_between_blink;
    public static String experimenter_name;
    public static String test_date;


    // Settings
    public static int done; // Button
    public static int number_of_rounds; //  number of rounds as set in settings
    // number of trials over all the rounds as counted in Device Control Activity
    public static int number_of_trials;
    // How intervals calculated: Staircase (increasing) = 0, Random = 1, converge (closes in on score based on feedback loop)= 2
    private static int mode;
    private static int t_mode;     //Visual=0, tactile=1, multimodal=3
    public static Boolean new_round; //  new round used for results table
    static Boolean voiceCommand = Boolean.FALSE; // Voice Command
    static Boolean catch_random = Boolean.FALSE; // Catch trial is set to random
    static int selected_catch = 5; // Catch defaulted to trial 5
    static int catch_count;

// Determines increment interval for visual test
    static float refresh_rate = 60;
    static float frame_time = 1000/refresh_rate;

    // Score for a given round
    public static int[] discrete_tdt = new int[100];

    // How many times in a row 'different' has been selected
    public static int Async_count;

    // waiting time before first stimulation
    private static int wait_time_bef_aft_blink = 5000;

    //  Duration of stimulation
    private static float blink_time = frame_time;
    // For Audio (not working yet)
    private static float beep_time = 100;

    // Delay between stimulations in byte form
    private static byte delay_between_blink_byte;

    // Sum of scores across rounds
    public static int total;



    // Hold = 1 while catch trial is being run
    public static int Hold;

    // This may be redundant
    public static float TDT_value;

    // How many times stimulation has run
    public static int stim_count;

    // error fixer: may have to adjust size of array
    public static float[] Random_ISI = new float[85];





    // Method for getting the minimum value
    public static float getMin(float[] inputArray) {
        float minValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] < minValue) {
                minValue = inputArray[i];
            }
        }
        return minValue;
    }


    // Data for results Table (will be emailed to user)
    public static ArrayList<String> response = new ArrayList<String>(1); // "Same" or "Different"
    public static ArrayList<String> round_label  = new ArrayList<String>(1); // states round if not already stated
    public static ArrayList<String> interval  = new ArrayList<String>(1); // states round if not already stated
    public static ArrayList<String> catcher = new ArrayList<String>(1); // states if catch occured here



    public static int number_of_responses = response.size();    // number of responses
    public static int presented_interval;     // ISI
    // TDT_Round
    public static int Left_Right;             // equates to coloun number (2 or 3)




    // Invalidated due to "Different" for Catch trial
    public static String Catch_Error;



    // Getters and Setters


    public int getDone() {
        return done;
    }

    public void setDone(int Done) {
        this.done = done;
    }

    public String getCatch_Error() {
        return Catch_Error;
    }

    public void setCatch_Error(String catch_Error) {
        Catch_Error = catch_Error;
    }


    public ArrayList<String> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<String> response) {
        this.response = response;
    }

    public ArrayList<String> getRound_label() {
        return round_label;
    }

    public void setRound_label(ArrayList<String> round_label) {
        this.round_label = round_label;
    }

    public int getNumber_of_responses() {
        return number_of_responses;
    }

    public void setNumber_of_responses(int number_of_responses) {
        this.number_of_responses = number_of_responses;
    }


    public int getPresented_interval() {
        return presented_interval;
    }

    public void setPresented_interval(int presented_interval) {
        this.presented_interval = presented_interval;
    }

    public int getLeft_Right() {
        return Left_Right;
    }

    public void setLeft_Right(int left_Right) {
        Left_Right = left_Right;
    }





    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getExperimenter_name() {
        return experimenter_name;
    }

    public void setExperimenter_name(String experimenter_name) {
        this.experimenter_name = experimenter_name;
    }

    public String getTest_date() {
        return test_date;
    }

    public void setTest_date(String test_date) {
        this.test_date = test_date;
    }




    public static int Tdt_round ;


    public static int getT_mode() {
        return t_mode;
    }

    public void setT_mode(int t_mode) {
        this.t_mode = t_mode;
    }

    public static int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static int getTdt_round() {
        return Tdt_round;
    }

    public static void setTdt_round(int tdt_round) {
        Tdt_round = tdt_round;
    }

    public static int getNumber_of_rounds() {
        return number_of_rounds;
    }

    public static void setNumber_of_rounds(int number_of_rounds) {
        GlobalClass.number_of_rounds = number_of_rounds;

    }

    public static int getWait_time_bef_aft_blink() {
        return wait_time_bef_aft_blink;
    }

    public void setWait_time_bef_aft_blink(int wait_time_bef_aft_blink) {
        this.wait_time_bef_aft_blink = wait_time_bef_aft_blink;
    }

    public static float getBlink_time() {
        return blink_time;
    }

    public void setBlink_time(float blink_time) {
        this.blink_time = blink_time;
    }

    public static float getDelay_between_blink() {
        return delay_between_blink;
    }

    public void setDelay_between_blink(float delay_between_blink) {
        this.delay_between_blink = delay_between_blink;
    }

    public static byte getDelay_between_blink_byte() {
        return delay_between_blink_byte;
    }

    public void setDelay_between_blink_byte(byte delay_between_blink_byte) {
        this.delay_between_blink_byte = delay_between_blink_byte;
    }

    public static int getAsync_count() {
        return Async_count;
    }

    public static void setAsync_count(int async_count) {
        Async_count = async_count;
    }

    public static float[] getRandom_ISI() {
        return Random_ISI;
    }

    public static float getFrame_time() {
        return frame_time;
    }

    public void setFrame_time(float frame_time) {
        this.frame_time = frame_time;
    }

    public static int[] getDiscrete_tdt(int tdt_round) {
        return discrete_tdt;
    }

    public void setDiscrete_tdt(int[] discrete_tdt) {
        this.discrete_tdt = discrete_tdt;
    }

    public static int getStim_count() {
        return stim_count;
    }

    public void setStim_count(int stim_count) {
        this.stim_count = stim_count;
    }


    public static int getCatch_count() {
        return catch_count;
    }

    public void setCatch_count(int catch_count) {
        this.catch_count = catch_count;
    }


    public static int getHold() {
        return Hold;
    }

    public void setHold(int hold) {
        this.Hold = hold;
    }

    public static float getTDT_value() {
        return TDT_value;
    }

    public void setTDT_value(float TDT_value) {
        this.TDT_value = TDT_value;
    }

    public static void setRandom_ISI(float[] random_ISI) {
        Random_ISI = random_ISI;
    }

    public static Boolean getCatch_random() {
        return catch_random;
    }

    public void setCatch_random(Boolean catch_random) {
        this.catch_random = catch_random;
    }

    public static int getSelected_catch() {
        return selected_catch;
    }

    public void setSelected_catch(int selected_catch) {
        this.selected_catch = selected_catch;
    }

    public static float getRefresh_rate() {
        return refresh_rate;
    }

    public void setRefresh_rate(float refresh_rate) {
        this.refresh_rate = refresh_rate;
    }


    public static Boolean getVoiceCommand() {
        return voiceCommand;
    }

    public static void setVoiceCommand(Boolean voiceCommand) {
        GlobalClass.voiceCommand = voiceCommand;
    }

    public static float getBeep_time() {
        return beep_time;
    }

    public static void setBeep_time(float beep_time) {
       GlobalClass.beep_time = beep_time;
    }

    public static Boolean getNew_round() {
        return new_round;
    }

    public static void setNew_round(Boolean new_round) {
        GlobalClass.new_round = new_round;
    }


    public static int getNumber_of_trials() {
        return number_of_trials;
    }

    public static void setNumber_of_trials(int number_of_trials) {
        GlobalClass.number_of_trials = number_of_trials;
    }



}
