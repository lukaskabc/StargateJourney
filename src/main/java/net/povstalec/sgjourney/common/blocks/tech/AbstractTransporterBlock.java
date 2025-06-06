package net.povstalec.sgjourney.common.blocks.tech;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.common.block_entities.StructureGenEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.block_entities.tech.AbstractTransporterEntity;
import net.povstalec.sgjourney.common.init.BlockInit;

public abstract class AbstractTransporterBlock extends BaseEntityBlock
{
	protected String listName;
	
	protected AbstractTransporterBlock(Properties properties)
	{
		super(properties);
	}
	
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}
	
	@Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
        if (state.getBlock() != newState.getBlock())
        {
            BlockEntity entity = level.getBlockEntity(pos);
            
            if(entity instanceof AbstractTransporterEntity transporterEntity)
            	transporterEntity.removeTransporterFromNetwork();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
	
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		BlockEntity blockentity = level.getBlockEntity(pos);
		if(blockentity instanceof AbstractTransporterEntity transporter)
		{
			if(!level.isClientSide() && !player.isCreative())
			{
				ItemStack itemstack = new ItemStack(BlockInit.TRANSPORT_RINGS.get());
				
				blockentity.saveToItem(itemstack, level.registryAccess());
				if(transporter.hasCustomName())
					itemstack.set(DataComponents.ITEM_NAME, transporter.getCustomName());
					

				ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemstack);
				itementity.setDefaultPickUpDelay();
				level.addFreshEntity(itementity);
			}
		}

		return super.playerWillDestroy(level, pos, state, player);
	}
	
	@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
		String id = "";
		boolean hasData = stack.has(DataComponents.BLOCK_ENTITY_DATA);

		if(hasData && stack.get(DataComponents.BLOCK_ENTITY_DATA).getUnsafe().contains(AbstractTransporterEntity.ID))
			id = stack.get(DataComponents.BLOCK_ENTITY_DATA).getUnsafe().getString(AbstractTransporterEntity.ID);
		
        tooltipComponents.add(Component.literal("ID: " + id).withStyle(ChatFormatting.AQUA));
		
        if(hasData && stack.get(DataComponents.BLOCK_ENTITY_DATA).getUnsafe().contains(AbstractTransporterEntity.GENERATION_STEP, CompoundTag.TAG_BYTE)
				&& StructureGenEntity.Step.SETUP == StructureGenEntity.Step.fromByte(stack.get(DataComponents.BLOCK_ENTITY_DATA).getUnsafe().getCompound("BlockEntityTag").getByte(AbstractTransporterEntity.GENERATION_STEP)))
            tooltipComponents.add(Component.translatable("tooltip.sgjourney.generates_inside_structure").withStyle(ChatFormatting.YELLOW));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
	
	public static ItemStack excludeFromNetwork(ItemStack stack, BlockEntityType<?> blockEntityType)
	{
        CompoundTag compoundtag = new CompoundTag();
		compoundtag.putByte(AbstractStargateEntity.GENERATION_STEP, StructureGenEntity.Step.SETUP.byteValue());
		BlockEntity.addEntityType(compoundtag, blockEntityType);

		stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundtag));
		
		return stack;
	}
}
