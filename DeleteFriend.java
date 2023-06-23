import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class DeleteFriend {
    public static void main(String data[]) {
        try {
            // Get the name of the contact to be deleted
            // from the command line argument
            String newName = data[0];

            String nameNumberString;
            String name;
            int index;
            boolean found = false;

            // Using file pointer to create the file
            File file = new File("friendsContact.txt");

            if (!file.exists()) {
                // Create a new file if it doesn't exist
                file.createNewFile();
            }

            // Opening the file in read and write mode
            RandomAccessFile raf = new RandomAccessFile(file, "rw");

            // Checking whether the contact exists
            while (raf.getFilePointer() < raf.length()) {
                // Reading line from the file
                nameNumberString = raf.readLine();

                // Splitting the string to get name and number
                String[] lineSplit = nameNumberString.split("!");

                // Separating name and number
                name = lineSplit[0];

                // If condition to find the existence of the record
                if (name.equals(newName)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                // Creating a temporary file
                File tmpFile = new File("temp.txt");

                // Opening the temporary file in read and write mode
                RandomAccessFile tmpraf = new RandomAccessFile(tmpFile, "rw");

                // Set file pointer to start
                raf.seek(0);

                // Traversing the friendsContact.txt file
                while (raf.getFilePointer() < raf.length()) {
                    // Reading the contact from the file
                    nameNumberString = raf.readLine();

                    index = nameNumberString.indexOf('!');
                    name = nameNumberString.substring(0, index);

                    // Check if the fetched contact is the one to be deleted
                    if (name.equals(newName)) {
                        // Skip inserting this contact into the temporary file
                        continue;
                    }

                    // Add this contact to the temporary file
                    tmpraf.writeBytes(nameNumberString);

                    // Add the line separator in the temporary file
                    tmpraf.writeBytes(System.lineSeparator());
                }

                // The contact has been deleted now
                // So copy the updated content from the temporary file to the original file

                // Set both files' pointers to start
                raf.seek(0);
                tmpraf.seek(0);

                // Copy the contents from the temporary file to the original file
                while (tmpraf.getFilePointer() < tmpraf.length()) {
                    raf.writeBytes(tmpraf.readLine());
                    raf.writeBytes(System.lineSeparator());
                }

                // Set the length of the original file to that of the temporary file
                raf.setLength(tmpraf.length());

                // Closing the resources
                tmpraf.close();
                raf.close();

                // Deleting the temporary file
                tmpFile.delete();

                System.out.println("Friend deleted.");
            } else {
                // Closing the resources
                raf.close();

                // Print the message
                System.out.println("Contact does not exist.");
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
