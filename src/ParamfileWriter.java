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
        //System.out.println(problemPath);
        String problemFileName = problemPath.getFileName().toString();
        //System.out.println(problemFileName);

        String[] problemFileNameArray = problemFileName.split("[.]");
        String paramFileName = "";
        for(int i = 0; i<problemFileNameArray.length-1;i++) {
            paramFileName += problemFileNameArray[i] + ".";
        }
        paramFileName = paramFileName + "par"; //the ending of the paramFileName has an additional dot which is why we write par and not .par

        LKHDirectory = LKHDirectory.toAbsolutePath();
        problemPath = problemPath.toAbsolutePath();

        Path fullParamPathString = paramDirectory.resolve(paramFileName);
        Path relativeProblemPath = LKHDirectory.relativize(problemPath);



        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fullParamPathString.toString()));
            bufferedWriter.write("PROBLEM_FILE = " + relativeProblemPath.toString().replaceAll("\\\\","/")); bufferedWriter.newLine();
            if(basicFileText.length() == 0){
                System.out.println("Writing default parameters");

                bufferedWriter.write("STOP_AT_OPTIMUM = NO"); bufferedWriter.newLine();
                bufferedWriter.write("CANDIDATE_SET_TYPE = POPMUSIC");
                bufferedWriter.write("RUNS = 5"); bufferedWriter.newLine();
                bufferedWriter.write("MAX_CANDIDATES = 5"); bufferedWriter.newLine();
                bufferedWriter.write("SEED = 1"); bufferedWriter.newLine();
                bufferedWriter.write("TRACE_LEVEL = 1"); bufferedWriter.newLine();

            }else{
                bufferedWriter.write(basicFileText);
            }

            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
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
