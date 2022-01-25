package com.qooapp.opensdk.sample.qooapp.model;

import java.util.List;

/**
 *
 * @email devel@qoo-app.com
 */
public class Product {
    /**
     * {
     *   "product_id": "1",
     *   "name": "1",
     *   "description": "1",
     *   "price": {
     *     "paypal": {
     *       "channel": "paypal",
     *       "amount": "2.00",
     *       "currency": "USD"
     *     },
     *     "stripe": {
     *       "channel": "stripe",
     *       "amount": "15.58",
     *       "currency": "HKD"
     *     },
     *     "mol": {
     *       "channel": "mol",
     *       "amount": "2.00",
     *       "currency": "USD"
     *     },
     *     "mycard": {
     *       "channel": "mycard",
     *       "amount": 56,
     *       "currency": "TWD"
     *     }
     *   },
     *   "one_off_limit": {
     *     "cycle": "none",
     *     "length": 0
     *   },
     *   "consumable": true
     */
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
