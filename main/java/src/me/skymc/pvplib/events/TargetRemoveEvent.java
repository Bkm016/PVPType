package me.skymc.pvplib.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.skymc.pvplib.type.PVPType;

public class TargetRemoveEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private Player target;
	
	private boolean cancel;
  
	public TargetRemoveEvent(Player player, Player target) {
		this.player = player;
		this.target = target;
		this.cancel = false;
	}
  
	public Player getPlayer() {
		return this.player;
	}
	
	public Player getTarget() {
		return this.target;
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