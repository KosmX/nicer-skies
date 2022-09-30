package codes.ztereohype.example.mixin.debug;

import codes.ztereohype.example.ExampleMod;
import codes.ztereohype.example.sky.star.Starbox;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {
    @Inject(at = @At("HEAD"), method = "keyPress")
    private void printKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        // \ key, keydown action
        if (key == 92 && action == 1) {
            ExampleMod.skyManager.generateSky(123L);
            ExampleMod.toggle = !ExampleMod.toggle;
        }
    }
}
