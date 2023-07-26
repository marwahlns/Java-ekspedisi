package ADMIN;

import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class KELOLA_JENIS_PENGIRIMAN {
    public JPanel PanelJenisPengiriman;
    private JPanel Panel_Form;
    private JPanel Panel_Button;
    private JScrollPane Panel_Data;
    private JPanel Panel_Atas;
    private JPanel Panel_Kanan;
    private JPanel Panel_Bawah;
    private JPanel Panel_Kiri;
    private JPanel Panel_Cari;
    private JTable tableJenisPengiriman;
    private JTextField txtIdJenisP;
    private JButton btnSave;
    private JTextField txtJenisPengiriman;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtCari;
    private JButton btnSearch;
    private JTextField txtEstimasi;
    private JTextField txtHargaJenis;
    private DefaultTableModel tableModel;
    private DBConnect connection;
    public KELOLA_JENIS_PENGIRIMAN() {
        connection = new DBConnect();

        tableModel = new DefaultTableModel();
        tableJenisPengiriman.setModel(tableModel);
        addColumn();
        clear();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtJenisPengiriman.getText().isEmpty() || txtEstimasi.getText().isEmpty() || txtHargaJenis.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdJenisP.getText();
                        String Nama = txtJenisPengiriman.getText();
                        String Estimasi = txtEstimasi.getText();
                        float HargaJenis = Integer.parseInt(txtHargaJenis.getText());

                        String checkQuery = "SELECT COUNT(*) AS count FROM tblJenisPengiriman WHERE Nama_Jenis = '" + txtJenisPengiriman.getText() + "'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();
                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            JOptionPane.showMessageDialog(null, "This type is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_InsertJenisPengiriman(?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Nama);
                        connection.pstat.setString(3, Estimasi);
                        connection.pstat.setFloat(4, HargaJenis);

                        // Execute the stored procedure
                        connection.pstat.execute();
                        // Close the statement and connection
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data saved successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
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
                if (txtJenisPengiriman.getText().isEmpty() || txtEstimasi.getText().isEmpty() || txtHargaJenis.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdJenisP.getText();
                        String Nama = txtJenisPengiriman.getText();
                        String Estimasi = txtEstimasi.getText();
                        float HargaJenis = Integer.parseInt(txtHargaJenis.getText());

                        String checkQuery = "SELECT COUNT(*) AS count FROM tblJenisPengiriman WHERE Nama_Jenis = '" + txtJenisPengiriman.getText() + "' AND ID_Jenis != '"+ txtIdJenisP.getText() +"'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();
                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            JOptionPane.showMessageDialog(null, "This type is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        String storeProcedure = "{CALL sp_UpdateJenisPengiriman(?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Nama);
                        connection.pstat.setString(3, Estimasi);
                        connection.pstat.setFloat(4, HargaJenis);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data updated successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
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
                    ID = txtIdJenisP.getText();

                    String procedureCall = "{CALL sp_DeleteJenisPengiriman(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, ID);

                    connection.pstat.execute();
                    connection.pstat.close();

                    JOptionPane.showMessageDialog(null, "Data deleted successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
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
                String searchText = txtCari.getText().trim();
                if (searchText.isEmpty()) {
                    // Show a message box to inform the user to enter a search term
                    JOptionPane.showMessageDialog(null, "Please enter a search term.", "Search term not entered", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                search(txtCari.getText());
            }
        });
        tableJenisPengiriman.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableJenisPengiriman.getSelectedRow();
                if(i == -1) {
                    return;
                }

                txtIdJenisP.setText((String) tableModel.getValueAt(i, 0));
                txtJenisPengiriman.setText((String) tableModel.getValueAt(i, 1));
                txtEstimasi.setText((String) tableModel.getValueAt(i,2));
                Float hargaJenisFloat = (Float) tableModel.getValueAt(i, 3);

                String hargaJenisStr = hargaJenisFloat.toString();
                txtHargaJenis.setText(String.valueOf(hargaJenisStr));

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnClear.setEnabled(true);
            }
        });
    }
    public void addColumn(){
        tableModel.addColumn("Shipment ID");
        tableModel.addColumn("Type of Shippment");
        tableModel.addColumn("Estimation");
        tableModel.addColumn("Price");
    }

    public void loadData(){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "SELECT * FROM tblJenisPengiriman";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[4]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Jenis");
                obj[1] = connection.result.getString("Nama_Jenis");
                obj[2] = connection.result.getString("Estimasi");
                obj[3] = connection.result.getFloat("HargaJenis");

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
        txtJenisPengiriman.setText("");
        txtEstimasi.setText("");
        txtHargaJenis.setText("");
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnClear.setEnabled(true);
    }

    public void generateId(){
        try {
            String query = "SELECT dbo.GenerateIdJPengiriman() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtIdJenisP.setText(connection.result.getString("newId"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void search(String JenisPengiriman){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getJenisPengiriman(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, JenisPengiriman);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[5]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Jenis");
                obj[1] = connection.result.getString("Nama_Jenis");
                obj[2] = connection.result.getString("Estimasi");
                obj[3] = connection.result.getFloat("HargaJenis");

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
}