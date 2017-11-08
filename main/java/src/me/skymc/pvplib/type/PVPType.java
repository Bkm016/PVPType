package me.skymc.pvplib.type;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.skymc.pvplib.events.TargetAddEvent;
import me.skymc.pvplib.events.TargetRemoveEvent;

public class PVPType {
	
	/**
	 * һ��һ�Ծ�״̬
	 * 
	 * ������� A �趨 B Ϊ�Ծ�Ŀ�꣬
	 * ��ô A ֻ�ܶ� B ����˺�
	 * ���� B ȴ����ֱ�ӹ��� A
	 */
	
	// PVP_PRIVATE("PVP_PRIVATE", "", null),
	
	/**
	 * �����Ծ�״̬
	 * 
	 * ֻҪ˫�������� �����Ծ� ״̬���ɻ�������˺�
	 */
	
	// PVP_PUBLIC("PVP_PUBLIC", "", null),
	
	/**
	 * ����״̬
	 * 
	 * �޷��յ������κ���ҵ��˺�
	 */
	
	// PROTECT("PROTECT", "", null);
	
	
	/**
	 * Ϊ�Ծ��б��Ƴ����
	 * 
	 * @param player ���
	 */
	public void removeTarget(Player player) {
		
		// �ж��Ƿ�Ϊ�Ծ�ģʽ
		if (name.equals("PVP_PRIVATE")) {
			
			// �ж��Ƿ����
			if (getTargets().contains(player.getName())) {
				
				TargetRemoveEvent event = new TargetRemoveEvent(Bukkit.getPlayerExact(this.player), player);
				Bukkit.getPluginManager().callEvent(event);
				
				// ����¼���ȡ��
				if (event.cancel()) {
					return;
				}
				
				value = value.replace(player.getName() + "//", "");
			}
		}
	}
	
	/**
	 * Ϊ�Ծ��б��������
	 * 
	 * @param player ���
	 */
	public void addTarget(Player player) {
		
		// �ж��Ƿ�Ϊ�Ծ�ģʽ
		if (name.equals("PVP_PRIVATE")) {
			
			// �ж��Ƿ��ظ�
			if (!getTargets().contains(player.getName())) {
				
				TargetAddEvent event = new TargetAddEvent(Bukkit.getPlayerExact(this.player), player);
				Bukkit.getPluginManager().callEvent(event);
				
				// ����¼���ȡ��
				if (event.cancel()) {
					return;
				}
				
				value = value += player.getName() + "//";
			}
		}
	}
	
	/**
	 * ��ȡ�Ծ��б�
	 * 
	 * @return �Ծ���� 
	 */
	public List<String> getTargets() {
		
		// ������ǶԾ�ģʽ��Ծ��б�Ϊ��
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
