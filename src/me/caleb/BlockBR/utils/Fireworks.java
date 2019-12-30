package me.caleb.BlockBR.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import me.caleb.BlockBR.Main;

public class Fireworks implements Listener{

	private Main plugin;
	private boolean celebration;
	
	public Fireworks(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}
	
	public void spawnFireWorks(Player p) {
		
		Location playerLoc = p.getLocation();
		Firework fw = (Firework) playerLoc.getWorld().spawnEntity(playerLoc, EntityType.FIREWORK);
		
		FireworkMeta fwm = fw.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.LIME).withTrail().build();
		FireworkEffect effect2 = FireworkEffect.builder().flicker(true).withColor(Color.AQUA).withTrail().withFade(Color.BLACK).build();
		FireworkEffect effect3 = FireworkEffect.builder().flicker(true).withColor(Color.YELLOW).withTrail().build();
		FireworkEffect effect4 = FireworkEffect.builder().flicker(true).withColor(Color.SILVER).withTrail().build();
		
		fwm.addEffect(effect);
		fwm.addEffect(effect2);
		fwm.addEffect(effect3);
		fwm.addEffect(effect4);
		
		fw.setFireworkMeta(fwm);
		
		celebration = true;
		detonate(fw);
		
	}
	
	public void detonate(final Firework fw) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                try{
                    fw.detonate();
                }catch(Exception e){}
            }
        }, (25));
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		/*
		 * Celebration variable makes it to where this does not effect with any other firework damaging. Only the damaging
		 * when the person levels up or goes up a tier. 
		 */
	    if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player && celebration == true) {
	        event.setCancelled(true);
	    }
	}
	
}
