import java.util.*;
import java.text.SimpleDateFormat;
import java.security.InvalidParameterException;
import java.text.NumberFormat;

public class Checkout {
    
    public static final String CHAINSAW_STIHL = "CHNS";
    public static final String LADDER_WERNER = "LADW";
    public static final String JACKHAMMER_DEWALT = "JAKD";
    public static final String JACKHAMMER_RIDGID = "JAKR";

    public static boolean independenceDay = false;
    public static boolean laborDay = false;

    public static Tool stihlChainsaw = new Tool("Chainsaw", "Stihl", "CHNS", 1.49);
    public static Tool wernerLadder = new Tool("Ladder", "Werner", "LADW", 1.99);
    public static Tool dewaltJackhammer = new Tool("Jackhammer", "Dewalt", "JAKD", 2.99);
    public static Tool ridgidJackhammer = new Tool("Jackhammer", "Ridgid", "JAKR", 2.99);

    
    public static void userInput(){
        Scanner userInput = new Scanner(System.in);
            boolean validToolCode = false;
            String toolCode = new String();

            while(!validToolCode){
                System.out.println("Enter Tool Code");
                toolCode = userInput.nextLine();

                if  (!CHAINSAW_STIHL.equals(toolCode) && 
                    !LADDER_WERNER.equals(toolCode) && 
                    !JACKHAMMER_DEWALT.equals(toolCode) && 
                    !JACKHAMMER_RIDGID.equals(toolCode)) {
                        System.out.println("Please enter Valid Tool Code");
                    }else{
                        validToolCode = true;
                    }
            }

            boolean validCheckoutDate = false;
            String checkoutDate = new String();
            Date date = new Date();

            while(!validCheckoutDate){
                System.out.println("Enter Checkout Date in the pattern 'MM/dd/yy'");
                checkoutDate = userInput.nextLine();
                try{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                    dateFormat.setLenient(false);
                    date = dateFormat.parse(checkoutDate);
                    System.out.println(date);
                    validCheckoutDate = true;
                    }catch(Exception e){
                        System.out.println("Please enter valid Checkout Date");
                    }
            }
            
            boolean validDuration = false;
            String durationInput = new String();
            int duration = 0;

            while(!validDuration){
                try{
                    System.out.println("Enter Rental Duration");
                    durationInput = userInput.nextLine();
                    duration = Integer.parseInt(durationInput);
                    if  (duration <= 0) {
                        System.out.println("Duration needs to be greater than 0");
                    }else{
                        validDuration = true;
                    }
                }catch(Exception e){
                        System.out.println("Duration needs to be a whole number greater than 0");
                }
            }
            
            boolean validDiscount = false;
            String discountInput = new String();
            int discount = 0;

            while(!validDiscount){
                try{    
                    System.out.println("Enter Rental Discount% (0 - 100)");
                    discountInput = userInput.nextLine();
                    discount = Integer.parseInt(discountInput);

                    if  ((discount < 0) || (discount > 100)) {
                            System.out.println("Discount needs to be a whole number from 0 and 100");
                        }else{
                            validDiscount = true;
                        }
                    }catch(Exception e){
                        System.out.println("Discount needs to be a whole number from 0 and 100");
                    }
            }
            userInput.close();

            Checkout.checkout(toolCode, date, duration, discount);
        }

    public static HashMap<String, Object> checkout(String itemCode, Date checkoutDate, int rentalDuration, int rentalDiscount){

        int chargeDays = 0;
        Calendar checkoutDateCalendar = Calendar.getInstance();
        checkoutDateCalendar.setTime(checkoutDate);
        double[] totalCost = new double[2];
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        HashMap<String, Tool> toolMap = new HashMap<String, Tool>();
    
        toolMap.put(CHAINSAW_STIHL, stihlChainsaw);
        toolMap.put(LADDER_WERNER, wernerLadder);
        toolMap.put(JACKHAMMER_DEWALT, dewaltJackhammer);
        toolMap.put(JACKHAMMER_RIDGID, ridgidJackhammer);

        Calendar dueDate = Calendar.getInstance();
        dueDate.setTime(checkoutDate);
        dueDate.add(Calendar.DATE, rentalDuration-1);
        chargeDays = chargeDays(checkoutDateCalendar, rentalDuration, itemCode);

        totalCost = transactionTotal(rentalDuration, rentalDiscount, itemCode);

        HashMap<String, Object> checkoutMap = new HashMap<String, Object>();
        checkoutMap.put("code", itemCode);
        checkoutMap.put("type", toolMap.get(itemCode).toolType);
        checkoutMap.put("brand", toolMap.get(itemCode).toolBrand);
        checkoutMap.put("duration", rentalDuration);
        checkoutMap.put("checkout", checkoutDate);
        checkoutMap.put("due", dueDate.getTime());
        checkoutMap.put("charge", toolMap.get(itemCode).toolDailyCharge);
        checkoutMap.put("chargeDays", chargeDays);
        checkoutMap.put("discount", (rentalDiscount + "%"));
        checkoutMap.put("initialCost", currencyFormatter.format((totalCost[0]+totalCost[1])));
        checkoutMap.put("discountAmount", currencyFormatter.format(totalCost[1]));
        checkoutMap.put("finalAmount", currencyFormatter.format(totalCost[0]));
        
        System.out.println("Tool Code: " + checkoutMap.get("code"));
        System.out.println("Tool Type: " + checkoutMap.get("type"));
        System.out.println("Tool Brand: " + checkoutMap.get("brand"));
        System.out.println("Rental Days: " + checkoutMap.get("duration"));
        System.out.println("Checkout Date: " + checkoutMap.get("checkout"));
        System.out.println("Due Date: " + checkoutMap.get("due"));
        System.out.println("Daily Rental Charge: " + checkoutMap.get("charge"));
        System.out.println("Charge Days: " + checkoutMap.get("chargeDays"));
        System.out.println("Pre-Discount charge: " + checkoutMap.get("initialCost"));
        System.out.println("Discount Percent: " + checkoutMap.get("discount"));
        System.out.println("Discount amount: " + checkoutMap.get("discountAmount"));
        System.out.println("Final Charge: " + checkoutMap.get("finalAmount"));

        return checkoutMap;        
    }

    private static int chargeDays(Calendar checkoutDate, int duration, String itemCode){

        int chargeDays = 0;
        
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(checkoutDate.getTime());

        Calendar chargeDate = Calendar.getInstance();
        chargeDate.setTime(checkoutDate.getTime());

        Calendar endDate  = Calendar.getInstance();
        endDate.setTime(checkoutDate.getTime());
        endDate.add(Calendar.DATE, duration);

        while(chargeDate.before(endDate)){
            if (!laborDay){
                laborDay = laborDayCheck (chargeDate);
            }

            if(!independenceDay){
                independenceDay = independenceDayCheck(chargeDate);
            }

            if ((!(chargeDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) &&
                !(chargeDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) ||
                (itemCode.equals(LADDER_WERNER) &&
                ((chargeDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) ||
                (chargeDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)))){
                    chargeDays++;
                }

            chargeDate.add(Calendar.DATE, 1);
        }

        if ((LADDER_WERNER.equals(itemCode) ||
            JACKHAMMER_DEWALT.equals(itemCode) ||
            JACKHAMMER_RIDGID.equals(itemCode)) &&
            laborDay){
            chargeDays--;
        }

        if ((LADDER_WERNER.equals(itemCode) ||
            JACKHAMMER_DEWALT.equals(itemCode) ||
            JACKHAMMER_RIDGID.equals(itemCode)) &&
            independenceDay){
            chargeDays--;
        }

        return chargeDays;
    }

    private static boolean independenceDayCheck (Calendar targetDate){
        
        boolean holiday = false;

        if ((targetDate.get(Calendar.MONTH) == Calendar.JULY) &&
            (targetDate.get(Calendar.DAY_OF_MONTH) == 4) &&
            ((targetDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) ||
            (targetDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY))){

                holiday = true;
            }

        if ((targetDate.get(Calendar.MONTH) == Calendar.JULY) &&
            (targetDate.get(Calendar.DAY_OF_MONTH) == 3) &&
            (targetDate.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)){
                holiday = true;
            }

        if ((targetDate.get(Calendar.MONTH) == Calendar.JULY) &&
            (targetDate.get(Calendar.DAY_OF_MONTH) == 5) &&
            (targetDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)){
                holiday = true;
            }

        return holiday;
    }

    private static boolean laborDayCheck (Calendar targetDate){

        boolean holiday = false;

        if ((targetDate.get(Calendar.MONTH) == Calendar.SEPTEMBER) &&
            (targetDate.get(Calendar.DAY_OF_MONTH) < 8) &&
            (targetDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) &&
            (targetDate.get(Calendar.WEEK_OF_MONTH) <= 2)){
                holiday = true;
            }
        
        return holiday;
    }

    private static double[] transactionTotal(int chargeDays, int discount, String itemCode){

        double[] costList = new double[2];

        if (discount>100 || discount <0){
            
            throw new InvalidParameterException("Please enter a value between 0 - 100");
        }

        if(wernerLadder.toolCode.equals(itemCode)){
            costList[0] = wernerLadder.toolDailyCharge*chargeDays*((100-discount)/100.00);
            costList[1] = wernerLadder.toolDailyCharge*chargeDays*((discount/100.00));
            return costList;
        }

        if(stihlChainsaw.toolCode.equals(itemCode)){
            costList[0] = stihlChainsaw.toolDailyCharge*chargeDays*((100-discount)/100.00);
            costList[1] = stihlChainsaw.toolDailyCharge*chargeDays*((discount/100.00));
            return costList;
        }
        
        if(dewaltJackhammer.toolCode.equals(itemCode) || ridgidJackhammer.toolCode.equals(itemCode)){
            costList[0] = dewaltJackhammer.toolDailyCharge*chargeDays*((100-discount)/100.00);
            costList[1] = dewaltJackhammer.toolDailyCharge*chargeDays*((discount/100.00));
            return costList;
        }

        return costList;
    }


}
