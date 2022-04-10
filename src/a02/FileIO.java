package a02;

import java.io.*;
import java.util.Scanner;
import net.datastructures.ArrayList;
/**
 * handles reading and manipulation of the jobs.txt file
 */
public class FileIO {
    final static String[] CORRECT_FORMAT = { "add", "job", "name", "with", "length", "", "and", "priority", "" };

    /**
     * opens the txt file containing job data. verifies format, pulls relevent data,
     * and returns in an arrayList containing arrayLists of strings. 
     * 
     * @param jobsTxtFileName
     * @return
     */
    public static ArrayList<ArrayList<String>> parseJobs(String jobsTxtFileName) {
        ArrayList<ArrayList<String>> jobList = new ArrayList<>();
        Scanner fileText = null;
        try {
            fileText = new Scanner(new BufferedReader(new FileReader(jobsTxtFileName)));
            while (fileText.hasNextLine()) {
                ArrayList<String> importantValues = formatChecker(fileText.nextLine());
                if (importantValues != null) { //if the format checker returned relevant data, store it
                    jobList.add(jobList.size(), importantValues);
                } else {
                    continue;
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            if (fileText != null) {
                fileText.close();
            }
        }
        return jobList;
    }//parseJobs method

    /**
     * grabs the name of the job being parsed. 
     * @param stringSplit the current line being parsed
     * @return ArrayList containing the full name and the index marking where formatChecker will continue processing
     */
    public static ArrayList<Object> nameGrabber(String[] stringSplit) {
        ArrayList<Object> returnArray = new ArrayList<>(); //store the name at 0, and the index at 1
        StringBuilder name = new StringBuilder(); //easier to append the name if it is longer than 1 string
        int k = 2; //starting index of the name if the format is correct. 
        name.append(stringSplit[k]); //get the first "piece" of the name immediately. 
        k++; //move to the next bit
        while (k < stringSplit.length - 1) { //and search for more "pieces"
            returnArray.add(0, name);
            returnArray.add(1, k);
            if (stringSplit[k].equals("with") && stringSplit[k + 1].equals("length") && isAnInteger(stringSplit[k+2])) {
                return returnArray; //return when the current string and it's followers match format for length assignment.
            } else {
                name.append(" " + stringSplit[k]); //or add current string and
                k++; //move to the next
            }
        }

        return null; //if we scan the rest of the line and don't return to format, return nothing
    }//nameGrabber method

    /**
     * scans the current line of jobs.txt and verifies format, pulls relevant data, and stores it in 
     * an arrayList<String>. uses helper methods nameGrabber(String[]) and isAnInteger(String)
     *  
     * @param currentLine line being checked
     * @return ArrayList<String> with relevant job data || "Skip" if no new job is introduced 
     * ||null if format is incorrect
     */
    public static ArrayList<String> formatChecker(String currentLine) {
        ArrayList<String> importantValues = new ArrayList<>(); //important values in form [0]:name [1]:length [2]: priority
        String[] stringSplit = currentLine.split("\\s"); //split the currentline into an array based on spaces
        //String[] stringSplit = stringSplitter(currentLine); //not sure if the above line is regex or not so made a fallback method.. 
        if (currentLine.equals("no new job this slice")) {
            importantValues.add(importantValues.size(), "Skip"); //edge case returns array holding just "Skip"
            return importantValues;
        }
        int inputIndex = 0;
        for (int i = 0; i < CORRECT_FORMAT.length; i++) {
            if (stringSplit[inputIndex] == null) {
                return null; //if the string is empty
            } else if (i == 2) {
                ArrayList<Object> nameGrabberRestults = nameGrabber(stringSplit); //send the string[] to namegrabber
                if (nameGrabberRestults != null) {
                    importantValues.add(importantValues.size(), nameGrabberRestults.get(0).toString());
                    inputIndex = (int)nameGrabberRestults.get(1); //store the name and update our index to compensate for name length
                } else {
                    return null; //if namegrabber failed the line is of improper format and we return null
                }
            } else if (i == 5 || i == 8) {
                if (!isAnInteger(stringSplit[inputIndex])) { 
                    return null; //if we don't have ints for length and priority we return null
                } else {
                    importantValues.add(importantValues.size(), stringSplit[inputIndex]);
                    inputIndex++; //if we do, store em and move on
                }
            } else {
                if (!stringSplit[inputIndex].equals(CORRECT_FORMAT[i])) {
                    return null; //just comparing filler words meet format ie. add, job, with, etc..
                }
                inputIndex++;
            }
        }
        return importantValues;
    }//formatChecker method

    /**
     * threw this together in case String.split("\\s") is regex and forbidden.
     * 
     * @param stringToSplit String to convert to array
     * @return String[] of words in the stringToSplit
     */
    private static String[] stringSplitter(String stringToSplit) {
        ArrayList<String> splitStringAList = new ArrayList<>();
        Scanner reader = new Scanner(stringToSplit);
        while (reader.hasNext()){
            splitStringAList.add(splitStringAList.size(), reader.next());
        }
        String[] splitString = new String[splitStringAList.size()];
        int i = 0;
        for (String string : splitStringAList) {
            splitString[i] = string;
            i++;
        }
        return splitString;
    }//stringSplitter method

    /**
     * returns true if String is an integer else false
     * 
     * @param toCheck String being checked
     * @return
     */
    private static boolean isAnInteger(String toCheck) {
        if (toCheck == null) {
            return false;
        }
        try {
            int l = Integer.parseInt(toCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }//isAnInteger method
}//FileIO class