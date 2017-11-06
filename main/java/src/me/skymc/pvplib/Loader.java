package me.skymc.pvplib;

import org.bukkit.Bukkit;

import me.skymc.pvplib.command.MainCommand;
import me.skymc.pvplib.listener.LisPlayerDamage;
import me.skymc.pvplib.listener.LisPlayerDeath;

public class Loader{
	
	public static void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new LisPlayerDamage(), PVPLib.getInst());
		Bukkit.getPluginManager().registerEvents(new LisPlayerDeath(), PVPLib.getInst());
	}
	
	public static void registerCommnad() {
		PVPLib.getInst().getServer().getPluginCommand("pvplib").setExecutor(new MainCommand());
	}
	
	public static void loadConfig() {
		PVPLib.getInst().saveDefaultConfig();
		PVPLib.getInst().reloadConfig();
	}

}