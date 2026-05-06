package hust.soict.hedspi.aims;

public class Aims {
    public static void main(String[] args) {
        // khởi tạp chay của lập trình viên / không phải nguười dùng nhập
        DigitalVideoDisc dvd1 = new DigitalVideoDisc("The Lion King", "Animation", "Roger Allers", 87, 19.95f);
        DigitalVideoDisc dvd2 = new DigitalVideoDisc("Star Wars", "Science Fiction", "George Lucas", 87, 24.95f);
        DigitalVideoDisc dvd3 = new DigitalVideoDisc("Aladin", "Animation", 18.99f);
        DigitalVideoDisc[] dvds = new DigitalVideoDisc[] {
                dvd1,
                new DigitalVideoDisc("Frozen", "Animation", "Chris Buck", 102, 21.99f),
                new DigitalVideoDisc("Interstellar", "Sci-Fi", "Christopher Nolan", 169, 29.99f)
        };
        Cart cart = new Cart();
        cart.addDigitalVideoDisc(dvd1);
        cart.addDigitalVideoDisc(dvd2);
        cart.addDigitalVideoDisc(dvd3);
        cart.removeDigitalVideoDisc(dvd1);
        cart.addDigitalVideoDisc(dvd2,dvd3);
        cart.addDigitalVideoDisc(dvds);
        System.out.println("Total Cost is: "+ cart.totalcost());
    }
}
