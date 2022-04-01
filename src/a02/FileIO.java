package a02;

import java.io.*;
import java.util.Scanner;
import net.datastructures.ArrayList;

public class FileIO {
    final static String[] CORRECT_FORMAT = { "add", "job", "name", "with", "length", "", "and", "priority", "" };
    
    public static ArrayList<ArrayList<String>> parseJobs() {
        ArrayList<ArrayList<String>> jobList = new ArrayList<>();
        Scanner fileText = null;
        try {
            fileText = new Scanner(new BufferedReader(new FileReader("jobs.txt")));
            while (fileText.hasNextLine()) {
                ArrayList<String> importantValues = formatChecker(fileText.nextLine());
                if (importantValues != null) {
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
}

public static ArrayList<String> formatChecker(String s) {
    ArrayList<String> importantValues = new ArrayList<>();
    String[] stringSplit = s.split("\\s");
    if (s.equals("no new job slice")){
        importantValues.add(importantValues.size(), "Skip");
        return importantValues;
    }
    for (int i = 0; i < stringSplit.length; i++) {
        if (stringSplit[i] == null) {
            return null;
        } else if (i == 2) {
            importantValues.add(importantValues.size(), stringSplit[i]);
        } else if (i == 5 || i == 8) {
            if (!isANumber(stringSplit[i])) {
                return null;
            } else {
                importantValues.add(importantValues.size(), stringSplit[i]);
            }
        } else {
            if (!stringSplit[i].equals(CORRECT_FORMAT[i])) {
                return null;
            }
        }
    }
    return importantValues;
}

private static boolean isANumber(String toCheck) {
    if (toCheck == null) {
        return false;
    }
    try {
        int l = Integer.parseInt(toCheck);
    } catch (NumberFormatException e) {
        return false;
    }
    return true;
}
}
