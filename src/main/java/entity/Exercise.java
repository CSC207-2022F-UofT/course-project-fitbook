package entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.*;

@SuperBuilder
@Data
@NoArgsConstructor
public abstract class Exercise {

    @Builder.Default
    @NonNull
    protected String id = UUID.randomUUID().toString();
    @NonNull
    protected List<String> keywords;
    @NonNull
    protected List<String> bodyParts;

}
