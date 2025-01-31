package codes.ztereohype.nicerskies.mixin;

import codes.ztereohype.nicerskies.NicerSkies;
import codes.ztereohype.nicerskies.core.NebulaSeedManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(at = @At("TAIL"), method = "setLevel")
    private void onWorldLoad(CallbackInfo ci) {
        NicerSkies.skyManager.generateSky(NebulaSeedManager.getSeed());
    }
}
