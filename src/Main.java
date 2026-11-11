import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Main {


    public static void main(String[] args) {
        ATSPController atspController = new ATSPController();
        try{
            String modeArg = args[0];

            switch(modeArg){
                case "convertAndParam": //tested
                    atspController.convertAndWriteParams(args[1], args[2], args[3], args[4]); //String problemPath,  String newATSPProblem, String writeNewParamPath, String LKHPath
                    break;

                case "convert": //tested
                    atspController.convertTSPtoATSP(args[1],args[2]);  //String problemPath,  String writeNewProblemPath
                    break;

                case "convertDir": //tested
                    atspController.convertTSPDirToATSPDir(args[1],args[2]); //String sourceDir, String atspDir
                    break;

                case "convertDirAndParam": //tested
                    atspController.convertTSPDirToATSPDirWithParams(args[1], args[2], args[3], args[4]); //String sourceDir, String atspDir, String writeNewParamPath, String LKHPath
                    break;

                case "specificParams":
                    atspController.specificParams(args[1], args[2], args[3], args[4]);
                    break;

                case "specificParamsDir":
                    atspController.specificParamsDir(args[1], args[2], args[3], args[4]);
                    break;

                case "param": //tested
                    atspController.writeParams(args[1],args[2],args[3]); //String atspProblemPath, String writeNewParamPath, String LKHPath
                    break;

                case "paramFromDir": //tested
                    atspController.writeParamsFromDir(args[1], args[2], args[3]); //String atspProblemDir, String writeNewParamPath, String LKHPath
                    break;

                case "reduceNodes": //tested
                    atspController.reduceNodesOnce(args[1], args[2], args[3]); //String sourcePath, String writingDir, String numberOfDeletionsString
                    break;

                case "reduceNodesInSteps": //tested
                    atspController.reduceNodesInSteps(args[1], args[2], args[3]); //String sourcePath, String writingDir, String stepSizeString
                    break;

                case "reduceEdges": //tested
                    atspController.reduceEdgesOnce(args[1],args[2],args[3]); //String sourcePath, String writingDir, String deletionPercentage
                    break;

                case "reduceEdgesInDir": //tested
                    atspController.reduceEdgesInDir(args[1], args[2], args[3]); //String sourceDir, String writingDir, String deletionPercentage
                    break;

                case "scaleMatrixDown": //tested
                    atspController.scaleMatrixDown(args[1],args[2],args[3]); //String problemPath, String writingDirPath, String factor
                    break;

                case "scaleMatrixUp": //tested
                    atspController.scaleMatrixUp(args[1], args[2], args[3]); //String problemPath, String writingDirPath, String factor
                    break;

                case "writeParamsGivenSeedAndTourPath"://tested
                    atspController.writeParamsGivenSeedAndTourPath(args[1], args[2], args[3], args[4], args[5]);
                    break;

                case "roundWeightsDown": //tested
                    if(args.length>2){
                        atspController.roundMatrix(args[1],args[2]);
                    } else{
                        atspController.roundMatrix(args[1]);
                    }
                    break;


                default:
                    printExplainingMessage();
                    break;
            }

        }catch (ArrayIndexOutOfBoundsException e){
            printExplainingMessage();
        }

        String fna52057 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\testingHighest\\asymsynth_52057_bigger_rounded.atsp";
        String atspDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\testingHighest\\";

        atspController.reduceEdgesOnce(fna52057,atspDir,"20");

/*
        String fna52057 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\testingHighest\\asymsynth_52057_bigger.atsp";
        String asymsynth_237 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\Rounding\\atsp\\asymsynth_237.atsp";
        String atspDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\Rounding\\atsp";
        atspController.roundMatrix(asymsynth_237, atspDir);
        atspController.roundMatrix(fna52057);

 */

        /*
        String atspFile = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\seedAndTOur\\ATSP\\asymsynth_237.atsp";
        String atspDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\seedAndTOur\\ATSP";
        String paramDest = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\seedAndTOur\\Params";
        String LKHDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\seedAndTOur";
        String tourDest = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\seedAndTOur\\Tours";

        String fna52057 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\testingHighest\\asymsynth_52057.atsp";
        String bigDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\testingHighest";


        atspController.writeParamsGivenSeedAndTourPath(paramDest,atspFile,LKHDir,"65",tourDest);

        atspController.scaleMatrixUp(atspFile, atspDir,"5");
        atspController.scaleMatrixDown(atspFile,atspDir,"5");

        atspController.scaleMatrixUp(fna52057,bigDir,"5");
        */


        /*
        String smolExpATSP = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\RemovingEdges\\SmolExperiment\\asymsynth_10.atsp";
        RandomEdgeRemover randomEdgeRemover = new RandomEdgeRemover();
        //randomEdgeRemover.getBiggestEdge(Paths.get(smolExpATSP),10);

        String fna52057 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\testingHighest\\asymsynth_52057.atsp";
        randomEdgeRemover.removeRandomEdges(fna52057,"C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\testingHighest\\",0);*/

/*
        String bigATSP10000 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\RemovingEdges\\BigExperiment\\BIG_ATSP\\asymsynth_10150.atsp";
        String bigATSP10000Dir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\RemovingEdges\\BigExperiment\\BIG_ATSP";

        //atspController.reduceEdgesOnce(bigATSP10000,bigATSP10000Dir,"50");


        String reduceSourceDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\RemovingEdges\\BigExperiment\\ATSP";
        String reduceWritingDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\RemovingEdges\\BigExperiment\\ATSP_ReducedTo20";
        String multipeParamPath = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\RemovingEdges\\BigExperiment\\Paramfiles";

        String LKHPath = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\Original Source Code\\LKH-Relaxation-Code\\lkh-relaxation-main\\lkh-relaxation-main\\algorithms\\LKH-3.0.8";


        String multipleTSPDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\multiple_SLOPED_ME\\TSP";
        String multipleATSPDir = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\multiple_SLOPED_ME\\ATSP";
        String paramPath = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\multiple_SLOPED_ME\\Params";

        String smolExpATSP = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\RemovingEdges\\SmolExperiment\\asymsynth_10.atsp";
        String writingReducedEdgesSmol = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\RemovingEdges\\SmolExperiment\\reduced";


        //String fakeLKHPath = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments";

        //atspController.convertTSPDirToATSPDirWithParams(multipleTSPDir,multipleATSPDir,paramPath,LKHPath);

        String singleTestTSP = multipleTSPDir + "\\bcl380.tsp";

        //atspController.convertAndWriteParams(singleTestTSP,multipleATSPDir,paramPath,LKHPath);

        //atspController.reduceNodesInSteps(reduceSourcePath,reduceWritingDir,"5000");

        //atspController.reduceEdgesInDir(reduceSourceDir,reduceWritingDir,"99");
        //atspController.writeParamsFromDir(reduceWritingDir,multipeParamPath,LKHPath);
        atspController.reduceEdgesOnce(smolExpATSP,writingReducedEdgesSmol,"20");
*/


/*
        String path = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\Original Source Code\\LKH-Relaxation-Code\\lkh-relaxation-main\\lkh-relaxation-main\\parameterFiles\\ALPHA_5_20230724-151153\\vlsi_small_bcl380.par";

        String path1 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\Original Source Code\\LKH-Relaxation-Code\\lkh-relaxation-main\\lkh-relaxation-main\\parameterFiles\\ALPHA_5_20230724-151153\\vlsi_small_xqf131.par";
        //String pathBig744710 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\Original Source Code\\LKH-Relaxation-Code\\lkh-relaxation-main\\lkh-relaxation-main\\parameterFiles\\vlsi\\lrb744710.tsp";
        String pathBig52000 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\Original Source Code\\LKH-Relaxation-Code\\lkh-relaxation-main\\lkh-relaxation-main\\parameterFiles\\vlsi\\fna52057.tsp";
        String path19402 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\Original Source Code\\LKH-Relaxation-Code\\lkh-relaxation-main\\lkh-relaxation-main\\parameterFiles\\vlsi\\fnc19402.tsp";
        String ownPath0 = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\Original Source Code\\LKH-Relaxation-Code\\lkh-relaxation-main\\lkh-relaxation-main\\parameterFiles\\myOwnProblems\\10shortTest.txt";

        TSPReader reader = new TSPReader(path);
        RandomDistanceCalculator randomDistanceCalculator = new RandomDistanceCalculator(reader.getxCoordinates(), reader.getyCoordinates());
        randomDistanceCalculator.printMatrix();


        String writingPath = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\Sloped_ME\\";
        String writingParamPath = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\experiments\\Sloped_ME\\params";
        String localLKHPath = "C:\\Users\\dafwe\\OneDrive\\Desktop\\Unistuff\\Master\\Masterarbeit\\Original Source Code\\LKH-Relaxation-Code\\lkh-relaxation-main\\lkh-relaxation-main\\algorithms\\LKH-3.0.8";
        

        TSPReader reader = new TSPReader(ownPath0);
        SlopeDistanceMatrixMaker slopeDistanceMatrixMaker = new SlopeDistanceMatrixMaker(reader.getxCoordinates(), reader.getyCoordinates());


        slopeDistanceMatrixMaker.generateAndWriteSlopedDistanceMatrix(writingPath);
        //TSPToolkit.printMatrix(slopeDistanceMatrixMaker.getDistanceMatrix());

        ParamfileWriter paramfileWriter = new ParamfileWriter();
        //paramfileWriter.writeATSPParamFile(writingParamPath,writingPath,localLKHPath);
        //slopeDistanceMatrixMaker.calcAndPrintOldMatrix();
        //slopeDistanceMatrixMaker.printCoordinates();
        */

    }

    public static void printExplainingMessage(){
        System.out.println("\n The callable functions are: \n" +
                "convertAndParam (String problemPath,  String newATSPProblem, String writeNewParamPathDir, String LKHPath)\n" +
                "convert (String problemPath,  String writeNewProblemPath)\n" +
                "convertDir(String sourceDir, String atspDir)\n" +
                "convertDirAndParam(String sourceDir, String atspDir, String writeNewParamPath, String LKHPath)\n" +
                "specificParams(String atspProblemPath, String writeNewParamPath, String LKHPath, String basicParamReadPath)\n" +
                "specificParamsDir(String atspProblemDir, String writeNewParamPath, String LKHPath, String basicParamReadPath)\n" +
                "param (String atspProblemPath, String writeNewParamPath, String LKHPath)\n" +
                "paramFromDir(String atspProblemDir, String writeNewParamPath, String LKHPath)\n" +
                "reduceNodes (String sourcePath, String writingDir, String numberOfDeletionsString)\n" +
                "reduceNodesInSteps (String sourcePath, String writingDir, String stepSizeString)\n" +
                "reduceEdges(String sourcePath, String writingDir, String deletionPercentageInt)\n" +
                "reduceEdgesInDir(String sourceDir, String writingDir, String deletionPercentageInt)\n" +
                "scaleMatrixDown(String problemPath, String writingDirPath, String factor)\n" +
                "scaleMatrixUp(String problemPath, String writingDirPath, String factor)\n" +
                "writeParamsGivenSeedAndTourPath(String writeNewParamPath, String atspProblemPath, String LKHPath, String seed, String writeTourPath)");
    }
}
