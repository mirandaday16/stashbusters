package edu.neu.madcourse.stashbusters.model;

import edu.neu.madcourse.stashbusters.enums.MaterialType;

/**
 * Stash Swap Post is a type of Post whose content is specifically about swapping materials.
 */
public class StashSwapPost extends Post {
    private String interestedMaterial;
    private Boolean isAvailable;

    public StashSwapPost() {
        // For DataSnapshot
        super();
    }

    public StashSwapPost(String material) {
        super();
        this.interestedMaterial = material;
        // by default, the material is available for swapping
        this.isAvailable = true;
    }

    public StashSwapPost(String title, String description, String photoUrl, String material) {
        super(title, description, photoUrl);
        this.interestedMaterial = material;
        this.isAvailable = true;
    }

    public String getMaterial() {
        return interestedMaterial;
    }

    public Boolean getAvailability() {
        return isAvailable;
    }

    public void setMaterial(String material) {
        this.interestedMaterial = material;
    }

    public void setAvailability(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
