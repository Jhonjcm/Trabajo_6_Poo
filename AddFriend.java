import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class AddFriend {
    public static void main(String data[]) {
        try {
            // Get the name of the contact to be added
            // from the command line argument
            String newName = data[0];

            // Get the number to be added
            // from the command line argument
            long newNumber = Long.parseLong(data[1]);

            String nameNumberString;
            String name;
            long number;
            boolean found = false;

            // Using file pointer to create the file
            File file = new File("friendsContact.txt");

            if (!file.exists()) {
                // Create a new file if it doesn't exist
                file.createNewFile();
            }

            // Opening the file in read and write mode
            RandomAccessFile raf = new RandomAccessFile(file, "rw");

            // Checking whether the contact already exists
            while (raf.getFilePointer() < raf.length()) {
                // Reading line from the file
                nameNumberString = raf.readLine();

                // Splitting the string to get name and number
                String[] lineSplit = nameNumberString.split("!");

                // Separating name and number
                name = lineSplit[0];
                number = Long.parseLong(lineSplit[1]);

                // If condition to check if the contact already exists
                if (name.equals(newName) || number == newNumber) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                // Enter the block when the contact doesn't already exist in the file
                nameNumberString = newName + "!" + String.valueOf(newNumber);

                // Writing the contact to the file
                raf.writeBytes(nameNumberString);
                raf.writeBytes(System.lineSeparator());

                // Print the message
                System.out.println("Friend added.");

                // Closing the resources
                raf.close();
            } else {
                // Closing the resources
                raf.close();

                // Print the message
                System.out.println("Contact already exists.");
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        } catch (NumberFormatException nef) {
            System.out.println(nef);
        }
    }
}
