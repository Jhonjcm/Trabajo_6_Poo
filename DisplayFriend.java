import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class DisplayFriends {
    public static void main(String data[]) {
        try {
            String nameNumberString;
            String name;
            long number;

            // Using file pointer to create the file
            File file = new File("friendsContact.txt");

            if (!file.exists()) {
                // Create a new file if it doesn't exist
                file.createNewFile();
            }

            // Opening the file in read mode
            RandomAccessFile raf = new RandomAccessFile(file, "r");

            // Traversing the file
            while (raf.getFilePointer() < raf.length()) {
                // Reading line from the file
                nameNumberString = raf.readLine();

                // Splitting the string to get name and number
                String[] lineSplit = nameNumberString.split("!");

                // Separating name and number
                name = lineSplit[0];
                number = Long.parseLong(lineSplit[1]);

                // Print the contact data
                System.out.println("Friend Name: " + name + "\n" + "Contact Number: " + number + "\n");
            }

            // Closing the resources
            raf.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        } catch (NumberFormatException nef) {
            System.out.println(nef);
        }
    }
}
