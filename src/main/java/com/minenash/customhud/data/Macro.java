package com.minenash.customhud.data;

import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.conditionals.Operation;

import java.util.List;

public record Macro(List<HudElement> elements, Operation op) {}
