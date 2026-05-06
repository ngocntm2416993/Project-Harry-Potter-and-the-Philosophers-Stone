package hust.soict.hedspi.test.store;

import hust.soict.hedspi.aims.DigitalVideoDisc;
import hust.soict.hedspi.aims.Aims;
import hust.soict.hedspi.aims.Cart;
import hust.soict.hedspi.aims.Store;


public class StoreTest {
    public static void main(String[] args) {

        hust.soict.hedspi.aims.Store store = new hust.soict.hedspi.aims.Store();
        DigitalVideoDisc dvd1 = new DigitalVideoDisc("Spirited Away", "Animation", "Hayao Miyazaki", 125, 22.95f);
        DigitalVideoDisc dvd2 = new DigitalVideoDisc("Parasite", "Thriller", "Bong Joon-ho", 132, 24.95f);
        DigitalVideoDisc dvd3 = new DigitalVideoDisc("Oldboy", "Action", "Park Chan-wook", 120, 19.95f);
        DigitalVideoDisc dvd4 = new DigitalVideoDisc("Your Name", "Romance", "Makoto Shinkai", 106, 21.95f);
        DigitalVideoDisc dvd5 = new DigitalVideoDisc("Train to Busan", "Horror", "Yeon Sang-ho", 118, 18.95f);

        DigitalVideoDisc[] dvds = new DigitalVideoDisc[]{dvd1, dvd2, dvd3, dvd4, dvd5};

        store.addDVD(dvds);
        DigitalVideoDisc[] dvd_s = new DigitalVideoDisc[]{dvd2,dvd5};
        store.removeDVD(dvd_s);
    }
}
