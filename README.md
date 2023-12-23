# Task-Manager
Data storage
Store tasks in “data.txt” in the working directory. You are free to devise any coherent structure to store the data. If the data.txt does not refer to the working directory, the project may not be graded. See the note in the working directory below. 
Update “data.txt” after each operation (add, view, delete, complete/incomplete).
Automatically create “data.txt” if it does not exist. 
Data persists across sessions. This means that if I start the program again and there are tasks stored in “data.txt,” I can view and interact with those tasks. 

The working directory is the directory from which the program is launched. The working directory may not be the directory that contains the source code. In IntelliJ, the working directory by default will be the project root directory. Relative paths are relative to the working directory. 

// file refers to data.txt in the working directory. 
File file = new File("data.txt");

// Create a data.txt file if it does not already exist
try {
   file.createNewFile();
} catch (IOException e) {
   e.printStackTrace();
}



Add task
Command: add <task_description>
Adds task with the specified description. 
Newly added tasks are marked incomplete and appended to the list. 

View tasks
Command: view
Displays tasks in a neat table format with position, description, and status. 

Delete task
Command: delete <task_position>
Deletes tasks at the position specified. 

Mark task complete/incomplete
Command: complete <task_position> or incomplete <task_position>
Marks task as complete or incomplete at the position specified. 

Instructions and help
Command: help
Displays clear usage instructions. 

Exit
Command: exit
Exits the application

Validate user inputs
Ensure valid inputs for all commands, with appropriate feedback for invalid inputs. Utilize Java Exception handling. 

JavaFX
Create a JavaFX GUI app that is compatible with “data.txt.” Once the app is started, the application displays tasks contained in “data.txt” in a visually appealing way.

Implement add, delete, and mark complete/incomplete functionalities 

