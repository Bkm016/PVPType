package me.skymc.pvplib;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.skymc.pvplib.support.SupportPAPI;
import me.skymc.pvplib.type.PVPType;

public class PVPLib extends JavaPlugin implements Listener
{
	private static final String prefix = "¡ìc¡ìl[¡ì4¡ìlSkyMcraft¡ìc¡ìl] ¡ì7";
	private static Plugin plugin = null;
	
	public static Plugin getInst() {
		return plugin;
	}
	
	public static String getPrefix() {
		return prefix;
	}
	
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(this, getInst());
		
		Loader.registerEvents();
		Loader.registerCommnad();
		Loader.loadConfig();
		
		SupportPAPI.hook(this);
		
	}
}
