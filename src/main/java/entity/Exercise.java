package entity;

import java.util.List;

public abstract class Exercise {

    private int id;
    private List<String> keywords;
    private List<String> bodyParts;

    protected Exercise(Builder<?> builder) {
        this.id = builder.id;
        this.keywords = builder.keywords;
        this.bodyParts = builder.bodyParts;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(List<String> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public static abstract class Builder<T> {

        private int id;
        private List<String> keywords;
        private List<String> bodyParts;

        public Builder() {}

        public Builder<T> withId(int val) {
            this.id = val;
            return this;
        }

        public Builder<T> withKeywords(List<String> val) {
            this.keywords = val;
            return this;
        }

        public Builder<T> withBodyParts(List<String> val) {
            this.bodyParts = val;
            return this;
        }

        public abstract T build();
    }
}
