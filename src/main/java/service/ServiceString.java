package service;

import java.util.Arrays;
import java.util.Objects;

public class ServiceString {

    private static final ServiceString instance = new ServiceString();

    private ServiceString() {
    }

    public static ServiceString getInstance() {
        return instance;
    }

    public int getTrimmedLen(String str) {
        if (Objects.isNull(str)) {
            return -1;
        }
        return str.trim().length();
    }

    public boolean isEmpty(String str) {
        return getTrimmedLen(str) < 1;
    }

    public boolean isContainEmptyString(String... strs) {
        return Arrays.stream(strs).anyMatch(this::isEmpty);
    }

}
