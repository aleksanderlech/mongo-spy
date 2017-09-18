package ae.teletronics.mongospy.db;

import org.bson.Document;

import java.util.Optional;
import java.util.function.Function;

/**
 * Extracts value of type <T> from the Mongo DB {@link Document}. Value can be optional as the contract says.
 *
 * @param <T> target value type
 * @author aleksanderlech
 */
public interface ValueExtractor<T> extends Function<Document, Optional<T>> {

}
