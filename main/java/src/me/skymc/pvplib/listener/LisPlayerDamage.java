package me.skymc.pvplib.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.skymc.pvplib.PVPLib;
import me.skymc.pvplib.manager.PVPManager;
import me.skymc.pvplib.utils.ActionUtils;
import me.skymc.pvplib.utils.ColorUtils;

public class LisPlayerDamage implements Listener {
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void damage(EntityDamageByEntityEvent e) {
		
		if (!PVPLib.getInst().getConfig().getBoolean("CancelAttack") || e.isCancelled()) {
			return;
		}
		
		// 受伤者是玩家
		if (e.getEntity() instanceof Player) {
			Player attacker = null;
			
			if (e.getDamager() instanceof Player) {
				attacker = (Player) e.getDamager();
			}
			else if (e.getDamager() instanceof Projectile) {
				Projectile proj = (Projectile) e.getDamager();
				
				if (proj.getShooter() instanceof Player) {
					attacker = (Player) proj.getShooter();
				}
			}
			
			// 攻击者时玩家
			if (attacker != null) {
				
				// 是否可以攻击
				if (!PVPManager.canDamage(attacker, (Player) e.getEntity())) {
					e.setCancelled(true);
					
					if (!PVPLib.getInst().getConfig().getString("CancelMessage").equals("")) {
						ActionUtils.send(attacker, ColorUtils.color(PVPLib.getInst().getConfig().getString("CancelMessage")));
					}
				}
			}
		}
	}

}
