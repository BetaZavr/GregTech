package gregtech.api.metatileentity;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.impl.FilteredFluidHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.RecipeLogicSteam;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeMap;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.cube.SimpleSidedCubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.api.util.GTUtility;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;

public abstract class SteamMetaTileEntity extends MetaTileEntity {

    protected static final int STEAM_CAPACITY = 16000;

    protected final boolean isHighPressure;
    protected final ICubeRenderer renderer;
    protected RecipeLogicSteam workableHandler;
    protected FluidTank steamFluidTank;

    public SteamMetaTileEntity(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, ICubeRenderer renderer, boolean isHighPressure) {
        super(metaTileEntityId);
        this.workableHandler = new RecipeLogicSteam(this,
                recipeMap, isHighPressure, steamFluidTank, 1.0);
        this.isHighPressure = isHighPressure;
        this.renderer = renderer;
    }

    @Override
    public boolean isActive() {
        return workableHandler.isActive() && workableHandler.isWorkingEnabled();
    }

    @SideOnly(Side.CLIENT)
    protected SimpleSidedCubeRenderer getBaseRenderer() {
        if (isHighPressure) {
            if (isBrickedCasing()) {
                return Textures.STEAM_BRICKED_CASING_STEEL;
            } else {
                return Textures.STEAM_CASING_STEEL;
            }
        } else {
            if (isBrickedCasing()) {
                return Textures.STEAM_BRICKED_CASING_BRONZE;
            } else {
                return Textures.STEAM_CASING_BRONZE;
            }
        }
    }

    @Override
    public int getDefaultPaintingColor() {
        return 0xFFFFFF;
    }

    @Override
    public boolean onWrenchClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        if (!playerIn.isSneaking()) {
            EnumFacing currentVentingSide = workableHandler.getVentingSide();
            if (currentVentingSide == facing ||
                    getFrontFacing() == facing) return false;
            workableHandler.setVentingSide(facing);
            return true;
        }
        return super.onWrenchClick(playerIn, hand, facing, hitResult);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Pair<TextureAtlasSprite, Integer> getParticleTexture() {
        return Pair.of(getBaseRenderer().getParticleSprite(), getPaintingColorForRendering());
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        IVertexOperation[] colouredPipeline = ArrayUtils.add(pipeline, new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering())));
        getBaseRenderer().render(renderState, translation, colouredPipeline);
        renderer.renderOrientedState(renderState, translation, pipeline, getFrontFacing(), workableHandler.isActive(), workableHandler.isWorkingEnabled());
        Textures.STEAM_VENT_OVERLAY.renderSided(workableHandler.getVentingSide(), renderState, translation, pipeline);
    }

    protected boolean isBrickedCasing() {
        return false;
    }

    @Override
    public FluidTankList createImportFluidHandler() {
        this.steamFluidTank = new FilteredFluidHandler(STEAM_CAPACITY)
                .setFillPredicate(ModHandler::isSteam);
        return new FluidTankList(false, steamFluidTank);
    }

    public ModularUI.Builder createUITemplate(EntityPlayer player) {
        return ModularUI.builder(GuiTextures.BACKGROUND_STEAM.get(isHighPressure), 176, 166)
                .label(6, 6, getMetaFullName()).shouldColor(false)
                .widget(new ImageWidget(79, 42, 18, 18, GuiTextures.INDICATOR_NO_STEAM.get(isHighPressure))
                        .setPredicate(() -> workableHandler.isHasNotEnoughEnergy()))
                .bindPlayerInventory(player.inventory, GuiTextures.SLOT_STEAM.get(isHighPressure), 0);
    }

    @Override
    public SoundEvent getSound() {
        return workableHandler.getRecipeMap().getSound();
    }

    @Override
    public void addToolUsages(ItemStack stack, @Nullable World world, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.tool_action.screwdriver.access_covers"));
        tooltip.add(I18n.format("gregtech.tool_action.wrench.set_facing"));
        tooltip.add(I18n.format("gregtech.tool_action.soft_hammer.reset"));
        super.addToolUsages(stack, world, tooltip, advanced);
    }
}
