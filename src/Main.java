import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;

public class Main{

    // Global variables to store best result
    private static int bestHealth = -1;
    private static ArrayList<String> bestPath = new ArrayList<>();

    public static int[] roomManager(char room, int maxHealth,int remainingHealth, int shieldValue, int key){
        // Manages all types of rooms and applies their effects to the character

        if (room == 'H'){
            HealingRoom healingRoom = new HealingRoom(maxHealth,remainingHealth, shieldValue);
            remainingHealth = healingRoom.getHealth();

        }else if (room == 'M'){
            MonsterRoom monsterRoom = new MonsterRoom(maxHealth, remainingHealth, shieldValue);
            remainingHealth = monsterRoom.getHealth();

        }else if (room == 'T'){
            TrapRoom trapRoom = new TrapRoom(maxHealth,remainingHealth, shieldValue);
            remainingHealth = trapRoom.getHealth();

        }else if (room == 'L'){
            ArtifactRoom artifactRoom = new ArtifactRoom(maxHealth,remainingHealth, shieldValue);
            artifactRoom.relicHeal();
            remainingHealth = artifactRoom.getHealth();

        }else if (room == 'A'){
            ArtifactRoom artifactRoom = new ArtifactRoom(maxHealth,remainingHealth, shieldValue);
            artifactRoom.armorRelic();
            shieldValue = artifactRoom.getShieldValue();

        }else if (room == 'B'){
            ArtifactRoom artifactRoom = new ArtifactRoom(maxHealth,remainingHealth, shieldValue);
            artifactRoom.boostRelic();
            shieldValue = artifactRoom.getShieldValue();

        }else if (room == 'K'){
            key = 1; // Character picks up the key
        }else if (room == 'E' || room == 'X'){}

        int[] result = {remainingHealth, shieldValue, key};
        return result;
    }

    public static void backtrack(int maxHealth, int remainingHealth, int X, int Y, char[][] dungeon, boolean[][] visited, int shieldValue, int key, ArrayList<String> currentPath){

        int row = dungeon.length;
        int column = dungeon[0].length;

        // If health is zero or position is out of grid, returns immediately
        if (remainingHealth == 0 || (X<0 || X>= row) || (Y<0 || Y>= column)){
            return;
        }
        // If it is on the grid, but the room is visited before, returns
        if (visited[X][Y] == true){
            return;
        }

        // Calculates new stats after entering the room
        int[] result = roomManager(dungeon[X][Y], maxHealth, remainingHealth, shieldValue, key);
        int newHealth = result[0];
        int newShieldValue = result[1];
        int newKey = result[2];

        // Marks the room as visited and saves current location info
        visited[X][Y] = true;
        String location = String.format("(%d, %d, %d)", X, Y, newHealth);
        currentPath.add(location);

        // If character dies, undoes the choice and returns
        if (newHealth == 0){
            visited[X][Y] = false;
            currentPath.remove(currentPath.size()-1);
            return;
        }

        // Checks if we reached the exit room
        if (dungeon[X][Y] == 'X'){
            if(newKey == 1){ // We can only exit if we have the key
                // Updates the best final health and best path if we found better one
                // Or if health is same, chooses the shorter path
                if (newHealth > bestHealth || (newHealth == bestHealth && currentPath.size() < bestPath.size())){
                    bestHealth = newHealth;

                    bestPath = new ArrayList<>(currentPath);
                }
            }
            // Stops exploring further from the exit room
            visited[X][Y] = false;
            currentPath.remove(currentPath.size()-1);
            return;
        }

        // Explores all 4 directions recursively
        backtrack(maxHealth, newHealth, X-1, Y, dungeon, visited, newShieldValue, newKey, currentPath); // Up
        backtrack(maxHealth, newHealth, X, Y+1, dungeon, visited, newShieldValue, newKey, currentPath); // Right
        backtrack(maxHealth, newHealth, X+1, Y, dungeon, visited, newShieldValue, newKey, currentPath); // Down
        backtrack(maxHealth, newHealth, X, Y-1, dungeon, visited, newShieldValue, newKey, currentPath); // Left

        // Undoes the choice to explore other possible paths
        visited[X][Y] = false;
        currentPath.remove(currentPath.size()-1);
    }

    public static ArrayList<String> makeOutput(int maxHealth, int remainingHealth, int X, int Y, char[][] dungeon){
        // Creates the final output lines based on backtracking results

        ArrayList<String> outputList = new ArrayList<>();
        int row = dungeon.length;
        int column = dungeon[0].length;
        boolean[][] visited = new boolean[row][column];
        int key = 0;
        int shieldValue = 0;
        ArrayList<String> currentPath = new ArrayList<>();

        // Starts the algorithm
        backtrack(maxHealth, remainingHealth, X, Y, dungeon, visited, shieldValue, key, currentPath);

        int step = bestPath.size() -1;

        //If best final health is changed, it means we successfully found a path
        if (bestHealth != -1){
            String result = "SUCCESS";
            outputList.add(result);

            String max_health = Integer.toString(maxHealth);
            outputList.add(max_health);

            String remaining_health = Integer.toString(bestHealth);
            outputList.add(remaining_health);

            String steps = Integer.toString(step);
            outputList.add(steps);

            String path = String.join(" -> ", bestPath);
            outputList.add(path);

        }
        // If best final health is unchanged, it finds the reason of failure
        else{
            String result = "FAILURE";
            outputList.add(result);

            String max_health = Integer.toString(maxHealth);
            outputList.add(max_health);

            String reason = "";

            // Searches if key room exists or not
            boolean keyExists = false;
            for(int i=0; i<row; i++){
                for (int j=0; j< column; j++){
                    if (dungeon[i][j]== 'K'){
                        keyExists = true;
                    }
                }
            }
            if (keyExists){
                reason = "Player died before reaching the exit.";
            }else{
                reason = "No key found.";
            }
            outputList.add(reason);
        }
        return  outputList;
    }

    public static void giveOutput(int maxHealth, int initialX, int initialY, char[][] dungeon, String outputFileName){
        // Writes the output into a text file

        try {
        PrintWriter writer = new PrintWriter(outputFileName);

        int remainingHealth = maxHealth;
        ArrayList<String> output = makeOutput(maxHealth, remainingHealth, initialX, initialY, dungeon);

        if (output.size()==3){
            // Prints failure case
            writer.println("RESULT: "+ output.get(0));
            writer.println("MAX_HEALTH: " + output.get(1));
            writer.println("REASON: " + output.get(2));

        }else if (output.size() == 5) {
            // Prints success case
            writer.println("RESULT: " + output.get(0));
            writer.println("MAX_HEALTH: " + output.get(1));
            writer.println("REMAINING_HEALTH: " + output.get(2));
            writer.println("STEPS: " + output.get(3));
            writer.println("PATH: \n" + output.get(4));

        }writer.close();
        }catch (FileNotFoundException e){
            System.out.println("An error occurred! " + e.getMessage());
        }
    }


    public static void main(String[] args){

    // Gets the file names from command line arguments
    String inputFileName = args[0];
    String outputFileName = args[1];

    // Gets the input from a text file
    try{
    File inputFile = new File(inputFileName);
    Scanner scan = new Scanner(inputFile);

    //Size of dungeon
    int row = scan.nextInt();
    int column = scan.nextInt();

    final int maxHealth = scan.nextInt();

    //Starting point
    int initialX = scan.nextInt();
    int initialY = scan.nextInt();

    //Exit point
    int exitX = scan.nextInt();
    int exitY = scan.nextInt();

    // Rooms
    char[][] dungeon = new char[row][column];
    for (int i=0; i<row; i++){
        for (int j=0; j<column; j++){
            char c = scan.next().charAt(0);
            dungeon[i][j] = c;
        }
    }
    if (dungeon[exitX][exitY] != 'X'){
        System.out.println("Error: Your exit room coordinates do not match with your dungeon.");
        return;
        }

    // Executes the algorithm and generates output
    giveOutput(maxHealth, initialX, initialY, dungeon, outputFileName);
    scan.close();
    }
    catch (FileNotFoundException e){
        System.out.println("An error occurred! " + e.getMessage());
    }
    }
}