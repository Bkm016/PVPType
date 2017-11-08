package me.skymc.pvplib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.skymc.pvplib.PVPLib;
import me.skymc.pvplib.command.MainCommand;
import me.skymc.pvplib.events.TargetKilledEvent;
import me.skymc.pvplib.manager.PVPManager;
import me.skymc.pvplib.utils.ColorUtils;

public class LisPlayerDeath implements Listener {
	
	@EventHandler
	public void death(PlayerDeathEvent e) {
		
		if (e.getEntity().getKiller() == null) {
			return;
		}
		
		// ������ �Ƿ��� ��ɱ�� �ĶԾ�Ŀ��
		if (PVPManager.getPlayerPVPType(e.getEntity().getKiller()).getValue().contains(e.getEntity().getName())) {
			TargetKilledEvent event = new TargetKilledEvent(e.getEntity().getKiller(), e.getEntity());
			Bukkit.getPluginManager().callEvent(event);
			
			if (event.remove()) {
				PVPManager.getPlayerPVPType(e.getEntity().getKiller()).removeTarget(e.getEntity());
			}
		}
		
		// �����ɱ�����Ƿ������ƽģʽ
		if (PVPLib.getInst().getConfig().getBoolean("DeathProtect")) {
			PVPManager.setPlayerPVPType(e.getEntity(), "PROTECT", true);
			
			MainCommand.toggleMessage(e.getEntity());
		}
	}
}
