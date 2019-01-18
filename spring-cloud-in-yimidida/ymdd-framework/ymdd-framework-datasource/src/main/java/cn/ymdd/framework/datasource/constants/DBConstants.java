//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.framework.datasource.constants;

public class DBConstants {
    public static final String DATA_ENABLE_STATUS = "Y";
    public static final String DATA_DISABLE_STATUS = "N";
    public static final String DISK_ENABLE_STATUS = "Y";
    public static final String DISK_DISABLE_STATUS = "N";
    public static final long DATA_FIRST_VERSION = 1L;

    public DBConstants() {
    }

    public static final long addVersion(long version) {
        return version + 1L;
    }
}
