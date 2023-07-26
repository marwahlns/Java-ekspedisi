package MANAGER;

import Connection.DBConnect;
import com.toedter.calendar.JDateChooser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class LAPORAN_PEMASUKKAN {
    public JPanel PanelPemasukkan;
    private JPanel Panel_Atas;
    private JPanel Panel_Bawah;
    private JPanel Panel_Kanan;
    private JPanel Panel_Kiri;
    private JPanel Panel_Filter;
    private JPanel Panel_Button;
    private JButton btnPrint;
    private JLabel Label_Icon;
    private JPanel Panel_Icon;
    private JPanel Panel_TextBox;
    private JPanel jpFrom;
    private JPanel jpUntil;
    private JTable tablePengiriman;
    private JButton btnFilter;
    DBConnect connection = new DBConnect();
    JDateChooser date_start = new JDateChooser();
    JDateChooser date_end = new JDateChooser();
    private DefaultTableModel tableModel;
    HashMap param = new HashMap();

    public LAPORAN_PEMASUKKAN() {
        jpFrom.add(date_start);
        jpUntil.add(date_end);
        tableModel = new DefaultTableModel();
        tablePengiriman.setModel(tableModel);
        addColumn();
        loadData();
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (date_start.getDate() == null || date_end.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Please enter the start date and end date!", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (date_start.getDate().after(date_end.getDate())) {
                    JOptionPane.showMessageDialog(null, "Start date cannot be after end date!", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        Format formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                        String From = formatter1.format(date_start.getDate());

                        Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                        String Until = formatter2.format(date_end.getDate());

                        param.put("start_date", From);
                        param.put("end_date", Until);
                        param.put("Total_Pendapatan", sumTotal());
                        param.put("Total_Transaksi", countTotal());

                        JRDataSource dataSource = new JRTableModelDataSource(tablePengiriman.getModel());
                        JasperDesign jd = JRXmlLoader.load("D:\\PRG3\\PROJECT_PRG3\\MyReports//Laporan_Pemasukan.jrxml");
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        JasperPrint jp = JasperFillManager.fillReport(jr, param, dataSource);
                        JasperViewer Viewer = new JasperViewer(jp, false);
                        Viewer.setVisible(true);

                    } catch (Exception ex) {
                        System.out.println("Error loading Jasper table: " + ex);
                    }
                }
            }
        });
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.getDataVector().removeAllElements();
                tableModel.fireTableDataChanged();
                if(date_start.getDate() == null || date_end.getDate() == null){
                    JOptionPane.showMessageDialog(null, "Please enter the start date and end date!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else if (date_start.getDate().after(date_end.getDate())) {
                JOptionPane.showMessageDialog(null, "Start date cannot be after end date!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        Format formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                        String From = formatter1.format(date_start.getDate());

                        Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                        String Until = formatter2.format(date_end.getDate());

                        String query = "SELECT * FROM tblTrsPengiriman WHERE Tanggal BETWEEN ? AND ?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, From);
                        connection.pstat.setString(2, Until);
                        connection.result = connection.pstat.executeQuery();

                        while (connection.result.next()) {
                            Object[] obj = new Object[3];
                            obj[0] = connection.result.getString("Nomor_Resi");
                            obj[1] = connection.result.getString("Tanggal");
                            obj[2] = connection.result.getFloat("Total_Harga");
                            tableModel.addRow(obj);
                        }
                        connection.stat.close();
                        connection.result.close();
                    } catch (SQLException ex) {
                        System.out.println("Error: " + ex.toString());
                    }
                }
            }
        });
    }

    public void addColumn(){
        tableModel.addColumn("Nomor_Resi");
        tableModel.addColumn("Tanggal");
        tableModel.addColumn("Total_Harga");
    }

    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "SELECT * FROM tblTrsPengiriman";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[3];
                obj[0] = connection.result.getString("Nomor_Resi");
                obj[1] = connection.result.getString("Tanggal");
                obj[2] = connection.result.getFloat("Total_Harga");
                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private float sumTotal(){
        float total = 0;

        try {
            Format formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            String From = formatter1.format(date_start.getDate());

            Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            String Until = formatter2.format(date_end.getDate());

            String query = "SELECT SUM(Total_Harga) AS jumlah FROM tblTrsPengiriman WHERE Tanggal BETWEEN ? AND ?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, From);
            connection.pstat.setString(2, Until);
            connection.result = connection.pstat.executeQuery();

            while(connection.result.next()){
                total = Float.parseFloat(connection.result.getString("jumlah"));
            }

            connection.stat.close();
            connection.result.close();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghitung total transaksi.\n" + ex);
        }
        return total;
    }

    private String countTotal(){
        String total = null;

        try {
            Format formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            String From = formatter1.format(date_start.getDate());

            Format formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            String Until = formatter2.format(date_end.getDate());

            String query = "SELECT COUNT(*) AS Jumlah FROM tblTrsPengiriman WHERE Tanggal BETWEEN ? AND ?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, From);
            connection.pstat.setString(2, Until);
            connection.result = connection.pstat.executeQuery();

            while(connection.result.next()){
                total = connection.result.getString("Jumlah");
            }

            connection.stat.close();
            connection.result.close();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghitung total transaksi.\n" + ex);
        }
        return total;
    }
}