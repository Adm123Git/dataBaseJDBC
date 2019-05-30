package ru.adm123.utils;

import java.util.Arrays;
import java.util.Objects;

public class UtilString {

    private static final UtilString instance = new UtilString();

    private UtilString() {
    }

    public static UtilString getInstance() {
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
        if (strs.length == 0) {
            return true;
        }
        return Arrays.stream(strs).anyMatch(this::isEmpty);
    }

}