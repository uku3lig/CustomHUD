package com.minenash.customhud.registry;

import com.minenash.customhud.HudElements.list.ListProviderSet;
import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.data.Profile;

public record ParseContext(Profile profile, int line, ComplexData.Enabled enabled, ListProviderSet listProviders) {}
