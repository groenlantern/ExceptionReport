/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */

package com.stellr.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andre Labuschagne
 */
public class TimezoneListUtil {
    private static final List<Timezone> DEC_LIST = new ArrayList<>();
    private static final List<Timezone> INT_LIST = new ArrayList<>();
    
    private static void Create() {
        if(DEC_LIST.isEmpty()) {     
            DEC_LIST.add(new Timezone());       
            DEC_LIST.add(new Timezone(-12.00, "-12:00"));
            DEC_LIST.add(new Timezone(-11.00, "-11:00"));
            DEC_LIST.add(new Timezone(-10.00, "-10:00"));
            DEC_LIST.add(new Timezone(-9.50, "-09:30"));
            DEC_LIST.add(new Timezone(-9.00, "-09:00"));
            DEC_LIST.add(new Timezone(-8.00, "-08:00"));
            DEC_LIST.add(new Timezone(-7.00, "-07:00"));
            DEC_LIST.add(new Timezone(-6.00, "-06:00"));
            DEC_LIST.add(new Timezone(-5.00, "-05:00"));
            DEC_LIST.add(new Timezone(-4.50, "-04:30"));
            DEC_LIST.add(new Timezone(-4.00, "-04:00"));
            DEC_LIST.add(new Timezone(-3.50, "-03:30"));
            DEC_LIST.add(new Timezone(-3.00, "-03:00"));
            DEC_LIST.add(new Timezone(-2.00, "-02:00"));
            DEC_LIST.add(new Timezone(-1.00, "-01:00"));
            DEC_LIST.add(new Timezone(0.00, "\u00B100:00"));
            DEC_LIST.add(new Timezone(1.00, "+01:00"));
            DEC_LIST.add(new Timezone(2.00, "+02:00"));
            DEC_LIST.add(new Timezone(3.00, "+03:00"));
            DEC_LIST.add(new Timezone(3.50, "+03:30"));
            DEC_LIST.add(new Timezone(4.00, "+04:00"));
            DEC_LIST.add(new Timezone(4.50, "+04:30"));
            DEC_LIST.add(new Timezone(5.00, "+05:00"));
            DEC_LIST.add(new Timezone(5.50, "+05:30"));
            DEC_LIST.add(new Timezone(5.75, "+05:45"));
            DEC_LIST.add(new Timezone(6.00, "+06:00"));
            DEC_LIST.add(new Timezone(6.50, "+06:30"));
            DEC_LIST.add(new Timezone(7.00, "+07:00"));
            DEC_LIST.add(new Timezone(8.00, "+08:00"));
            DEC_LIST.add(new Timezone(8.50, "+08:30"));
            DEC_LIST.add(new Timezone(8.75, "+08:45"));
            DEC_LIST.add(new Timezone(9.00, "+09:00"));
            DEC_LIST.add(new Timezone(9.50, "+09:30"));
            DEC_LIST.add(new Timezone(10.00, "+10:00"));
            DEC_LIST.add(new Timezone(10.30, "+10:30"));
            DEC_LIST.add(new Timezone(11.00, "+11:00"));
            DEC_LIST.add(new Timezone(11.30, "+11:30"));
            DEC_LIST.add(new Timezone(12.00, "+12:00"));
            DEC_LIST.add(new Timezone(12.75, "+12:45"));
            DEC_LIST.add(new Timezone(13.00, "+13:00"));
            DEC_LIST.add(new Timezone(14.00, "+14:00"));
        }
        if(INT_LIST.isEmpty()) {                    
            INT_LIST.add(new Timezone());       
            INT_LIST.add(new Timezone(-12, "-12"));
            INT_LIST.add(new Timezone(-11, "-11"));
            INT_LIST.add(new Timezone(-10, "-10"));
            INT_LIST.add(new Timezone(-9, "-09"));
            INT_LIST.add(new Timezone(-8, "-08"));
            INT_LIST.add(new Timezone(-7, "-07"));
            INT_LIST.add(new Timezone(-6, "-06"));
            INT_LIST.add(new Timezone(-5, "-05"));
            INT_LIST.add(new Timezone(-4, "-04"));
            INT_LIST.add(new Timezone(-3, "-03"));
            INT_LIST.add(new Timezone(-2, "-02"));
            INT_LIST.add(new Timezone(-1, "-01"));
            INT_LIST.add(new Timezone(0, "\u00B100"));
            INT_LIST.add(new Timezone(1, "+01"));
            INT_LIST.add(new Timezone(2, "+02"));
            INT_LIST.add(new Timezone(3, "+03"));
            INT_LIST.add(new Timezone(4, "+04"));
            INT_LIST.add(new Timezone(5, "+05"));
            INT_LIST.add(new Timezone(6, "+06"));
            INT_LIST.add(new Timezone(7, "+07"));
            INT_LIST.add(new Timezone(8, "+08"));
            INT_LIST.add(new Timezone(9, "+09"));
            INT_LIST.add(new Timezone(10, "+10"));
            INT_LIST.add(new Timezone(11, "+11"));
            INT_LIST.add(new Timezone(12, "+12"));
            INT_LIST.add(new Timezone(13, "+13"));
            INT_LIST.add(new Timezone(14, "+14"));
        }
    }
    
    public static List<Timezone> GetDoubleTimezones() {
        Create();
        return DEC_LIST;
    }
    
    public static List<Timezone> GetIntegerTimezones() {
        Create();
        return INT_LIST;
    }
}
