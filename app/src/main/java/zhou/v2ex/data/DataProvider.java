package zhou.v2ex.data;

/**
 * Created by 州 on 2015/7/19 0019.
 */
public class DataProvider {

    private static DataProvider dataProvider;
    private DataProvider() {
    }
    public static DataProvider getInstance() {
        if (dataProvider == null) {
            dataProvider = new DataProvider();
        }
        return dataProvider;
    }


}
