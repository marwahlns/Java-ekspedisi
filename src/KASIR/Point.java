package KASIR;

public class Point {
    private String name;
    private String gudang;
    private double latitude;
    private double longitude;

    public Point(String name, String gudang, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gudang = gudang;
    }

    public String getGudang() {
        return gudang;
    }

    public void setGudang(String gudang) {
        this.gudang = gudang;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }



    @Override
    public String toString() {
        return name + " (" + latitude + ", " + longitude + ")";
    }
}
