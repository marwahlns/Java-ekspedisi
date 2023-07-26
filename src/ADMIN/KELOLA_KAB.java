package ADMIN;

import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class KELOLA_KAB extends JFrame{
    private JTextField txtIdKab;
    private JTable tableKabupaten;
    private JButton btnSave;
    private JTextField txtCari;
    private JButton btnSearch;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JPanel Panel_Button;
    private JPanel Panel_Cari;
    public JPanel PanelKabupaten;
    private JPanel Panel_Atas;
    private JScrollPane Panel_Data;
    private JPanel Panel_Bawah;
    private JPanel Panel_Kanan;
    private JPanel Panel_Kiri;
    private JPanel Panel_Form;
    private JTextField txtNamaKab;
    private JTextField txtLongitude;
    private JTextField txtLatitude;
    private DefaultTableModel tableModel;
    private DBConnect connection;
public KELOLA_KAB() {
    setContentPane(PanelKabupaten);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setSize(500, 500);
    connection = new DBConnect();

    tableModel = new DefaultTableModel();
    tableKabupaten.setModel(tableModel);
    addColumn();
    clear();
    btnSave.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
                if(txtNamaKab.getText().isEmpty() || txtLongitude.getText().isEmpty() || txtLatitude.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdKab.getText();
                        String Nama = txtNamaKab.getText();
                        float Longitude = Float.parseFloat(txtLongitude.getText());
                        float Latitude = Float.parseFloat(txtLatitude.getText());

                        String checkQuery = "SELECT COUNT(*) AS count FROM tblKabupatenKota WHERE Nama_KabKot = '" + txtNamaKab.getText() + "'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();

                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            // Kabupaten already exists
                            JOptionPane.showMessageDialog(null, "This district already exists!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_InsertKabKot(?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Nama);
                        connection.pstat.setFloat(3, Longitude);
                        connection.pstat.setFloat(4, Latitude);

                        // Execute the stored procedure
                        connection.pstat.execute();
                        // Close the statement and connection
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data saved successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        clear();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while saving the data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtNamaKab.getText().isEmpty() || txtLongitude.getText().isEmpty() || txtLatitude.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdKab.getText();
                        String Nama = txtNamaKab.getText();
                        float Longitude = Float.parseFloat(txtLongitude.getText());
                        float Latitude = Float.parseFloat(txtLatitude.getText());

                        String checkQuery = "SELECT COUNT(*) AS count FROM tblKabupatenKota WHERE Nama_KabKot = '" + txtNamaKab.getText() + "'AND mamID_KabKot != '"+ txtIdKab.getText()+"'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();
                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            JOptionPane.showMessageDialog(null, "This district is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        String storeProcedure = "{CALL sp_UpdateKabKot(?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Nama);
                        connection.pstat.setFloat(3, Longitude);
                        connection.pstat.setFloat(4, Latitude);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        loadData();
                        clear();
                        JOptionPane.showMessageDialog(null, "Data updated successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception exc) {
                        System.out.println("Error: " + exc.toString());
                        JOptionPane.showMessageDialog(null, "An error occurred while updating the data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ID;
                    ID = txtIdKab.getText();

                    String procedureCall = "{CALL sp_DeleteKabKot(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, ID);

                    connection.pstat.execute();
                    connection.pstat.close();

                    loadData();
                    clear();
                    JOptionPane.showMessageDialog(null, "Data deleted successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex){
                    if (ex.getErrorCode() == 547) {
                        // Kode kesalahan 547 menunjukkan adanya REFERENCE constraint
                        JOptionPane.showMessageDialog(null, "Data cannot be deleted because it is referenced in another table.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        System.out.println("Error: " + ex.toString());
                        JOptionPane.showMessageDialog(null, "An error occurred!");
                    }
                } catch (Exception exc) {
                    System.out.println("Error: "+exc.toString());
                    JOptionPane.showMessageDialog(null, "An error occurred while deleting the data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = txtCari.getText().trim();
                if (searchText.isEmpty()) {
                    // Show a message box to inform the user to enter a search term
                    JOptionPane.showMessageDialog(null, "Please enter a search term.", "Search term not entered", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                search(txtCari.getText());
            }
        });
        tableKabupaten.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableKabupaten.getSelectedRow();
                if(i == -1) {
                    return;
                }

                txtIdKab.setText((String) tableModel.getValueAt(i, 0));
                txtNamaKab.setText((String) tableModel.getValueAt(i, 1));
                txtLongitude.setText((String) tableModel.getValueAt(i,2));
                txtLatitude.setText((String) tableModel.getValueAt(i,3));

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnClear.setEnabled(true);
            }
        });
    }
    public void addColumn(){
        tableModel.addColumn("District ID");
        tableModel.addColumn("District Name");
        tableModel.addColumn("Longitude");
        tableModel.addColumn("Latitude");
    }

    public void loadData(){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "SELECT * FROM tblKabupatenKota";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[4]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("ID_KabKot");
                obj[1] = connection.result.getString("Nama_KabKot");
                obj[2] = connection.result.getString("Longitude");
                obj[3] = connection.result.getString("Latitude");

                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (SQLException e){
            System.out.println("Error: " + e.toString());
        }
    }

    public void clear(){
        generateId();
        loadData();
        txtNamaKab.setText("");
        txtLongitude.setText("");
        txtLatitude.setText("");
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnClear.setEnabled(true);
    }

    public void generateId(){
        try {
            String query = "SELECT dbo.GenerateIdKabKot() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtIdKab.setText(connection.result.getString("newId"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void search(String kabupaten){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getListKabupatenKota(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, kabupaten);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[4]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("ID_KabKot");
                obj[1] = connection.result.getString("Nama_KabKot");
                obj[2] = connection.result.getString("Longitude");
                obj[3] = connection.result.getString("Latitude");

                tableModel.addRow(obj);
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }
}