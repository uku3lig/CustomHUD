package com.minenash.customhud.HudElements.functional;

import com.minenash.customhud.HudElements.interfaces.ExecuteElement;
import com.minenash.customhud.HudElements.interfaces.HudElement;
import com.minenash.customhud.HudElements.list.*;
import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.complex.ListManager;
import com.minenash.customhud.data.CHFormatting;
import com.minenash.customhud.data.Flags;
import com.minenash.customhud.data.HudTheme;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionalElement implements HudElement {

    @Override public String getString() { return null; }
    @Override public Number getNumber() { return null; }
    @Override public boolean getBoolean() { return false; }

    public static class ChangeTheme extends FunctionalElement {
        public final HudTheme theme;
        public ChangeTheme(HudTheme theme) { this.theme = theme; }
    }

    public static class ChangeFormatting extends FunctionalElement {
        public final CHFormatting formatting;
        public ChangeFormatting(CHFormatting formatting) { this.formatting = formatting; }
        public ChangeFormatting(int color) {
            formatting = new CHFormatting().color(color, mask(color));
        }
        protected int mask(int color) {return ((color & 0xFF000000) != 0 ? 0xFF000000 : 0x00000000)
                | ((color & 0x00FFFFFF) != 0 ? 0x00FFFFFF : 0x00000000); }
        public CHFormatting getFormatting() {return formatting;}
    }
    public static class ChangeFormattingFromElement extends ChangeFormatting {
        public final HudElement element;
        public ChangeFormattingFromElement(HudElement element) {super(null); this.element = element; }
        public CHFormatting getFormatting() {
            Number color = element.getNumber();
            return color == null ? null : new CHFormatting().color(color.intValue(), mask(color.intValue()));
        }
    }

    public static class NewLine extends FunctionalElement {}
    public static class LineBreak extends NewLine {}
    public static class EndProfile extends NewLine {}
    public static class IgnoreNewLineIfSurroundedByNewLine extends FunctionalElement {}

    public interface XList extends ExecuteElement {}
    public static class AdvanceList extends FunctionalElement implements XList {
        public final UUID providerID;
        public AdvanceList(UUID providerID) { this.providerID = providerID; }
        @Override public void run() { ListManager.advance(providerID); }
    }
    public static class PopList extends FunctionalElement implements XList {
        public final UUID providerID;
        public PopList(UUID providerID) { this.providerID = providerID; }
        @Override public void run() { ListManager.pop(providerID); }
    }
    public static class PushList extends FunctionalElement implements XList {
        public final UUID providerID;
        public final List<?> values;
        public PushList(UUID providerID, List<?> values) { this.providerID = providerID; this.values = values; }
        @Override public void run() { ListManager.push(providerID, values); }
    }
    public static class ExitList extends FunctionalElement {
        public final UUID providerID;
        public ExitList(UUID providerID) { this.providerID = providerID; }
    }
    public static class ContinueList extends FunctionalElement {
        public final UUID providerID;
        public ContinueList(UUID providerID) { this.providerID = providerID; }
    }
    public static boolean isList(HudElement element) {
        return element instanceof XList || element instanceof ListElement || element instanceof FilteredListElement;
    }

    public static class CreateListElement extends FunctionalElement {
        public final ListProviderSet.Entry entry;
        public final Attributers.Attributer attributer;
        public HudElement attribute;
        public CreateListElement(Supplier<?> supplier, Function<?,List<?>> function, Attributers.Attributer attributer, Flags flags) {
            String prefix = flags.listPrefix.isEmpty() ? Attributers.DEFAULT_PREFIX.get(attributer) : flags.listPrefix;
            this.entry = new ListProviderSet.Entry(new ListProvider.ListFunctioner(supplier,function), UUID.randomUUID(), prefix, flags.reverseList);
            this.attributer = attributer;
            Attributers.ATTRIBUTER_MAP.put(entry.provider(), attributer);
        }
    }

    public static class IgnoreErrorElement extends FunctionalElement {}

    public static class RefreshTimings extends FunctionalElement implements ExecuteElement {
        @Override public void run() { ComplexData.refreshTimings = true; }
    }

}
