import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPReader {
    BufferedReader paramsBufferedReader;
    BufferedReader problemBufferedReader;
    String paramFilePath;
    String problemFilePath;
    List<Double> xCoordinates;
    List<Double> yCoordinates;

    public TSPReader(String paramFilePath){
        this.paramFilePath = paramFilePath;
        try{
            this.paramsBufferedReader = new BufferedReader(new FileReader(this.paramFilePath));
            paramsBufferedReader.mark(10000);
            if(paramsBufferedReader.readLine().contains("PROBLEM_FILE")){
                paramsBufferedReader.reset();
                this.problemFilePath = findProblemFilePath();
            }else{
                this.problemFilePath = paramFilePath;
            }

            System.out.println(problemFilePath);
            this.problemBufferedReader = new BufferedReader(new FileReader(this.problemFilePath));
            findCoordinates();

            paramsBufferedReader.close();
            problemBufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String findProblemFilePath() throws IOException{
        String line = paramsBufferedReader.readLine();

        while(line != null){
            String[] lineArray = line.split("=");
            String descriptor = lineArray[0];
            descriptor = descriptor.replaceAll(" ", "");
            if(descriptor.compareTo("PROBLEM_FILE") == 0){
                String unfinishedPath = lineArray[1];
                unfinishedPath = unfinishedPath.replaceAll(" ", "");

                unfinishedPath = PathMaker.convertSlashesToBackslashes(unfinishedPath);
                int numberOfReverts = PathMaker.findNumberOfReverts(unfinishedPath);
                //System.out.println(numberOfReverts);

                String frontPath = PathMaker.deleteXPostPaths(numberOfReverts, paramFilePath);
                frontPath = PathMaker.removeLastElement(frontPath);
                String endPath = PathMaker.deleteXPrePaths(numberOfReverts, unfinishedPath);
                return PathMaker.fuseTwoPaths(frontPath,endPath);
            }
            line = paramsBufferedReader.readLine();
        }
        System.err.println("No problem file path was found in the given file");
        return null;
    }


    private void findCoordinates() throws IOException{
        String line = problemBufferedReader.readLine();
        boolean beginCoords = false;
        boolean checkForCoordSection = true;

        xCoordinates = new ArrayList<>();
        yCoordinates = new ArrayList<>();

        while(line != null && !line.contains("EOF")){
            if (beginCoords) {
                String[] lineArray = line.split(" ");
                xCoordinates.add(Double.valueOf(lineArray[1]));
                yCoordinates.add(Double.valueOf(lineArray[2]));
            }


            if(checkForCoordSection && line.contains("NODE_COORD_SECTION")){
                checkForCoordSection = false;
                beginCoords = true;
            }
            line = problemBufferedReader.readLine();
        }
    }


    public String getProblemFilePath() {
        return problemFilePath;
    }

    public List<Double> getxCoordinates() {
        return xCoordinates;
    }

    public List<Double> getyCoordinates() {
        return yCoordinates;
    }
}
