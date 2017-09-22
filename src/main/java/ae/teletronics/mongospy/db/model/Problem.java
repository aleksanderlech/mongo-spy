package ae.teletronics.mongospy.db.model;

import lombok.Value;

@Value
public class Problem {

    private Level level;
    private String message;
}
