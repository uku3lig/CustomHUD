package com.minenash.customhud.HudElements;

import com.minenash.customhud.ProfileManager;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.minenash.customhud.ProfileManager.getActive;

public class LastUpdatedElement implements HudElement {

    private final DateTimeFormatter formatter;

    public LastUpdatedElement(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String getString() {
        return getActive() == null ? "" : getActive().updatedDateTime.format(formatter);
    }

    @Override
    public Number getNumber() {
        return new Date().getTime();
    }

    @Override
    public boolean getBoolean() {
        return true;
    }
}
