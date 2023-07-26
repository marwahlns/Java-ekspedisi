package KASIR;

import Connection.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class KONFIRMASI_PAKET extends JFrame{
    private JPanel Panel_Table;
    private JTable tableTrsPengiriman;
    private JTextField txtResi;
    private JButton btnKonfirmasi;
    public JPanel PanelKonfirmasi;
    private JTable tableTrsPelacakan;
    DefaultTableModel tableModel = new DefaultTableModel();
    DefaultTableModel model = new DefaultTableModel();
    private DBConnect connection;

    public KONFIRMASI_PAKET() {
        connection = new DBConnect();
        tableTrsPengiriman.setModel(tableModel);
        tableTrsPelacakan.setModel(model);
        addColumnPengiriman();
        addColumnPelacakan();
        loadData();
        txtResi.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                searchResi(txtResi.getText());
            }
        });
        tableTrsPengiriman.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableTrsPengiriman.getSelectedRow();
                if (i == -1) {
                    return;
                }
                txtResi.setText((String) tableModel.getValueAt(i, 0));
                loadDataPelacakan();
            }
        });
        btnKonfirmasi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String resi = txtResi.getText();
                    int idPelacakan = 0;
                    String idgudang = "";
                    String infoPaket = "";
                    String informasiPaket = "";

                    //mendapatkan id gudang saat ini
                    String namagud = DASHBOARD_KASIR.namaGudang;
                    String query1 = "SELECT ID_Gudang FROM tblGudang WHERE Nama_Gudang = '" + namagud + "'";
                    connection.stat = connection.conn.createStatement();
                    connection.result = connection.stat.executeQuery(query1);
                    while (connection.result.next()) {
                        idgudang = connection.result.getString("ID_Gudang");
                    }

                    //mendapatkan id pelacakan
                    String query2 = "SELECT ID_Pelacakan FROM tblTrsPelacakan WHERE Nomor_Resi = '"+ resi +"' AND ID_Gudang = '"+ idgudang +"'";
                    connection.stat = connection.conn.createStatement();
                    connection.result = connection.stat.executeQuery(query2);
                    while (connection.result.next()) {
                        idPelacakan = connection.result.getInt("ID_Pelacakan");
                    }

                    //mendapatkan informasi paket dari gudang sebelumnya
                    String query3 = "SELECT Informasi_Paket FROM tblTrsPelacakan WHERE ID_Pelacakan = " + (idPelacakan - 1 ) +" AND Nomor_Resi = '"+ resi +"'";
                    connection.stat = connection.conn.createStatement();
                    connection.result = connection.stat.executeQuery(query3);
                    infoPaket = null;
                    while (connection.result.next()) {
                        infoPaket = connection.result.getString("Informasi_Paket");
                    }

                    if (infoPaket == null) {
                        // Lanjutkan dengan menjalankan stored procedure
                        String storeProcedure = "{CALL sp_UpdateTrsPelacakan(?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, resi);
                        connection.pstat.setString(2, idgudang);

                        // Execute the stored procedure
                        connection.pstat.execute();
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data confirmation successfully!");
                        return;
                    }

                    String queryCheck = "SELECT Informasi_Paket FROM tblTrsPelacakan WHERE Nomor_Resi = '" + resi + "' AND ID_Gudang = '" + idgudang + "'";
                    connection.stat = connection.conn.createStatement();
                    connection.result = connection.stat.executeQuery(queryCheck);
                    while (connection.result.next()) {
                        informasiPaket = connection.result.getString("Informasi_Paket");
                    }

                    if (informasiPaket.equals("Diterima")) {
                        // Tampilkan pesan jika informasi paket sudah 'Diterima'
                        JOptionPane.showMessageDialog(null, "You have already confirmed this package!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (infoPaket.equals("Dikirim")) {
                        JOptionPane.showMessageDialog(null, "Failed to confirm!");
                        return;
                    }

                    // Lanjutkan dengan menjalankan stored procedure
                    String storeProcedure = "{CALL sp_UpdateTrsPelacakan(?,?)}";
                    connection.pstat = connection.conn.prepareCall(storeProcedure);
                    connection.pstat.setString(1, resi);
                    connection.pstat.setString(2, idgudang);

                    // Execute the stored procedure
                    connection.pstat.execute();
                    // Close the statement and connection
                    connection.pstat.close();

                    JOptionPane.showMessageDialog(null, "Data confirmation successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    public void addColumnPengiriman(){
        tableModel.addColumn("Receipt Number");
        tableModel.addColumn("Date");
        tableModel.addColumn("Type of Shipment");
        tableModel.addColumn("Courier ID");
        tableModel.addColumn("Promo ID");
        tableModel.addColumn("Sender Name");
        tableModel.addColumn("Sender Address");
        tableModel.addColumn("Sender Phone Number");
        tableModel.addColumn("Receiver Name");
        tableModel.addColumn("Receiver Subdistrict");
        tableModel.addColumn("Receiver Address");
        tableModel.addColumn("Receiver Phone Number");
        tableModel.addColumn("Dictance");
        tableModel.addColumn("Weight");
        tableModel.addColumn("Total");
    }

    public void addColumnPelacakan(){
        model.addColumn("Tracking ID");
        model.addColumn("Receipt Number");
        model.addColumn("Tanggal");
        model.addColumn("Warehouse ID");
        model.addColumn("Information");
    }

    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String idgudang = "";
            String namagud = DASHBOARD_KASIR.namaGudang;
            String query = "SELECT ID_Gudang FROM tblGudang WHERE Nama_Gudang = '" + namagud + "'";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                idgudang = connection.result.getString("ID_Gudang");
            }

            String functionCall = "SELECT * FROM dbo.GetResiByGudang(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, idgudang);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[15]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("Nomor_Resi");
                obj[1] = connection.result.getString("Tanggal");
                obj[2] = connection.result.getString("ID_Jenis");
                obj[3] = connection.result.getString("ID_Gudang");
                obj[4] = connection.result.getString("ID_Promo");
                obj[5] = connection.result.getString("Nama_Pengirim");
                obj[6] = connection.result.getString("Alamat_Pengirim");
                obj[7] = connection.result.getString("No_Telp_Pengirim");
                obj[8] = connection.result.getString("Nama_Penerima");
                obj[9] = connection.result.getString("ID_Kecamatan");
                obj[10] = connection.result.getString("Alamat_Penerima");
                obj[11] = connection.result.getString("No_Telp_Penerima");
                obj[12] = connection.result.getFloat("Jarak");
                obj[13] = connection.result.getFloat("Berat");
                obj[14] = connection.result.getFloat("Total_Harga");
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

    public void loadDataPelacakan(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            String query = "SELECT * FROM tblTrsPelacakan WHERE Nomor_Resi = ?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, txtResi.getText());
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString("ID_Pelacakan");
                obj[1] = connection.result.getString("Nomor_Resi");
                obj[2] = connection.result.getString("Tanggal");
                obj[3] = connection.result.getString("ID_Gudang");
                obj[4] = connection.result.getString("Informasi_Paket");
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (SQLException ex){
            System.out.println("Error: " + ex.toString());
        }
    }

    public void searchResi(String Resi){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getResi(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, Resi);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[15]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("Nomor_Resi");
                obj[1] = connection.result.getString("Tanggal");
                obj[2] = connection.result.getString("ID_Jenis");
                obj[3] = connection.result.getString("ID_Gudang");
                obj[4] = connection.result.getString("ID_Promo");
                obj[5] = connection.result.getString("Nama_Pengirim");
                obj[6] = connection.result.getString("Alamat_Pengirim");
                obj[7] = connection.result.getString("No_Telp_Pengirim");
                obj[8] = connection.result.getString("Nama_Penerima");
                obj[9] = connection.result.getString("ID_Kecamatan");
                obj[10] = connection.result.getString("Alamat_Penerima");
                obj[11] = connection.result.getString("No_Telp_Penerima");
                obj[12] = connection.result.getString("Jarak");
                obj[13] = connection.result.getString("Berat");
                obj[14] = connection.result.getString("Total_Harga");

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