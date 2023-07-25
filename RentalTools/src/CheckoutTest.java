import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;
import org.junit.*;

public class CheckoutTest {
    private Calendar rentalCalendar;
    private Date rentalDate;

    @Before
    public void setup(){
        rentalCalendar = Calendar.getInstance();
    }
    
    @After
    public void tearDown() {
        rentalCalendar = null;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void test1(){
        rentalCalendar.set(Calendar.YEAR, 15);
        rentalCalendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        rentalCalendar.set(Calendar.DAY_OF_MONTH, 3);
        rentalDate = rentalCalendar.getTime();
        
        Checkout.checkout("LADW", rentalDate, 6, 101);
        
    }

    @Test
    public void test2(){
        rentalCalendar.set(Calendar.YEAR, 20);
        rentalCalendar.set(Calendar.MONTH, Calendar.JULY);
        rentalCalendar.set(Calendar.DAY_OF_MONTH, 2);
        rentalDate = rentalCalendar.getTime();
        HashMap<String, Object> checkoutMap= new HashMap<>();
        
        checkoutMap = Checkout.checkout("LADW", rentalDate, 3, 10);

        assertTrue(Checkout.wernerLadder.toolBrand.equals(checkoutMap.get("brand")));
        assertTrue(Checkout.wernerLadder.toolCode.equals(checkoutMap.get("code")));
        assertTrue(Checkout.wernerLadder.toolType.equals(checkoutMap.get("type")));
        assertTrue("10%".equals(checkoutMap.get("discount")));
        assertTrue("$5.97".equals(checkoutMap.get("initialCost")));
        assertTrue("$0.60".equals(checkoutMap.get("discountAmount")));
        assertEquals(2, checkoutMap.get("chargeDays"));
    }

    @Test
    public void test3(){
        rentalCalendar.set(Calendar.YEAR, 2015);
        rentalCalendar.set(Calendar.MONTH, Calendar.JULY);
        rentalCalendar.set(Calendar.DAY_OF_MONTH, 2);
        rentalDate = rentalCalendar.getTime();
        HashMap<String, Object> checkoutMap= new HashMap<>();
        
        checkoutMap = Checkout.checkout("CHNS", rentalDate, 5, 25);

        assertTrue(Checkout.stihlChainsaw.toolBrand.equals(checkoutMap.get("brand")));
        assertTrue(Checkout.stihlChainsaw.toolCode.equals(checkoutMap.get("code")));
        assertTrue(Checkout.stihlChainsaw.toolType.equals(checkoutMap.get("type")));
        assertTrue("25%".equals(checkoutMap.get("discount")));
        assertTrue("$7.45".equals(checkoutMap.get("initialCost")));
        assertTrue("$1.86".equals(checkoutMap.get("discountAmount")));
        assertEquals(3, checkoutMap.get("chargeDays"));
    }

    @Test
    public void test4(){
        rentalCalendar.set(Calendar.YEAR, 2015);
        rentalCalendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        rentalCalendar.set(Calendar.DAY_OF_MONTH, 3);
        rentalDate = rentalCalendar.getTime();
        HashMap<String, Object> checkoutMap= new HashMap<>();
        
        checkoutMap = Checkout.checkout("JAKD", rentalDate, 6, 0);

        assertTrue(Checkout.dewaltJackhammer.toolBrand.equals(checkoutMap.get("brand")));
        assertTrue(Checkout.dewaltJackhammer.toolCode.equals(checkoutMap.get("code")));
        assertTrue(Checkout.dewaltJackhammer.toolType.equals(checkoutMap.get("type")));
        assertTrue("0%".equals(checkoutMap.get("discount")));
        assertTrue("$17.94".equals(checkoutMap.get("initialCost")));
        assertTrue("$0.00".equals(checkoutMap.get("discountAmount")));
        assertEquals(3, checkoutMap.get("chargeDays"));
    }

    @Test
    public void test5(){
        rentalCalendar.set(Calendar.YEAR, 2015);
        rentalCalendar.set(Calendar.MONTH, Calendar.JULY);
        rentalCalendar.set(Calendar.DAY_OF_MONTH, 2);
        rentalDate = rentalCalendar.getTime();
        HashMap<String, Object> checkoutMap= new HashMap<>();
        
        checkoutMap = Checkout.checkout("JAKR", rentalDate, 9, 0);
        
        assertTrue(Checkout.ridgidJackhammer.toolBrand.equals(checkoutMap.get("brand")));
        assertTrue(Checkout.ridgidJackhammer.toolCode.equals(checkoutMap.get("code")));
        assertTrue(Checkout.ridgidJackhammer.toolType.equals(checkoutMap.get("type")));
        assertTrue("0%".equals(checkoutMap.get("discount")));
        assertTrue("$26.91".equals(checkoutMap.get("initialCost")));
        assertTrue("$0.00".equals(checkoutMap.get("discountAmount")));
        assertEquals(6, checkoutMap.get("chargeDays"));
    }

    @Test
    public void test6(){
        rentalCalendar.set(Calendar.YEAR, 2020);
        rentalCalendar.set(Calendar.MONTH, Calendar.JULY);
        rentalCalendar.set(Calendar.DAY_OF_MONTH, 2);
        rentalDate = rentalCalendar.getTime();
        HashMap<String, Object> checkoutMap= new HashMap<>();
        
        checkoutMap = Checkout.checkout("JAKR", rentalDate, 4, 50);
        
        assertTrue(Checkout.ridgidJackhammer.toolBrand.equals(checkoutMap.get("brand")));
        assertTrue(Checkout.ridgidJackhammer.toolCode.equals(checkoutMap.get("code")));
        assertTrue(Checkout.ridgidJackhammer.toolType.equals(checkoutMap.get("type")));
        assertTrue("50%".equals(checkoutMap.get("discount")));
        assertTrue("$11.96".equals(checkoutMap.get("initialCost")));
        assertTrue("$5.98".equals(checkoutMap.get("discountAmount")));
        assertEquals(1, checkoutMap.get("chargeDays"));
    }
    
}
