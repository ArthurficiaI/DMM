import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class RandomEdgeRemover {

    private static final double SECURITY_FACTOR = 1; //It was 5 before, but that lead to complications with the size of the barrierValue and currently we have to keep it as little as possible as we cannot factor down the edges too much

    public void removeRandomEdges(String sourceFile, String writingDir, int percentageRemoved){
        removeRandomEdges(Paths.get(sourceFile),Paths.get(writingDir),percentageRemoved);
    }

    public void removeRandomEdgesFromWholeDirectory(String sourceDir, String writingDir, int percentageRemoved){
        removeRandomEdgesFromWholeDirectory(Paths.get(sourceDir),Paths.get(writingDir),percentageRemoved);
    }

    public void removeRandomEdgesFromWholeDirectory(Path sourceDir, Path writingDir, int percentageRemoved){
        File sDir = new File(sourceDir.toString());

        if(sDir.isDirectory()){
            File[] sourceFiles = sDir.listFiles();
            for(File sourceFile : sourceFiles){
                if(sourceFile.isFile()){
                    String sourcePath = sourceFile.getPath();
                    removeRandomEdges(sourcePath,writingDir.toString(),percentageRemoved);
                }
            }
        }else{
            System.err.println("The given sourcePath is not to a directory!");
        }
    }

    public void removeRandomEdges(Path sourceFile, Path writingDir, int percentageRemoved){
        String writingFileName = sourceFile.getFileName().toString();
        int percentageLeft = 100 - percentageRemoved;
        writingFileName = writingFileName.replace(".atsp",("_at" + percentageLeft + "Percent.atsp"));
        String writingPath = writingDir.resolve(writingFileName).toString();

        int graphSize = TSPToolkit.readGraphSize(sourceFile);

        String barrierValue = calcWeakerBarrierValueString(sourceFile,graphSize); //using weaker version to have less of a number explosion
        System.out.println("Current barrierValue: " + barrierValue);

        Random random = new Random((long)graphSize * percentageRemoved);
        double chanceOfRemoval = calcTruePercentage(graphSize,percentageRemoved);

        int currentLine = 0;
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile.toString()));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writingPath));

            List<int[]> safeEdges = generateMinimalRandomTour(graphSize,random);

            String tourString = secureTourToString(safeEdges);
            writeBeginningOfFile(bufferedWriter, writingPath, graphSize, percentageRemoved, tourString);

            sortEdgeList(safeEdges);

            String line;
            while((line = bufferedReader.readLine()) != null) {

                if (line.isEmpty() || Character.isLetter(line.charAt(0))) { //WORKS!
                    continue;
                }

                List<String> edgeList = new ArrayList<>(Arrays.asList(line.split(" "))); //has to be converted to ArrayList for some operations

                while(edgeList.get(edgeList.size() -1) == ""){
                    edgeList.remove(edgeList.size() - 1);
                }
                if(edgeList.size() != graphSize){
                    System.err.println("The lines in the document do not correspond with the matrix lines, or there is another error with space split formatting");
                    System.exit(0);
                }


                for(int currentColumn = 0; currentColumn<graphSize;currentColumn++){

                    if(!shouldEdgeBeKeptSorted(safeEdges,currentLine,currentColumn)){
                        if(random.nextDouble() < chanceOfRemoval){ //nextdouble is of the form [0,1)

                            edgeList.set(currentColumn,barrierValue);
                        }
                    }
                }
                TSPToolkit.writeLineWithNewNumbers(edgeList,bufferedWriter);
                currentLine++;
            }
            bufferedReader.close();
            bufferedWriter.close();
        }catch(IOException e){
               e.printStackTrace();
               System.exit(0);
        }
    }

    private List<int[]> generateMinimalRandomTour(int graphSize, Random random){
        List<int[]> keepEdges = new ArrayList<>();

        List<Integer> positionsLeft = new ArrayList<>(graphSize);
        for(int i = 0; i<graphSize;i++){
            positionsLeft.add(i);
        }
        System.out.println("Size of the list positionsLeft: " + positionsLeft.size());


        int position1 = 0;
        positionsLeft.remove(0);
        int position2;
        for(int i = 0; i<graphSize;i++){
            int[] edge = new int[2];

            if(i == graphSize-1){
                position2 = 0;
            }else{
                int indexInLeftPositions = random.nextInt(positionsLeft.size());
                position2 = positionsLeft.remove(indexInLeftPositions);
            }

            edge[0] = position1;
            edge[1] = position2;
            keepEdges.add(edge);

            position1 = position2;
        }

        return keepEdges;
    }

    private double calcTruePercentage(int graphSize, int removePercentageInt){ //since some of the edges are undeleteable, we have to up the deletion odds a bit to still delete about the right percentage, this is especially relevant for smaller examples
        double percentage = removePercentageInt/100.0;
        double reducedGraphSize = Math.pow(graphSize,2) - graphSize;

        double approximateCorrectRemoveSize = Math.pow(graphSize,2) * percentage;
        double adjustedPercentage = approximateCorrectRemoveSize / reducedGraphSize; //for graphsize 100 (10 edges) and removepercentage 50, this would be 55.55555 (or 5/9)

        return adjustedPercentage;
    }

    private boolean shouldEdgeBeKept(List<int[]> keepEdges, int edgePart1, int edgePart2){
        int[] edge = {edgePart1,edgePart2};
        return shouldEdgeBeKept(keepEdges,edge);
    }

    private boolean shouldEdgeBeKept(List<int[]> keepEdges, int[] edge){
        for(int i = 0; i< keepEdges.size(); i++){
            int[] currentComparisonEdge = keepEdges.get(i);
            if(currentComparisonEdge[0] == edge[0] && currentComparisonEdge[1] == edge[1]){
                keepEdges.remove(i);
                return true;
            }
        }
        return false;
    }

    private boolean shouldEdgeBeKeptSorted(List<int[]> keepEdges, int edgePart1, int edgePart2){
        if(keepEdges.isEmpty()){
            return false;
        }

        if(keepEdges.get(0)[0] == edgePart1 && keepEdges.get(0)[1] == edgePart2 ){
            keepEdges.remove(0);
            return true;
        }
        return false;
    }

    private void sortEdgeList(List<int[]> edges){
        Collections.sort(edges, (edge1, edge2) -> {
            if(edge1[0] < edge2[0]){
                return -1;
            }
            if(edge2[0] < edge1[0]){
                return 1;
            }

            if(edge1[1] < edge2[1]){
                return -1;
            }
            if(edge2[1] < edge1[1]){
                return 1;
            }

            return 0;
        });
    }

    private void writeBeginningOfFile(BufferedWriter bufferedWriter, String fullWritingPath, int graphSize, int removePercent, String secureTourString) throws IOException{
        bufferedWriter.write("NAME: " + Paths.get(fullWritingPath).getFileName().toString()); bufferedWriter.newLine();
        bufferedWriter.write("TYPE: ATSP"); bufferedWriter.newLine();
        bufferedWriter.write("COMMENT: Graph with "+ removePercent +"% of the edges removed by Arthur von Creytz"); bufferedWriter.newLine();
        bufferedWriter.write("COMMENT: Secured Undeleted Tour: " + secureTourString); bufferedWriter.newLine();
        bufferedWriter.write("DIMENSION: " + graphSize); bufferedWriter.newLine();
        bufferedWriter.write("EDGE_WEIGHT_TYPE: EXPLICIT"); bufferedWriter.newLine();
        bufferedWriter.write("EDGE_WEIGHT_FORMAT: FULL_MATRIX"); bufferedWriter.newLine();
        bufferedWriter.write("EDGE_WEIGHT_SECTION"); //the newline will be set in the beginning of each time writing a new line, we get no trailing empty line thusly
    }

    private String secureTourToString(List<int[]> secureTour){
        StringBuilder stringBuilder = new StringBuilder();
        for(int[] edge : secureTour){
            stringBuilder.append(edge[0]).append("-");
        }
        return stringBuilder.toString().substring(0,stringBuilder.length()-1);
        //return stringBuilder.toString().substring(0,stringBuilder.length()-2);
    }

    private String calcBarrierValueString(Path sourceFile, int graphSize){ //int should be fine as a datatype as the biggest relevant value is a good amount below int_max
        double biggestEdgeValue = getBiggestEdge(sourceFile,graphSize);

        int barrierValue = (int) (biggestEdgeValue * graphSize * SECURITY_FACTOR); //intuition is that it should always be more worth to go around the whole graph than to take a barrier as a route

        /* Chose to remove this feature to keep the relationship between edges and barrierValue more stable
        if(barrierValue<999999){
            barrierValue = 999999; //this value should never make any problems, so if we can easily afford it, we can raise our value up to it
        }*/

        return String.valueOf(barrierValue);
    }

    private String calcWeakerBarrierValueString(Path sourceFile, int graphSize){ //The other calculationmethod was way too big for LKH to handle, so we had to size it down by a lot
        double biggestEdgeWeight = getBiggestEdge(sourceFile, graphSize);

        int barrierValue = (int) (biggestEdgeWeight * 500);
        return String.valueOf(barrierValue);
    }


    private double getBiggestEdge(Path sourceFile, int graphSize){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile.toString()));
            String line;
            int currentLine = 0;
            double biggestEdge = 0;
            double smallestEdge = 9999999;
            while((line = bufferedReader.readLine()) != null) {

                if (line.isEmpty() || Character.isLetter(line.charAt(0))) { //WORKS!
                    continue;
                }
                List<String> edgeList = new ArrayList<>(Arrays.asList(line.split(" ")));

                for(int currentColumn = 0; currentColumn<graphSize;currentColumn++){
                    double currentValue = Double.valueOf(edgeList.get(currentColumn));

                    if(currentValue > biggestEdge && currentLine!=currentColumn){
                        biggestEdge = currentValue;
                    }
                    if(currentValue < smallestEdge){
                        smallestEdge = currentValue;
                    }
                }
                currentLine++;
            }
            //System.out.println("Biggest edge value is: " + biggestEdge);
            System.out.println("Smallest edge value is: " + smallestEdge);



            return biggestEdge;
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
        return 0;
    }


}
