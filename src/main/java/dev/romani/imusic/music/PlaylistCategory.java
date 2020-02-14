package dev.romani.imusic.music;

public enum PlaylistCategory {
    PARTY("party"), POP("pop"), ROCK("rock"), CLASSICAL("classical");

    private String categoryId;

    private PlaylistCategory(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
