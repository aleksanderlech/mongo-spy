package ae.teletronics.mongospy.db;

import lombok.Value;

@Value
public class Problem {

    private Level level;
    private String message;
}
