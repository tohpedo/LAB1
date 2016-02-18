import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class MysqlClient {

	public MysqlConnector connector;
	
	
	public MysqlClient(String uri){
		connector = new MysqlConnector(uri);
	}
	
	
//------------------------------------------------------------------------------------
	
	public void post(String message, int id){
		boolean success = connector.insert(message,id);
		if(!success){
			System.err.println("Insert failed");
		}
		else{
			System.out.println("Insert successful");
		}
	}
	
	
	public String get(int id){
		String message = connector.get(id);
		return message;
	}
	
	
	public Map<Integer, String> getAll(){
		Map<Integer, String> messages = connector.getAll();
		return messages;
		
	}
	
	
	public boolean delete(int id){
		boolean success = connector.delete(id);
		if(!success){
			System.err.println("Deletion failed");
			return false;
		}
		else{
			System.out.println("Deletion successful");
			return true;
		}
	}
	
	
	public boolean replicate(String uri){
		boolean success = connector.replicate(uri);
		if(!success){
			System.err.println("Replication failed");
			return false;
		}
		else{
			System.out.println("Replication successful");
			return true;
		}
	}
//---------------------HELPER METHODS---------------------------------------------------------------
	
	public void parse(String cmd) throws NumberFormatException{
		String[] cmd_arr = cmd.split(" ");
	
		// IF POST
		if (cmd_arr[0].equals("POST")){
			String message = "";
			for (int i = 2; i < cmd_arr.length; i++){
				message = message + cmd_arr[i] + " "; 
			}
			try{
				int id = Integer.parseInt(cmd_arr[1]);
				this.post(message, id);
			}
			catch(NumberFormatException e){
				System.out.println("Not a valid POST expression -- see example of valid POST command below");
				System.out.println("POST 99 Go home and eat lunch");
			}
		}
		
		// IF DELETE
		else if (cmd_arr[0].equals("DELETE")){
			try{
				int id = Integer.parseInt(cmd_arr[1]);
				this.delete(id);
			}
			catch(NumberFormatException e){
				System.out.println("Not a valid DELETE expression -- see example of valid DELETE command below");
				System.out.println("DELETE 99");
			}
		}
		
		// IF REPLICATE
		else if (cmd_arr[0].equals("REPLICATE")){
			this.replicate(cmd_arr[1]);
		}
		
		// IF GET 
		else if (cmd_arr[0].equals("GET")){
			if(cmd_arr.length >= 2){
				try{
					int id = Integer.parseInt(cmd_arr[1]);
					System.out.println(this.get(id));
				}
				catch(NumberFormatException e){
					System.out.println("Not a valid GET expression -- see example of valid GET command below");
					System.out.println("GET 99");
				}
			}
			else{
				Map<Integer, String> messages = this.getAll();
				Set<Integer> ids = messages.keySet();
				Iterator iter = ids.iterator();
				while(iter.hasNext()){
					int id = (int) iter.next();
					String message = messages.get(id);
					System.out.println("id: " + id + " ------ message: " + message);
				}
				
				
			}

		}
		
		// IF COMMAND NOT RECOGNIZED
		else{
			System.out.println("Acceptable commands are listed below");
			System.out.println("POST [id] [todo message]");
			System.out.println("GET [id]");
			System.out.println("GET");
			System.out.println("DELETE [id]");
			System.out.println("REPLICATE [URI]");
		}
		
		
	}
	
//-------------------------------------------------------------------------------
	
	public static void main(String[] args) {
		MysqlClient client = new MysqlClient("localhost/todo"); //default location of database.
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a command: [for help type HELP, to exit type EXIT]");
		while(sc.hasNextLine()){
			String cmd = sc.nextLine();
			if(cmd.equals("EXIT")){
				sc.close();		
				System.exit(0);
			}
			client.parse(cmd);
			System.out.println("Enter a command: [for help type HELP, to exit type EXIT]");
		}
		sc.close();		
		

		
	}


}