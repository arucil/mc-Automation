package plodsoft.automation.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import plodsoft.automation.Automation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import plodsoft.automation.renderers.FeederTESR;
import plodsoft.automation.tileentities.TileEntityFeeder;

import javax.annotation.Nullable;

public class BlockFeeder extends Block {
   public static final String NAME = "feeder";
   private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(.0625, 0, .0625, .9375, .3125, .9375);
   public static final PropertyDirection FACING = BlockHorizontal.FACING;

   public static Item item;

   public BlockFeeder() {
      super(Material.ROCK);
      setHardness(3f);
      setCreativeTab(Automation.Tab);
      setRegistryName(NAME);
      setUnlocalizedName(NAME);

      GameRegistry.register(this);
      GameRegistry.register(item = new ItemBlock(this), getRegistryName());
      GameRegistry.registerTileEntity(TileEntityFeeder.class, getRegistryName().toString());
      setDefaultState(blockState.getBaseState()
                  .withProperty(FACING, EnumFacing.NORTH));
   }

   public static void initModel() {
      Automation.proxy.registerItemRenderer(item, 0);
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFeeder.class, new FeederTESR());
   }

   @Override
   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      return false;
   }

   @Override
   public boolean isBlockNormalCube(IBlockState blockState) {
      return false;
   }

   @Override
   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   @Override
   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileEntityFeeder();
   }

   // must override this method or the item rendered would be dark
   @Override
   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   @Override
   public boolean isFullCube(IBlockState state) {
      return false;
   }

   @Override protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
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
      world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()), 2);
   }

   @Override
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (worldIn.isRemote)
         return true;
      TileEntity te = worldIn.getTileEntity(pos);
      if (!(te instanceof TileEntityFeeder))
         return false;
      return true;
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      return BOUNDS;
   }
}
