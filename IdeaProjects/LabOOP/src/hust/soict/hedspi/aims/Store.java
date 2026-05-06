package hust.soict.hedspi.aims;

public class Store {
    public static final int max_number_store = 20;
    public int number_of_store =0;
    private DigitalVideoDisc[] itemsInStore = new DigitalVideoDisc[max_number_store];
    public void addDVD (DigitalVideoDisc disc){
        if (number_of_store >max_number_store ){
            System.out.println("The store is almost full");
            return;
        }
        else{
            itemsInStore[number_of_store++]=disc;
            System.out.println("The disc has been store");
        }
    }

    public void addDVD (DigitalVideoDisc[] disc){
        for (int i = 0; i < disc.length; i++) {  // i = number_of_disc =0;
            itemsInStore[number_of_store++] = disc[i];
        }
        System.out.println("These discs have been store");
    }

    public void removeDVD (DigitalVideoDisc disc){
        for (int i = 0; i < number_of_store; i++) {
            if(itemsInStore[i]==disc){
                for (int j=i;j<number_of_store-1;j++){
                    itemsInStore[j]=itemsInStore[j+1];
                }
                itemsInStore[number_of_store-1]=null;
                number_of_store--;
                i--;
                System.out.println("This disc has been remove");
                break;
            }
        }
    }
    public void removeDVD (DigitalVideoDisc[] disc) {
        int index_of_disc = 0;
        for(;index_of_disc<disc.length;index_of_disc++) {
            for (int i = 0; i < number_of_store; i++) {
                if (itemsInStore[i] == disc[index_of_disc]) {
                    for (int j = i; j < number_of_store-1; j++) {
                        itemsInStore[j] = itemsInStore[j + 1];
                    }
                    itemsInStore[number_of_store - 1] = null;
                    number_of_store--;
                    i--;
                    System.out.println("This disc has been remove");
                }
            }
        }

    }


    public DigitalVideoDisc[] getItemsInStore() {
        return itemsInStore;
    }

    public void setItemsInStore(DigitalVideoDisc[] itemsInStore) {
        this.itemsInStore = itemsInStore;
    }
}
