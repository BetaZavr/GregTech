package gregtech.api.capability.impl;

import gregtech.api.capability.IThermalFluidHandlerItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

public class SimpleThermalFluidHandlerItemStack extends GTSimpleFluidHandlerItemStack implements IThermalFluidHandlerItemStack {

    public final int maxFluidTemperature;
    private final boolean gasProof;
    private final boolean acidProof;
    private final boolean cryoProof;
    private final boolean plasmaProof;

    /**
     * @param container The container itemStack, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     */
    public SimpleThermalFluidHandlerItemStack(@NotNull ItemStack container, int capacity, int maxFluidTemperature, boolean gasProof, boolean acidProof, boolean cryoProof, boolean plasmaProof) {
        super(container, capacity);
        this.maxFluidTemperature = maxFluidTemperature;
        this.gasProof = gasProof;
        this.acidProof = acidProof;
        this.cryoProof = cryoProof;
        this.plasmaProof = plasmaProof;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return IThermalFluidHandlerItemStack.super.canFillFluidType(fluid);
    }

    @Override
    public int getMaxFluidTemperature() {
        return maxFluidTemperature;
    }

    @Override
    public boolean isGasProof() {
        return gasProof;
    }

    @Override
    public boolean isAcidProof() {
        return acidProof;
    }

    @Override
    public boolean isCryoProof() {
        return cryoProof;
    }

    @Override
    public boolean isPlasmaProof() {
        return plasmaProof;
    }
}
