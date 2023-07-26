package Connection;
import java.sql.*;

public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;
    public PreparedStatement calculatePstat;
    public ResultSet calculateResult;

    public DBConnect(){
        try {
            String url = "jdbc:sqlserver://DESKTOP-DBLH6OP;database=Ekspedisi_Kel08;user=new_sa;password=polman";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
        }catch(Exception e){
            System.out.println("Error saat connect database: "+e);
        }
    }

    public static void main(String[] args){
        DBConnect connection = new DBConnect();
        System.out.println("Connection berhasil");
    }
}
