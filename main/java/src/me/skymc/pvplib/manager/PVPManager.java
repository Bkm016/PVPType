package me.skymc.pvplib.manager;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.skymc.pvplib.PVPLib;
import me.skymc.pvplib.command.MainCommand;
import me.skymc.pvplib.events.PVPTypeChanceEvent;
import me.skymc.pvplib.events.TargetLoseEvent;
import me.skymc.pvplib.type.PVPType;

public class PVPManager implements Listener {
	
	private static ConcurrentHashMap<String, PVPType> data = new ConcurrentHashMap<>();
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		data.put(e.getPlayer().getName(), getDefaultType(e.getPlayer()));
		
		if (PVPLib.getInst().getConfig().getInt("JoinPVPLater") != 0) {
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					setPlayerPVPType(e.getPlayer(), "PVP_PUBLIC", true);
					MainCommand.toggleMessage(e.getPlayer());
				}
			}.runTaskLater(PVPLib.getInst(), PVPLib.getInst().getConfig().getInt("JoinPVPLater") * 20);
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		
		// 从数据库中移除该玩家的战斗模式
		data.remove(e.getPlayer().getName());
		
		for (PVPType type : data.values()) {
			
			// 判断当前服务器是否有人以该玩家为对决目标
			if (type.getValue().contains(e.getPlayer().getName())) {
				
				// 触发监听器
				TargetLoseEvent event = new TargetLoseEvent(Bukkit.getPlayerExact(type.getPlayer()), e.getPlayer());
				Bukkit.getPluginManager().callEvent(event);
				
				if (event.remove()) {
					
					// 从目标中移除该玩家
					type.removeTarget(e.getPlayer());
				}
			}
		}
	}
	
	/**
	 * 格式化战斗模式名称，错误的名称将会被改为和平模式（PROTECT）
	 * 
	 * @param name
	 * @return
	 */
	public static String formatTypeName(String name) {
		if (name.equals("PVP_PRIVATE")) {
			return name;
		}
		if (name.equals("PVP_PUBLIC")) {
			return name;
		}
		return "PROTECT";
	}
	
	/**
	 * 获取玩家的战斗模式，如果没有数据则返回默认战斗模式
	 * 
	 * @param player
	 * @return
	 */
	public static PVPType getPlayerPVPType(Player player) {
		if (data.containsKey(player.getName())) {
			return data.get(player.getName());
		}
		// 返回默认模式
		return getDefaultType(player);
	}
	
	/**
	 * 设置玩家的战斗模式（简易版）
	 * 
	 * @param player 玩家
	 * @param name 模式
	 * @param clearTarget 是否清理对决目标
	 */
	public static void setPlayerPVPType(Player player, String name, boolean clearTarget) {
		PVPType type = getPlayerPVPType(player);
		
		// 格式化名称
		name = formatTypeName(name);
		
		// 触发监听器
		PVPTypeChanceEvent event = new PVPTypeChanceEvent(player, name, type.getName());
		Bukkit.getPluginManager().callEvent(event);
		
		// 如果事件没有被取消
		if (!event.cancel()) {
			type.setName(name);
			
			// 是否清理对决目标
			if (clearTarget) {
				type.setValue("");
			}
			data.put(player.getName(), type);
		}
	}
	
	/**
	 * 获取默认战斗模式（和平模式）
	 * 
	 * @param player
	 * @return
	 */
	public static PVPType getDefaultType(Player player) {
		return new PVPType("PROTECT", "", player);
	}
	
	/**
	 * 重新载入全服所有玩家的战斗模式，并设置为和平模式
	 * 
	 */
	public static void reloadPVPType() {
		data.clear();
		
		// 重新载入所有玩家的 PVP 状态
		for (Player player : Bukkit.getOnlinePlayers()) {
			data.put(player.getName(), getDefaultType(player));
		}
	}
	
	/**
	 * 玩家 A 是否可以攻击 玩家 B
	 * 
	 * @param playerA
	 * @param playerB
	 * @return 是/否
	 */
	public static boolean canDamage(Player playerA, Player playerB) {
		
		PVPType typeA = getPlayerPVPType(playerA);
		PVPType typeB = getPlayerPVPType(playerB);
		
		// 判断玩家 A 是否处于对决模式
		if (typeA.getName().equals("PVP_PRIVATE")) {
			
			// 玩家 B 是否为玩家 A 的对决目标
			if (typeA.getValue().contains(playerB.getName())) {
				return true;
			}
			return false;
		}
		
		// 判断玩家 B 是否处于对决模式
		if (typeB.getName().equals("PVP_PRIVATE")) {
					
			// 玩家 A 是否为玩家 B 的对决目标
			if (typeB.getValue().contains(playerA.getName())) {
				return true;
			}
			return false;
		}
		
		// 是否双方都处于公开状态
		if (typeA.getName().equals("PVP_PUBLIC") && typeB.getName().equals("PVP_PUBLIC")) {
			return true;
		}
		
		return false;
	}
}
