package ca.utoronto.fitbook;

import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.RepetitiveExercise;
import ca.utoronto.fitbook.entity.TemporalExercise;
import ca.utoronto.fitbook.entity.User;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtilities
{
    public static String randomString(int length) {
        byte[] array = new byte[length];
        ThreadLocalRandom.current().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public static int randomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static Date randomDate(Date minDate, Date maxDate) {
        return new Date(ThreadLocalRandom.current().nextLong(minDate.getTime(), maxDate.getTime()));
    }

    public static User randomUser() {
        Instant maxTime = Instant.now();
        Instant minTime = maxTime.minus(Duration.ofDays(300));
        Date maxDate = Date.from(maxTime);
        Date minDate = Date.from(minTime);
        return User.builder()
                .followersIdList(new ArrayList<>())
                .joinDate(randomDate(minDate, maxDate))
                .name(randomString(8))
                .totalLikes(randomInteger(0, 10))
                .password(randomString(12))
                .postIdList(new ArrayList<>())
                .followingIdList(new ArrayList<>())
                .followersIdList(new ArrayList<>())
                .likedPostIdList(new ArrayList<>())
                .build();
    }

    public static Post randomPost(String authorId) {
        Instant maxTime = Instant.now();
        Instant minTime = maxTime.minus(Duration.ofDays(300));
        Date maxDate = Date.from(maxTime);
        Date minDate = Date.from(minTime);
        return Post.builder()
                .postDate(randomDate(minDate, maxDate))
                .authorId(authorId)
                .likes(randomInteger(0, 100))
                .exerciseIdList(new ArrayList<>())
                .description(randomString(50))
                .build();
    }

    public static TemporalExercise randomTemporalExercise() {
        return TemporalExercise.builder()
                .time(randomInteger(20, 60))
                .name(randomString(10))
                .bodyParts(List.of(randomString(10), randomString(9)))
                .keywords(List.of(randomString(10), randomString(9)))
                .build();
    }

    public static RepetitiveExercise randomRepetitiveExercise() {
        return RepetitiveExercise.builder()
                .reps(randomInteger(10,30))
                .sets(randomInteger(3, 7))
                .name(randomString(10))
                .bodyParts(List.of(randomString(10), randomString(9)))
                .keywords(List.of(randomString(10), randomString(9)))
                .build();
    }
}
