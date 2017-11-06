package me.skymc.pvplib.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.skymc.pvplib.type.PVPType;

public class TargetLoseEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private Player target;
	
	private boolean remove;
  
	public TargetLoseEvent(Player player, Player target) {
		this.player = player;
		this.target = target;
		this.remove = true;
	}
  
	public Player getPlayer() {
		return this.player;
	}
	
	public Player getTarget() {
		return this.target;
	}
	
	public boolean remove() {
		return this.remove;
	}
	
	public void remove(boolean b) {
		this.remove = b;
	}
  
	public HandlerList getHandlers() {
		return handlers;
	}
  
	public static HandlerList getHandlerList() {
		return handlers;
	}
}