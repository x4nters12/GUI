package pl.chomik.app;

import java.io.File;
import java.sql.*;

public class DatabaseHandler {
    public static Connection connect;

    public static Connection Connector(){
        try{
            Class.forName("org.postgresql.Driver");

            connect = DriverManager.getConnection("jdbc:postgresql://192.168.0.34:5432/Chomik", "postgres", "postgres");
            if(connect!=null){
                System.out.println("[ DBH OK ] CONNECT > POŁĄCZONO Z BAZĄ DANYCH [connect:" + connect + "]");
                return connect;
            }
            else{
                System.out.println("[ DBH FAILED ] CONNECT > NIEUDANA PRÓBA ŁĄCZENIA Z BAZĄ DANYCH");
                return null;
            }
        }
        catch(ClassNotFoundException e){
            System.out.println("[ DBH FAILED ] CONNECT > NIE ZNALEZIONO STEROWNIKA BAZY DANYCH");
            return null;
        }
        catch(SQLException e){
            System.out.println("[ DBH FAILED ] CONNECT > SQLException");
            return null;
        }
    }

    public static Boolean CloseConnection(){
        try{
            connect.close();
            System.out.println("[ DBH OK ] CLOSE > POMYŚLNIE ROZŁĄCZONO Z BAZĄ DANYCH");
            return true;
        }
        catch(SQLException e){
            System.out.println("[ DBH FALSE ] CLOSE > ROZŁĄCZANIE Z BAZĄ DANYCH NIE POWIODŁO SIĘ");
            return false;
        }
    }

    public static ResultSet Query(String sqlstring){
        try{
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstring);
            System.out.println("[ DBH OK ] UPDATE > ODCZYTANO DANE Z BAZY");
            return rs;
        }
        catch(SQLException e){
            System.out.println("[ DBH FAILED ] UPDATE > SQLException");
            return null;
        }
    }

    private static String getDBL(){
        Ini ini = Ini(new File("/pl/chomik/app/hamster.ini"));
        return "";
    }
}
