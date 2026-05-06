package hust.soict.hedspi.aims;

public class Cart {
    public static final int max_number_order = 20;
    public int number_of_order = 0;
    private DigitalVideoDisc[] orderdvd = new DigitalVideoDisc[max_number_order];
    public void addDigitalVideoDisc(DigitalVideoDisc disc){
        if(number_of_order >= max_number_order){
            System.out.println("The cart is almost full");
            return;
        }
        else{
            orderdvd[number_of_order++]=disc;
            System.out.println("The disc has been added");
        }
    }
    public int number_of_disc =0;
    public void addDigitalVideoDisc(DigitalVideoDisc[] disc){
        orderdvd[number_of_order++]=disc[number_of_disc++];
    }// may be this way is better
    public float totalcost(){
        float total = 0;
        for(int i=0;i<number_of_order;i++){
            total+=orderdvd[i].getCost();
        }
        return total;
    }
    public void addDigitalVideoDisc(DigitalVideoDisc dvd1,DigitalVideoDisc dvd2){
        orderdvd[number_of_order++]=dvd1;
        orderdvd[number_of_order++]=dvd2;
    }
    public void removeDigitalVideoDisc(DigitalVideoDisc disc){
        for(int i=0;i<number_of_order;i++){
            if(orderdvd[i]==disc){
                for (int j=i;j<number_of_order-1;j++){
                    orderdvd[j]=orderdvd[j+1];
                }
                orderdvd[number_of_order-1]=null;
                number_of_order--;
                System.out.println("The cart has been remove");
                break;
            }
        }
    }

    public void print() {
        System.out.println("***********************CART*********************** ");
        System.out.println("Ordered Items: ");
        for (int i = 0; i < number_of_order; i++) {
            System.out.println((i + 1) + ". " + orderdvd[i].toString());
            // hoặc ngắn hơn:
            System.out.println((i + 1) + ". " + orderdvd[i]);
        }
        System.out.println("Total cost: " + totalcost() + "$\n" +"***************************************************");
    }

    public void search_by_title (String title){
        System.out.println("Search results for: " + title);
        boolean found = false;

        for (int i = 0; i < number_of_order; i++) {
            if (orderdvd[i].isMatch(title)) {
                System.out.println(orderdvd[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No disc found with title: " + title);
        }
    }
}
