package edu.neu.madcourse.stashbusters.enums;

/**
 * This enum represents material type.
 */
public enum MaterialType {
    CRAYONS("Crayons", Category.STATIONARY),
    WATERCOLORS("Watercolors", Category.PAINT),
    YARN("Yarn", Category.TEXTILE),
    INK_PAD("Ink pad", Category.PRINTING),
    FEATHERS("Feathers", Category.OTHER),
    FABRIC("Fabric", Category.TEXTILE),
    MISC("Misc", Category.OTHER);

    private final String dbCode;
    private final Category category;

    MaterialType(String dbCode, Category category){
        this.dbCode = dbCode;
        this.category = category;
    }

    public String getDbCode() {
        return dbCode;
    }

    public Category getCategory() {
        return category;
    }
}

/**
 * This enum represents categories for craft materials.
 */
enum Category {
    STATIONARY, PAINT, TEXTILE, PRINTING, OTHER
}
