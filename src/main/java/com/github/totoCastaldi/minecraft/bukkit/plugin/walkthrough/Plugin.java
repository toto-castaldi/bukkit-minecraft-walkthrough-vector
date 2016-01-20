package com.github.totoCastaldi.minecraft.bukkit.plugin.walkthrough;

import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public class Plugin extends JavaPlugin {

    private Map<Player, Boolean> tntLaunch = new HashMap<Player, Boolean>();

    public boolean onCommand(
            CommandSender sender,
            Command command,
            String commandLabel,
            String[] args
    ) {

        System.out.println("onCommand command [" + command + "], commandLabel [" + commandLabel + "] args [" + Arrays.asList(args) + "]");

        Player me = (Player) sender;

        final String firstParameter = StringUtils.stripToEmpty(Iterables.getFirst(Arrays.asList(args), StringUtils.EMPTY));
        final String secondParameter = StringUtils.stripToEmpty(Iterables.get(Arrays.asList(args), 1, StringUtils.EMPTY));

        if (StringUtils.equalsIgnoreCase("step", commandLabel)) {

            if ("1".equalsIgnoreCase(firstParameter)) step1(me, StringUtils.equalsIgnoreCase("start", secondParameter));
        }

        return true;
    }

    private void step1(Player player, Boolean start) {
        tntLaunch.put(player, start);
    }

    public class PlayerEventListener implements Listener {
        PlayerEventListener(Plugin plugin) {
            Server server = plugin.getServer();
            PluginManager pluginManager = server.getPluginManager();
            pluginManager.registerEvents(this, plugin);
        }

        @EventHandler
        public void onPlayerJoinEvent(PlayerJoinEvent playerJoinEvent) {
            Player player = playerJoinEvent.getPlayer();

            tntLaunch.put(player, false);
        }

        @EventHandler
        public void onPlayerAnimationEvent (PlayerAnimationEvent playerAnimationEvent) {
            Player player = playerAnimationEvent.getPlayer();

            if (playerAnimationEvent.getAnimationType() == PlayerAnimationType.ARM_SWING) {
                if (true == tntLaunch.get(player)) {
                    answer1Action(player);
                }
            }
        }
    }

    private void step1Action(Player player) {
        Location location = player.getEyeLocation();

        player.sendMessage(ChatColor.DARK_AQUA + "BOOM!");

        Vector v = location.getDirection().multiply(2);

        TNTPrimed tnt = player.getWorld().spawn(location, TNTPrimed.class);
        tnt.setVelocity(v);
    }

    private void answer1Action(Player player) {
        step1Action(player);
        Location location = player.getEyeLocation();

        Vector v = location.getDirection().multiply(2);

        Cow cow = player.getWorld().spawn(location, Cow.class);
        cow.setVelocity(v);
    }

    public void onEnable() {
        System.out.println("enEnable");

        new PlayerEventListener(this);
    }


    public void onLoad() {
        System.out.println("onLoad");
    }

    public void onDisable() {
        System.out.println("onDisable");
    }

    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args) {

        System.out.println("onTabComplete");
        return new ArrayList<String>();
    }
}