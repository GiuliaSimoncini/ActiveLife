package project;

import project.operations.ToDoVisitor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ShoppingSession extends ToDo {
    private int duration;
    private String place;
    private int maxBudget;
    private Map<String, Integer> shoppingList = new LinkedHashMap<String, Integer>();

    public ShoppingSession(String title, int duration, int maxBudget) {
        super(title);
        if (duration <= 0)
            throw new IllegalArgumentException("Duration must be a positive number");
        if (maxBudget <= 0)
            throw new IllegalArgumentException("Max budget must be a positive number");
        this.duration = duration;
        this.maxBudget = maxBudget;
    }

    public ShoppingSession(String title, int duration, int maxBudget, Map<String, Integer> shoppingList) {
        this(title, duration, maxBudget);
        if (shoppingList == null)
            throw new IllegalArgumentException("Shopping list can not be null");
        this.shoppingList = shoppingList;
    }

    public ShoppingSession addItem(String item, int quantity) {
        if (item == null)
            throw new IllegalArgumentException("Item can not be null");
        if (item.isEmpty())
            throw new IllegalArgumentException("Item can not be empty");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be a positive number");
        shoppingList.put(item, quantity);
        return this;
    }

    public Stream<Map.Entry<String, Integer>> streamShopping() {
        return shoppingList.entrySet().stream();
    }
    public ShoppingSession setPlace(String place) {
        if (place == null)
            throw new IllegalArgumentException("Place can not be null");
        if (place.isEmpty())
            throw new IllegalArgumentException("Place can not be empty");
        this.place = place;
        return this;
    }

    public String getPlace() {
        return place;
    }

    public int getMaxBudget() {
        return maxBudget;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void accept(ToDoVisitor visitor) {
        visitor.visitShoppingSession(this);
    }
}
