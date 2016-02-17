import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



public class MysqlClient {

	public MysqlConnector connector;
	
	public MysqlClient(){
		connector = new MysqlConnector();
	}
	
	
	public void runQueries() throws SQLException{
		
		
		
		
		String todo = "POST [69] [clean your room]";
		
		
		
		
		}
	
	
	
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
	
	
	
	
	
	public static void main(String[] args) {
		MysqlClient client = new MysqlClient();
		try {
			Scanner sc = new Scanner(System.in);
			//while(sc.hasNextLine()){
				//System.out.println(sc.nextLine());
			//}
			client.runQueries();
			//client.post("do your homeworkasdfasdfasd", 5);
			System.out.println(client.getAll());
			System.out.println(client.delete(69));
			System.out.println(client.getAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}