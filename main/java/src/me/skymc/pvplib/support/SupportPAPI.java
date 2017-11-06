package me.skymc.pvplib.support;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.skymc.pvplib.PVPLib;
import me.skymc.pvplib.manager.PVPManager;
import me.skymc.pvplib.type.PVPType;
import me.skymc.pvplib.utils.ColorUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SupportPAPI
    extends EZPlaceholderHook
{
    public PVPLib ourPlugin;
    
    public static void hook(PVPLib plugin) {
    	
    	new BukkitRunnable() {
			
			@Override
			public void run() {
				new SupportPAPI(plugin).hook();
				
			}
		}.runTask(plugin);
    }
       
    public SupportPAPI(PVPLib plugin) 
    {
    	super(plugin, "pvplib");
	    ourPlugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player p, String s) {
        if (p == null) {
            return "-";
        }
        
        if (s.equals("name")) {
        	PVPType type = PVPManager.getPlayerPVPType(p);
        	
        	return ColorUtils.color(PVPLib.getInst().getConfig().getString("PlaceHolder." + type.getName()));
        }
        return null;
    }
    
}