import java.util.Scanner;

public class hashFunctions {

    //Make the keys part of the class so that we do not have to remake them.
    private static final int[] keys = {1234, 8234, 7867, 1009, 5438, 4312, 3420, 9487, 5418, 5299,
            5078, 8239, 1208, 5098, 5195, 5329, 4543, 3344, 7698, 5412,
            5567, 5672, 7934, 1254, 6091, 8732, 3095, 1975, 3843, 5589,
            5439, 8907, 4097, 3096, 4310, 5298, 9156, 3895, 6673, 7871,
            5787, 9289, 4553, 7822, 8755, 3398, 6774, 8289, 7665, 5523};

    public static void main(String[] args) {
        boolean running = true;
        do{
            printMenu();
            switch (getValidateUserMenuInput()){
                case 1: //Run HF 1 (Linear Probing)
                    HF1(keys);
                    break;
                case 2: //Run HF 2 (Quadratic Probing)
                    HF2(keys);
                    break;
                case 3: //Run HF 3 (Double Hashing)
                    HF3(keys);
                    break;
                case 4: //Run HF 4 (Student Designed)
                    HF4(keys);
                    break;
                case 5: //exit program
                    running = false;
                    break;
            }
            System.out.println(); //new line after every single hash method.
        }while (running);
    }

    //This method will print the menu for the user.
    private static void printMenu(){
        System.out.println("-----MAIN MENU--------------------------------------");
        System.out.println("1. Run HF1 (Division method with Linear Probing)");
        System.out.println("2. Run HF2 (Division method with Quadratic Probing)");
        System.out.println("3. Run HF3 (Division method with Double Hashing)");
        System.out.println("4. Run HF4 (Student Designed HF)");
        System.out.println("5. Exit program\n");
    }


    //This method aims to use the division method and uses linear probing as open addressing technique.
    public static void HF1(int[] keys){
        //Type char was picked for table as it allows for any key to be inserted while still checking if the address is vacant.
        Integer[][] Table = new Integer[50][2]; //Create the table for the hashmap inside the method as each method will have its own table.

        for(int key : keys) { //For each key, we will find an address for it and then attempt to resolve any collisions.
            int address = findAddressDivMethod(key, Table.length);
            int numProbes = 0;
            if(Table[address][0]!=null){ //If the spot in question is already taken, then we must preform collision resolution.
                do{
                    address++; //This will look at the next address of the Table using Linear Probing.
                    numProbes++; //Increment the number of probes as we have collided must be resolved.
                    address = address % Table.length; //Make sure the address does not go out of bounds.
                }while(Table[address][1] != null);
            }
            //This will happen regardless of collision or not.
            Table[address][0] = key;
            Table[address][1] = numProbes;

        }
        printOutput(Table,"HF1");
    }

    //This method aims to use the division method and uses quadratic probing as open addressing technique.
    public static void HF2(int[] keys){
        //Type Integer was picked for table as it allows for any key to be inserted while still checking if the address is vacant.
        Integer[][] Table = new Integer[50][2]; //Create the table for the hashmap inside the method as each method will have its own table.

        for(int key : keys) { //For each key, we will find an address for it and then attempt to resolve any collisions.
            int address = findAddressDivMethod(key, Table.length);
            int numProbes = 0;
            if(Table[address][0]!=null){ //If the spot in question is already taken, then we must preform collision resolution.
                int counter =0;
                int originalAddress = address; //Use this temp value to keep track of the original address when we preform the quadratic probing.
                do{
                    counter++;
                    address = Math.addExact(originalAddress,(int)Math.pow(counter,2)); //This will look at the next address of the Table using Quadratic Probing. Add exact to make sure we do not overflow.
                    numProbes++; //Increment the number of probes as we have collided must be resolved.
                    address = address % Table.length; //Make sure the address does not go out of bounds.
                }while(Table[address][1] != null);
            }
            //This will happen regardless of collision or not.
            Table[address][0] = key;
            Table[address][1] = numProbes;

        }
        printOutput(Table,"HF2");
    }

    //This method aims to use the division method and uses Double Hashing as open addressing technique.
    public static void HF3(int[] keys){
        //Type Integer was picked for table as it allows for any key to be inserted while still checking if the address is vacant.
        Integer[][] Table = new Integer[50][2]; //Create the table for the hashmap inside the method as each method will have its own table.
        int numKeysUnableToMap = 0;

        forEachKeyLoop:
        for(int key : keys) { //For each key, we will find an address for it and then attempt to resolve any collisions.
            int address = findAddressDivMethod(key, Table.length);
            int numProbes = 0;
            if(Table[address][0]!=null){ //If the spot in question is already taken, then we must preform collision resolution.
                int counter =0;
                int originalAddress = address; //Use this temp value to keep track of the original address when we preform the quadratic probing.
                do{
                    counter++;
                    address = originalAddress + (counter * (30 - key % 25));
                    numProbes++; //Increment the number of probes as we have collided must be resolved.
                    address = address % Table.length; //Make sure the address does not go out of bounds.
                    if(numProbes >= Table.length){ //If we made a full loop and came back to where we started, give up and go to the next key.
                        System.out.println("Unable to hash key "+key+" to the table");
                        numKeysUnableToMap++;
                        continue forEachKeyLoop;
                    }
                }while(Table[address][1] != null);
            }
            //This will happen regardless of collision or not.
            Table[address][0] = key;
            Table[address][1] = numProbes;
        }

        //If we were unable to store any keys, print out how many keys we were unable to store.
        if(numKeysUnableToMap >=1){
            System.out.println(numKeysUnableToMap+" keys \"unable to store in the table\"");
        }

        printOutput(Table,"HF3");
    }

    //GOAL: Less than  80 probes and hashes 90% or more of the keys (45 keys or more stored).
    //This implementation: 66 probes, 48/50 keys mapped. Success!!
    //This Hash Function uses the Mid-Square Method and Double Hashing as the collision resolution technique.
    //This mid squared implementation works as we get the key and square it to get a long value in which we find the middle number as well as the one number to the left of it.
    //these 2 values are then put together to get 1 key which is then checked using modulus and then put into the array.
    //If there is a collision, the collision will be resolved using Double Hashing formula given to us in the Assignment as they give good results.
    //There are a couple of keys that are unmapped, however there are a few tradeoffs that have to be made because we did not load refactor this array and we are filling it to the brim.
    public static void HF4(int[] keys){
        Integer[][] Table = new Integer[50][2]; //Create the table for the hashmap inside the method as each method will have its own table.

        int numKeysUnableToMap = 0;

        forEachKeyLoop:
        for(int key : keys) { //For each key, we will find an address for it and then attempt to resolve any collisions.
            int address = findAddressMidSquared(key,Table);
            int numProbes = 0;
            if(Table[address][0]!=null){ //If the spot in question is already taken, then we must preform collision resolution.
                int counter =0;
                int originalAddress = address; //Use this temp value to keep track of the original address when we preform the quadratic probing.
                do{
                    counter++;
                    address = originalAddress + (counter * (30 - key % 25));
                    numProbes++; //Increment the number of probes as we have collided must be resolved.
                    address = address % Table.length; //Make sure the address does not go out of bounds.
                    if(numProbes >= Table.length){ //If we made a full loop and came back to where we started, give up and go to the next key.
                        System.out.println("Unable to hash key "+key+" to the table");
                        numKeysUnableToMap++;
                        continue forEachKeyLoop;
                    }
                }while(Table[address][1] != null);
            }
            //This will happen regardless of collision or not.
            Table[address][0] = key;
            Table[address][1] = numProbes;
        }

        //If we were unable to store any keys, print out how many keys we were unable to store.
        if(numKeysUnableToMap >=1){
            System.out.println(numKeysUnableToMap+" keys \"unable to store in the table\"");
        }

        printOutput(Table,"HF4");
    }

    //This method will find the address using the mid squared technique.
    //This technique squares the key and gets 2 digits from the middle of the key.
    private static int findAddressMidSquared(int key,Integer[][] Table){
        long midSquared = (long) key * key;
        int length = (int)(Math.log10(midSquared)+1);
        int middle = length/2;
        int firstDigit = extractDigit(middle,midSquared);
        int secondDigit = extractDigit(middle-1,midSquared);

        return (firstDigit * 10 + secondDigit) % Table.length;
    }

    //Gets the number at any given position in a number. (1 is rightmost digit.)
    private static int extractDigit(int pos, long num){
        return (int)((num % Math.pow(10,pos)) / (Math.pow(10,(pos-1))));
    }



    //This method will implement the division method and will return an address for each key.
    //Use H(X) = (X mod m) + 1 where x is the key and m is table size.
    private static int findAddressDivMethod(int key, int tableSize){
        return (key % tableSize); //using the formula for the division method.
    }

    //This method will get and validate the user menu input.
    private static int getValidateUserMenuInput(){
        boolean testPassed = false;
        int userInputInteger = 0;
        Scanner sc = new Scanner(System.in);
        do{
            System.out.print("Enter option number: ");
            try{
                userInputInteger = Integer.parseInt(sc.nextLine());
                if(userInputInteger>=1 && userInputInteger <= 5){ //if the number is actually a valid menu option, return that number.
                    testPassed = true;
                }else{
                    System.out.println("Please enter a number between 1-5.");
                }
            }catch (NumberFormatException e){
                System.out.println("Please enter an Integer.");
            }
        }while(!testPassed);
        return userInputInteger;
    }

    //This will sum up all the probes in the second column. Less probes means a better hash function.
    public static int sumProbes(Integer[][] Table){
        int sum = 0;
        for(int i =0;i<Table.length;i++){
            if(Table[i][1] != null){
                sum+= Table[i][1];
            }
        }
        return sum;
    }

    //This method will be run after each hash function to print out the output in a table like format as well as tell how many probes.
    private static void printOutput(Integer[][] map,String nameOfHF){
        System.out.println("Hash Table resulted from "+nameOfHF+":");

        System.out.println("Index\tKey\t\tProbes");
        System.out.println("------------------------");
        for(int i=0;i< map.length;i++){
            System.out.println(i+"\t\t"+map[i][0]+"\t\t"+map[i][1]);
        }
        System.out.println("------------------------\n");

        System.out.println("Sum of probe values = "+sumProbes(map)+" probes.");
    }
}
