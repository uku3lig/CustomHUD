package com.minenash.customhud.gui;

import com.minenash.customhud.ConfigManager;
import com.minenash.customhud.CustomHud;
import com.minenash.customhud.ProfileManager;
import com.minenash.customhud.gui.profiles_widget.LineEntry;
import com.minenash.customhud.gui.profiles_widget.ProfileLineEntry;
import com.minenash.customhud.gui.profiles_widget.ProfileLinesWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import static com.minenash.customhud.CustomHud.CLIENT;
import static net.minecraft.client.gui.navigation.NavigationDirection.*;

public class NewConfigScreen extends Screen {

    private final Screen parent;
    private final TextRenderer font;

    private ProfileLinesWidget profiles;
    public KeyBinding selectedKeybind;
    public ProfileLineEntry editing;

    public enum Mode {NORMAL, REORDER, DELETE}
    public Mode mode = Mode.NORMAL;

    public NewConfigScreen(Screen parent) {
        super(Text.translatable("sml.config.screen.title"));
        this.parent = parent;
        this.font = MinecraftClient.getInstance().textRenderer;
    }

    @Override
    public void init() {
        profiles = new ProfileLinesWidget(this,30, height-32);
        addSelectableChild(profiles);

        this.addDrawableChild( ButtonWidget.builder(Text.literal("Open Folder"),
                button -> new Thread(() -> Util.getOperatingSystem().open(CustomHud.PROFILE_FOLDER.toFile())).start())
                .position(this.width / 2 - 155, this.height - 26).size(150, 20).build() );

        this.addDrawableChild( ButtonWidget.builder(ScreenTexts.DONE, button -> close())
                .position(this.width / 2 - 155 + 160, this.height - 26).size(150, 20).build() );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(font, Text.translatable("config.custom_hud.title"), this.width / 2, 11, 0xFFFFFF);
        profiles.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (selectedKeybind != null) {
            selectedKeybind.setBoundKey(InputUtil.Type.MOUSE.createFromCode(button));
            selectedKeybind = null;
            profiles.update();
            return true;
        }

        if (super.mouseClicked(mouseX, mouseY, button))
            return true;

        for (var c : profiles.children())
            if (c instanceof ProfileLineEntry e)
                e.editName.setFocused(false);

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (selectedKeybind != null) {
            selectedKeybind.setBoundKey(keyCode == GLFW.GLFW_KEY_ESCAPE ? InputUtil.UNKNOWN_KEY : InputUtil.fromKeyCode(keyCode, scanCode));
            selectedKeybind = null;
            profiles.update();
            return true;
        }

        switch (keyCode) {
            case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                boolean wasFocused = false;
                for (var c : profiles.children()) {
                    if (c instanceof ProfileLineEntry e) {
                        if (e.editName.isFocused())
                            wasFocused = true;
                        e.editName.setFocused(false);
                    }
                }
                if (keyCode != GLFW.GLFW_KEY_ESCAPE || wasFocused)
                    return true;
            }
            case GLFW.GLFW_KEY_LEFT -> {
                for (var c : profiles.children())
                    if (c instanceof ProfileLineEntry e)
                        if (e.editName.isFocused())
                            if (e.editName.getCursor() == 0)
                                return move(LEFT);
                            else break;
                        else if (e.cycled.isFocused()) {
                            e.editName.setCursorToEnd(false);
                            return move(LEFT);
                        }
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                for (var c : profiles.children())
                    if (c instanceof ProfileLineEntry e)
                        if (e.editName.isFocused())
                            if (e.editName.getCursor() == e.editName.getText().length())
                                return move(RIGHT);
                            else break;
                        else if (e.selected.isFocused()) {
                            e.editName.setCursorToStart(false);
                            return move(RIGHT);
                        }
            }
            case GLFW.GLFW_KEY_UP -> {
                int max = profiles.children().size()-1;
                for (int i = 0; i < max; i++)
                    if (profiles.children().get(i) instanceof ProfileLineEntry e && e.toggles.isFocused())
                        if (i > 0)
                            return move(LEFT, UP);
                        else break;
                if (((LineEntry.NewProfile)profiles.children().get(max)).deleteProfiles.isFocused())
                    return move(LEFT, UP);
            }
            case GLFW.GLFW_KEY_DOWN -> {
                for (int i = 0; i < profiles.children().size()-1; i++)
                    if (profiles.children().get(i) instanceof ProfileLineEntry e && e.toggles.isFocused())
                        return move(LEFT, DOWN);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean move(NavigationDirection... ds) {
        for (var d : ds) this.switchFocus( super.getNavigationPath(new GuiNavigation.Arrow(d)) );
        return true;
    }

    @Override
    public void close() {
        CLIENT.setScreen(parent);
        profiles.update();
        ConfigManager.save();
    }
}
