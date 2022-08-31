package com.qooapp.opensdk.sample.qooapp.model;

import java.util.List;

/**
 *
 * @email devel@qoo-app.com
 */
public class Product {
    private String product_id;
    private String name;
    private String description;
    private List<ItemPrice> price;
    private boolean consumable;

    public class ItemPrice {
        public String amount;
        public String currency;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ItemPrice> getPrice() {
        return price;
    }

    public boolean getConsumable() {
        return consumable;
    }
}
