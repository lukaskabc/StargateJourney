package net.povstalec.sgjourney.common.items.crystals;

import java.util.List;

import net.minecraft.core.Vec3i;
import net.povstalec.sgjourney.common.misc.Conversion;
import net.povstalec.sgjourney.common.sgjourney.TransporterID;
import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.povstalec.sgjourney.common.sgjourney.Address;

public class MemoryCrystalItem extends AbstractCrystalItem
{
	public static final int DEFAULT_MEMORY_CAPACITY = 5;
	public static final int ADVANCED_MEMORY_CAPACITY = 2 * DEFAULT_MEMORY_CAPACITY;
	
	public static final int BAR_COLOR_RGB = 0x0095ff;
	
	public static final String MEMORY_LIST = "memory_list";
	
	public static final String TEXT = "text";
	public static final String ADDRESS = Address.ADDRESS;
	public static final String TRANSPORTER_ID = TransporterID.TRANSPORTER_ID;
	public static final String COORDINATES = "coords";
	
	public enum MemoryType
	{
		UNKNOWN(Component.translatable("tooltip.sgjourney.unknown").withStyle(ChatFormatting.DARK_RED)),
		TEXT(Component.translatable("tooltip.sgjourney.text").withStyle(ChatFormatting.GRAY)),
		ADDRESS(Component.translatable("tooltip.sgjourney.address").withStyle(ChatFormatting.AQUA)),
		TRANSPORTER_ID(Component.translatable("tooltip.sgjourney.transporter_id").withStyle(ChatFormatting.DARK_AQUA)),
		COORDINATES(Component.translatable("tooltip.sgjourney.coordinates").withStyle(ChatFormatting.BLUE));
		
		private final Component component;
		
		MemoryType(Component component)
		{
			this.component = component;
		}
		
		public Component getComponent()
		{
			return this.component;
		}
	}

	public MemoryCrystalItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		return getMemoryListSize(stack) > 0;
	}
	
	@Override
	public int getBarWidth(ItemStack stack)
	{
		return (int) Math.floor(13.0F * (float) getMemoryListSize(stack) / getMemoryCapacity());
	}
	
	@Override
	public int getBarColor(ItemStack stack)
	{
		return BAR_COLOR_RGB;
	}

	public int getMemoryCapacity()
	{
		return DEFAULT_MEMORY_CAPACITY;
	}

	/*@Override
	public Optional<Component> descriptionInDHD()
	{
		return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_dhd.memory.basic").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
	}

	@Override
	public Optional<Component> descriptionInRing()
	{
		return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_ring.memory.basic").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
	}*/

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced)
	{
		ListTag list = getMemoryList(stack);
		
		tooltipComponents.add(Component.translatable("tooltip.sgjourney.memory_capacity").append(Component.literal(": " + list.size() + '/' + getMemoryCapacity())).withStyle(ChatFormatting.BLUE));
		
		for(int i = 0; i < list.size(); i++)
		{
			tooltipComponents.add(Component.literal("[" + i + "] ").append(memoryTypeComponentAt(list, i)));
		}

		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
	}
	
	public MemoryType memoryTypeAt(ListTag list, int index)
	{
		if(list.getCompound(index).contains(ADDRESS, Tag.TAG_INT_ARRAY))
			return MemoryType.ADDRESS;
		else if(list.getCompound(index).contains(COORDINATES, Tag.TAG_INT_ARRAY))
			return MemoryType.COORDINATES;
		else if(list.getCompound(index).contains(TRANSPORTER_ID, Tag.TAG_INT_ARRAY))
			return MemoryType.TRANSPORTER_ID;
		else if(list.getCompound(index).contains(TEXT, Tag.TAG_STRING))
			return MemoryType.TEXT;
		else
			return MemoryType.UNKNOWN;
	}
	
	public Component memoryTypeComponentAt(ListTag list, int index)
	{
		return memoryTypeAt(list, index).getComponent();
	}
	
	public String memoryStringAt(ListTag list, int index)
	{
		MemoryType memoryType = memoryTypeAt(list, index);
		
		return switch(memoryType)
		{
			case TEXT -> getText(list, index);
			case ADDRESS -> getAddress(list, index).toString();
			case COORDINATES -> getCoords(list, index).toString();
			case TRANSPORTER_ID -> getTransporterID(list, index).toString();
			default -> "-";
		};
	}
	
	//============================================================================================
	//*************************************Saving and Loading*************************************
	//============================================================================================
	
	private boolean saveMemory(ItemStack stack, CompoundTag memory, boolean overrideOldMemory)
	{
		ListTag list = getMemoryList(stack);
		
		if(list.size() < getMemoryCapacity())
		{
			ListTag newList = new ListTag();
			newList.add(memory);
			newList.addAll(list);
			setMemoryList(stack, newList);
			return true;
		}
		
		if(!overrideOldMemory)
			return false;
		
		ListTag newList = new ListTag();
		newList.add(memory);
		newList.addAll(list);
		newList.remove(newList.size() - 1);
		setMemoryList(stack, newList);
		
		return true;
	}
	
	public boolean saveText(ItemStack stack, String text, boolean overrideOldMemory) // TODO Save formatted text
	{
		CompoundTag addressTag = new CompoundTag();
		addressTag.putString(TEXT, text);
		
		return saveMemory(stack, addressTag, overrideOldMemory);
	}
	
	public boolean saveAddress(ItemStack stack, Address address, boolean overrideOldMemory)
	{
		CompoundTag addressTag = new CompoundTag();
		addressTag.putIntArray(ADDRESS, address.toArray());
		
		return saveMemory(stack, addressTag, overrideOldMemory);
	}
	
	public boolean saveCoords(ItemStack stack, Vec3i coords, boolean overrideOldMemory)
	{
		CompoundTag coordsTag = new CompoundTag();
		coordsTag.putIntArray(COORDINATES, Conversion.vecToIntArray(coords));
		
		return saveMemory(stack, coordsTag, overrideOldMemory);
	}
	
	public boolean saveTransporterID(ItemStack stack, TransporterID transporterID, boolean overrideOldMemory)
	{
		CompoundTag uuidTag = new CompoundTag();
		uuidTag.putIntArray(TRANSPORTER_ID, transporterID.toArray());
		
		return saveMemory(stack, uuidTag, overrideOldMemory);
	}
	
	@Nullable
	public static String getText(ListTag list, int index)
	{
		if(list.getCompound(index).contains(TEXT, Tag.TAG_STRING))
			return list.getCompound(index).getString(TEXT);
		
		return null;
	}
	
	@Nullable
	public static String getFirstText(ListTag list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			String text = getText(list, i);
			if(text != null)
				return text;
		}
		
		return null;
	}
	
	@Nullable
	public static String getFirstText(ItemStack stack)
	{
		return getFirstText(getMemoryList(stack));
	}
	
	@Nullable
	public static Address.Immutable getAddress(ListTag list, int index)
	{
		if(list.getCompound(index).contains(ADDRESS, Tag.TAG_INT_ARRAY))
			return new Address.Immutable(list.getCompound(index).getIntArray(ADDRESS));
		
		return null;
	}
	
	@Nullable
	public static Address.Immutable getFirstAddress(ListTag list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			Address.Immutable address = getAddress(list, i);
			if(address != null)
				return address;
		}
		
		return null;
	}
	
	@Nullable
	public static Address.Immutable getFirstAddress(ItemStack stack)
	{
		return getFirstAddress(getMemoryList(stack));
	}
	
	@Nullable
	public static Vec3i getCoords(ListTag list, int index)
	{
		if(list.getCompound(index).contains(COORDINATES, Tag.TAG_INT_ARRAY))
			return Conversion.intArrayToVec(list.getCompound(index).getIntArray(COORDINATES));
		
		return null;
	}
	
	@Nullable
	public static Vec3i getFirstCoords(ListTag list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			Vec3i coords = getCoords(list, i);
			if(coords != null)
				return coords;
		}
		
		return null;
	}
	
	@Nullable
	public static Vec3i getFirstCoords(ItemStack stack)
	{
		return getFirstCoords(getMemoryList(stack));
	}
	
	@Nullable
	public static TransporterID.Immutable getTransporterID(ListTag list, int index)
	{
		if(list.getCompound(index).contains(TRANSPORTER_ID, Tag.TAG_INT_ARRAY))
			return new TransporterID.Immutable(list.getCompound(index).getIntArray(TRANSPORTER_ID));
		
		return null;
	}
	
	@Nullable
	public static TransporterID.Immutable getFirstTransporterID(ListTag list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			TransporterID.Immutable transporterID = getTransporterID(list, i);
			if(transporterID != null)
				return transporterID;
		}
		
		return null;
	}
	
	@Nullable
	public static TransporterID.Immutable getFirstTransporterID(ItemStack stack)
	{
		return getFirstTransporterID(getMemoryList(stack));
	}
	
	public static int getMemoryListSize(ItemStack stack)
	{
		if(stack.getItem() instanceof MemoryCrystalItem)
		{
			CompoundTag tag = stack.getTag();
			if(tag != null && tag.contains(MEMORY_LIST, Tag.TAG_LIST))
				return tag.getList(MEMORY_LIST, Tag.TAG_COMPOUND).size();
		}
		
		return 0;
	}
	
	public static ListTag getMemoryList(ItemStack stack)
	{
		if(stack.getItem() instanceof MemoryCrystalItem)
		{
			CompoundTag tag = stack.getTag();
			if(tag != null && tag.contains(MEMORY_LIST, Tag.TAG_LIST))
				return tag.getList(MEMORY_LIST, Tag.TAG_COMPOUND);
		}
		
		return new ListTag();
	}
	
	private static void setMemoryList(ItemStack stack, ListTag list)
	{
		if(stack.getItem() instanceof MemoryCrystalItem && list != null)
		{
			CompoundTag tag = new CompoundTag();
			tag.put(MEMORY_LIST, list);
			stack.setTag(tag);
		}
	}
	
	

	public static class Advanced extends MemoryCrystalItem
	{
		public Advanced(Properties properties)
		{
			super(properties);
		}

		@Override
		public int getMemoryCapacity()
		{
			return ADVANCED_MEMORY_CAPACITY;
		}

		/*@Override
		public Optional<Component> descriptionInDHD()
		{
			return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_dhd.memory.advanced").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
		}

		@Override
		public Optional<Component> descriptionInRing()
		{
			return Optional.of(Component.translatable("tooltip.sgjourney.crystal.in_ring.memory.advanced").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
		}*/

		@Override
		public boolean isAdvanced()
		{
			return true;
		}
	}
}
