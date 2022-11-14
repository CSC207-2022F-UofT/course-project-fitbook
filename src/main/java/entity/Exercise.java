package entity;

import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Exercise {

    @NotEmpty
    protected String id;
    @NotEmpty
    protected List<String> keywords;
    @NotEmpty
    protected List<String> bodyParts;

    /**
     * @param exerciseBuilder to initialize Exercise object using builder attributes
     */
    protected Exercise(ExerciseBuilder<?> exerciseBuilder) {
        this.id = exerciseBuilder.id;
        this.keywords = Objects.requireNonNullElseGet(exerciseBuilder.keywords, ArrayList::new);
        this.bodyParts = Objects.requireNonNullElseGet(exerciseBuilder.bodyParts, ArrayList::new);
    }

    /**
     * @return id class variable
     */
    public String getId() {
        return id;
    }

    /**
     * @param id to set class variable id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return keywords class variable
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * @param keywords to set class variable keywords
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * @return bodyParts class variable
     */
    public List<String> getBodyParts() {
        return bodyParts;
    }

    /**
     * @param bodyParts to set class variable bodyParts
     */
    public void setBodyParts(List<String> bodyParts) {
        this.bodyParts = bodyParts;
    }

    /**
     * @param bodyPart to add bodyPart String to list bodyParts class variable
     */
    public void addBodyPart(String bodyPart) {
        this.bodyParts.add(bodyPart);
    }

    /**
     * @param keyword to add keyword String to list keywords class variable
     */
    public void addKeyword(String keyword) {
        this.bodyParts.add(keyword);
    }

    protected static abstract class ExerciseBuilder<T> {

        protected String id;
        protected List<String> keywords;
        protected List<String> bodyParts;

        protected ExerciseBuilder() { }

        /**
         * @param val to set id class variable
         * @return current object
         */
        protected ExerciseBuilder<T> withId(String val) {
            this.id = val;
            return this;
        }

        /**
         * @param val to set keywords class variable
         * @return current object
         */
        public ExerciseBuilder<T> withKeywords(List<String> val) {
            this.keywords = val;
            return this;
        }

        /**
         * @param val to set bodyParts class variable
         * @return current object
         */
        public ExerciseBuilder<T> withBodyParts(List<String> val) {
            this.bodyParts = val;
            return this;
        }

        /**
         * @return object of type T
         */
        public abstract T build();
    }
}
