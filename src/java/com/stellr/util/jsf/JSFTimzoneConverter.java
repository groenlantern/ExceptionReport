/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */

package com.stellr.util.jsf;

import com.stellr.util.Timezone;
import com.stellr.util.TimezoneUtil;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Andre Labuschagne
 */
@FacesConverter("JSFTimzoneConverter")
public class JSFTimzoneConverter implements Converter {

    private static final Logger LOG = Logger.getLogger(JSFTimzoneConverter.class.getName());

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (StringUtils.isNotBlank(string)) {
            return TimezoneUtil.MakeTimezone(string);
        }
        return new Timezone();
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object obj) {
        if(obj == null) {
            return "";
        }
        if (obj instanceof Timezone) {
            Timezone tz = (Timezone) obj;
            return tz.getLabel();
        } else if (obj instanceof Integer) {
            Integer tz = (Integer) obj;
            return tz.toString();
        }  else if (obj instanceof Double) {
            Double tz = (Double) obj;
            return tz.toString();
        } else if (obj instanceof BigDecimal) {
            BigDecimal tz = (BigDecimal)obj;
            return tz.toPlainString();
        } else {
            StringBuilder sbWarn = new StringBuilder("Object received is not a timaezone, it is ");
            sbWarn.append(obj.getClass().getName());
            LOG.log(Level.WARNING, sbWarn.toString());
            return null;
        }
    }

}
