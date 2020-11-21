package edu.neu.madcourse.stashbusters.enums;

/**
 * This enum represents material type.
 */
public enum MaterialType {
    CRAYONS("crayons", Category.STATIONARY),
    WATERCOLORS("watercolors", Category.PAINT),
    YARN("yarn", Category.TEXTILE),
    INK_PAD("ink_pad", Category.PRINTING),
    FEATHERS("feathers", Category.OTHER);

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
