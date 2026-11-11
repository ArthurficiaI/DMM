import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParamfileWriter {

    public ParamfileWriter(){}

    public void writeATSPParamFilesFromDir(String paramDirectory, String problemDirectory, String LKHDirectory){
        writeATSPParamFilesFromDir(paramDirectory, problemDirectory,LKHDirectory, "");
    }

    public void writeATSPParamFilesFromDir(String paramDirectory, String problemDirectory, String LKHDirectory, String basicFileText){
        File sDir = new File(problemDirectory);

        if(sDir.isDirectory()){
            File[] sourceFiles = sDir.listFiles();
            for(File sourceFile : sourceFiles){
                if(sourceFile.isFile()){
                    String problemPath = sourceFile.getPath();
                    writeATSPParamFile(paramDirectory,problemPath,LKHDirectory, basicFileText);
                }
            }
        }else{
            System.err.println("The given sourcePath is not to a directory!");
        }
    }

    public void writeATSPParamFile(Path paramDirectory, Path problemPath, Path LKHDirectory, String basicFileText){
        String problemFileName = problemPath.getFileName().toString();
        String paramPath = createFullParamPath(problemFileName, paramDirectory);

        LKHDirectory = LKHDirectory.toAbsolutePath();
        problemPath = problemPath.toAbsolutePath();

        Path relativeProblemPath = LKHDirectory.relativize(problemPath);

        String problem_file = relativeProblemPath.toString().replaceAll("\\\\","/");

        writeParameterText(problem_file,paramPath);
    }

    private String createFullParamPath(String problemFileName, Path paramDirectory){
        String[] problemFileNameArray = problemFileName.split("[.]");
        String paramFileName = "";
        for(int i = 0; i<problemFileNameArray.length-1;i++) {
            paramFileName += problemFileNameArray[i] + ".";
        }
        paramFileName = paramFileName + "par"; //the ending of the paramFileName has an additional dot which is why we write par and not .par

        Path fullParamPath = paramDirectory.resolve(paramFileName);

        return fullParamPath.toString();
    }

    private String createTourFileName(String problemFileName){
        String[] problemFileNameArray = problemFileName.split("[.]");
        String tourFileName = "";
        for(int i = 0; i<problemFileNameArray.length-1;i++) {
            tourFileName += problemFileNameArray[i] + ".";
        }
        tourFileName = tourFileName + "tour";

        return tourFileName;
    }

    public void writeATSPParamFileWithSeedAndTourPath(String paramDirectory, String problemPathString, String LKHDirectory, String seed, String tourPathString){
        Path problemPath = Paths.get(problemPathString);
        String problemFileName = problemPath.getFileName().toString();
        String finalParamPathString = createFullParamPath(problemFileName, Paths.get(paramDirectory));
        String tourFileName = createTourFileName(problemFileName);

        Path LKHPath = Paths.get(LKHDirectory).toAbsolutePath();
        problemPath = problemPath.toAbsolutePath();
        Path tourPath = Paths.get(tourPathString).toAbsolutePath();

        Path relativeProblemPath = LKHPath.relativize(problemPath);
        Path relativeTourPath = LKHPath.relativize(tourPath).resolve(tourFileName);

        String finalTourPath = relativeTourPath.toString().replaceAll("\\\\","/");
        String finalProblemPath = relativeProblemPath.toString().replaceAll("\\\\","/");

        writeParameterText(finalProblemPath, finalParamPathString, seed, finalTourPath);
    }

    public void writeATSPParamFile(String paramDirectory, String problemPath, String LKHDirectory){
        writeATSPParamFile(paramDirectory,problemPath,LKHDirectory,"");
    }

    public void writeATSPParamFile(String paramDirectory, String problemPath, String LKHDirectory, String basicText){
        writeATSPParamFile(Paths.get(paramDirectory),Paths.get(problemPath), Paths.get(LKHDirectory),basicText);
    }

    public void writeATSPParamFileWithBasic(String paramDirectory, String problemPath, String LKHDirectory, String basicParamPath){
        String basicText = readBasicParams(basicParamPath);
        writeATSPParamFile(paramDirectory, problemPath,LKHDirectory,basicText);
    }

    public void writeATSPParamDirWithBasic(String paramDirectory, String problemDirectory, String LKHDirectory, String basicParamPath){
        String basicText = readBasicParams(basicParamPath);
        writeATSPParamFilesFromDir(paramDirectory,problemDirectory,LKHDirectory,basicText);
    }

    private String readBasicParams(String basicParamPath){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(basicParamPath));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()).length() != 0){
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();

        }catch (IOException e){
            System.err.println("Error while trying to read the basic parameter file");
            System.exit(0);
        }
        return "";
    }


    private void writeParameterText(String problem_file, String paramPath){
        writeParameterText(problem_file, paramPath,"1","");
    }

    private void writeParameterText(String problem_file, String paramPath, String seed, String tourPath){
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(paramPath));
            bufferedWriter.write("PROBLEM_FILE = " + problem_file); bufferedWriter.newLine();


            bufferedWriter.write("STOP_AT_OPTIMUM = NO"); bufferedWriter.newLine(); //
            bufferedWriter.write("CANDIDATE_SET_TYPE = ALPHA"); bufferedWriter.newLine(); //probably the only set type that works for atsp, as POPMUSIC doesn't work with atsp. The other types only work for coordinate files
            bufferedWriter.write("RUNS = 1"); bufferedWriter.newLine(); //used 1 instead of 5 so we can run the 5 runs in parallel
            bufferedWriter.write("MAX_CANDIDATES = 5"); bufferedWriter.newLine(); //Standard
            bufferedWriter.write("SEED = " + seed); bufferedWriter.newLine();
            bufferedWriter.write("TRACE_LEVEL = 1"); bufferedWriter.newLine(); //Standard I think
            bufferedWriter.write("TIME_LIMIT = 259200"); bufferedWriter.newLine(); //time limit of 3 days
            if(tourPath.length()>0){
                bufferedWriter.write("TOUR_FILE = " + tourPath);
            }


            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }

    }







    /*
    private Path produceRelativeProblemPath(Path paramDirectory, Path problemPath){
        String paramDirectoryString = paramDirectory.toString();
        String problemPathString  = problemPath.toString();

        boolean keepSearchingForDifference = true;
        int i = 0;
        while(keepSearchingForDifference){
            i++;
            keepSearchingForDifference = (paramDirectory.subpath(0,i).compareTo(problemPath.subpath(0,i)) == 0);
        }
        int numberOfSameDirs = i - 1;

        Path relativeLatterHalf = problemPath.subpath(numberOfSameDirs,problemPath.getNameCount());

        int numberOfReverts = paramDirectory.getNameCount() - numberOfSameDirs;
        Path relativeFirstHalf = Paths.get("");
        for (int j = 0; j<numberOfReverts;j++){
            relativeFirstHalf.resolve(("../"));
        }

        Path fullRelativePath = relativeFirstHalf.resolve()

    }


     */
}
