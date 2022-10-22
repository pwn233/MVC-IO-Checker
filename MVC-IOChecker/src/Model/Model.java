/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class Model {
    private int amount = 926;
    private String[] account = new String[amount+1];
    private String[] message = new String[amount+1];
    private String[] hashtag = new String[5];
    private String[] alphabetSmall = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private String[] alphabetCapital = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    public void generateAccounts() {
        //create account, small aphabet for account and account length between 6 and not over 14.
        String dummy = "";
        for(int i = 1; i< account.length ; i++) {
            dummy = "";
            Random rd = new Random();
            for(int j = 0; j < rd.nextInt(10)+5; j++) {
                dummy += alphabetSmall[rd.nextInt(alphabetSmall.length)];
            }
            account[i] = dummy;
            //System.out.println(i+" : "+uncheckedAccount[i]);
        }
    }
    public void setHashtag(String[] inp) {
        String dummy;
        for(int i = 0; i<5; i++) {
            dummy = "";
            for(int k = 0; k < inp[i].length(); k++) {
            //System.out.println("word "+i+" char at : "+k);
                for(int j = 0; j < alphabetSmall.length; j++) {
                    String x = String.valueOf(inp[i].charAt(k));
                    //System.out.println("x : " +x);
                    if(x.equals(alphabetSmall[j]) || x.equals(alphabetCapital[j])) {
                        dummy += alphabetCapital[j];
                        break;
                    } 
                }
            }
            hashtag[i] = dummy;
            //System.out.println("Hashtag : "+i+" "+hashtag[i]);
        }
    }
    public void generateMessage() {
        //create message randomly for every account (also with the input hashtag at the end of message)
        String dummy;
        for(int i = 1; i< account.length ; i++) {
            dummy = "";
            Random rd = new Random();
            for(int j = 0; j < rd.nextInt(10)+5; j++) {
                dummy += alphabetCapital[rd.nextInt(alphabetCapital.length)];
            }
            //start random hashtag and random input hashtag between 30 - 140 alphabets
            int lucky, pos; //lucky stand for random input hashtag, pos stand for create hashtag randomly from alphabet
            int border = rd.nextInt(110)+30; // between 30 - 140
            int space = rd.nextInt(5)+10; // adjust hashtag length around 10 - 15 alphabets
            while(border >= 0) {
                lucky = rd.nextInt(14)+7;
                dummy += " #";
                border -= 2;
                if(lucky > 7) {
                    pos = rd.nextInt(5);
                    dummy += hashtag[pos];
                    border -= hashtag[pos].length();
                } else {
                    for(int l = 0; l < space; l++) {
                        dummy += alphabetCapital[rd.nextInt(alphabetCapital.length)];
                    }
                    border -= space;
                }
            }
            message[i] = dummy;
            System.out.println(i+" : "+message[i]);
        }
    }
    public int checkIO(String message) {
        int iocontain = 0;
        for(int i = 1; i< message.length() ; i++) {
            if(message.charAt(i) == 'I' || message.charAt(i) == 'O' )
                iocontain++;
        }
        return iocontain;
    }
    public String insertDB() {
        String query = "";
        String status = "";
        for(int i = 1; i< account.length ; i++) {
        //query valid check in phpmyadmin 
        System.out.print("account : "+account[i]+" message : "+message[i]+" contain IO : "+checkIO(message[i])+"\n");
            query = String.format("INSERT INTO accountdb (id, account, io_contain, message_hashtag)VALUES (NULL, '%1$s', '%2$d', '%3$s')", account[i], checkIO(message[i]), message[i]);
            try {
                DBConnect conn = new DBConnect();
                boolean result = conn.execute(query);
                if (result) {
                    status = "complete";
                } else {
                    status = "fail (insertDataDB)";
                }
                conn.close();
            } catch (Exception ex) {
                status = "Error Update: " + ex;
            }
        }
        return status;
    }

    public DefaultTableModel showData(DefaultTableModel model) {
        String query = "";
        try {
            DBConnect conn = new DBConnect();
            query = String.format("SELECT * FROM accountdb ORDER BY io_contain DESC LIMIT 10");
            ResultSet rs = conn.getResult(query);
            while (rs.next()) {
                String id = rs.getString("id");
                String acc = rs.getString("account");
                String io = Integer.toString(rs.getInt("io_contain"));
                String msg = rs.getString("message_hashtag");
                String[] row = {id, acc, io, msg};
                model.addRow(row);
                // critical variable name, beware of it!
            }
            conn.close();
        } catch (Exception ex) {
            //sss
        }
        return model;
    }
    
}
