package gregtech.api.items.toolitem;

import gregtech.api.items.IToolItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractToolItemCapabilityProvider<T> implements ICapabilityProvider {

    private final ItemStack itemStack;

    public AbstractToolItemCapabilityProvider(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    protected abstract Capability<T> getCapability();

    public boolean damageItem(int damage, boolean simulate) {
        return ((IToolItem) itemStack.getItem()).damageItem(itemStack, null, damage, simulate);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == getCapability();
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <R> R getCapability(@NotNull Capability<R> capability, @Nullable EnumFacing facing) {
        if (capability == getCapability()) {
            return getCapability().cast((T) this);
        }
        return null;
    }
}
