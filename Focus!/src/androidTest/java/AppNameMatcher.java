//import org.hamcrest.Matcher;
//
///**
// * Created by niccolashernandez on 10/29/17.
// */
//
//public class AppNameMatcher {
//
//    public static Matcher<Object> withProductID(final String id) {
//        return new BoundedMatcher<Object, Product >(Product.class) {
//            @Override
//            protected boolean matchesSafely(Product product) {
//                return id.equals(product.getId);
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with id: " + id);
//            }
//        };
//    }
//}
//}
