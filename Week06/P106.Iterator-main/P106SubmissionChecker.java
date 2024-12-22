import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.lang.reflect.Field;

/**
* This class extends the IterableRedBlackTree class to run submission checks on it.
*/
public class P106SubmissionChecker extends IterableRedBlackTree<Integer> {

       @Test
       public void testNonNullIterator() {
           IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
           Assertions.assertFalse(tree.iterator() == null, "iterator method should not return null");
       }

       @Test
       public void simpleIterator() {
           IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
           tree.insert(5);
           Assertions.assertEquals(5, tree.iterator().next());
       }

}
