package KASIR;

import Connection.DBConnect;
import component.ComboboxOpion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jgrapht.Graph;
//import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultEdge;


public class TRANSAKSI_PENGIRIMAN extends JFrame{
    public JPanel PanelPengiriman;
    private JPanel Panel_Atas;
    private JPanel Panel_Kanan;
    private JPanel Panel_Kiri;
    private JPanel Panel_Bawah;
    private JPanel Panel_Form;
    private JTextField txtResi;
    private JComboBox cbJenisPengiriman;
    private JTextField txtNamaPengirim;
    private JTextField txtJarak;
    private JTextField txtBerat;
    private JTextField txtTotal;
    private JTextField txtIdGudang;
    private JTextField txtTelpPengirim;
    private JTextField txtAlamatPengirim;
    private JTextField txtNamaPenerima;
    private JTextField txtAlamatPenerima;
    private JTextField txtTelpPenerima;
    private JTextField txtBayar;
    private JButton btnClear;
    private JButton btnSave;
    private JPanel Panel_Button;
    private JTextField txtTanggal;
    private JPanel Panel_DetailPenerima;
    private JPanel Panel_DetailPengirim;
    private JTextField txtIdPromo;
    private JComboBox cbKecamatan;
    private JTextField txtKembali;
    private DBConnect connection;
    DefaultTableModel datajasper = new DefaultTableModel();
    private String resi, tanggal, idGudang, idPromo, namaPengirim, alamatPengirim, telpPengirim,
            namaPenerima, alamatPenerima, telpPenerima;
    private float jarak, berat, total;
    private Graph<String, DefaultEdge> graph;

    public TRANSAKSI_PENGIRIMAN() {
        setContentPane(PanelPengiriman);
        setSize(1000,1000);
        connection = new DBConnect();
        loadJenisPengiriman();
        loadKecamatan();
        cbJenisPengiriman.setSelectedItem(null);
        cbKecamatan.setSelectedItem(null);
        txtIdGudang.setText(DASHBOARD_KASIR.namaGudang);

        generateId();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        txtTanggal.setText(formatter.format(calendar.getTime()));

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbJenisPengiriman.getSelectedItem()==null || txtNamaPengirim.getText().isEmpty() || txtAlamatPengirim.getText().isEmpty() || txtTelpPengirim.getText().isEmpty() || cbKecamatan.getSelectedItem()==null
                        || txtNamaPenerima.getText().isEmpty() || txtAlamatPenerima.getText().isEmpty() || txtTelpPenerima.getText().isEmpty() || txtJarak.getText().isEmpty() || txtBerat.getText().isEmpty() || txtTotal.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else if (txtTelpPengirim.getText().length() > 13 || txtTelpPenerima.getText().length() > 13){
                    JOptionPane.showMessageDialog(null, "Nomor Telepon tidak boleh lebih dari 13 digit!", "Warning",JOptionPane.WARNING_MESSAGE);
                }else{
                    try{
                        txtTanggal.setText(formatter.format(calendar.getTime()));

                        resi = txtResi.getText();
                        tanggal = txtTanggal.getText();
                        idGudang = txtIdGudang.getText();
                        String query = "SELECT ID_Gudang FROM tblGudang WHERE Nama_Gudang = ?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, DASHBOARD_KASIR.namaGudang);
                        connection.result = connection.pstat.executeQuery();
                        while (connection.result.next()) {
                            idGudang = connection.result.getString("ID_Gudang");
                        }
                        idPromo = txtIdPromo.getText();
                        namaPengirim = txtNamaPengirim.getText();
                        alamatPengirim = txtAlamatPengirim.getText();
                        telpPengirim = txtTelpPengirim.getText();
                        namaPenerima = txtNamaPenerima.getText();
                        alamatPenerima = txtAlamatPenerima.getText();
                        telpPenerima = txtTelpPenerima.getText();
                        jarak = Float.parseFloat(txtJarak.getText());
                        berat = Float.parseFloat(txtBerat.getText());
                        total = Float.parseFloat(txtTotal.getText());

                        ComboboxOpion selectedOptionJenis = (ComboboxOpion) cbJenisPengiriman.getSelectedItem();
                        String JenisP = selectedOptionJenis.getValue();

                        ComboboxOpion selectedOptionKecamatan = (ComboboxOpion) cbKecamatan.getSelectedItem();
                        String Kecamatan = selectedOptionKecamatan.getValue();

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_InsertTrsPengiriman(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, resi);
                        connection.pstat.setString(2, tanggal);
                        connection.pstat.setString(3, JenisP);
                        connection.pstat.setString(4, idGudang);
                        connection.pstat.setString(5, idPromo);
                        connection.pstat.setString(6, namaPengirim);
                        connection.pstat.setString(7, alamatPengirim);
                        connection.pstat.setString(8, telpPengirim);
                        connection.pstat.setString(9, namaPenerima);
                        connection.pstat.setString(10, Kecamatan);
                        connection.pstat.setString(11, alamatPenerima);
                        connection.pstat.setString(12, telpPenerima);
                        connection.pstat.setFloat(13, jarak);
                        connection.pstat.setFloat(14, berat);
                        connection.pstat.setFloat(15, total);

                        // Execute the stored procedure
                        connection.pstat.execute();
                        // Close the statement and connection
                        connection.pstat.close();

                        // PANGGIL PELACAKAN
                        insertPelacakan();

                        JOptionPane.showMessageDialog(null, "Data saved successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        PrintResi();
                        clear();
                    }catch (Exception ex){
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan transaksi pengiriman!");
                    }
                }
            }
        });
        txtJarak.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    String IDGudang = txtIdGudang.getText();
                    String query = "SELECT ID_Gudang FROM tblGudang WHERE Nama_Gudang = ?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, DASHBOARD_KASIR.namaGudang);
                    connection.result = connection.pstat.executeQuery();
                    while (connection.result.next()) {
                        IDGudang = connection.result.getString("ID_Gudang");
                    }

                    ComboboxOpion selectedOptionKecamatan = (ComboboxOpion) cbKecamatan.getSelectedItem();
                    String IDKecamatan = selectedOptionKecamatan.getValue();

                    float jarak = hitungJarak(IDGudang, IDKecamatan);
                    txtJarak.setText(String.valueOf(jarak));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghitung jarak!");
                }
            }
        });
        txtTotal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    float berat = Float.parseFloat(txtBerat.getText());
                    float jarak = Float.parseFloat(txtJarak.getText());

                    String jenis = null;
                    if (cbJenisPengiriman.getSelectedItem() != null) {
                        ComboboxOpion selectedOptionJenis = (ComboboxOpion) cbJenisPengiriman.getSelectedItem();
                        jenis = selectedOptionJenis.getValue();
                    }

                    float total;
                    total = hitungTotal(berat, jarak, jenis);
                    txtTotal.setText(String.valueOf(total));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghitung total!");
                }
            }
        });
        txtBayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitungKembalian();
            }
        });
    }

    //menampilkan data jenis pengiriman kedalam ComboBox
    public void loadJenisPengiriman() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT ID_Jenis, Nama_Jenis FROM tblJenisPengiriman";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String idJenis = connection.result.getString("ID_Jenis");
                String namaJenis = connection.result.getString("Nama_Jenis");
                ComboboxOpion option = new ComboboxOpion(idJenis, namaJenis);
                cbJenisPengiriman.addItem(option);
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }

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

    public void generateId(){
        try {
            String query = "SELECT dbo.getNoResi(?) AS newId";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, txtIdGudang.getText());
            connection.result = connection.pstat.executeQuery();

            // perbarui data
            while (connection.result.next()) {
                txtResi.setText(connection.result.getString("newId"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void clear(){
        generateId();
        cbJenisPengiriman.setSelectedItem(null);
        cbKecamatan.setSelectedItem(null);
        txtIdPromo.setText("");
        txtNamaPengirim.setText("");
        txtAlamatPengirim.setText("");
        txtTelpPengirim.setText("");
        txtNamaPenerima.setText("");
        txtAlamatPenerima.setText("");
        txtTelpPenerima.setText("");
        txtJarak.setText("");
        txtBerat.setText("");
        txtTotal.setText("");
        txtBayar.setText("");
        txtKembali.setText("");
    }

    private float hitungJarak(String idKaryawan, String idKecamatan) throws SQLException {
        float distance = 0;

        String query = "SELECT dbo.getDistance(?,?) AS Distance";
        connection.pstat = connection.conn.prepareStatement(query);
        connection.pstat.setString(1, idKaryawan);
        connection.pstat.setString(2, idKecamatan);

        connection.result = connection.pstat.executeQuery();

        if (connection.result.next()) {
            distance = connection.result.getFloat("Distance");
        }

        connection.pstat.close();
        connection.result.close();

        return distance;
    }

    private float hitungTotal(float berat, float jarak, String jenis) throws SQLException {
        float berattotal = 0;
        float jaraktotal = 0;
        float tarifdasarjarak = 1000;

        if (jarak <= 50) {
            jaraktotal = tarifdasarjarak;
        } else {
            float jaraklebih = jarak - 50;
            jaraktotal = (float) (tarifdasarjarak + (Math.ceil(jaraklebih / 10) * 500));
        }

        // Mendapatkan harga jenis pengiriman
        String query = "SELECT HargaJenis FROM tblJenisPengiriman WHERE Nama_Jenis = '" + cbJenisPengiriman.getSelectedItem() + "'";
        connection.pstat = connection.conn.prepareStatement(query);
        connection.result = connection.pstat.executeQuery();

        if (connection.result.next()) {
            float hargaJenis = connection.result.getFloat("HargaJenis");
            total = (jaraktotal + hargaJenis) * berat;

            // Kondisi jika txtIdPromo diisi
            if (txtIdPromo.getText().isEmpty() == false) {
                // Mengurangi total dengan harga promo jika promo masih berlaku
                String promoQuery = "SELECT Potongan FROM tblPromo WHERE ID_Promo = ? AND ? BETWEEN Tanggal_BerlakuAwal AND Tanggal_BerlakuAkhir";
                connection.pstat = connection.conn.prepareStatement(promoQuery);
                connection.pstat.setString(1, txtIdPromo.getText()); // get ID_Promo yang diinputkan
                connection.pstat.setString(2, txtTanggal.getText()); // get Tanggal_Pengiriman
                connection.result = connection.pstat.executeQuery();

                if (connection.result.next()) {
                    float hargaPromo = connection.result.getFloat("Potongan");
                    total -= hargaPromo;
                }
            }
        }

        connection.pstat.close();
        connection.result.close();

        return total;
    }

    public void PrintResi(){
        datajasper.getDataVector().removeAllElements();
        datajasper.fireTableDataChanged();
        String query = "SELECT * FROM tblTrsPengiriman WHERE Nomor_Resi = '"+ txtResi.getText()+"'";

        try {
            connection.pstat = connection.conn.prepareStatement(query);
            connection.result = connection.pstat.executeQuery();
            while (connection.result.next()) {
                HashMap parameter = new HashMap();
                parameter.put("Resi", txtResi.getText());
                parameter.put("Nama_Pengirim", txtNamaPengirim.getText());
                parameter.put("Alamat_Pengirim", txtAlamatPengirim.getText());
                parameter.put("NoTelp_Pengirim", txtTelpPengirim.getText());
                parameter.put("Nama_Penerima", txtNamaPenerima.getText());
                parameter.put("Alamat_Penerima", txtAlamatPenerima.getText());
                parameter.put("NoTelp_Penerima", txtTelpPenerima.getText());
                parameter.put("Jarak", txtJarak.getText());
                parameter.put("Berat", txtBerat.getText());
                parameter.put("Total", txtTotal.getText());
                parameter.put("Jenis_Pengiriman", cbJenisPengiriman.getSelectedItem().toString());

                JasperDesign jd = JRXmlLoader.load("D:\\PRG3\\PROJECT_PRG3\\MyReports//Resi.jrxml");
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, parameter, new JREmptyDataSource());
                JasperViewer Viewer = new JasperViewer(jp, false);
                Viewer.setVisible(true);

            }
            connection.stat.close();
            connection.result.close();
            connection.pstat.close();

        } catch (Exception ex) {
            System.out.println("Eror load table Jasper: " + ex);
        }
    }

    public double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double earthRadius = 6371; // Earth's radius in kilometers

        double latDiff = Math.toRadians(latitude2 - latitude1);
        double lonDiff = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    public void insertPelacakan() {
        try {
            DBConnect connection = new DBConnect();
            // Example list of points
            List<Point> points = new ArrayList<>();
            String query = "SELECT ID_Gudang, Nama_Gudang, Latitude, Longitude FROM tblGudang";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                String idgudang = connection.result.getString("ID_Gudang");
                String namaGudang = connection.result.getString("Nama_Gudang");
                double latitude = connection.result.getDouble("Latitude");
                double longitude = connection.result.getDouble("Longitude");
                points.add(new Point(namaGudang, idgudang, latitude, longitude));
            }

            String idgudang = txtIdGudang.getText();
            // select gudang berdasarkan yang login
            String query2 = "SELECT Latitude, Longitude FROM tblGudang WHERE Nama_Gudang = '" + idgudang + "'";
            connection.result = connection.stat.executeQuery(query2);
            // Coordinates of the reference point
            connection.result.next();
            float refLatitude = connection.result.getFloat("Latitude");
            float refLongitude = connection.result.getFloat("Longitude");

            points.sort((p1, p2) -> Double.compare(
                    calculateDistance(refLatitude, refLongitude, p1.getLatitude(), p1.getLongitude()),
                    calculateDistance(refLatitude, refLongitude, p2.getLatitude(), p2.getLongitude())
            ));

            float jarak = Float.parseFloat(txtJarak.getText());
            String Resi = txtResi.getText();
            String InformasiPaket = "Dikirim"; //default

            // Print the nearest points
            int maxGudang = (jarak > 100) ? 5 : 3; // Menentukan jumlah gudang yang akan dilewati
            int gudangCount = 0; // Variabel untuk menghitung jumlah gudang yang telah dilewati
            // Print the nearest points
            for (Point point : points) {
                // Cek apakah jumlah gudang yang telah dilewati sudah mencapai batas
                if (gudangCount >= maxGudang) {
                    break; // Keluar dari loop jika sudah mencapai batas gudang
                }

                // Insert ke tabel tblTrsPelacakan
                String storeProcedure = "{CALL sp_InsertTrsPelacakan(?,?,?)}";
                connection.pstat = connection.conn.prepareCall(storeProcedure);
                connection.pstat.setString(1, Resi);
                connection.pstat.setString(2, point.getGudang());
                connection.pstat.setString(3, InformasiPaket);

                System.out.println("resi = "+Resi+"\nid gudang = "+point.getGudang()+"\nInformasiPaket = "+ InformasiPaket);

                connection.pstat.execute();
                connection.pstat.close();

                JOptionPane.showMessageDialog(null, point);

                gudangCount++; // Increment jumlah gudang yang telah dilewati
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private void hitungKembalian() {
        double bayar, total;
        total = Float.parseFloat(txtTotal.getText());
        bayar = Float.parseFloat(txtBayar.getText());
        if (bayar >= total) {
            double kembali = bayar - total;

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');

            DecimalFormat decimalFormat = new DecimalFormat("Rp #,##0.00", symbols);
            String formattedKembalian = decimalFormat.format(kembali);

            txtKembali.setText(formattedKembalian);
        } else {
            JOptionPane.showMessageDialog(null, "Jumlah uang kurang!");
            txtTotal.setText("");
        }
    }
}