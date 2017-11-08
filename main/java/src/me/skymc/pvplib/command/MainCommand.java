package me.skymc.pvplib.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.skymc.pvplib.Loader;
import me.skymc.pvplib.PVPLib;
import me.skymc.pvplib.manager.PVPManager;
import me.skymc.pvplib.type.PVPType;
import me.skymc.pvplib.utils.ColorUtils;
import me.skymc.pvplib.utils.TitleUtils;

public class MainCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if (args.length == 0) {
			for (String msg : PVPLib.getInst().getConfig().getStringList("Command_help")) {
				sender.sendMessage(ColorUtils.color(msg));
			}
			
			return false;
		}
		
		if (args[0].equals("reload") && sender.hasPermission("pvplib.admin")) {
			Loader.loadConfig();
			sender.sendMessage("§4Reload ok");
			
			return false;
		}
		else if (args[0].equals("target") && sender.hasPermission("pvplib.admin")) {
			
			// 如果变量被省略
			if (args.length == 1) {
				
				if (sender instanceof Player) {
					PVPType type = PVPManager.getPlayerPVPType((Player) sender);
					
					sender.sendMessage("§4Your targets: §7" + type.getTargets());
					return true;
				}
			}
			else {
				Player player = Bukkit.getPlayerExact(args[1]);
				if (player == null) {
					sender.sendMessage("§4Player §c" + args[1] + "§4 is not online");
					return false;
				}
				
				PVPType type = PVPManager.getPlayerPVPType(player);
				
				sender.sendMessage("§c" + player.getName() + "§4's targets: §7" + type.getTargets());
				return true;
			}
		}
		else if (args[0].equals("toggle")) {
			
			// 如果变量被省略
			if (args.length == 1) {
				
				if (sender instanceof Player) {
					PVPType type = PVPManager.getPlayerPVPType((Player) sender);
					
					if (type.getName().equals("PVP_PUBLIC") || type.getName().equals("PVP_PRIVATE")) {
						PVPManager.setPlayerPVPType((Player) sender, "PROTECT", true);
					}
					else if (type.getName().equals("PROTECT")) {
						PVPManager.setPlayerPVPType((Player) sender, "PVP_PUBLIC", true);
					}
					
					toggleMessage((Player) sender);
					return true;
				}
				else {
					sender.sendMessage("§4This Command can not excute on CONSOLE");
					return false;
				}
			}
			else if (args.length >= 3){
				Player player = Bukkit.getPlayerExact(args[1]);
				if (player == null) {
					sender.sendMessage("§4Player §c" + args[1] + "§4 is not online");
					return false;
				}
				
				PVPManager.setPlayerPVPType(player, PVPManager.formatTypeName(args[2]), true);
				
				// 对决模式
				if (PVPManager.formatTypeName(args[2]).equals("PVP_PRIVATE") && args.length == 4) {
					for (String sub : args[3].split("//")) {
						Player target = Bukkit.getPlayerExact(sub);
						if (target != null) {
							PVPManager.getPlayerPVPType(player).addTarget(target);
						}
					}
				}
				
				// 指令使用者是否是玩家
				if (sender instanceof Player) {
					sender.sendMessage(ColorUtils.color(PlaceholderAPI.setPlaceholders(player, PVPLib.getInst().getConfig().getString("Command_Toggle_Admin"))));
				}
				
				toggleMessage(player);
				return true;
			}
			else {
				sender.sendMessage("§4Error Command!");
				return false;
			}
		}
		
		sender.sendMessage("reload ok!");
		return true;
	}
	
	public static void toggleMessage(Player player) {
		if (PVPLib.getInst().getConfig().getBoolean("Command_Toggle_Title")) {
			TitleUtils.sendTitle(player, "", 10, 40, 10, ColorUtils.color(PlaceholderAPI.setPlaceholders(player, PVPLib.getInst().getConfig().getString("Command_Toggle"))), 10, 40, 10);
		}
		else {
			player.sendMessage(ColorUtils.color(PlaceholderAPI.setPlaceholders(player, PVPLib.getInst().getConfig().getString("Command_Toggle"))));
		}
	}
}

