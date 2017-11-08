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
		
		// �����ݿ����Ƴ�����ҵ�ս��ģʽ
		data.remove(e.getPlayer().getName());
		
		for (PVPType type : data.values()) {
			
			// �жϵ�ǰ�������Ƿ������Ը����Ϊ�Ծ�Ŀ��
			if (type.getValue().contains(e.getPlayer().getName())) {
				
				// ����������
				TargetLoseEvent event = new TargetLoseEvent(Bukkit.getPlayerExact(type.getPlayer()), e.getPlayer());
				Bukkit.getPluginManager().callEvent(event);
				
				if (event.remove()) {
					
					// ��Ŀ�����Ƴ������
					type.removeTarget(e.getPlayer());
				}
			}
		}
	}
	
	/**
	 * ��ʽ��ս��ģʽ���ƣ���������ƽ��ᱻ��Ϊ��ƽģʽ��PROTECT��
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
	 * ��ȡ��ҵ�ս��ģʽ�����û�������򷵻�Ĭ��ս��ģʽ
	 * 
	 * @param player
	 * @return
	 */
	public static PVPType getPlayerPVPType(Player player) {
		if (data.containsKey(player.getName())) {
			return data.get(player.getName());
		}
		// ����Ĭ��ģʽ
		return getDefaultType(player);
	}
	
	/**
	 * ������ҵ�ս��ģʽ�����װ棩
	 * 
	 * @param player ���
	 * @param name ģʽ
	 * @param clearTarget �Ƿ�����Ծ�Ŀ��
	 */
	public static void setPlayerPVPType(Player player, String name, boolean clearTarget) {
		PVPType type = getPlayerPVPType(player);
		
		// ��ʽ������
		name = formatTypeName(name);
		
		// ����������
		PVPTypeChanceEvent event = new PVPTypeChanceEvent(player, name, type.getName());
		Bukkit.getPluginManager().callEvent(event);
		
		// ����¼�û�б�ȡ��
		if (!event.cancel()) {
			type.setName(name);
			
			// �Ƿ�����Ծ�Ŀ��
			if (clearTarget) {
				type.setValue("");
			}
			data.put(player.getName(), type);
		}
	}
	
	/**
	 * ��ȡĬ��ս��ģʽ����ƽģʽ��
	 * 
	 * @param player
	 * @return
	 */
	public static PVPType getDefaultType(Player player) {
		return new PVPType("PROTECT", "", player);
	}
	
	/**
	 * ��������ȫ��������ҵ�ս��ģʽ��������Ϊ��ƽģʽ
	 * 
	 */
	public static void reloadPVPType() {
		data.clear();
		
		// ��������������ҵ� PVP ״̬
		for (Player player : Bukkit.getOnlinePlayers()) {
			data.put(player.getName(), getDefaultType(player));
		}
	}
	
	/**
	 * ��� A �Ƿ���Թ��� ��� B
	 * 
	 * @param playerA
	 * @param playerB
	 * @return ��/��
	 */
	public static boolean canDamage(Player playerA, Player playerB) {
		
		PVPType typeA = getPlayerPVPType(playerA);
		PVPType typeB = getPlayerPVPType(playerB);
		
		// �ж���� A �Ƿ��ڶԾ�ģʽ
		if (typeA.getName().equals("PVP_PRIVATE")) {
			
			// ��� B �Ƿ�Ϊ��� A �ĶԾ�Ŀ��
			if (typeA.getValue().contains(playerB.getName())) {
				return true;
			}
			return false;
		}
		
		// �ж���� B �Ƿ��ڶԾ�ģʽ
		if (typeB.getName().equals("PVP_PRIVATE")) {
					
			// ��� A �Ƿ�Ϊ��� B �ĶԾ�Ŀ��
			if (typeB.getValue().contains(playerA.getName())) {
				return true;
			}
			return false;
		}
		
		// �Ƿ�˫�������ڹ���״̬
		if (typeA.getName().equals("PVP_PUBLIC") && typeB.getName().equals("PVP_PUBLIC")) {
			return true;
		}
		
		return false;
	}
}
