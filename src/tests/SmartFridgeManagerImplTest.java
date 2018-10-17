package tests;

import impls.SmartFridgeManagerImpl;
import items.ItemFillFactor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SmartFridgeManagerImplTest {

    @Test
    public void testCase() {
        SmartFridgeManagerImpl testFridge = new SmartFridgeManagerImpl();
        testFridge.handleItemAdded(110, "0", "Generic Milk", 1.0);
        testFridge.handleItemAdded(111, "5", "Diamond Almond Milk", 5.0);
        testFridge.handleItemAdded(111, "6", "Diamond Soy Milk", 5.0);
        testFridge.handleItemAdded(120, "10", "Diet Coke", 3.0);
        testFridge.handleItemAdded(120, "20", "Diet Pepsi", 2.0);

        // test fill factor of 10 or less
        Object[] item = testFridge.getItems(10.0);
        Double fillFactorItemType = testFridge.getFillFactor(110);
        System.out.println("size of object array is " + item.length);
        assertNotSame(5, item.length);
        assertSame(3, item.length);
        System.out.println("how much milk is there? Should be 1.0: " + fillFactorItemType);
        assertEquals( 1.0,fillFactorItemType, .1);

        // test fill factor of 0 when Generic Milk has been removed
        testFridge.handleItemRemoved("0");
        item = testFridge.getItems(2.0);
        System.out.println("size of object array is " + item.length);
        assertNotSame(5, item.length);
        assertSame(1, item.length);
        double fillFactor = 0.0;
        double checkFillFactor = 0.0;
        for(Object itemFillFactor : item){
            fillFactor = ((ItemFillFactor)((Object[]) itemFillFactor)[0]).getFillFactor();
        }
        System.out.println("with removal of milk, check fill factor " + fillFactor);
        assertEquals(checkFillFactor, fillFactor, .01);
        // test fill factor of milk after removal
        fillFactorItemType = testFridge.getFillFactor(110);
        System.out.println("how much milk is there? Should be 0.0: " + fillFactorItemType);
        assertEquals( 0.0,fillFactorItemType, .01);

        // test fill factor when Generic Milk and another Milk are added
        testFridge.handleItemAdded(110, "0", "Generic Milk", .5);
        testFridge.handleItemAdded(110, "99", "Fat Free Milk", .75);
        item = testFridge.getItems(2.0);
        System.out.println("size of object array is " + item.length);
        assertNotSame(5, item.length);
        assertSame(1, item.length);
        fillFactor = 0.0;
        checkFillFactor = 1.25;
        for(Object itemFillFactor : item){
            fillFactor = ((ItemFillFactor)((Object[]) itemFillFactor)[0]).getFillFactor();
        }
        System.out.println("with addition of milk products check new fill factor of milk " + fillFactor);
        assertEquals(checkFillFactor, fillFactor, .01);

        // test fill factor of milk after additions
        fillFactorItemType = testFridge.getFillFactor(110);
        System.out.println("how much milk is there? Should be .625: " + fillFactorItemType);
        assertEquals( .625,fillFactorItemType, .01);

        testFridge.handleItemRemoved("99");
        fillFactorItemType = testFridge.getFillFactor(110);
        System.out.println("remove a milk, how much is left? Should be .5 instead of .625: " + fillFactorItemType);
        assertEquals( .5,fillFactorItemType, .01);

        testFridge.forgetItem(110);
        fillFactorItemType = testFridge.getFillFactor(110);
        System.out.println("remove all the milk, how much is left? Should be 0.0: " + fillFactorItemType);
        assertEquals( 0.0,fillFactorItemType, .001);

    }
}