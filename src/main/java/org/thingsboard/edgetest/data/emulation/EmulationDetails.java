package org.thingsboard.edgetest.data.emulation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmulationDetails {

    private String telemetrySendProtocol;
    private long emulationTime;
}
