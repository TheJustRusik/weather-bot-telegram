package org.kenuki.service2.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cevent {
    private String cString;
    private Integer cInteger;
    private Long cLong;
}
