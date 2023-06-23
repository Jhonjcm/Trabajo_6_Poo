import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class FriendGUI extends JFrame implements ActionListener {
    private JTextField nameField, numberField;
    private JButton addButton, deleteButton, updateButton, displayButton;

    public FriendGUI() {
        setTitle("Friend Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel numberLabel = new JLabel("Number:");
        numberField = new JTextField();

        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        displayButton = new JButton("Display");

        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        updateButton.addActionListener(this);
        displayButton.addActionListener(this);

        add(nameLabel);
        add(nameField);
        add(numberLabel);
        add(numberField);
        add(addButton);
        add(deleteButton);
        add(updateButton);
        add(displayButton);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String numberString = numberField.getText();

        if (e.getSource() == addButton) {
            if (!name.isEmpty() && !numberString.isEmpty()) {
                long number = Long.parseLong(numberString);
                addFriend(name, number);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter name and number.");
            }
        } else if (e.getSource() == deleteButton) {
            if (!name.isEmpty()) {
                deleteFriend(name);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter name.");
            }
        } else if (e.getSource() == updateButton) {
            if (!name.isEmpty() && !numberString.isEmpty()) {
                long number = Long.parseLong(numberString);
                updateFriend(name, number);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter name and number.");
            }
        } else if (e.getSource() == displayButton) {
            displayFriends();
        }
    }

    private void addFriend(String name, long number) {
        try {
            String nameNumberString;
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
                String existingName = lineSplit[0];
                long existingNumber = Long.parseLong(lineSplit[1]);

                // If condition to check if the contact already exists
                if (existingName.equals(name) || existingNumber == number) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                // Enter the block when the contact doesn't already exist in the file
                nameNumberString = name + "!" + String.valueOf(number);

                // Writing the contact to the file
                raf.writeBytes(nameNumberString);
                raf.writeBytes(System.lineSeparator());

                // Print the message
                JOptionPane.showMessageDialog(this, "Friend added.");

                // Closing the resources
                raf.close();
            } else {
                // Closing the resources
                raf.close();

                // Print the message
                JOptionPane.showMessageDialog(this, "Contact already exists.");
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, ioe.getMessage());
        } catch (NumberFormatException nef) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
        }
    }

    private void deleteFriend(String name) {
        try {
            String nameNumberString;
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
                String existingName = lineSplit[0];

                // If condition to find the existence of the record
                if (existingName.equals(name)) {
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
                    String existingName = nameNumberString.substring(0, index);

                    // Check if the fetched contact is the one to be deleted
                    if (existingName.equals(name)) {
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

                JOptionPane.showMessageDialog(this, "Friend deleted.");
            } else {
                // Closing the resources
                raf.close();

                // Print the message
                JOptionPane.showMessageDialog(this, "Contact does not exist.");
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, ioe.getMessage());
        }
    }

    private void updateFriend(String name, long newNumber) {
        try {
            String nameNumberString;
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
                String existingName = lineSplit[0];

                // If condition to find the existence of the record
                if (existingName.equals(name)) {
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
                    String existingName = nameNumberString.substring(0, index);

                    // Check if the fetched contact is the one to be updated
                    if (existingName.equals(name)) {
                        // Update the number of this contact
                        nameNumberString = existingName + "!" + String.valueOf(newNumber);
                    }

                    // Add this contact to the temporary file
                    tmpraf.writeBytes(nameNumberString);

                    // Add the line separator in the temporary file
                    tmpraf.writeBytes(System.lineSeparator());
                }

                // The contact has been updated now
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

                JOptionPane.showMessageDialog(this, "Friend updated.");
            } else {
                // Closing the resources
                raf.close();

                // Print the message
                JOptionPane.showMessageDialog(this, "Contact does not exist.");
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, ioe.getMessage());
        } catch (NumberFormatException nef) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
        }
    }

    private void displayFriends() {
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
            JOptionPane.showMessageDialog(this, ioe.getMessage());
        }
    }

    public static void main(String[] args) {
        new FriendGUI();
    }
}
