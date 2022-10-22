/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Model.Model;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class Control {
    private Model m = new Model();
    public void start(String[] inp) {
        m.generateAccounts();
        genMsg(inp);
    }
    private void genMsg(String[] inp) {
        m.setHashtag(inp);
        m.generateMessage();
    }

    public DefaultTableModel showData(DefaultTableModel model) {
        m.showData(model);
        return model;
    }

    public String returnStatus() {
        return  m.insertDB();
    }
}
