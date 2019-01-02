package com.wsu.dailyfitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SideViewContent {

    public static class Item {

        public String id;
        public String content;

        public Item(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static List<Item> ITEMS = new ArrayList<Item>();
    public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();

    static {
        addItem(new Item("1", "Today's Walk"));
        addItem(new Item("2", "Statistics"));
        addItem(new Item("3", "Logout"));
    }

    private static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
