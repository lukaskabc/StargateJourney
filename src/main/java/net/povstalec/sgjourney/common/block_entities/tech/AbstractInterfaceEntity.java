package net.povstalec.sgjourney.common.block_entities.tech;

import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import net.povstalec.sgjourney.common.block_entities.stargate.IrisStargateEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.RotatingStargateEntity;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.block_entities.EnergyBlockEntity;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.blocks.stargate.AbstractStargateRingBlock;
import net.povstalec.sgjourney.common.blocks.tech.AbstractInterfaceBlock;
import net.povstalec.sgjourney.common.blocks.tech.BasicInterfaceBlock;
import net.povstalec.sgjourney.common.blockstates.InterfaceMode;
import net.povstalec.sgjourney.common.blockstates.ShieldingState;
import net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals.InterfacePeripheralWrapper;
import net.povstalec.sgjourney.common.config.CommonInterfaceConfig;
import net.povstalec.sgjourney.common.packets.ClientboundInterfaceUpdatePacket;
import net.povstalec.sgjourney.common.sgjourney.StargateInfo;

public abstract class AbstractInterfaceEntity extends EnergyBlockEntity
{
	public static final String ENERGY_TARGET = "energy_target";

	public int signalStrength = 0;
	
	private int desiredSymbol = 0;
	private int currentSymbol = 0;
	private boolean rotate = false;
	private boolean rotateClockwise = true;
	
	private StargateInfo.IrisMotion irisMotion = StargateInfo.IrisMotion.IDLE;
	
	private long energyTarget = CommonInterfaceConfig.default_energy_target.get();
	
	public EnergyBlockEntity energyBlockEntity = null;
	protected InterfacePeripheralWrapper peripheralWrapper;
	
	public enum InterfaceType
	{
		BASIC("basic_interface"),
		CRYSTAL("crystal_interface"),
		ADVANCED_CRYSTAL("advanced_crystal_interface");
		
		private String typeName;
		
		InterfaceType(String typeName)
		{
			this.typeName = typeName;
		}
		
		public String getName()
		{
			return this.typeName;
		}
		
		public boolean hasCrystalMethods()
		{
			return this == CRYSTAL || this == ADVANCED_CRYSTAL;
		}
		
		public boolean hasAdvancedCrystalMethods()
		{
			return this == ADVANCED_CRYSTAL;
		}
	}
	
	protected InterfaceType interfaceType;
	
	public AbstractInterfaceEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, InterfaceType interfaceType)
	{
		super(type, pos, state);
		
		this.interfaceType = interfaceType;
		
		if(ModList.get().isLoaded(StargateJourney.COMPUTERCRAFT_MODID))
			peripheralWrapper = new InterfacePeripheralWrapper(this);
	}
	
	@Override
	public void onLoad()
	{
		Level level = this.getLevel();
		BlockPos pos = this.getBlockPos();
		BlockState state = this.getLevel().getBlockState(pos);
		if(level.getBlockState(pos).getBlock() instanceof AbstractInterfaceBlock ccInterface)
			ccInterface.updateInterface(state, level, pos);
		
		super.onLoad();
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);
		energyTarget = tag.getLong(ENERGY_TARGET);
	}
	
	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.putLong(ENERGY_TARGET, energyTarget);
		super.saveAdditional(tag, registries);
	}
	
	//============================================================================================
	//****************************************Capabilities****************************************
	//============================================================================================
	
	public boolean updateInterface(Level level, BlockPos pos, Block block, BlockState state)
	{
		if(peripheralWrapper != null)
			return peripheralWrapper.resetInterface();
		
		if(level.getBlockState(pos).getBlock() instanceof AbstractInterfaceBlock ccInterface)
			ccInterface.updateInterface(state, level, pos);
		
		return true;
	}
	
	public Direction getDirection()
	{
		BlockPos gatePos = this.getBlockPos();
		BlockState gateState = this.level.getBlockState(gatePos);
		
		if(gateState.getBlock() instanceof AbstractInterfaceBlock)
			return gateState.getValue(AbstractInterfaceBlock.FACING);

		StargateJourney.LOGGER.error("Couldn't find Direction " + this.getBlockPos().toString());
		return null;
	}

	@Nullable
	public EnergyBlockEntity findEnergyBlockEntity()
	{
		Direction direction = getDirection();
		if(direction == null)
			return null;

		BlockPos realPos = getBlockPos().relative(direction);
		BlockState state = level.getBlockState(realPos);

		if(level.getBlockState(realPos).getBlock() instanceof AbstractStargateRingBlock)
			realPos = state.getValue(AbstractStargateRingBlock.PART)
					.getBaseBlockPos(realPos, state.getValue(AbstractStargateRingBlock.FACING), state.getValue(AbstractStargateRingBlock.ORIENTATION));

		return level.getBlockEntity(realPos) instanceof EnergyBlockEntity energyBlockEntity ? energyBlockEntity : null;
	}
	
	public InterfaceType getInterfaceType()
	{
		return this.interfaceType;
	}
	
	//============================================================================================
	//*******************************************Energy*******************************************
	//============================================================================================
	
	@Override
	public boolean isCorrectEnergySide(Direction side)
	{
		if(side == getDirection())
			return false;
		return true;
	}

	@Override
	protected boolean outputsEnergy()
	{
		return true;
	}
	
	@Override
	protected boolean receivesEnergy()
	{
		return true;
	}
	
	@Override
	protected void outputEnergy(Direction outputDirection)
	{
		if(energyBlockEntity.getEnergyStored() >= energyTarget)
			return;
		
		long simulatedOutputAmount = getEnergyStorage().extractLongEnergy(this.maxExtract(), true);
		long simulatedReceiveAmount = energyBlockEntity.getEnergyStorage().receiveLongEnergy(simulatedOutputAmount, true);
		getEnergyStorage().extractLongEnergy(simulatedReceiveAmount, false);
		energyBlockEntity.getEnergyStorage().receiveLongEnergy(simulatedReceiveAmount, false);
	}
	
	public long getEnergyTarget()
	{
		return this.energyTarget;
	}
	
	public void setEnergyTarget(long energyTarget)
	{
		this.energyTarget = energyTarget;
	}
	
	//============================================================================================
	//*****************************************CC: Tweaked****************************************
	//============================================================================================
	
	public InterfacePeripheralWrapper getPeripheralWrapper()
	{
		if(!ModList.get().isLoaded(StargateJourney.COMPUTERCRAFT_MODID))
			return null;
		
		return this.peripheralWrapper;
	}
	
	public void queueEvent(String eventName, Object... objects)
	{
		if(!ModList.get().isLoaded(StargateJourney.COMPUTERCRAFT_MODID))
			return;
		
		if(this.peripheralWrapper != null)
			this.peripheralWrapper.queueEvent(eventName, objects);
	}
	
	//============================================================================================
	//******************************************Ticking*******************************************
	//============================================================================================
	
	public static void tick(Level level, BlockPos pos, BlockState state, AbstractInterfaceEntity interfaceEntity)
	{
		interfaceEntity.energyBlockEntity = interfaceEntity.findEnergyBlockEntity();
		
		if(interfaceEntity.energyBlockEntity != null)
		{
			int lastSymbol = interfaceEntity.currentSymbol;
			interfaceEntity.outputEnergy(interfaceEntity.getDirection());
			
			if(interfaceEntity.energyBlockEntity instanceof AbstractStargateEntity stargate)
			{
				interfaceEntity.handleShielding(state, stargate);
				
				if(stargate instanceof RotatingStargateEntity rotatingStargate)
					interfaceEntity.rotateStargate(rotatingStargate);
			}

			if(lastSymbol != interfaceEntity.currentSymbol)
			{
				if(!level.isClientSide())
				{
					setChanged(level, pos, state);
					level.updateNeighborsAtExceptFromFacing(pos, state.getBlock(), state.getValue(AbstractInterfaceBlock.FACING));
				}
			}
		}
		
		if(level.isClientSide())
			return;
		PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(interfaceEntity.worldPosition).getPos(),
				new ClientboundInterfaceUpdatePacket(interfaceEntity.worldPosition, interfaceEntity.getEnergyStored()));
			
	}
	
	private void rotateStargate(RotatingStargateEntity stargate)
	{
		if(this.rotate)
		{
			if(stargate.isCurrentSymbol(this.desiredSymbol))
				this.rotate = false;
			else
				stargate.rotate(rotateClockwise);
		}

		this.currentSymbol = stargate.getCurrentSymbol();
	}
	
	protected void handleShielding(BlockState state, AbstractStargateEntity stargate)
	{
		handleRedstone(state, stargate);
		
		if(stargate instanceof IrisStargateEntity irisStargate)
			handleIris(irisStargate);
	}
	
	private boolean belowMaxProgress(IrisStargateEntity stargate)
	{
		return stargate.irisInfo().getIrisProgress() < ShieldingState.MAX_PROGRESS;
	}
	
	private boolean aboveMinProgress(IrisStargateEntity stargate)
	{
		return stargate.irisInfo().getIrisProgress() > 0;
	}
	
	protected void handleRedstone(BlockState state, AbstractStargateEntity stargate)
	{
		InterfaceMode mode = state.getValue(BasicInterfaceBlock.MODE);
		
		if(mode != InterfaceMode.IRIS || !irisMotion.isRedstone())
			return;
		
		if(stargate instanceof IrisStargateEntity irisStargate)
		{
			if(signalStrength == 0 && irisMotion != StargateInfo.IrisMotion.IDLE)
				setIrisMotion(StargateInfo.IrisMotion.IDLE);
			else if(signalStrength > 0 && signalStrength <= 7 && irisMotion != StargateInfo.IrisMotion.CLOSING_REDSTONE && belowMaxProgress(irisStargate))
				setIrisMotion(StargateInfo.IrisMotion.CLOSING_REDSTONE);
			else if(signalStrength >= 8 && signalStrength <= 15 && irisMotion != StargateInfo.IrisMotion.OPENING_REDSTONE && aboveMinProgress(irisStargate))
				setIrisMotion(StargateInfo.IrisMotion.OPENING_REDSTONE);
		}
	}
	
	protected void handleIris(IrisStargateEntity stargate)
	{
		if(irisMotion.isClosing())
		{
			if(belowMaxProgress(stargate))
				stargate.irisInfo().increaseIrisProgress();
			else
				irisMotion = StargateInfo.IrisMotion.IDLE;
		}
		else if(irisMotion.isOpening())
		{
			if(aboveMinProgress(stargate))
				stargate.irisInfo().decreaseIrisProgress();
			else
				irisMotion = StargateInfo.IrisMotion.IDLE;
		}
	}
	
	public boolean setIrisMotion(StargateInfo.IrisMotion irisMotion)
	{
		if(this.irisMotion == irisMotion)
			return false;
		
		this.irisMotion = irisMotion;
		return true;
	}
}
