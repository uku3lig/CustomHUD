package com.minenash.customhud.data;

import com.minenash.customhud.complex.ComplexData;

public enum DebugCharts {
    PROFILER ("profiler"),
    FPS ("fps"),
    TICK ("tick"),
    PING ("ping"),
    PACKET_SIZE ("packet_size"),
    NONE ("none");

    final String name;
    DebugCharts(String name) {
        this.name = name;
    }

    static DebugCharts parse(String name, ComplexData.Enabled enabled) {
        switch (name) {
            case "profiler": return PROFILER;
            case "fps": return FPS;
            case "tick": return TICK;
            case "ping": return PING;
            case "packetsize": return PACKET_SIZE;
            case "none": return NONE;
            default: return null;
        }
    }

    public String getName() {
        return name;
    }
}
