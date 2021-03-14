package org.seasar.framework.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class UtcUtil {
    protected UtcUtil() {}

    public static OffsetDateTime toOffsetDateTime(Timestamp ts) {
        return ts == null ?  null :
            OffsetDateTime.of(ts.toLocalDateTime(), ZoneOffset.UTC);
    }
    public static Timestamp toTimestamp(OffsetDateTime odt) {
        return Timestamp.valueOf(odt.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
    }
    
    public static OffsetDateTime toOffsetDateTime(LocalDateTime ldt) {
        return ldt == null ? null : 
            OffsetDateTime.of(ldt, ZoneOffset.UTC);
    }
    public static LocalDateTime toLocalDateTime(OffsetDateTime odt) {
        return odt.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    

}
