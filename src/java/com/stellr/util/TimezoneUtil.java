/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */

package com.stellr.util;

import java.util.List;

/**
 *
 * @author Andre Labuschagne
 */
public class TimezoneUtil {
    
    public static Timezone MakeTimezone(String str) {
        List<Timezone> tzList = TimezoneListUtil.GetDoubleTimezones();
        for(Timezone tz : tzList) {
            if(tz.getLabel().equals(str)) {
                return tz;
            }
        }
        
        tzList = TimezoneListUtil.GetIntegerTimezones();
        for(Timezone tz : tzList) {
            if(tz.getLabel().equals(str)) {
                return tz;
            }
        }
        return null;
    }
}
