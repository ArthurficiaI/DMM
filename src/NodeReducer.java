import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class NodeReducer {
    private Path sourcePath;
    private Path writingDir;
    private int graphSize;

    public NodeReducer(String sourcePath, String writingDir){
        this(Paths.get(sourcePath), Paths.get(writingDir));
    }

    public NodeReducer(Path sourcePath, Path writingDir){
        this.sourcePath = sourcePath;
        this.graphSize = TSPToolkit.readGraphSize(sourcePath);
        this.writingDir = writingDir;
    }


    public void writeReducedGraph(int numberOfDeletions){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(sourcePath.toString()));
            String newFileName = sourcePath.getFileName().toString();
            newFileName = newFileName.replace(".atsp","-" + (graphSize - numberOfDeletions) + ".atsp");
            newFileName = "reduced-" + newFileName;

            String fullWritingPath = writingDir.toString() + "/" + newFileName;
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fullWritingPath));
            writeBeginningOfFile(bufferedWriter,numberOfDeletions,fullWritingPath);

            int[] delPositions = determineRandomPositions(numberOfDeletions);

            List<Integer> listOfDelPosSorted = Arrays.stream(delPositions).boxed().collect(Collectors.toCollection(ArrayList::new));
            Collections.sort(listOfDelPosSorted, Collections.reverseOrder());

            //System.out.println(listOfDelPosSorted);
            System.out.println("Writing file with " + numberOfDeletions + " deletions.");

            String line;
            int pos = 0;
            while((line = bufferedReader.readLine()) != null){ //we want to delete all columns and rows in the given matrix.


                if(line.isEmpty() || Character.isLetter(line.charAt(0))){ //WORKS!
                    continue;
                }

                if(checkIfIntContained(delPositions,pos)){ //this check should delete the rows         IntStream.of(delPositions).anyMatch(x -> x == pos)
                    pos++;
                    continue;
                }

                List<String> lineList = new ArrayList<>(Arrays.asList(line.split(" "))); //has to be converted to ArrayList for some operations

                while(lineList.get(lineList.size() -1) == ""){
                    lineList.remove(lineList.size() - 1);
                }
                if(lineList.size() != graphSize){
                    System.err.println("The lines in the document do not correspond with the matrix lines, or there is another error with space split formatting");
                    System.exit(0);
                }

                for(int delIndex : listOfDelPosSorted){ //theoretically we could also write each line and omit the positions in the array, both ways have their advantages. The deletionlist has been reverse sorted, so we don't have to worry about size shrinking and moving the index
                    lineList.remove(delIndex);
                }

                TSPToolkit.writeLineWithNewNumbers(lineList, bufferedWriter);
                pos++;
            }

            bufferedReader.close();
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean checkIfIntContained(int[] array, int checkNumber){
        for(int arrayNumber : array){
            if(checkNumber == arrayNumber){
                return true;
            }
        }
        return false;
    }

    private int[] determineRandomPositions(int numberOfDeletions){
        List<Integer> restPositions = new ArrayList<>();
        for(int i = 0; i<graphSize;i++){
            restPositions.add(i);
        }

        int[] positionsToDelete = new int[numberOfDeletions];
        Random random = new Random(numberOfDeletions); //The used seed is the number of deletions, this ensures that we don't get similar deletions if we delete different amounts from the same file

        for(int i = 0; i<numberOfDeletions;i++){
            int positionInList = random.nextInt(restPositions.size());

            int actualPositionToDelete = restPositions.get(positionInList); //after a few deletions both of these positions may differ, especially for large deletions like 50% this may matter
            restPositions.remove(positionInList); //this should use the positionInList as an index instead of as an Integer Object, you'd have to use (Integer) positionInList to delete it as an object

            positionsToDelete[i] = actualPositionToDelete;
        }

        return positionsToDelete;
    }





    private void writeBeginningOfFile(BufferedWriter bufferedWriter, int numberOfDeletions, String fullWritingPath) throws IOException{

        bufferedWriter.write("NAME: " + Paths.get(fullWritingPath).getFileName().toString()); bufferedWriter.newLine();
        bufferedWriter.write("TYPE: ATSP"); bufferedWriter.newLine();
        bufferedWriter.write("COMMENT: Graph reduced from nodenumber " + graphSize + " to " + (graphSize - numberOfDeletions) + " by Arthur von Creytz"); bufferedWriter.newLine();
        bufferedWriter.write("DIMENSION: " + (graphSize - numberOfDeletions)); bufferedWriter.newLine();
        bufferedWriter.write("EDGE_WEIGHT_TYPE: EXPLICIT"); bufferedWriter.newLine();
        bufferedWriter.write("EDGE_WEIGHT_FORMAT: FULL_MATRIX"); bufferedWriter.newLine();
        bufferedWriter.write("EDGE_WEIGHT_SECTION"); //the newline will be set in the beginning of each time writing a new line, we get no trailing empty line thusly
    }

    public int getGraphSize(){
        return graphSize;
    }




}
