package com.toddway.shelf;


import org.junit.Before;
import org.junit.Test;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ShelfTest {

    String itemKey = "item key";
    String itemValue = "item value";
    Shelf shelf;

    @Before
    public void setUp() throws Exception {
        shelf = new Shelf(new File("/tmp"));
        shelf.clear("");
    }

    @Test
    public void testGetString() throws Exception {
        shelf.item(itemKey).put(itemValue);

        assertEquals(shelf.item(itemKey).get(String.class), itemValue);
    }

    @Test
    public void testIsOlderThan() throws Exception {
        shelf.item(itemKey).put(itemValue);

        TimeUnit.SECONDS.sleep(5);

        assertTrue(shelf.item(itemKey).isOlderThan(1, TimeUnit.SECONDS));
        assertTrue(!shelf.item(itemKey).isOlderThan(100, TimeUnit.SECONDS));
    }

    @Test
    public void testGetPojo() throws Exception{
        Pojo pojo = Pojo.create();

        long start = System.currentTimeMillis();
        shelf.item(itemKey).put(pojo);
        System.out.println("put time:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        pojo = shelf.item(itemKey).get(Pojo.class);
        System.out.println("get time:" + (System.currentTimeMillis() - start));

        Integer i = pojo.integerArrayList.iterator().next();
        System.out.println("first item: " + i);
        assertEquals((int) i, 0);
        assertEquals(pojo.num, 5);
    }

//    @Test
//    public void testGetMap() throws Exception {
//        HashMap<String, Pojo> map = new HashMap<>();
//        map.put(itemKey, initPojo());
//
//        shelf.item(itemKey).put(map);
//
//        map = shelf.item(itemKey).get(HashMap.class);
//
//        System.out.println("first item: " + map.get(itemKey).strings.iterator().next());
//
//    }

    static class Pojo implements Serializable {
        public ArrayList<Integer> integerArrayList;
        public int num = 5;

        static Pojo create() {
            Pojo pojo = new Pojo();
            ArrayList<Integer> list = new ArrayList();
            for (int i = 0; i < 99999; i++) {
                list.add(i);
            }
            pojo.integerArrayList = list;
            return pojo;
        }
    }
}