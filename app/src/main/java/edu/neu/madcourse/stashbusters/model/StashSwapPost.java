package edu.neu.madcourse.stashbusters.model;

import edu.neu.madcourse.stashbusters.enums.MaterialType;

/**
 * Stash Swap Post is a type of Post whose content is specifically about swapping materials.
 */
public class StashSwapPost extends Post {
    private MaterialType material;
    private Boolean isAvailable;

    public StashSwapPost(MaterialType material) {
        super();
        this.material = material;
        // by default, the material is available for swapping
        this.isAvailable = true;
    }

    public StashSwapPost(String title, String description, String photoUrl, MaterialType material) {
        super(title, description, photoUrl);
        this.material = material;
        this.isAvailable = true;
    }

    public MaterialType getMaterial() {
        return material;
    }

    public Boolean getAvailability() {
        return isAvailable;
    }

    public void setMaterial(MaterialType material) {
        this.material = material;
    }

    public void setAvailability(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
