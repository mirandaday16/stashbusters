package edu.neu.madcourse.stashbusters.model;

/**
 * Stash Swap Post is a type of Post whose content is specifically about swapping materials.
 */
public class StashSwapPost extends Post {
    private String material;
    private Boolean isAvailable;

    public StashSwapPost() {
        super();
    }

    public StashSwapPost(String material) {
        super();
        this.material = material;
        // by default, the material is available for swapping
        this.isAvailable = true;
    }

    public String getMaterial() {
        return material;
    }

    public Boolean getAvailability() {
        return isAvailable;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setAvailability(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
