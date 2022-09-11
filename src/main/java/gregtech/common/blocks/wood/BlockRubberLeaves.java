package gregtech.common.blocks.wood;

import com.google.common.collect.Lists;
import gregtech.GregTechMod;
import gregtech.api.GregTechAPI;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Random;

public class BlockRubberLeaves extends BlockLeaves {

    public BlockRubberLeaves() {
        setDefaultState(this.blockState.getBaseState()
                .withProperty(CHECK_DECAY, true)
                .withProperty(DECAYABLE, true));
        setTranslationKey("rubber_leaves");
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        Blocks.FIRE.setFireInfo(this, 30, 60);
    }

    @Override
    public EnumType getWoodType(int meta) {
        return null;
    }

    @NotNull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
                .withProperty(DECAYABLE, (meta & 1) == 0)
                .withProperty(CHECK_DECAY, (meta & 2) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (!state.getValue(DECAYABLE)) {
            meta |= 1;
        }
        if (state.getValue(CHECK_DECAY)) {
            meta |= 2;
        }
        return meta;
    }

    @NotNull
    @Override
    public Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return Item.getItemFromBlock(MetaBlocks.RUBBER_SAPLING);
    }

    @NotNull
    @Override
    public List<ItemStack> onSheared(@NotNull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Lists.newArrayList(new ItemStack(this, 1, 0));
    }

    @Override
    @NotNull
    public BlockRenderLayer getRenderLayer() {
        if (!fancyLeaves()) {
            return super.getRenderLayer();
        }
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        if (!fancyLeaves()) {
            return super.isOpaqueCube(state);
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(@NotNull IBlockState blockState, @NotNull IBlockAccess blockAccess, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        if (!fancyLeaves()) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        return true;
    }

    private boolean fancyLeaves() {
        return GregTechMod.proxy.isFancyGraphics();
    }
}
