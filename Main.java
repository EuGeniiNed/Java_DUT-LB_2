import java.util.ArrayList;
import java.util.List;

abstract class Item {
    private String title;
    private String uniqueID;
    private boolean isBorrowed;

    public Item(String title, String uniqueID) {
        this.title = title;
        this.uniqueID = uniqueID;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public abstract void borrowItem();

    public abstract void returnItem();
}

class Book extends Item {
    private String author;

    public Book(String title, String uniqueID, String author) {
        super(title, uniqueID);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public void borrowItem() {
        setBorrowed(true);
    }

    @Override
    public void returnItem() {
        setBorrowed(false);
    }
}

class DVD extends Item {
    private int duration;

    public DVD(String title, String uniqueID, int duration) {
        super(title, uniqueID);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public void borrowItem() {
        setBorrowed(true);
    }

    @Override
    public void returnItem() {
        setBorrowed(false);
    }
}

class Patron {
    private String name;
    private String ID;
    private List<Item> borrowedItems;

    public Patron(String name, String ID) {
        this.name = name;
        this.ID = ID;
        this.borrowedItems = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public List<Item> getBorrowedItems() {
        return borrowedItems;
    }

    public void borrow(Item item) {
        borrowedItems.add(item);
    }

    public void returnItem(Item item) {
        borrowedItems.remove(item);
    }
}

interface IManageable {
    void add(Item item);

    void remove(Item item);

    void listAvailable();

    void listBorrowed();
}

class Library implements IManageable {
    private List<Item> items;
    private List<Patron> patrons;

    public Library() {
        this.items = new ArrayList<>();
        this.patrons = new ArrayList<>();
    }

    public void registerPatron(Patron patron) {
        patrons.add(patron);
    }

    public void lendItem(Patron patron, Item item) {
        if (!item.isBorrowed()) {
            patron.borrow(item);
            item.borrowItem();
        }
    }

    public void returnItem(Patron patron, Item item) {
        if (patron.getBorrowedItems().contains(item)) {
            patron.returnItem(item);
            item.returnItem();
        }
    }

    @Override
    public void add(Item item) {
        items.add(item);
    }

    @Override
    public void remove(Item item) {
        items.remove(item);
    }

    @Override
    public void listAvailable() {
        for (Item item : items) {
            if (!item.isBorrowed()) {
                System.out.println("Available: " + item.getTitle());
            }
        }
    }

    @Override
    public void listBorrowed() {
        for (Patron patron : patrons) {
            List<Item> borrowedItems = patron.getBorrowedItems();
            if (!borrowedItems.isEmpty()) {
                for (Item item : borrowedItems) {
                    System.out.println("Patron: " + patron.getName() + " borrowed: " + item.getTitle());
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        Book book = new Book("Java Programming", "B001", "John Doe");
        DVD dvd = new DVD("Introduction to Java", "D001", 120);

        library.add(book);
        library.add(dvd);

        Patron patron1 = new Patron("Alice", "P001");
        Patron patron2 = new Patron("Bob", "P002");

        library.registerPatron(patron1);
        library.registerPatron(patron2);

        library.listAvailable();

        library.lendItem(patron1, book);
        library.lendItem(patron2, dvd);

        library.listBorrowed();
    }
}