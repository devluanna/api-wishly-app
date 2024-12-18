package com.app.domain.model.Categorys;

public enum Category {

    TECHNOLOGY("TECHNOLOGY"),
    FASHION("FASHION"),
    BEAUTY("BEAUTY"),
    HOMEANDDECOR("HOME AND DECOR"),
    BOOKSANDMEDIA("BOOKS AND MEDIA"),
    TRAVEL("TRAVEL"),
    SPORTS("SPORTS"),
    LIFESTYLE("LIFESTYLE"),
    FITNESS("FITNESS"),
    HOBBIES("HOBBIES"),
    PETS("PETS"),
    PERSONALPROJECTS("PERSONAL PROJECTS"),
    CARS("CARS"),
    HAIR("HAIR"),
    LOVEANDWEDDING("LOVE AND WEDDING");

    private final String category_description;

    private Category(String category_description) {
        this.category_description = category_description;
    }

    public String getCategoryDescription() {
        return category_description;
    }
}
