import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TSPToolkit {

    public static double findHighestDistance2D(List<Double> xCoords, List<Double> yCoords){
        double[] highestLowestxy = TSPToolkit.findHighestLowestCoordinates2D(xCoords,yCoords);
        double lowestx = highestLowestxy[2];
        double lowesty = highestLowestxy[3];
        double highestx = highestLowestxy[0];
        double highesty = highestLowestxy[1];

        return 5 * TSPToolkit.calculateDistance2D(lowestx,lowesty,highestx, highesty);
    }

    public static double[] findHighestLowestCoordinates2D(List<Double> xCoords, List<Double> yCoords){
        double highestx = xCoords.get(0);
        double highesty = yCoords.get(0);
        double lowestx = xCoords.get(0);
        double lowesty = yCoords.get(0);

        for(int i = 0; i < xCoords.size(); i++){
            double currentX = xCoords.get(i);
            double currentY = yCoords.get(i);

            if(currentX < lowestx){
                lowestx = currentX;
            } else if (currentX > highestx) {
                highestx = currentX;
            }

            if(currentY < lowesty){
                lowesty = currentY;
            } else if (currentY > highesty) {
                highesty = currentY;
            }
        }

        double[] result = new double[4];
        result[0] = highestx; result[1] = highesty; result[2] = lowestx; result[3] = lowesty;
        return result;

    }

    public static double findHighestDistance3D(List<Double> xCoords, List<Double> yCoords, List<Double> zCoords){
        double highestx = xCoords.get(0);
        double highesty = yCoords.get(0);
        double lowestx = xCoords.get(0);
        double lowesty = yCoords.get(0);

        double highestz = zCoords.get(0);
        double lowestz = zCoords.get(0);

        for(int i = 0; i < xCoords.size(); i++){
            double currentX = xCoords.get(i);
            double currentY = yCoords.get(i);

            double currentZ = zCoords.get(i);

            if(currentX < lowestx){
                lowestx = currentX;
            } else if (currentX > highestx) {
                highestx = currentX;
            }

            if(currentY < lowesty){
                lowesty = currentY;
            } else if (currentY > highesty) {
                highesty = currentY;
            }

            if(currentZ < lowestz){
                lowestz = currentZ;
            } else if(currentZ > highestz){
                highestz = currentZ;
            }
        }

        return 5 * TSPToolkit.calculateDistance3D(lowestx,lowesty,lowestz,highestx, highesty,highestz);
    }


    public static double calculateDistance2D(double xCoord1, double yCoord1, double xCoord2, double yCoord2){
        double horizontalDistance = xCoord1 - xCoord2;
        double verticalDistance = yCoord1 - yCoord2;
        double fullDistance = Math.sqrt((Math.pow(horizontalDistance,2) + Math.pow(verticalDistance,2)));
        return fullDistance;
    }

    public static double calculateDistance3D(double xCoord1, double yCoord1, double zCoord1, double xCoord2, double yCoord2,double zCoord2){
        double horizontalDistance = xCoord1 - xCoord2;
        double verticalDistance = yCoord1 - yCoord2;
        double realVerticalDistance = zCoord1 - zCoord2; //order doesn't matter as it gets squared anyway, so don't sweat the minus
        double fullDistance = Math.sqrt(Math.pow(horizontalDistance,2) + Math.pow(verticalDistance,2) + Math.pow(realVerticalDistance,2));

        return fullDistance;
    }

    public static void printMatrix(List<List<Double>> distanceMatrix){
        for(List<Double> lineList : distanceMatrix){
            System.out.println(matrixLineToString(lineList));
        }
    }


    public static String matrixLineToString(List<Double> matrixLine){
        StringBuilder stringBuilder = new StringBuilder();
        for(double distance : matrixLine){
            stringBuilder.append(distance).append(" ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    public static void printCoordinates(List<Double> xCoords, List<Double> yCoords, List<Double> zCoords){
        System.out.println("\n\nCOORDINATE SECTION:");
        for(int i = 0;i<xCoords.size();i++){
            System.out.println(xCoords.get(i) + " " + yCoords.get(i) + " " + zCoords.get(i));
        }
        System.out.println();
    }

    public static int readGraphSize(Path sourcePath){
        int graphSize = 0;
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(sourcePath.toString()));
            String line = bufferedReader.readLine();

            while(!line.contains("DIMENSION")){
                line  = bufferedReader.readLine();
            }
            String[] splitLine = line.split(" ");
            graphSize = Integer.valueOf(splitLine[splitLine.length - 1]);

        }catch (Exception e){
            System.err.println("Exception while trying to read the size of the Graph");
            e.printStackTrace();
            System.exit(0);
        }
        return graphSize;
    }


    public static void writeLineWithNewNumbers(List<String> stringList, BufferedWriter bufferedWriter) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for(String number : stringList){
            stringBuilder.append(number).append(" ");
        }
        if(!stringBuilder.isEmpty()){
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }



        bufferedWriter.newLine();
        bufferedWriter.write(stringBuilder.toString());

    }

    public static void scaleMatrix(Path problemPath, Path writingDirPath, double dividingFactor){
        try{
            String problemPathString = problemPath.toString();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(problemPathString));

            String fileSuffix = "_smaller.atsp";
            if(dividingFactor < 1){
                fileSuffix = "_bigger.atsp";
            }

            String writeFileName = problemPathString.replace(".atsp",fileSuffix);
            Path writeFilePath = writingDirPath.resolve(writeFileName);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writeFilePath.toString()));

            int edgeAmountSetToOne = 0;

            String line;
            while((line = bufferedReader.readLine()) != null){
                if (Character.isLetter(line.charAt(0))) { //WORKS!
                    bufferedWriter.write(line); bufferedWriter.newLine();
                    continue;
                }
                String[] edgeRow = line.split(" ");
                List<String> newEdgeRow = new ArrayList<>();
                for(String edgeString : edgeRow){
                    if(edgeString.length() > 0){
                        double edge = Double.valueOf(edgeString);
                        edge = edge/dividingFactor;

                        if(edge < 1){ //anything below 1 gets interpreted as a 0 by LKH, so we make sure that the smallest edges have a value of 1
                            edgeAmountSetToOne++;
                            edge = 1;
                        }

                        newEdgeRow.add(String.valueOf(edge));
                    }
                }
                writeLineWithNewNumbers(newEdgeRow, bufferedWriter);
            }

            System.out.println(edgeAmountSetToOne + "edges were set to one");

            bufferedWriter.close();
            bufferedReader.close();

        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void roundMatrix(Path problemPath, Path writingDirPath){ //this exists to circumvent rounding differences from different programs
        try{
            String problemPathString = problemPath.toString();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(problemPathString));

            String fileSuffix = "_rounded.atsp";

            String writeFileName = problemPathString.replace(".atsp",fileSuffix);
            Path writeFilePath = writingDirPath.resolve(writeFileName);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writeFilePath.toString()));

            int edgeAmountSetToOne = 0;

            String line;
            while((line = bufferedReader.readLine()) != null){
                if (line.isEmpty() || Character.isLetter(line.charAt(0))) { //WORKS!
                    bufferedWriter.write(line); bufferedWriter.newLine();
                    continue;
                }
                String[] edgeRow = line.split(" ");
                List<String> newEdgeRow = new ArrayList<>();
                for(String edgeString : edgeRow){
                    if(edgeString.length() > 0){
                        int edge = Double.valueOf(edgeString).intValue(); //truncates, so it rounds down

                        if(edge < 1){ //anything below 1 gets interpreted as a 0 by LKH, so we make sure that the smallest edges have a value of 1
                            edgeAmountSetToOne++;
                            edge = 1;
                        }

                        newEdgeRow.add(String.valueOf(edge));
                    }
                }
                writeLineWithNewNumbers(newEdgeRow, bufferedWriter);
            }

            System.out.println(edgeAmountSetToOne + "edges were set to one");

            bufferedWriter.close();
            bufferedReader.close();

        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }


    //public static void write

}
