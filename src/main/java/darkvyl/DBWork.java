package darkvyl;

import java.sql.*;
import java.util.ArrayList;

class DBWork {
    private static final String url = "jdbc:mysql://hrr5mwqn9zs54rg4.chr7pe7iynqr.eu-west-1.rds.amazonaws.com:3306/h99dotx8gc3ijyks";
    private static final String user = "lfkhd0ep7a9ba7yk";
    private static final String password = "hs5wrhrs6ndrh5uo";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;

    static boolean Update(User usertodb) {
        String insert = "UPDATE users " +
                "SET nick = '" + usertodb.getNick()  + "', profession = '" + usertodb.getProfession() +
                "', level = '" + usertodb.getLevel() + "', attack = " + usertodb.getAttack()
                + " WHERE users.id = " + usertodb.getId();
        try {
            if(!isUserExist(usertodb.getId())) return false;
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            stmt.execute(insert);
            DragonBot.users = getAll();
            return true;
        } catch (Exception exp) {
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
    }
    static boolean Insert(User usertodb) {
        String insert = "INSERT INTO `users` (id, name, attack) VALUES ('" + usertodb.getId() + "','"
                + usertodb.getName() + "',0);";
        try {
            if(isUserExist(usertodb.getId())) return false;
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            stmt.execute(insert);
            DragonBot.users = getAll();
            return true;
        } catch (Exception exp) {
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
    }

     static boolean Delete(String UserId) {
         String insert = "DELETE FROM users WHERE id = " + UserId;
         try {
             if(!(isUserExist(UserId))) return false;
             con = DriverManager.getConnection(url, user, password);
             stmt = con.createStatement();
             stmt.execute(insert);
             DragonBot.users = getAll();
             return true;
         } catch (Exception exp) {
             return false;
         } finally {
             try {
                 con.close();
             } catch (SQLException se) { /*can't do anything */ }
             try {
                 stmt.close();
             } catch (SQLException se) { /*can't do anything */ }
         }
     }
    static ArrayList<User> getAll(){
        ArrayList<User> reply = new ArrayList<>();
        String query = "SELECT * FROM `users`";
        ResultSet rs;
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()){
                reply.add(new User().setId(rs.getString(1))
                        .setName(rs.getString(2))
                        .setNick(rs.getString(3))
                        .setProfession(rs.getString(4))
                        .setLevel(rs.getString(5))
                        .setAttack(rs.getInt(6)));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
        return reply;
    }
    private static boolean isUserExist(String id){
        String query = "SELECT attack FROM `users` WHERE id='"+id+"';";
        ResultSet rs;
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next()) return rs.getInt(1)!=-1;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
        return false;
    }
}