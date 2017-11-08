package me.skymc.pvplib.cooldown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import me.skymc.pvplib.PVPLib;

public class CooldownUtils implements Listener {
	
	private static ConcurrentHashMap<String, CooldownPack> packlist = new ConcurrentHashMap<>();

	public static void register(CooldownPack pack) {
		packlist.put(pack.getPackName(), pack);
		PVPLib.getInst().getLogger().info("ע����ȴ��: " + pack.getPackName() + ", ʱ��: " + pack.getPackSeconds() + " �� (����ע��)");
	}
	
	public static void register(CooldownPack pack, Plugin plugin) {
		pack.setPlugin(plugin.getName());
		
		packlist.put(pack.getPackName(), pack);
		PVPLib.getInst().getLogger().info("ע����ȴ��: " + pack.getPackName() + ", ʱ��: " + pack.getPackSeconds() + " �� (" + plugin.getName() + ")");
	}
	
	public static void unregister(String name) {
		packlist.remove(name);
		
		PVPLib.getInst().getLogger().info("ע����ȴ��: " + name + " (����ע��)");
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		for (CooldownPack pack : packlist.values()) {
			if (!pack.isCooldown(e.getPlayer().getName(), 0)) {
				pack.unRegister(e.getPlayer().getName());
			}
		}
	}
	
	@EventHandler
	public void disable(PluginDisableEvent e) {
		for (CooldownPack pack : packlist.values()) {
			if (pack.getPlugin().equals(e.getPlugin().getName())) {
				packlist.remove(pack.getPackName());
				
				PVPLib.getInst().getLogger().info("ע����ȴ��: " + pack.getPackName() + " (�Զ�ע��)");
			}
		}
	}
}
