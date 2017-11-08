package me.skymc.pvplib.type;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.skymc.pvplib.events.TargetAddEvent;
import me.skymc.pvplib.events.TargetRemoveEvent;

public class PVPType {
	
	/**
	 * 一对一对决状态
	 * 
	 * 比如玩家 A 设定 B 为对决目标，
	 * 那么 A 只能对 B 造成伤害
	 * 但是 B 却可以直接攻击 A
	 */
	
	// PVP_PRIVATE("PVP_PRIVATE", "", null),
	
	/**
	 * 公开对决状态
	 * 
	 * 只要双方都处于 公开对决 状态均可互相造成伤害
	 */
	
	// PVP_PUBLIC("PVP_PUBLIC", "", null),
	
	/**
	 * 保护状态
	 * 
	 * 无法收到来自任何玩家的伤害
	 */
	
	// PROTECT("PROTECT", "", null);
	
	
	/**
	 * 为对决列表移除玩家
	 * 
	 * @param player 玩家
	 */
	public void removeTarget(Player player) {
		
		// 判断是否为对决模式
		if (name.equals("PVP_PRIVATE")) {
			
			// 判断是否存在
			if (getTargets().contains(player.getName())) {
				
				TargetRemoveEvent event = new TargetRemoveEvent(Bukkit.getPlayerExact(this.player), player);
				Bukkit.getPluginManager().callEvent(event);
				
				// 如果事件被取消
				if (event.cancel()) {
					return;
				}
				
				value = value.replace(player.getName() + "//", "");
			}
		}
	}
	
	/**
	 * 为对决列表增加玩家
	 * 
	 * @param player 玩家
	 */
	public void addTarget(Player player) {
		
		// 判断是否为对决模式
		if (name.equals("PVP_PRIVATE")) {
			
			// 判断是否重复
			if (!getTargets().contains(player.getName())) {
				
				TargetAddEvent event = new TargetAddEvent(Bukkit.getPlayerExact(this.player), player);
				Bukkit.getPluginManager().callEvent(event);
				
				// 如果事件被取消
				if (event.cancel()) {
					return;
				}
				
				value = value += player.getName() + "//";
			}
		}
	}
	
	/**
	 * 获取对决列表
	 * 
	 * @return 对决玩家 
	 */
	public List<String> getTargets() {
		
		// 如果不是对决模式或对决列表为空
		if (!name.equals("PVP_PRIVATE") && value.equals("")) {
			return new ArrayList<>();
		}
		
		List<String> list = new ArrayList<>();
		for (String name : value.split("//")) {
			list.add(name);
		}
		
		return list;
	}
	
	private String name = null;
	private String value = null;
	private String player = null;
	
	public PVPType(String name, String value, Player player) {
		this.name = name;
		this.value = value;
		this.player = player.getName();
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getPlayer() {
		return this.player;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
