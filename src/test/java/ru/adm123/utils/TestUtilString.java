package ru.adm123.utils;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUtilString {

    private static UtilString utilString;

    @BeforeClass
    public static void setupForClass() {
        utilString = UtilString.getInstance();
    }


    @Test
    public void testGetInstance_returnUtilStringClass() {

        Class result = UtilString.getInstance().getClass();

        Assert.assertEquals(UtilString.class, result);

    }

    @Test
    public void testGetTrimmedLen_valueIsNull_returnMinusOne() {

        String value = null;

        int result = UtilString.getInstance().getTrimmedLen(value);

        Assert.assertEquals(-1, result);

    }

    @Test
    public void testGetTrimmedLen_valueIsSpaces_returnZero() {

        String value = "    ";

        int result = UtilString.getInstance().getTrimmedLen(value);

        Assert.assertEquals(0, result);

    }

    @Test
    public void testGetTrimmedLen_valueIsSpaceAroundString_returnTrimmedStringLength() {

        String value = " 1 23 ";
        int strLen = 4;

        int result = UtilString.getInstance().getTrimmedLen(value);

        Assert.assertEquals(strLen, result);

    }

    @Test
    public void testGetTrimmedLen_valueIsNormalString_returnStringLength() {

        String value = "1 23";
        int strLen = 4;

        int result = UtilString.getInstance().getTrimmedLen(value);

        Assert.assertEquals(strLen, result);

    }

    @Test
    public void testIsEmpty_valueIsNull_returnTrue() {

        String value = null;

        boolean result = UtilString.getInstance().isEmpty(value);

        Assert.assertTrue(result);

    }

    @Test
    public void testIsEmpty_valueIsSpaces_returnTrue() {

        String value = "   ";

        boolean result = UtilString.getInstance().isEmpty(value);

        Assert.assertTrue(result);

    }

    @Test
    public void testIsEmpty_valueIsSpaceAroundString_returnFalse() {

        String value = " 123  ";

        boolean result = UtilString.getInstance().isEmpty(value);

        Assert.assertFalse(result);

    }

    @Test
    public void testIsEmpty_valueIsNormalString_returnFalse() {

        String value = "1 23";

        boolean result = UtilString.getInstance().isEmpty(value);

        Assert.assertFalse(result);

    }

    @Test
    public void testIsContainEmptyString_valuesIsNull_returnTrue() {

        String values = null;

        boolean result = UtilString.getInstance().isContainEmptyString(values);

        Assert.assertTrue(result);

    }

    @Test
    public void testIsContainEmptyString_valuesIsEmpty_returnTrue() {

        boolean result = UtilString.getInstance().isContainEmptyString();

        Assert.assertTrue(result);

    }

    @Test
    public void testIsContainEmptyString_valuesIsContainNull_returnTrue() {

        String[] values = new String[]{"123", null, "456"};

        boolean result = UtilString.getInstance().isContainEmptyString(values);

        Assert.assertTrue(result);

    }

    @Test
    public void testIsContainEmptyString_valuesIsContainEmptyString_returnTrue() {

        String[] values = new String[]{"123", "   ", "456"};

        boolean result = UtilString.getInstance().isContainEmptyString(values);

        Assert.assertTrue(result);

    }

    @Test
    public void testIsContainEmptyString_valuesIsNotContainEmptyString_returnTrue() {

        String[] values = new String[]{" 123 ", "456", "789"};

        boolean result = UtilString.getInstance().isContainEmptyString(values);

        Assert.assertFalse(result);

    }

}