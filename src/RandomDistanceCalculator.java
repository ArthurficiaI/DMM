import java.util.ArrayList;
import java.util.List;

public class RandomDistanceCalculator {

    private List<Double> xCoords;
    private List<Double> yCoords;
    private List<List<Double>> distanceMatrix;
    private double overHighestDistance;

    private static final double stddevFraction = 10; //the number by which the actualDistance is divided to calculate the stddev

    public RandomDistanceCalculator(List<Double> xCoords, List<Double> yCoords){
        this.xCoords = xCoords;
        this.yCoords = yCoords;
        this.overHighestDistance = TSPToolkit.findHighestDistance2D(xCoords,yCoords);
        generateDistanceMatrix();
    }

    private void generateDistanceMatrix(){
        this.distanceMatrix = new ArrayList<List<Double>>();
        java.util.Random r = new java.util.Random();
        for(int i = 0; i<xCoords.size();i++){
            List<Double> currentDistanceLine = new ArrayList<>();
            double currentX = xCoords.get(i);
            double currentY = yCoords.get(i);

            for(int j = 0; j<xCoords.size();j++){
                if(i == j){
                    currentDistanceLine.add(overHighestDistance);
                }else{
                    double actualDistance = TSPToolkit.calculateDistance2D(currentX,currentY,xCoords.get(j),yCoords.get(j));
                    double noisedDistance = r.nextGaussian(actualDistance,actualDistance/10);
                    currentDistanceLine.add(noisedDistance);
                }
            }
            distanceMatrix.add(currentDistanceLine);
        }
    }


}
