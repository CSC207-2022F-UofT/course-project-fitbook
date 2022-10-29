package entity;

public class RepetetiveExercise extends Exercise {

    private int reps;
    private int sets;

    public RepetetiveExercise(Builder builder) {
        super(builder);
        this.reps = builder.reps;
        this.sets = builder.sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public static class Builder extends Exercise.Builder<RepetetiveExercise> {

        private int reps;
        private int sets;

        public Builder() {}

        public Builder withReps(int val) {
            this.reps = val;
            return this;
        }

        public Builder withSets(int val) {
            this.sets = val;
            return this;
        }

        @Override
        public RepetetiveExercise build() {
            return new RepetetiveExercise(this);
        }
    }
}
