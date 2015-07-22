package zhou.v2ex.util;

import java.io.File;
import java.util.Comparator;

/**
 * Created by zzhoujay on 2015/7/22 0022.
 */
public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File lhs, File rhs) {
        return (int) (rhs.lastModified() - lhs.lastModified());
    }
}
