import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

	public static void main(String [] args) throws FileNotFoundException, ExInvalidDateFormat, ExInvalidNewDay {	
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("Please input the file pathname: ");
		String filepathname = in.nextLine();
		Scanner inFile = null;

		try {
			inFile = new Scanner(new File(filepathname));
			
			//The first command in the file must be to set the system date 
			//(eg. "startNewDay 03-Jan-2021"); and it cannot be undone
			String cmdLine1 = inFile.nextLine();
			String[] cmdLine1Parts = cmdLine1.split(" ");
			System.out.println("\n> "+cmdLine1);
			SystemDate.createTheInstance(cmdLine1Parts[1]);
			
			while (inFile.hasNext()) {
				String cmdLine = inFile.nextLine().trim();
				
				//Blank lines exist in data file as separators.  Skip them.
				if (cmdLine.equals("")) continue;  

				System.out.println("\n> "+cmdLine);
				
				//split the words in actionLine => create an array of word strings
				String[] cmdParts = cmdLine.split(" "); 
				
				if (cmdParts[0].equals("register"))
					(new CmdRegister()).execute(cmdParts);
				else if (cmdParts[0].equals("listMembers"))
					(new CmdListMembers()).execute(cmdParts);
				else if (cmdParts[0].equals("listEquipment"))
					(new CmdListEquipment()).execute(cmdParts);
				else if (cmdParts[0].equals("listMemberStatus"))
					(new CmdListMemberStatus()).execute(cmdParts);
				else if (cmdParts[0].equals("listEquipmentStatus"))
					(new CmdListEquipmentStatus()).execute(cmdParts);
				else if (cmdParts[0].equals("startNewDay"))
					(new CmdStartNewDay()).execute(cmdParts);
				else if (cmdParts[0].equals("create"))
					(new CmdCreateEquipment()).execute(cmdParts);
				else if (cmdParts[0].equals("arrive"))
					(new CmdAddSetOfEquipment()).execute(cmdParts);
				else if (cmdParts[0].equals("borrow"))
					(new CmdBorrowEquipment()).execute(cmdParts);
				else if (cmdParts[0].equals("request"))
					(new CmdRequestEquipment()).execute(cmdParts);
				else if (cmdParts[0].equals("undo"))
					RecordedCommand.undoOneCommand();
				else if (cmdParts[0].equals("redo"))
					RecordedCommand.redoOneCommand();
				else 
					throw new ExWrongCommand();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (ExWrongCommand e) {
			System.out.println(e.getMessage());
		}

		finally {
			if (inFile != null)
				inFile.close();
			in.close();
		}
	}
}