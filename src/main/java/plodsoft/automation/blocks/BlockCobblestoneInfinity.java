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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import plodsoft.automation.Automation;
import plodsoft.automation.gui.GuiHandler;
import plodsoft.automation.tileentities.TileEntityCbsInf;

import javax.annotation.Nullable;

public class BlockCobblestoneInfinity extends Block {
   public static final String NAME = "cobblestoneInf";
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static Item item;

   public BlockCobblestoneInfinity() {
      super(Material.ROCK);

      setCreativeTab(Automation.Tab);
      setRegistryName(NAME);
      setUnlocalizedName(NAME);

      GameRegistry.register(this);
      GameRegistry.register(item = new ItemBlock(this), getRegistryName());
      GameRegistry.registerTileEntity(TileEntityCbsInf.class, getRegistryName().toString());
      setDefaultState(blockState.getBaseState()
            .withProperty(FACING, EnumFacing.NORTH));
   }

   public static void initModel() {
      Automation.proxy.registerItemRenderer(item, 0);
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (worldIn.isRemote)
         return true;
      TileEntity te = worldIn.getTileEntity(pos);
      if (te instanceof TileEntityCbsInf) {
         playerIn.openGui(Automation.instance, GuiHandler.GUI_COBBLESTONE_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
      }
      return true;
   }

   @Override
   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   @Override
   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileEntityCbsInf();
   }

   public IBlockState getStateFromMeta(int meta) {
      EnumFacing enumfacing = EnumFacing.getFront(meta);

      if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
         enumfacing = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, enumfacing);
   }

   public int getMetaFromState(IBlockState state) {
      return state.getValue(FACING).getIndex();
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
   }
}
