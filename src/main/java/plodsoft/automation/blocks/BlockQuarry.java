package plodsoft.automation.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
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
import net.minecraftforge.items.ItemStackHandler;
import plodsoft.automation.Automation;
import plodsoft.automation.gui.GuiHandler;
import plodsoft.automation.tileentities.TileEntityQuarry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockQuarry extends Block {
   public static final String NAME = "quarry";
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static Item item;

   public BlockQuarry() {
      super(Material.ROCK);
      setHardness(1.5f);
      setResistance(30f);
      setCreativeTab(Automation.Tab);
      setRegistryName(NAME);
      setUnlocalizedName(NAME);

      GameRegistry.register(this);
      GameRegistry.register(item = new ItemBlock(this), getRegistryName());
      GameRegistry.registerTileEntity(TileEntityQuarry.class, getRegistryName().toString());
      setDefaultState(blockState.getBaseState()
            .withProperty(FACING, EnumFacing.NORTH));
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
      return new TileEntityQuarry();
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
      world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
   }

   @Override
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (worldIn.isRemote) {
         return true;
      }
      TileEntity te = worldIn.getTileEntity(pos);
      if (!(worldIn.getTileEntity(pos) instanceof TileEntityQuarry))
         return false;
      playerIn.openGui(Automation.instance, GuiHandler.GUI_QUARRY_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
      return true;
   }

   @Override
   public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      List<ItemStack> ret =  super.getDrops(world, pos, state, fortune);
      TileEntity te = world.getTileEntity(pos);
      if (!(te instanceof TileEntityQuarry))
         return ret;
      ItemStackHandler handler = ((TileEntityQuarry) te).handler;
      for (int i = TileEntityQuarry.SIZE; --i >= 0; ) {
         ItemStack stack = handler.getStackInSlot(i);
         if (stack != null)
            ret.add(stack);
      }
      return ret;
   }

   // copy from BlockFlowerPot
   @Override
   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
   {
      if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
      return super.removedByPlayer(state, world, pos, player, willHarvest);
   }
   @Override
   public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool)
   {
      super.harvestBlock(world, player, pos, state, te, tool);
      world.setBlockToAir(pos);
   }
}
