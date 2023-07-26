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

public class KELOLA_GUDANG extends JFrame{
    public JPanel PanelGudang;
    private JPanel Panel_Form;
    private JPanel Panel_Button;
    private JScrollPane Panel_Data;
    private JTextField txtIdGudang;
    private JComboBox cbKecamatan;
    private JButton btnSave;
    private JTable tableGudang;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtNamaGudang;
    private JTextField txtLongitude;
    private JTextField txtLatitude;
    private JTextField txtCari;
    private JButton btnSearch;
    private JPanel Panel_Atas;
    private JPanel Panel_Kanan;
    private JPanel Panel_Kiri;
    private JPanel Panel_Bawah;
    private JPanel Panel_Cari;
    private JComboBox cbFilter;
    private DefaultTableModel tableModel;
    private DBConnect connection;

    public KELOLA_GUDANG() {
        connection = new DBConnect();

        tableModel = new DefaultTableModel();
        tableGudang.setModel(tableModel);
        addColumn();
        loadKecamatan();
        cbKecamatan.setSelectedItem(null);
        cbFilter.setSelectedItem(null);
        generateId();
        clear();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbKecamatan.getSelectedItem() == null || txtNamaGudang.getText().isEmpty() || txtLongitude.getText().isEmpty() ||txtLatitude.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdGudang.getText();
                        String Nama = txtNamaGudang.getText();
                        float Longitude = Float.parseFloat(txtLongitude.getText());
                        float Latitude = Float.parseFloat(txtLatitude.getText());

                        ComboboxOpion selectedOptionGudang = (ComboboxOpion) cbKecamatan.getSelectedItem();
                        String Kecamatan = selectedOptionGudang.getValue();

                        // Prepare the query to check if the warehouse already exists
                        String checkQuery = "SELECT COUNT(*) AS count FROM tblGudang WHERE Nama_Gudang = '" + txtNamaGudang.getText() + "'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();

                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            // jika nama gudang sama
                            JOptionPane.showMessageDialog(null, "This warehouse is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_InsertGudang(?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Kecamatan);
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
                if(cbKecamatan.getSelectedItem() == null || txtNamaGudang.getText().isEmpty() || txtLongitude.getText().isEmpty() ||txtLatitude.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdGudang.getText();
                        String Nama = txtNamaGudang.getText();
                        float Longitude = Float.parseFloat(txtLongitude.getText());
                        float Latitude = Float.parseFloat(txtLatitude.getText());

                        ComboboxOpion selectedOptionJenis = (ComboboxOpion) cbKecamatan.getSelectedItem();
                        String Kecamatan = selectedOptionJenis.getValue();

                        String checkQuery = "SELECT COUNT(*) AS count FROM tblGudang WHERE Nama_Gudang = '" + txtNamaGudang.getText() + "'AND ID_Gudang != '"+ txtIdGudang.getText()+"'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();
                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            JOptionPane.showMessageDialog(null, "This warehouse is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        String storeProcedure = "{CALL sp_UpdateGudang(?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Kecamatan);
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
                    ID = txtIdGudang.getText();

                    String procedureCall = "{CALL sp_DeleteGudang(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, ID);

                    connection.pstat.execute();
                    connection.pstat.close();

                    JOptionPane.showMessageDialog(null, "Data deleted successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    clear();
                } catch (SQLException exc) {
                    if (exc.getErrorCode() == 547) {
                        // Kode kesalahan 547 menunjukkan adanya REFERENCE constraint
                        JOptionPane.showMessageDialog(null, "Data cannot be deleted because it is referenced in another table.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        System.out.println("Error: " + exc.toString());
                        JOptionPane.showMessageDialog(null, "An error occurred!");
                    }
                } catch (Exception exc) {
                    System.out.println("Error: " + exc.toString());
                    JOptionPane.showMessageDialog(null, "An error occurred!");
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

                if(cbFilter.getSelectedItem().toString() == "Warehouse") {
                    searchGudang(txtCari.getText());
                }else{
                    searchKecamatan();
                }
            }
        });
        tableGudang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableGudang.getSelectedRow();
                if(i == -1) {
                    return;
                }

                txtIdGudang.setText((String) tableModel.getValueAt(i, 0));

                String kecamatan = (String) tableModel.getValueAt(i, 1);
                for (int x = 0; x < cbKecamatan.getItemCount(); x++) {
                    Object item = cbKecamatan.getItemAt(x);
                    String kecamatanIDCb = ((ComboboxOpion) item).getDisplay();
                    if (kecamatanIDCb.equals(kecamatan)) {
                        cbKecamatan.setSelectedItem(item);
                        break;
                    }
                }

                txtNamaGudang.setText((String) tableModel.getValueAt(i, 2));
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
        tableModel.addColumn("Warehouse ID");
        tableModel.addColumn("Subdistrict");
        tableModel.addColumn("Warehouse Name");
        tableModel.addColumn("Longitude");
        tableModel.addColumn("Latitude");
    }

    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "SELECT g.ID_Gudang, k.Nama_Kecamatan, g.Nama_Gudang, g.Longitude, g.Latitude " +
                    "FROM tblGudang g " +
                    "JOIN tblKecamatan k ON g.ID_Kecamatan = k.ID_Kecamatan";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString("ID_Gudang");
                obj[1] = connection.result.getString("Nama_Kecamatan");
                obj[2] = connection.result.getString("Nama_Gudang");
                obj[3] = connection.result.getString("Longitude");
                obj[4] = connection.result.getString("Latitude");

                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    // method untuk menampilkan daftar nama kecamatan pada cbKecamatan
    public void loadKecamatan() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT ID_Kecamatan, Nama_Kecamatan FROM tblKecamatan";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String ID = connection.result.getString("ID_Kecamatan");
                String Nama = connection.result.getString("Nama_Kecamatan");
                cbKecamatan.addItem(new ComboboxOpion( ID, Nama));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }

    // method untuk membersihkan form
    public void clear(){
        generateId();
        loadData();
        cbKecamatan.setSelectedItem(null);
        txtNamaGudang.setText("");
        txtLongitude.setText("");
        txtLatitude.setText("");
        txtCari.setText("");
        cbFilter.setSelectedItem(null);
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnClear.setEnabled(true);
    }

    // method untuk membuat id otomatis
    public void generateId() {
        try {
            String query = "SELECT dbo.GenerateIdGudang() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtIdGudang.setText(connection.result.getString("newId"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    // method untuk filter data berdasarkan nama gudang
    public void searchGudang(String Gudang){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getGudang(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, Gudang);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[5]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Gudang");
                obj[1] = connection.result.getString("Nama_Kecamatan");
                obj[2] = connection.result.getString("Nama_Gudang");
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

    // method untuk filter data berdasarkan nama kecamatan
    public void searchKecamatan(){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try{
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT gud.*, kec.Nama_Kecamatan FROM tblGudang gud " +
                    "JOIN tblKecamatan kec ON gud.ID_Kecamatan = kec.ID_Kecamatan " +
                    "WHERE kec.Nama_Kecamatan LIKE '%" +txtCari.getText() +"%'";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString("ID_Gudang");
                obj[1] = connection.result.getString("Nama_Kecamatan");
                obj[2] = connection.result.getString("Nama_Gudang");
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
            System.out.println("An error occurred while loading data : "+e);
        }
    }
}