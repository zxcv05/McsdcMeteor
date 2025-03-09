package com.mcsdc.addon.mixin;

import com.mcsdc.addon.gui.ServerInfoScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin extends Screen {

    protected GameMenuMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;I)Lnet/minecraft/client/gui/widget/Widget;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void oninitWidgets(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder, Text text){
        MinecraftClient mc = MinecraftClient.getInstance();
        ServerInfo info = mc.getNetworkHandler().getServerInfo();

        adder.add(ButtonWidget.builder(Text.literal("Info"), (button) -> {
            mc.setScreen(new ServerInfoScreen(info.address));
        }).width(204).build(), 2);

        adder.add(ButtonWidget.builder(Text.literal("Reconnect"), (button) -> {
            MinecraftClient.getInstance().world.disconnect();
            ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()), mc,
                ServerAddress.parse(info.address), info, false, null);

        }).width(204).build(), 2);
    }

}
