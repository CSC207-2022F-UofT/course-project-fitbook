package entity;

import java.util.Date;

public class TemporalExercise extends Exercise{

    private Date time;

    /**
     * @param temporalExerciseBuilder to initialize TemporalExercise object using builder attributes
     */
    public TemporalExercise(TemporalExerciseBuilder temporalExerciseBuilder) {
        super(temporalExerciseBuilder);
        this.time = temporalExerciseBuilder.time;

    }

    /**
     * @return time class variable
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time to set class variable time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    public static class TemporalExerciseBuilder extends ExerciseBuilder<TemporalExercise> {

        private Date time;

        public TemporalExerciseBuilder() { }

        /**
         * @param val to set time class variable
         * @return current object
         */
        public TemporalExerciseBuilder withTime(Date val) {
            this.time = val;
            return this;
        }

        /**
         * @return TemporalExercise object using current object attributes
         */
        @Override
        public TemporalExercise build() {
            return new TemporalExercise(this);
        }
    }

}
