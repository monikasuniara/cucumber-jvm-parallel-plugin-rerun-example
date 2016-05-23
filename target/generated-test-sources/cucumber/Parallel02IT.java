import cucumber.runtime.Runtime;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import org.apache.maven.plugin.MojoExecutionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import com.github.timm.cucumber.options.ExtendedRuntimeOptions;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import org.apache.commons.io.FileUtils;
import net.masterthought.cucumber.ReportBuilder;

public class Parallel02IT{

        private String outputPath="target/cucumber-parallel/Parallel02IT/Parallel02IT";
        private String glue ="foo, bar";

        @Test
        public void reRun()  {

                defaultRun();

                int count= 2;

                System.out.println("Retry Count specified is :- "+count);

                switch (count){
                        case 1:
                               firstRun();
                               break;
                        case 2:
                               firstRun();
                               secondRun();
                               break;
                        case 3:
                               firstRun();
                               secondRun();
                               thirdRun();
                               break;
                        case 4:
                               firstRun();
                               secondRun();
                               thirdRun();
                               fourthRun();
                                break;
                        case 5:
                                firstRun();
                                secondRun();
                                thirdRun();
                                fourthRun();
                                fifthRun();
                                break;
                        default:
                                break;
                }
                 GenerateAllRunReports();

        }

        private void defaultRun() {
                       List<String> arguments = new ArrayList<String>();
                       arguments.add("classpath:features/feature2.feature");
                       String[] tags = {"@complete"};
                          for(String tag : tags) {
                              arguments.add("--tags");
                              arguments.add(tag);
                          }
                       arguments.add("--plugin");
                       arguments.add("html:target/cucumber-parallel/Parallel02IT/Parallel02IT.html");
                       arguments.add("--plugin");
                       arguments.add("json:target/cucumber-parallel/Parallel02IT/Parallel02IT.json");
                       arguments.add("--plugin");
                       arguments.add("rerun:target/cucumber-parallel/Parallel02IT/Parallel02IT.rerun");
                       String[] gluepackages = glue.split(",");
                       for (String packages : gluepackages) {
                               if (!packages.contains("none")) {
                                       arguments.add("--glue");
                                       arguments.add(packages);
                               }
                       }
                       final String[] argv = arguments.toArray(new String[0]);
                       try {
                               executetests(argv);
                       } catch (InterruptedException e) {
                               e.printStackTrace();
                       } catch (IOException e) {
                               e.printStackTrace();
                       }
               }

        public void firstRun(){
        try{
                if(new File(outputPath+".rerun").exists() && new BufferedReader(new FileReader(outputPath + ".rerun")).readLine() != null) {
                        //ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                        ExecuteReRerun("@"+outputPath+".rerun",outputPath+"/cucumber1.json",outputPath+"/rerun1.rerun");
                }
           }catch(Exception e){e.printStackTrace();}
        }
        public void secondRun(){
        try{
                if(new File(outputPath+"/rerun1.rerun").exists()&& new BufferedReader(new FileReader(outputPath+"/rerun1.rerun")).readLine() != null) {
                        //ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                        ExecuteReRerun("@"+outputPath+"/rerun1.rerun",outputPath+"/cucumber2.json",outputPath+"/rerun2.rerun");
                }
            }catch(Exception e){e.printStackTrace();}
        }
        public void thirdRun(){
        try{
             if(new File(outputPath+"/rerun2.rerun").exists() && new BufferedReader(new FileReader(outputPath+"/rerun2.rerun")).readLine() != null) {
                        //ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                        ExecuteReRerun("@"+outputPath+"/rerun2.rerun",outputPath+"/cucumber3.json",outputPath+"/rerun3.rerun");
                }
             }catch(Exception e){e.printStackTrace();}
        }
        public void fourthRun(){
               try{
                if(new File(outputPath+"/rerun3.rerun").exists()&& new BufferedReader(new FileReader(outputPath+"/rerun3.rerun")).readLine() != null) {
                       //ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                       ExecuteReRerun("@"+outputPath+"/rerun3.rerun",outputPath+"/cucumber4.json",outputPath+"/rerun4.rerun");
               }
                }catch(Exception e){e.printStackTrace();}
        }

        public void fifthRun(){
           try{
                if(new File(outputPath+"/rerun4.rerun").exists() && new BufferedReader(new FileReader(outputPath+"/rerun4.rerun")).readLine() != null) {
                      //ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                  ExecuteReRerun("@"+outputPath+"/rerun4.rerun",outputPath+"/cucumber5.json",outputPath+"/rerun5.rerun");
                }
              }catch(Exception e){e.printStackTrace();}
        }

        public void ExecuteReRerun(String rerunFile,String targetJson,String targetRerun) {
                List<String> arguments = new ArrayList<String>();
                arguments.add(rerunFile);
                arguments.add("--plugin");
                arguments.add("pretty:"+outputPath+"/cucumber-pretty.txt");
                arguments.add("--plugin");
                arguments.add("json:"+targetJson);
                arguments.add("--plugin");
                arguments.add("rerun:"+targetRerun);
                String[] gluepackages = glue.split(",");
                for (String packages : gluepackages) {
                        if (!packages.contains("none")) {
                                arguments.add("--glue");
                                arguments.add(packages);
                        }
                }
                final String[] argv = arguments.toArray(new String[0]);
                try {
                        executetests(argv);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }


        public byte executetests(final String[] argv) throws InterruptedException, IOException {

                ExtendedRuntimeOptions runtimeOptions = new ExtendedRuntimeOptions(new ArrayList(Arrays.asList(argv)));
                MultiLoader resourceLoader = new MultiLoader(this.getClass().getClassLoader());
                ResourceLoaderClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, this.getClass().getClassLoader());
                Runtime runtime = new Runtime(resourceLoader, classFinder, this.getClass().getClassLoader(), runtimeOptions);
                runtime.run();
                System.out.println(runtime.exitStatus());
                return runtime.exitStatus();

        }

        public static  void GenerateAllRunReports() {

               try{
                   List<File> jsons= finder("target/cucumber-parallel");
                   List<File> defaultRunJSONs = new ArrayList<File>();
                   List<File> firstRunJSONs = new ArrayList<File>();
                   List<File> secondRunJSONs = new ArrayList<File>();
                   List<File> thirdRunJSONs = new ArrayList<File>();
                   List<File> fourthRunJSONs = new ArrayList<File>();
                   List<File> fifthRunJSONs = new ArrayList<File>();
                   for (File f : jsons){
                       if(f.getName().contains("cucumber1") && f.getName().endsWith(".json")){
                           firstRunJSONs.add(f);
                       }else if(f.getName().contains("cucumber2") && f.getName().endsWith(".json")){
                           secondRunJSONs.add(f);
                       } else if(f.getName().contains("cucumber3") && f.getName().endsWith(".json")){
                           thirdRunJSONs.add(f);
                       }else if(f.getName().contains("cucumber4") && f.getName().endsWith(".json")){
                           fourthRunJSONs.add(f);
                       }else if(f.getName().contains("cucumber5") && f.getName().endsWith(".json")){
                           fifthRunJSONs.add(f);
                       }else{
                           defaultRunJSONs.add(f);
                       }
                   }

                   if (defaultRunJSONs.size()!=0){
                          generateRunWiseReport(defaultRunJSONs,"Default_Run");
                      }
                      if(firstRunJSONs.size()!=0){
                          generateRunWiseReport(firstRunJSONs,"First_Re-Run");
                      }
                      if(secondRunJSONs.size()!=0){
                          generateRunWiseReport(secondRunJSONs,"Second_Re-Run");
                      }
                      if(thirdRunJSONs.size()!=0){
                          generateRunWiseReport(thirdRunJSONs,"Third_Re-Run");
                      }
                      if(fourthRunJSONs.size()!=0){
                          generateRunWiseReport(fourthRunJSONs,"Fourth_Re-Run");
                      }
                      if(fifthRunJSONs.size()!=0){
                          generateRunWiseReport(fifthRunJSONs,"Fifth_Re-Run");
                      }

               }catch (Exception e){
                   e.printStackTrace();
               }
        }
        public static List<File> finder( String dirName){
                       return (List<File>)FileUtils.listFiles(new File(dirName),new String[]{"json"},true);
        }

         public static void generateRunWiseReport(List<File> jsons,String run){
         String timeStamp = new SimpleDateFormat("yyyyMMddhhmm").format(new java.util.Date());
                       try {
                           File rd = new File("../Reports/Result_"+timeStamp+"/"+run);
                           List<String> jsonReports = new ArrayList<String>();
                           for (File json : jsons) {
                               jsonReports.add(json.getAbsolutePath());
                           }
                           //List<String> jsonReports, File reportDirectory, String pluginUrlPath, String buildNumber, String buildProject, boolean skippedFails, boolean undefinedFails, boolean flashCharts, boolean runWithJenkins, boolean artifactsEnabled, String artifactConfig, boolean highCharts
                           ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", run, "cucumber-reporting", true, true, true, false, false, "", false);
                           reportBuilder.generateReports();
                           System.out.println(run+ " consolidated reports are generated under directory ../Reports/Result_"+timeStamp);
                       }catch (Exception e){
                           e.printStackTrace();
                       }
               }
}
