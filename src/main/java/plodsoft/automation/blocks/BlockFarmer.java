package plodsoft.automation.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import plodsoft.automation.Automation;
import plodsoft.automation.tileentities.TileEntityFarmer;
import plodsoft.automation.tileentities.TileEntityTickable;

import javax.annotation.Nullable;

public class BlockFarmer extends Block {
   public static final String NAME = "farmer";
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static Item item;

   public BlockFarmer() {
      super(Material.WATER);
      setHardness(1.5f);
      setResistance(30f);
      setCreativeTab(Automation.Tab);
      setRegistryName(NAME);
      setUnlocalizedName(NAME);
      setLightLevel(1f);

      GameRegistry.register(this);
      GameRegistry.register(item = new ItemBlock(this), getRegistryName());
      GameRegistry.registerTileEntity(TileEntityFarmer.class, getRegistryName().toString());
      setDefaultState(blockState.getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(BlockLiquid.LEVEL, 1));
   }

   public static void initModel() {
      Automation.proxy.registerItemRenderer(item, 0);
   }

   @Override
   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   @Override
   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileEntityFarmer();
   }

   @Override protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, BlockLiquid.LEVEL);
   }

   @Override public int getMetaFromState(IBlockState state) {
      return state.getValue(FACING).getIndex();
   }

   @Override public IBlockState getStateFromMeta(int meta) {
      EnumFacing f = EnumFacing.getFront(meta);
      if (f.getAxis() == EnumFacing.Axis.Y)
         f = EnumFacing.NORTH;
      return getDefaultState().withProperty(FACING, f);
   }

   @Override
   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
   }

   @Override
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (worldIn.isRemote) {
         return true;
      }
      TileEntity te = worldIn.getTileEntity(pos);
      if (!(te instanceof TileEntityFarmer))
         return false;
      if (null == heldItem && playerIn.isSneaking()) {
         ((TileEntityTickable) te).resetTimer();
      }
      return true;
   }

   @Override
   public boolean isPassable(IBlockAccess world, BlockPos pos) {
      return false;
   }

   @Override
   public boolean isBlockSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
      return true;
   }

   @Override
   public boolean canSpawnInBlock() {
      return false;
   }

   @Override
   public boolean isReplaceable(IBlockAccess world, BlockPos pos) {
      return false;
   }

   @Override public boolean isOpaqueCube(IBlockState state) {
      return false;
   }
}
