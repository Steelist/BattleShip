import java.util.Scanner;

/**
 * @author      Aleksi Hella <aleksi.hella@cs.tamk.fi>
 * @version     2015.1226
 * @since       1.6
 */
public class Battleship {
    
    /**
     * Is the game.
     * 
     * @param args Command line parameters. Not used.
     */
    public static void main(String[] args) {
        int[][][] shipsOwn = ships();
        int[][][] shipsFoe = ships();
        String[][] tableOwn = new String[10][10];
        String[][] tableFoe = new String[10][10];
        int[] one = {0, 0};
        int[] two = {1, 1};
        int[] three = {2, 2};
        int[] four = {3, 3};
        int[] five = {4, 4};

        intro(tableOwn, shipsOwn);
        setter(tableOwn);
        setter(tableFoe);
        System.out.println("");
        System.out.println("Type from range A-J and 1-10 to shoot"
            + " to the wanted location.");
        System.out.println("");
        System.out.println("Here are your ships.");
        shownTable(tableOwn, shipsOwn);
        
        while (emptyBoard(tableOwn) == false &&
            emptyBoard(tableFoe) == false) {
            
            shooting(tableFoe, shipsFoe);
            shootingAI(tableOwn, shipsOwn);
            one[1] = blownShipsOwn(tableOwn, shipsOwn, one[1]);
            two[1] = blownShipsOwn(tableOwn, shipsOwn, two[1]);
            three[1] = blownShipsOwn(tableOwn, shipsOwn, three[1]);
            four[1] = blownShipsOwn(tableOwn, shipsOwn, four[1]);
            five[1] = blownShipsOwn(tableOwn, shipsOwn, five[1]);
            shownTable(tableOwn, shipsOwn);
            one[0] = blownShips(tableFoe, shipsFoe, one[0]);
            two[0] = blownShips(tableFoe, shipsFoe, two[0]);
            three[0] = blownShips(tableFoe, shipsFoe, three[0]);
            four[0] = blownShips(tableFoe, shipsFoe, four[0]);
            five[0] = blownShips(tableFoe, shipsFoe, five[0]);
            hiddenTable(tableFoe);
        }
        
        if (emptyBoard(tableOwn) == true) {
            defeat();
        }
        
        if (emptyBoard(tableFoe) == true) {
            victory();
        }
    }
    
    /**
     * Sets waves to the tables.
     * 
     * @param table the 10x10 table.
     * @return modified table.
     */
    public static String[][] setter(String[][] table) {
        
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {                
                if (table[i][j] == null) {
                    table[i][j] = "~";
                }
            }
        }
        
        return table;
    }
    
    /**
     * Places the ships to a wanted location.
     * 
     * @return the placed ships.
     */
    public static int[][][] ships() {
        int[][][] ships = new int[5][][];       
        ships[0] = new int[2][2];
        ships[1] = new int[3][2];
        ships[2] = new int[3][2];
        ships[3] = new int[4][2];
        ships[4] = new int[5][2];
        
        for ( int i = 0; i < ships.length; i++) {          
            for ( int j = 0; j < ships[i].length; j++) {
                ships[i][j][0] = -1;
                ships[i][j][1] = -1;
            }
        }
        
        main:

        for (int i = 0; i < ships.length; i++) {
            int[] coordinates = new int[2];
            coordinates[0] = (int)(Math.random() * 10 + 1) - 1;
            coordinates[1] = (int)(Math.random() * 10 + 1) - 1;
            
            for (int j = 0; j < ships[i].length; j++) {                        
                for (int k = 0; k < ships[i].length; k++) {                   
                    if (outOfBounds(coordinates[0] + k, coordinates[1])
                        == false) {
                        
                        i--;
                        continue main;
                    }
                }
                
                for (int k = 0; k < ships[i].length; k++) {
                    if (checkSpace(ships, coordinates[0] + k, coordinates[1])
                        == false) {
                        
                        i--;
                        continue main;
                    }
                }
            }
            
            for (int k = 0; k < ships[i].length; k++) {
                    ships[i][k][0] = coordinates[0] + k;
                    ships[i][k][1] = coordinates[1];
                }
            }

        return ships;
    }
    
    /**
     * Checks if the ship is still within the table's limits.
     * 
     * @param x the randomized x coordinate.
     * @param y the randomized y coordinate.
     * @return true or false based on the result.
     */
    public static boolean outOfBounds(int x, int y) {
        
        if (x >= 10 || y >= 10) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if there are other ships in place to avoid overlapping.
     * 
     * @param ships the array of ships.
     * @param x the randomized x coordinate.
     * @param y the randomized y coordinate.
     * @return true or false based on the result.
     */
    public static boolean checkSpace(int[][][] ships, int x, int y) {
        
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].length; j++) {                   
                if (ships[i][j][0] == x && ships[i][j][1] == y) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Checks if the maximum amount of 17 hits have occurred to finish the game.
     * 
     * @param table the given table.
     * @return true or false based on the result.
     */
    public static boolean emptyBoard(String[][] table) {
        int shipHits = 0;
        
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (table[i][j] == "o") {
                    shipHits++;
                }
            }
        }
        
        if (shipHits == 17) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Is the enemy's randomized shooting.
     * 
     * @param own the player's table.
     * @param ships the player's ships.
     * @return the table after adding hits or misses.
     */
    public static String[][] shootingAI(String[][] own, int[][][] ships) {  
        int shot = (int)(Math.random() * 10 + 1) -1;
        int shot2 = (int)(Math.random() * 10 + 1) -1;
        boolean hit = false;

        while (true) {
            if ("o".equals(own[shot][shot2]) ||
                "m".equals(own[shot][shot2])) {
                
                shot = (int)(Math.random() * 10 + 1) -1;
                shot2 = (int)(Math.random() * 10 + 1) -1;
            } else {
                break;
            }
        }
        
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].length; j++) {
                if ((shot) == ships[i][j][0] &&
                    (shot2) == ships[i][j][1]) {
                    
                    System.out.println("Your ship got hit!");
                    
                    own[shot][shot2] = "o";
                    hit = true;
                }
            }
        }
        
        if (hit == false) {
            System.out.println("Enemy missed!");
            
            own[shot][shot2] = "m";
        }
        
        return own;
    }
    
    /**
     * Asks the player where to shoot and places the marks.
     * 
     * @param foe the enemy's table.
     * @param ships the enemy's ships.
     * @return the table after adding hits or misses.
     */
    public static String[][] shooting(String[][] foe, int[][][] ships) {
        char shotOwn;
        int shotOwn2 = 0;   
        Scanner scanner = new Scanner(System.in);
            
        System.out.println("Type the letter (A-J)");
        
        main:

        while (true) {
            shotOwn = scanner.next().toUpperCase().charAt(0);
            
            for (int i = 0; i < 10; i++) {
                if (shotOwn == (65 + i)) {
                    break main;
                }
            }
            
            System.err.println("ERROR: not a valid letter!");
        }
        
        System.out.println("Type the number (1-10)");
        
        while (true) {
            String str = scanner.next();
            
            try {                                
                shotOwn2 = Integer.parseInt(str);
                
                if (shotOwn2 < 1 || shotOwn2 > 10) {
                    System.err.println("Number not in the given range!"); 
                } else {
                    break;
                }
            } catch (Exception error) {
                System.err.println("ERROR: not a number!");
            }
        }
        
        System.out.println("");
        
        boolean hit;
        hit = false;
        
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].length; j++) {
                if ((shotOwn - 65) == ships[i][j][0] &&
                    (shotOwn2 - 1) == ships[i][j][1]) {
                    
                    System.out.println("Ship got hit!");
                    
                    foe[shotOwn - 65][shotOwn2 - 1] = "o";
                    hit = true;
                }
            }
        }
        
        if (hit == false) {
            System.out.println("You missed!");
            
            foe[shotOwn - 65][shotOwn2 - 1] = "m";
        }
        
        return foe;
    }
    
    /**
     * Checks if a whole ship has been shot to notify of the event (enemy).
     * 
     * @param foe the enemy's table.
     * @param ships the enemy's ships.
     * @param x the number of the ship.
     * @return either '5' to mark as destroyed or the same number as given.
     */
    public static int blownShips(String[][] foe, int[][][] ships, int x) {
        int counter = 0;
        
        if (x != 5) {
            for (int j = 0; j < ships[x].length;j++) {
                if ("o".equals(foe[ships[x][j][0]][ships[x][j][1]])) {
                    counter++;
                }
                
                if (counter == ships[x].length) {
                    System.out.print("*   *   *   ");
                    System.out.print("Enemy battleship destroyed!");
                    System.out.println("   *   *   *");
                    System.out.println("");
                    
                    return 5;
                }
            }
        }
        
        counter = 0;
        return x;
    }
    
    /**
     * Checks if a whole ship has been shot to notify of the event (player).
     * 
     * @param own the player's table.
     * @param ships the player's ships.
     * @param x the number of the ship.
     * @return either '5' to mark as destroyed or the same number as given.
     */
    public static int blownShipsOwn(String[][] own, int[][][] ships, int x) {
        int counter = 0;
        
        if (x != 5) {
            for (int j = 0; j < ships[x].length;j++) {
                if ("o".equals(own[ships[x][j][0]][ships[x][j][1]])) {
                    counter++;
                }
                
                if (counter == ships[x].length) {
                    System.out.println("");
                    System.out.print("*   *   *   ");
                    System.out.print("Your battleship was destroyed!");
                    System.out.println("   *   *   *");
                    
                    return 5;
                }
            }
        }
        
        counter = 0;
        return x;
    }
    
    /**
     * Prints and shows the player's table with marks and remaining ships.
     * 
     * @param own the player's table.
     * @param ships the player's ships.
     */
    public static void shownTable(String[][] own, int[][][] ships) {
        String line = "";
        char letter = 65;
        int num = 1;
        
        System.out.println("");
        System.out.println("Your table:");
        
        for (int i = 0; i < own.length; i++) {
            line += "+---";
        }
        
        line += "+";
        
        System.out.print(" ");
        
        for (int i = 0; i < own.length; i++) {
            System.out.print("  " + num + " ");
            
            num++;
        }
        
        System.out.println("");
            
        for (int i = 0; i < own.length; i++) {          
            System.out.println(" " + line);
            System.out.print(letter);
            
            letter++;
            
            for (int j = 0; j < own[0].length; j++) {
                boolean found = false; 
                
                System.out.print("| ");           
                
                for (int k = 0; k < ships.length; k++) {
                    for (int l = 0; l < ships[k].length; l++) {
                        if (ships[k][l][0] == i && ships[k][l][1] == j) {
                            found = true;
                        }
                    }
                }
                
                boolean hit = false;
                boolean miss = false;
                
                for (int k = 0; k < own.length; k++) {
                    for (int l = 0; l < own[k].length; l++) {
                        if ("o".equals(own[i][j]) ||
                            "x".equals(own[i][j])) {
                            
                            hit = true;
                        } else if ("m".equals(own[i][j])) {
                            miss = true;
                        }
                    }
                }
                
                if (hit == true) {
                    System.out.print("o");   
                } else if (miss == true) {
                    System.out.print("m");
                } else if (found == true) {
                    System.out.print("x");
                } else {
                    System.out.print("~");
                }
                
                System.out.print(" ");
            }
            
            System.out.print("|");
            System.out.println("");
        }
        
        System.out.println(" " + line);
        System.out.println("");
    }
    
    /**
     * Prints and shows the enemy's table with marks.
     * 
     * @param foe the enemy's table.
     */
    public static void hiddenTable(String[][] foe) {
        String line = "";
        char letter = 65;
        int num = 1;
        
        System.out.println("Enemy table:");
        
        for (int i = 0; i < foe.length; i++) {
            line += "+---";
        }
        
        line += "+";
        
        System.out.print(" ");
        
        for (int i = 0; i < foe.length; i++) {
            System.out.print("  " + num + " ");
            
            num++;
        }
        
        System.out.println("");
            
        for (int i = 0; i < foe.length; i++) {            
            System.out.println(" " + line);
            System.out.print(letter);
            
            letter++;
            
            for (int j = 0; j < foe[0].length; j++) {
                boolean hit = false;
                boolean miss = false;
                
                System.out.print("| ");
                
                for (int k = 0; k < foe.length; k++) {                   
                    for (int l = 0; l < foe[k].length; l++) {
                        if ("o".equals(foe[i][j])) {
                            hit = true;
                        } else if ("m".equals(foe[i][j])) {
                            miss = true;
                        }
                    }
                }
                
                if (hit == true) {
                    System.out.print("o");   
                } else if (miss == true) {
                    System.out.print("m");
                } else {
                    System.out.print("~");
                }
                
                System.out.print(" ");
            }
            
            System.out.print("|");
            System.out.println("");
        }
        
        System.out.println(" " + line);
        System.out.println("");
    }   
    
    /**
     * Pops up at the start of the game.
     * 
     * @param own the player's table.
     * @param ships the player's ships.
     * @return player's table after possible markings.
     */
    public static String[][] intro(String[][] own, int[][][] ships) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("###     #    ########   #     ####");
        System.out.println("#  #   # #     #  #     #     #");
        System.out.println("###   #####    #  #     #     ##");
        System.out.println("#  #  #   #    #  #     #     #");
        System.out.println("###   #   #    #  #     ####  ####");
        System.out.println("");
        System.out.println(" ### #  # # ###    ¤¤¤  ¤¤¤¤ ¤    ¤  ¤ ¤  ¤ ¤¤¤¤");
        System.out.println("#    #  # # #  #   ¤  ¤ ¤    ¤    ¤  ¤ ¤  ¤ ¤");
        System.out.println(" ### #### # ###    ¤  ¤ ¤¤   ¤    ¤  ¤  ¤¤  ¤¤");
        System.out.println("   # #  # # #      ¤  ¤ ¤    ¤    ¤  ¤ ¤  ¤ ¤");
        System.out.println("###  #  # # #      ¤¤¤  ¤¤¤¤ ¤¤¤¤  ¤¤  ¤  ¤ ¤¤¤¤");
        System.out.println("");
        
        System.out.println("Welcome to the best Battleship Deluxe"
                + " game you'll ever see!\n");
        System.out.println("If you'd wish to see the instructions, please"
                + " type 'yes'.\nIf you'd like to enter the hard more,"
                + " please type 'hard'.\nOtherwise type anything else.");
        
        String answer = scanner.nextLine();
        
        if (answer.equalsIgnoreCase("yes")) {
            System.out.println("");
            System.out.println("You play in a 10x10 map with an AI"
                    + " opponent.");
            System.out.println("You'll both take turns trying to shoot"
                    + " each others' ships.");
            System.out.println("If a player hits a ship it is marked with 'o'"
                    + " and if players miss it's marked with 'm'.");
            System.out.println("First one to destroy all"
                    + " enemy ships is the winner. Good luck!");
            System.out.println("Type anything to continue");
            
            String anyKey = scanner.nextLine();
        }
        
        if (answer.equalsIgnoreCase("hard")) {
            int shots = 0;
            
            System.out.println("");
            System.out.println("How many free turns would you like to"
                    + " give to the enemy? (max 16)");
            
            while (true) {
                String str = scanner.next();
            
                try {                                
                    shots = Integer.parseInt(str);
                
                    if (shots < 1 || shots > 16) {
                        System.err.println("Number not in the given range!"); 
                    } else {
                        break;
                    }
                } catch (Exception error) {
                    System.err.println("ERROR: not a number!");
                }
            }
            
            System.out.println("");
                        
            for (int i = 0; i < shots; i++) {
                shootingAI(own, ships);
            }
            
            return own;
        }
        
        return own;
    }
    
    /**
     * Declares victory.
     */
    public static void victory() {
        System.out.println("Congratulations! You destroyed all the "
                + "enemy ships! You won!");  
    }
    
    /**
     * Declares defeat.
     */
    public static void defeat() {
        System.out.println("Defeat! All your ships were sank! You lost!");
    }
}
