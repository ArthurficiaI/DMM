public class PathMaker {

    public static String deleteXPrePaths(int numberOfDeletions, String path){
        String[] pathArray = pathToArray(path);
        String[] cleanedPathArray = new String[pathArray.length - numberOfDeletions];
        for(int i = 0; i< cleanedPathArray.length; i++){
            cleanedPathArray[i] = pathArray[i + numberOfDeletions];
        }


        return arrayToPath(cleanedPathArray);
    }

    public static String deleteXPostPaths(int numberOfDeletions, String path){
        String[] pathArray = pathToArray(path);
        String[] cleanedPathArray = new String[pathArray.length - numberOfDeletions];

        for(int i = 0; i < cleanedPathArray.length; i++){
            cleanedPathArray[i] = pathArray[i];
        }

        return arrayToPath(cleanedPathArray);
    }

    public static String[] pathToArray(String path){
        return path.split("\\\\");
    }

    public static int findNumberOfReverts(String path){
        return path.split("[.][.]\\\\", -1).length -1; //Slightly illegal but otherwise I get a double string if I put a -1 to the very end of the calc, normally there should be one
    }

    public static String convertSlashesToBackslashes(String path){
        if(path.contains("/")){
            return path.replaceAll("/","\\\\");
        }
        System.err.println("No front slash found in: " + path);
        return path;
    }

    public static String arrayToPath(String[] pathArray){
        StringBuilder stringbuilder = new StringBuilder();
        for(int i = 0; i< pathArray.length;i++){
            if(pathArray[i].length() != 0){
                stringbuilder.append(pathArray[i]).append("\\");
            }
        }

        return removeEndSlash(stringbuilder.toString());
    }

    public static String removeEndSlash(String path){
        if(path.charAt(path.length() - 1) == '\\'){
            return path.substring(0,path.length() -1);
        }
        return path;
    }

    public static String[] fuseTwoPathArrays(String[] pathStart, String[] pathEnd){
        String[] fullPath = new String[pathEnd.length + pathStart.length];

        for(int i = 0; i< pathStart.length; i++){
            fullPath[i] = pathStart[i];
        }

        for(int i = 0; i< pathEnd.length;i++){
            fullPath[i+ pathStart.length] = pathEnd[i];
        }

        return fullPath;
    }

    public static String fuseTwoPaths(String pathStart, String pathEnd){
        String[] pathStartArray = pathToArray(pathStart);
        String[] pathEndArray = pathToArray(pathEnd);

        String[] fullArray = fuseTwoPathArrays(pathStartArray, pathEndArray);
        return arrayToPath(fullArray);
    }

    public static String removeLastElement(String path){
        String[] pathArray = pathToArray(path);
        String[] pathArrayCapped = new String[pathArray.length-1];

        for(int i = 0; i< pathArrayCapped.length; i++){
            pathArrayCapped[i] = pathArray[i];
        }
        return arrayToPath(pathArrayCapped);
    }
}
