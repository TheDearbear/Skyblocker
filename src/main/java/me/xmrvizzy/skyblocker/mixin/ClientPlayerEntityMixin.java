package me.xmrvizzy.skyblocker.mixin;

import com.mojang.authlib.GameProfile;

import dev.cbyrne.betterinject.annotations.Inject;
import me.xmrvizzy.skyblocker.skyblock.HotbarSlotLock;
import me.xmrvizzy.skyblocker.skyblock.item.ItemProtection;
import me.xmrvizzy.skyblocker.skyblock.rift.HealingMelonIndicator;
import me.xmrvizzy.skyblocker.utils.Utils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    public void skyblocker$dropSelectedItem(CallbackInfoReturnable<Boolean> cir) {
        if (Utils.isOnSkyblock()) {
            if (ItemProtection.isItemProtected(this.getInventory().getMainHandStack())) cir.setReturnValue(false);
            HotbarSlotLock.handleDropSelectedItem(this.getInventory().selectedSlot, cir);
        }
    }

    @Inject(method = "updateHealth", at = @At("RETURN"))
    public void skyblocker$updateHealth() {
        HealingMelonIndicator.updateHealth();
    }
}