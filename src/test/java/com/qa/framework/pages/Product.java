package com.qa.framework.pages;

/** Products on the demo store. slug matches the data-test attribute of the add/remove buttons. */
public enum Product {
    BACKPACK("Sauce Labs Backpack", "sauce-labs-backpack"),
    BIKE_LIGHT("Sauce Labs Bike Light", "sauce-labs-bike-light"),
    BOLT_TSHIRT("Sauce Labs Bolt T-Shirt", "sauce-labs-bolt-t-shirt");

    private final String displayName;
    private final String slug;

    Product(String displayName, String slug) {
        this.displayName = displayName;
        this.slug = slug;
    }

    public String displayName() {
        return displayName;
    }

    public String slug() {
        return slug;
    }

    public static Product fromName(String name) {
        for (Product p : values()) {
            if (p.displayName.equalsIgnoreCase(name)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown product: " + name);
    }
}
