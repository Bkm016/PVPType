package me.skymc.pvplib.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.skymc.pvplib.type.PVPType;

public class PVPTypeChanceEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private String newtype;
	private String oldtype;
	
	private boolean cancel;
  
	public PVPTypeChanceEvent(Player player, String n, String o) {
		this.player = player;
		this.cancel = false;
		
		this.newtype = n;
		this.oldtype = o;
	}
  
	public Player getPlayer() {
		return this.player;
	}
	
	public String getOldType() {
		return this.oldtype;
	}
	
	public String getNewType() {
		return this.newtype;
	}
	
	public boolean cancel() {
		return this.cancel;
	}
	
	public void cancel(boolean b) {
		this.cancel = b;
	}
  
	public HandlerList getHandlers() {
		return handlers;
	}
  
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
