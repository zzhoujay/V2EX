package zhou.v2ex.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by zzhoujay on 2015/7/22 0022.
 */
public class FileNameFilter implements FilenameFilter {

    private String start;

    public FileNameFilter(String start) {
        this.start = start;
    }

    @Override
    public boolean accept(File dir, String filename) {
        return filename != null && filename.startsWith(start);
    }
}
