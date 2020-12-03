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

    public static MaterialType getByInt(int i) {
        switch (i) {
            case 0:
                return MaterialType.CRAYONS;
            case 1:
                return MaterialType.WATERCOLORS;
            case 2:
                return MaterialType.YARN;
            case 3:
                return MaterialType.INK_PAD;
            case 4:
                return MaterialType.FEATHERS;
            case 5:
                return MaterialType.FABRIC;
            case 6:
                return MaterialType.MISC;
            default:
                return null;
        }
    }
}

/**
 * This enum represents categories for craft materials.
 */
enum Category {
    STATIONARY, PAINT, TEXTILE, PRINTING, OTHER
}
