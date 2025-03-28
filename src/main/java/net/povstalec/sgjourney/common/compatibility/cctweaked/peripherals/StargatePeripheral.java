package net.povstalec.sgjourney.common.compatibility.cctweaked.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.block_entities.tech.AbstractInterfaceEntity;
import net.povstalec.sgjourney.common.compatibility.cctweaked.StargatePeripheralWrapper;
import net.povstalec.sgjourney.common.compatibility.computer_functions.GenericStargateFunctions;

public class StargatePeripheral extends InterfacePeripheral
{
	protected AbstractStargateEntity stargate;
	
	public StargatePeripheral(AbstractInterfaceEntity interfaceEntity, AbstractStargateEntity stargate)
	{
		super(interfaceEntity);
		this.stargate = stargate;
		
		stargate.registerInterfaceMethods(new StargatePeripheralWrapper(this, interfaceEntity.getInterfaceType()));
	}

	@Override
	public MethodResult callMethod(IComputerAccess computer, ILuaContext context, int method, IArguments arguments)
			throws LuaException
	{
		String methodName = getMethodNames()[method];
		
		return methods.get(methodName).use(computer, context, this.interfaceEntity, this.stargate, arguments);
	}
	
	//============================================================================================
	//*************************************CC: Tweaked Events*************************************
	//============================================================================================
	
	public void queueEvent(String eventName, Object... objects)
	{
		for(IComputerAccess computer : interfaceEntity.getPeripheralWrapper().computerList)
		{
			int length = objects.length + 1;
			Object[] attachmentObjects = new Object[length];
			
			attachmentObjects[0] = computer.getAttachmentName();
			
			for(int i = 1; i < length; i++)
			{
				attachmentObjects[i] = objects[i - 1];
			}
			
			computer.queueEvent(eventName, attachmentObjects);
		}
	}
	
	//============================================================================================
	//*****************************************CC: Tweaked****************************************
	//============================================================================================
	
	@LuaFunction
	public final int getStargateGeneration()
	{
		return GenericStargateFunctions.getStargateGeneration(stargate);
	}
	
	@LuaFunction
	public final String getStargateType()
	{
		return GenericStargateFunctions.getStargateType(stargate);
	}
	
	@LuaFunction
	public final boolean isStargateConnected()
	{
		return GenericStargateFunctions.isStargateConnected(stargate);
	}
	
	@LuaFunction
	public final boolean isStargateDialingOut()
	{
		return GenericStargateFunctions.isStargateDialingOut(stargate);
	}
	
	@LuaFunction
	public final boolean isWormholeOpen()
	{
		return GenericStargateFunctions.isWormholeOpen(stargate);
	}
	
	@LuaFunction
	public final long getStargateEnergy()
	{
		return GenericStargateFunctions.getStargateEnergy(stargate);
	}
	
	@LuaFunction
	public final int getChevronsEngaged()
	{
		return GenericStargateFunctions.getChevronsEngaged(stargate);
	}
	
	@LuaFunction
	public final int getOpenTime()
	{
		return GenericStargateFunctions.getOpenTime(stargate);
	}
	
	@LuaFunction
	public final MethodResult disconnectStargate(ILuaContext context) throws LuaException
	{
		return context.executeMainThreadTask(() -> new Object[] {GenericStargateFunctions.disconnectStargate(stargate)});
	}
}
