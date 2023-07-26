package MANAGER;

import LOGIN.LOGIN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DASHBOARD_MANAGER extends JFrame{
    private JPanel MainPanel;
    private JPanel Panel_Kiri;
    private JPanel Panel_Akun;
    private JPanel Panel_Bantuan_Akun;
    private JLabel Label_Manager;
    private JLabel Label_Role;
    private JLabel Label_Username;
    private JPanel Panel_Bantuan;
    private JLabel Label_Logo;
    private JPanel Panel_Menu;
    private JButton btnPemasukkan;
    private JButton btnLogout;
    private JButton btnBestKurir;
    private JPanel Panel_Tengah;
    private JPanel Panel_Form;
    private JLabel Label_Gudang;
    Color newColor = new Color(164,172,118);
    Color defaultColor = new Color(32, 52, 27);

    public void FrameConfig(){
        add(this.MainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public DASHBOARD_MANAGER(String[] value) {
        FrameConfig();
        Panel_Kiri.setSize(300,-1);
        Label_Username.setText(value[1]);
        Label_Role.setText(value[2]);
        Label_Gudang.setText(value[3]);
        btnPemasukkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Kiri.setSize(300,-1);
                btnPemasukkan.setBackground(newColor);
                btnBestKurir.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                LAPORAN_PEMASUKKAN pemasukkan = new LAPORAN_PEMASUKKAN();
                pemasukkan.PanelPemasukkan.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(pemasukkan.PanelPemasukkan);
            }
        });
        btnBestKurir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Kiri.setSize(300,-1);
                btnBestKurir.setBackground(newColor);
                btnPemasukkan.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                LAPORAN_BEST_LAYANAN baseKurir = new LAPORAN_BEST_LAYANAN();
                baseKurir.PanelBestKurir.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(baseKurir.PanelBestKurir);
            }
        });
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogout.setBackground(newColor);
                btnPemasukkan.setBackground(defaultColor);
                btnBestKurir.setBackground(defaultColor);

                dispose();
                LOGIN login = new LOGIN();
                login.setVisible(true);
            }
        });
    }
}