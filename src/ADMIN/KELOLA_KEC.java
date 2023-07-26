package ADMIN;

import Connection.DBConnect;
import component.ComboboxOpion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class KELOLA_KEC {
    private JPanel Panel_Atas;
    private JPanel Panel_Kiri;
    private JPanel Panel_Kanan;
    private JPanel Panel_Bawah;
    private JPanel Panel_Form;
    private JTextField txtIdKec;
    private JTextField txtNamaKec;
    private JTextField txtLongitude;
    private JTextField txtLatitude;
    private JPanel Panel_Button;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JScrollPane Panel_Data;
    private JTable tableKecamatan;
    private JPanel Panel_Cari;
    private JTextField txtCari;
    private JButton btnSearch;
    private JComboBox cbKabupaten;
    public JPanel PanelKecamatan;
    private JComboBox cbFilter;
    private DefaultTableModel tableModel;
    private DBConnect connection;

    public KELOLA_KEC() {
        connection = new DBConnect();

        tableModel = new DefaultTableModel();
        tableKecamatan.setModel(tableModel);
        addColumn();
        loadData();
        loadKabupaten();
        cbKabupaten.setSelectedItem(null);
        cbFilter.setSelectedItem(null);
        generateId();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbKabupaten.getSelectedItem() == null || txtNamaKec.getText().isEmpty() || txtLongitude.getText().isEmpty() ||txtLatitude.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdKec.getText();
                        String Nama = txtNamaKec.getText();
                        float Longitude = Float.parseFloat(txtLongitude.getText());
                        float Latitude = Float.parseFloat(txtLatitude.getText());

                        ComboboxOpion selectedOptionJenis = (ComboboxOpion) cbKabupaten.getSelectedItem();
                        String Kabupaten = selectedOptionJenis.getValue();

                        String checkQuery = "SELECT COUNT(*) AS count FROM tblKecamatan WHERE Nama_Kecamatan = '" + txtNamaKec.getText() + "'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();
                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            JOptionPane.showMessageDialog(null, "This subdistrict is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_InsertKecamatan(?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Kabupaten);
                        connection.pstat.setString(3, Nama);
                        connection.pstat.setFloat(4, Longitude);
                        connection.pstat.setFloat(5, Latitude);

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
                if(cbKabupaten.getSelectedItem() == null || txtNamaKec.getText().isEmpty() || txtLongitude.getText().isEmpty() ||txtLatitude.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdKec.getText();
                        String Nama = txtNamaKec.getText();
                        float Longitude = Float.parseFloat(txtLongitude.getText());
                        float Latitude = Float.parseFloat(txtLatitude.getText());

                        ComboboxOpion selectedOptionKecamatan = (ComboboxOpion) cbKabupaten.getSelectedItem();
                        String Kabupaten = selectedOptionKecamatan.getValue();

                        String checkQuery = "SELECT COUNT(*) AS count FROM tblKecamatan WHERE Nama_Kecamatan = '" + txtNamaKec.getText() + "' AND ID_Kecamatan != '"+ txtIdKec.getText()+"'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();
                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            JOptionPane.showMessageDialog(null, "This subdistrict is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        String storeProcedure = "{CALL sp_UpdateKecamatan(?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Kabupaten);
                        connection.pstat.setString(3, Nama);
                        connection.pstat.setFloat(4, Longitude);
                        connection.pstat.setFloat(5, Latitude);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data updated successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        clear();
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
                    ID = txtIdKec.getText();

                    String procedureCall = "{CALL sp_DeleteKecamatan(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, ID);

                    connection.pstat.execute();
                    connection.pstat.close();

                    JOptionPane.showMessageDialog(null, "Data deleted successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    clear();
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
                String selectedFilter = cbFilter.getSelectedItem() != null ? cbFilter.getSelectedItem().toString() : "";
                String searchText = txtCari.getText().trim();
                if (selectedFilter.isEmpty()) {
                    // Show a message box to inform the user to select a filter
                    JOptionPane.showMessageDialog(null, "Please select a filter before searching!", "Filter not selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (searchText.isEmpty()) {
                    // Show a message box to inform the user to enter a search term
                    JOptionPane.showMessageDialog(null, "Please enter a search term.", "Search term not entered", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(cbFilter.getSelectedItem().toString() == "Kecamatan"){
                    searchKecamatan(txtCari.getText());
                }else{
                    searchKabupaten();
                }
            }
        });
        tableKecamatan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableKecamatan.getSelectedRow();
                if(i == -1) {
                    return;
                }

                txtIdKec.setText((String) tableModel.getValueAt(i, 0));

                String kabupaten = (String) tableModel.getValueAt(i, 1);
                for (int x = 0; x < cbKabupaten.getItemCount(); x++) {
                    Object item = cbKabupaten.getItemAt(x);
                    String kabupatenIDCb = ((ComboboxOpion) item).getDisplay();
                    if (kabupatenIDCb.equals(kabupaten)) {
                        cbKabupaten.setSelectedItem(item);
                        break;
                    }
                }

                txtNamaKec.setText((String) tableModel.getValueAt(i, 2));
                txtLongitude.setText((String) tableModel.getValueAt(i,3));
                txtLatitude.setText((String) tableModel.getValueAt(i,4));

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnClear.setEnabled(true);
            }
        });
    }

    public void addColumn(){
        tableModel.addColumn("Subdistrict ID");
        tableModel.addColumn("District");
        tableModel.addColumn("Subdistrict Name");
        tableModel.addColumn("Longitude");
        tableModel.addColumn("Latitude");
    }

    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "SELECT kec.ID_Kecamatan, kab.Nama_KabKot, kec.Nama_Kecamatan, kec.Longitude, kec.Latitude " +
                    "FROM tblKecamatan kec " +
                    "JOIN tblKabupatenKota kab ON kec.ID_KabKot = kab.ID_KabKot";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[5]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("ID_Kecamatan");
                obj[1] = connection.result.getString("Nama_KabKot");
                obj[2] = connection.result.getString("Nama_Kecamatan");
                obj[3] = connection.result.getString("Longitude");
                obj[4] = connection.result.getString("Latitude");

                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (SQLException e){
            System.out.println("Error: " + e.toString());
        }
    }

    public void loadKabupaten() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT ID_KabKot, Nama_KabKot FROM tblKabupatenKota";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String ID = connection.result.getString("ID_KabKot");
                String Nama = connection.result.getString("Nama_KabKot");
                cbKabupaten.addItem(new ComboboxOpion( ID, Nama));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }

    public void clear(){
        generateId();
        loadData();
        cbKabupaten.setSelectedItem(null);
        txtNamaKec.setText("");
        txtLongitude.setText("");
        txtLatitude.setText("");
        txtCari.setText("");
        cbFilter.setSelectedItem(null);
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnClear.setEnabled(true);
    }

    public void generateId() {
        try {
            String query = "SELECT dbo.GenerateIdKecamatan() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtIdKec.setText(connection.result.getString("newId"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void searchKecamatan(String kecamatan){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getListKecamatan(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, kecamatan);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[5]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Kecamatan");
                obj[1] = connection.result.getString("Nama_KabKot");
                obj[2] = connection.result.getString("Nama_Kecamatan");
                obj[3] = connection.result.getString("Longitude");
                obj[4] = connection.result.getString("Latitude");

                tableModel.addRow(obj);
            }

            if(tableModel.getRowCount() == 0){
                JOptionPane.showMessageDialog(null,"Data not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void searchKabupaten(){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try{
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT kec.*, kab.Nama_KabKot FROM tblKecamatan kec " +
                    "JOIN tblKabupatenKota kab ON kec.ID_KabKot = kab.ID_KabKot " +
                    "WHERE kab.Nama_KabKot LIKE '%" +txtCari.getText() +"%'";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString("ID_Kecamatan");
                obj[1] = connection.result.getString("Nama_KabKot");
                obj[2] = connection.result.getString("Nama_Kecamatan");
                obj[3] = connection.result.getString("Longitude");
                obj[4] = connection.result.getString("Latitude");
                tableModel.addRow(obj);
            }

            if(tableModel.getRowCount() == 0){
                JOptionPane.showMessageDialog(null,"Data not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e){
            System.out.println("Terjadi error saat load data kecamatan: "+e);
        }
    }
}
