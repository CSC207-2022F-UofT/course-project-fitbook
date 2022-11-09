package entity;


import jakarta.validation.constraints.NotNull;

public class RepetitiveExercise extends Exercise {

    @NotNull
    private int reps;
    @NotNull
    private int sets;

    /**
     * @param repetitiveExerciseBuilder to initialize RepetitiveExercise object using builder attributes
     */
    public RepetitiveExercise(RepetitiveExerciseBuilder repetitiveExerciseBuilder) {
        super(repetitiveExerciseBuilder);
        this.reps = repetitiveExerciseBuilder.reps;
        this.sets = repetitiveExerciseBuilder.sets;
    }

    /**
     * @return reps class variable
     */
    public int getReps() {
        return reps;
    }

    /**
     * @param reps to set class variable reps
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * @return sets class variable
     */
    public int getSets() {
        return sets;
    }

    /**
     * @param sets to set class variable sets
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    public static class RepetitiveExerciseBuilder extends ExerciseBuilder<RepetitiveExercise> {

        private int reps;
        private int sets;

        public RepetitiveExerciseBuilder() { }

        /**
         * @param val to set reps class variable
         * @return current object
         */
        public RepetitiveExerciseBuilder withReps(int val) {
            this.reps = val;
            return this;
        }

        /**
         * @param val to set sets class variable
         * @return current object
         */
        public RepetitiveExerciseBuilder withSets(int val) {
            this.sets = val;
            return this;
        }

        /**
         * @return RepetitiveExercise object using current object attributes
         */
        @Override
        public RepetitiveExercise build() {
            return new RepetitiveExercise(this);
        }
    }
}
