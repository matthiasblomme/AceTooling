package com.id.ace.policies;

import com.id.ace.bar.BarUtils;
import com.id.ace.utils.FileUtils;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
This class will search for Policies usage.
It will search all bar files in a folder (recursively) and will compare each property that is set on the bar file
to a list of policy names that are provided via an input file.
It will print all found hits to the console window
 */
public class FindPoliciesInBar {

    /*
    The list that contains the policies to search for
     */
    static HashSet<String> csList = new HashSet<>();
    /*
    A regex pattern to quickly match all policies
     */
    static String csPattern;
    /*
    The directory to search in
     */
    static String searchDir = "C:\\Users\\blmm_m\\workspace\\CVS_export";
    /*
    The file contain the policy names to search
     */
    static String csFileName = "C:\\ProgramData\\IBM\\MQSI\\components\\ACEMIG\\policies.txt";

    /*
    Main entry class to run the code
     */
    public static void main(String[] args) {
        List<Path> hitList = FileUtils.find(searchDir, ".bar");
        csList = PoliciesList.readCsFile(csFileName);
        csPattern = String.join("|", csList);
        Pattern p = Pattern.compile(csPattern);
        assert hitList != null;
        hitList.stream().sorted().forEach(k -> {
            //System.out.println(k);
            try {
                Map<String, String> barContents = BarUtils.readBar(k.toString());
                for (String key : barContents.keySet()) {
                    Matcher m = p.matcher(barContents.get(key));
                    if (m.matches()) {
                        System.out.println(k + "#" + key + "#" + barContents.get(key));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
