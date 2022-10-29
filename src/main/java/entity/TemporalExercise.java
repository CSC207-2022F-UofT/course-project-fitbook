package entity;

import java.util.Date;

public class TemporalExercise extends Exercise{

    private Date time;

    public TemporalExercise(Builder builder) {
        super(builder);
        this.time = builder.time;

    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public static class Builder extends Exercise.Builder<TemporalExercise> {

        private Date time;

        public Builder() {}

        public Builder withTime(Date val) {
            this.time = val;
            return this;
        }

        @Override
        public TemporalExercise build() {
            return new TemporalExercise(this);
        }
    }

}
