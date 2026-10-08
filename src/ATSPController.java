import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ATSPController {

    public ATSPController(){}

    public void convertAndWriteParams(String problemPath,  String newATSPProblem, String writeNewParamPath, String LKHPath){
        String newATSPProblemName = convertTSPtoATSP(problemPath, newATSPProblem);
        String problemFileNameATSP = Paths.get(newATSPProblem).getFileName().toString();

        if(!problemFileNameATSP.contains(".")){
            newATSPProblem = newATSPProblem + "/" + newATSPProblemName + ".atsp";
        }
        writeParams(newATSPProblem, writeNewParamPath, LKHPath);
    }

    public void writeParams(String atspProblemPath, String writeNewParamPath, String LKHPath){
        ParamfileWriter paramfileWriter = new ParamfileWriter();
        paramfileWriter.writeATSPParamFile(writeNewParamPath,atspProblemPath,LKHPath);
    }

    public void writeParamsFromDir(String atspProblemDir, String writeNewParamPath, String LKHPath){
        ParamfileWriter paramfileWriter = new ParamfileWriter();
        paramfileWriter.writeATSPParamFilesFromDir(writeNewParamPath,atspProblemDir,LKHPath);
    }

    public void specificParams(String atspProblemPath, String writeNewParamPath, String LKHPath, String basicParamReadPath){
        ParamfileWriter paramfileWriter = new ParamfileWriter();
        paramfileWriter.writeATSPParamFileWithBasic(writeNewParamPath,atspProblemPath,LKHPath,basicParamReadPath);
    }

    public void specificParamsDir(String atspProblemDir, String writeNewParamPath, String LKHPath, String basicParamReadPath){
        ParamfileWriter paramfileWriter = new ParamfileWriter();
        paramfileWriter.writeATSPParamDirWithBasic(writeNewParamPath,atspProblemDir,LKHPath,basicParamReadPath);
    }

    public String convertTSPtoATSP(String problemPath,  String writeNewProblemPath){
        TSPReader tspReader = new TSPReader(problemPath);
        SlopeDistanceMatrixMaker slopeDistanceMatrixMaker = new SlopeDistanceMatrixMaker(tspReader.getxCoordinates(),tspReader.getyCoordinates());
        return slopeDistanceMatrixMaker.generateAndWriteSlopedDistanceMatrix(writeNewProblemPath);
    }

    public void convertTSPDirToATSPDir(String sourceDir, String atspDir){
        File sDir = new File(sourceDir.toString());

        if(sDir.isDirectory()){
            File[] sourceFiles = sDir.listFiles();
            for(File sourceFile : sourceFiles){
                if(sourceFile.isFile()){
                    String sourcePath = sourceFile.getPath();
                    String problemName = convertTSPtoATSP(sourcePath,atspDir);

                }
            }
        }else{
            System.err.println("The given sourcePath is not to a directory!");
        }
    }

    public void convertTSPDirToATSPDirWithParams(String sourceDir, String atspDir, String writeNewParamPath, String LKHPath){
        File sDir = new File(sourceDir);

        if(sDir.isDirectory()){
            File[] sourceFiles = sDir.listFiles();
            for(File sourceFile : sourceFiles){
                if(sourceFile.isFile()){
                    String sourcePath = sourceFile.getPath();
                    convertAndWriteParams(sourcePath, atspDir, writeNewParamPath, LKHPath);
                }
            }
        }else{
            System.err.println("The given sourcePath is not to a directory!");
        }
    }

    public void convertAndWriteParams(Path problemPath, Path writeNewProblemPath, Path writeNewParamPath, Path LKHPath){
        convertAndWriteParams(problemPath.toString(),writeNewProblemPath.toString(),writeNewParamPath.toString(),LKHPath.toString());
    }

    public void reduceNodesOnce(String sourcePath, String writingDir, String numberOfDeletionsString){
        int numberOfDeletions = Integer.valueOf(numberOfDeletionsString);

        NodeReducer nodeReducer = new NodeReducer(sourcePath,writingDir);
        nodeReducer.writeReducedGraph(numberOfDeletions);
    }

    public void reduceNodesInSteps(String sourcePath, String writingDir, String stepSizeString){
        int stepSize = Integer.valueOf(stepSizeString);

        NodeReducer nodeReducer = new NodeReducer(sourcePath,writingDir);
        int graphSize = nodeReducer.getGraphSize();

        System.out.println("Generating " + (graphSize/stepSize) + " new files");

        for(int i = stepSize; i<graphSize;i = i + stepSize){
            nodeReducer.writeReducedGraph(i);
        }
    }

    public void reduceEdgesOnce(String sourcePath, String writingDir, String deletionPercentage){
        int deletionPercentageInt = Integer.valueOf(deletionPercentage);

        RandomEdgeRemover randomEdgeRemover = new RandomEdgeRemover();
        randomEdgeRemover.removeRandomEdges(sourcePath,writingDir,deletionPercentageInt);
    }

    public void reduceEdgesInDir(String sourceDir, String writingDir, String deletionPercentage){
        int deletionPercentageInt = Integer.valueOf(deletionPercentage);

        RandomEdgeRemover randomEdgeRemover = new RandomEdgeRemover();
        randomEdgeRemover.removeRandomEdgesFromWholeDirectory(sourceDir,writingDir,deletionPercentageInt);
    }
}
