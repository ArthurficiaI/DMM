import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlopeDistanceMatrixMaker {
    private List<Double> xCoords;
    private List<Double> yCoords;

    private List<Double> zCoords;

    //private List<List<Double>> distanceMatrix;
    private double overHighestDistance;


    private static final double maxSlopeFactor = 2;  //The factor that gets applied for a complete 90 degree slope, be careful with high factors as they might allow shortcuts


    public SlopeDistanceMatrixMaker(List<Double> xCoords, List<Double> yCoords){
        this.xCoords = xCoords;
        this.yCoords = yCoords;

        this.zCoords = makezCoords();

        this.overHighestDistance = maxSlopeFactor * TSPToolkit.findHighestDistance3D(xCoords,yCoords,zCoords);

    }

    public void generateSlopedDistanceMatrix(){
        //this.distanceMatrix = new ArrayList<List<Double>>();

        for(int i = 0; i<xCoords.size();i++){
            List<Double> currentDistanceLine = new ArrayList<>();
            double currentX = xCoords.get(i);
            double currentY = yCoords.get(i);
            double currentZ = zCoords.get(i);

            for(int j = 0; j<xCoords.size();j++){
                if(i == j){
                    currentDistanceLine.add(overHighestDistance);
                }else{
                    double normalDistance = TSPToolkit.calculateDistance3D(currentX,currentY,currentZ,xCoords.get(j),yCoords.get(j),zCoords.get(j));

                    double run = TSPToolkit.calculateDistance2D(currentX,currentY,xCoords.get(j),yCoords.get(j));
                    double rise = Math.abs(currentZ - zCoords.get(j));

                    double angle = Math.toDegrees(Math.atan(rise/run));


                    double slopeFactor = (angle / 90) * (maxSlopeFactor -1) + 1;
                    if(currentZ > zCoords.get(j)){ //if our current position is higher, then we divide to make the cost of going downhill smaller
                        slopeFactor = 1/slopeFactor;
                    }
                    //System.out.println("angel:" + angle +"          run:" + run + "                 rise:" + rise + "      factor:" + slopeFactor);

                    double slopedDistance = normalDistance * slopeFactor;

                    currentDistanceLine.add(slopedDistance);
                }
            }
            //TSPToolkit.printMatrixLine(currentDistanceLine);

            //distanceMatrix.add(currentDistanceLine);
        }
    }

    public String generateAndWriteSlopedDistanceMatrix(String writingPath){
        String problemName = "asymsynth_" + xCoords.size();
        String completeWritingPath = Paths.get(writingPath).resolve(problemName) + ".atsp";
        try {


            File file = new File(completeWritingPath);
            if(!file.exists()){
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(completeWritingPath));
                bufferedWriter.write("NAME: " + problemName); bufferedWriter.newLine();
                bufferedWriter.write("TYPE: ATSP"); bufferedWriter.newLine();
                bufferedWriter.write("COMMENT: Synthesized through a symmetric TSP file by Arthur von Creytz"); bufferedWriter.newLine();
                bufferedWriter.write("DIMENSION: " + xCoords.size()); bufferedWriter.newLine();
                bufferedWriter.write("EDGE_WEIGHT_TYPE: EXPLICIT"); bufferedWriter.newLine();
                bufferedWriter.write("EDGE_WEIGHT_FORMAT: FULL_MATRIX"); bufferedWriter.newLine();
                bufferedWriter.write("EDGE_WEIGHT_SECTION");


                for(int i = 0; i<xCoords.size();i++) {
                    List<Double> currentDistanceLine = new ArrayList<>();
                    double currentX = xCoords.get(i);
                    double currentY = yCoords.get(i);
                    double currentZ = zCoords.get(i);

                    for (int j = 0; j < xCoords.size(); j++) {
                        if (i == j) {
                            currentDistanceLine.add(overHighestDistance);
                        } else {
                            double normalDistance = TSPToolkit.calculateDistance3D(currentX, currentY, currentZ, xCoords.get(j), yCoords.get(j), zCoords.get(j));

                            double run = TSPToolkit.calculateDistance2D(currentX, currentY, xCoords.get(j), yCoords.get(j));
                            double rise = Math.abs(currentZ - zCoords.get(j));

                            double angle = Math.toDegrees(Math.atan(rise / run));


                            double slopeFactor = (angle / 90) * (maxSlopeFactor - 1) + 1;
                            if (currentZ > zCoords.get(j)) { //if our current position is higher, then we divide to make the cost of going downhill smaller
                                slopeFactor = 1 / slopeFactor;
                            }
                            //System.out.println("angel:" + angle +"          run:" + run + "                 rise:" + rise + "      factor:" + slopeFactor);

                            double slopedDistance = normalDistance * slopeFactor;

                            currentDistanceLine.add(slopedDistance);
                        }
                    }
                    String currentDistanceLineString = TSPToolkit.matrixLineToString(currentDistanceLine);

                    bufferedWriter.newLine();
                    bufferedWriter.write(currentDistanceLineString);
                }
                bufferedWriter.close();
            }else{
                System.err.println("Found a file with the same name, that you wanted to write. The existing file will not be overwritten.");
            }
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
        return problemName;
    }

    private List<Double> makezCoords(){
        List<Double> zCoords = new ArrayList<>();

        double[] zCoordHighestLowest = findHighestAndLowestzCoords();
        double highestZ = zCoordHighestLowest[0];
        double lowestZ = zCoordHighestLowest[1];

        Random r  = new Random(yCoords.size());
        for(int i = 0; i<yCoords.size();i++){
            double newZ = r.nextDouble(highestZ - lowestZ) + lowestZ;
            zCoords.add(newZ);
        }

        return zCoords;
    }

    private double[] findHighestAndLowestzCoords(){
        double[] highestlowestZ = new double[2];
        double[] highestLowestxy = TSPToolkit.findHighestLowestCoordinates2D(xCoords,yCoords);

        highestlowestZ[0] = (highestLowestxy[0] + highestLowestxy[1]) / 2;
        highestlowestZ[1] = (highestLowestxy[2] + highestLowestxy[3]) / 2;

        return highestlowestZ;
    }


    public void printCoordinates(){
        TSPToolkit.printCoordinates(xCoords,yCoords,zCoords);
    }

    public void calcAndPrintOldMatrix(){
        List<List<Double>> old2DDistanceMatrix = new ArrayList<List<Double>>();

        for(int i = 0; i<xCoords.size();i++){
            List<Double> currentDistanceLine = new ArrayList<>();
            double currentX = xCoords.get(i);
            double currentY = yCoords.get(i);

            for(int j = 0; j<xCoords.size();j++){
                if(i == j){
                    currentDistanceLine.add(overHighestDistance);
                }else{
                    double normalDistance = TSPToolkit.calculateDistance2D(currentX,currentY,xCoords.get(j),yCoords.get(j));
                    currentDistanceLine.add(normalDistance);
                }
            }
            old2DDistanceMatrix.add(currentDistanceLine);
        }
        System.out.println("OLD MATRIX:");
        TSPToolkit.printMatrix(old2DDistanceMatrix);
    }

}
