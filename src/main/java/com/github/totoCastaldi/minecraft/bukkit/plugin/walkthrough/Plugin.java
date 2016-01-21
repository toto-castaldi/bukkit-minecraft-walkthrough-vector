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

    private Map<Player, String> launch = new HashMap<Player, String>();

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

            if ("1".equalsIgnoreCase(firstParameter)) step(me, "s1", StringUtils.equalsIgnoreCase("start", secondParameter));
            if ("2".equalsIgnoreCase(firstParameter)) step(me, "s2", StringUtils.equalsIgnoreCase("start", secondParameter));
            if ("3".equalsIgnoreCase(firstParameter)) step(me, "s3", StringUtils.equalsIgnoreCase("start", secondParameter));
        }

        if (StringUtils.equalsIgnoreCase("answer", commandLabel)) {

            if ("1".equalsIgnoreCase(firstParameter)) step(me, "a1", StringUtils.equalsIgnoreCase("start", secondParameter));
            if ("2".equalsIgnoreCase(firstParameter)) step(me, "a2", StringUtils.equalsIgnoreCase("start", secondParameter));
        }

        return true;
    }

    private void step(Player player, String name, Boolean start) {
        if (start) {
            launch.put(player, name);
        } else {
            launch.remove(player);
        }
    }

    public class PlayerEventListener implements Listener {
        PlayerEventListener(Plugin plugin) {
            Server server = plugin.getServer();
            PluginManager pluginManager = server.getPluginManager();
            pluginManager.registerEvents(this, plugin);
        }

        @EventHandler
        public void onPlayerAnimationEvent (PlayerAnimationEvent playerAnimationEvent) {
            Player player = playerAnimationEvent.getPlayer();

            if (playerAnimationEvent.getAnimationType() == PlayerAnimationType.ARM_SWING) {
                String name = launch.get(player);
                if (StringUtils.equals("s1", name)) {
                    step1Action(player);
                }
                if (StringUtils.equals("a1", name)) {
                    answer1Action(player);
                }
                if (StringUtils.equals("s2", name)) {
                    step2Action(player);
                }
                if (StringUtils.equals("a2", name)) {
                    answer2Action(player);
                }
                if (StringUtils.equals("s3", name)) {
                    step3Action(player);
                }
            }
        }
    }

    private void step3Action(Player player) {
        Location location = player.getEyeLocation();

        location.setYaw(location.getYaw() - 90);

        for (int i = 0; i < 18; i++) {
            location.setYaw(location.getYaw() + 10);

            Vector vTnt = location.getDirection().multiply(2);

            TNTPrimed tnt = player.getWorld().spawn(location, TNTPrimed.class);
            tnt.setVelocity(vTnt);
        }

    }

    private void answer2Action(Player player) {
        Location location = player.getEyeLocation();

        Vector vTnt = location.getDirection().multiply(2);

        TNTPrimed tnt = player.getWorld().spawn(location, TNTPrimed.class);
        tnt.setVelocity(vTnt);

        location.setYaw(location.getYaw() - 30);
        vTnt = location.getDirection().multiply(2);

        tnt = player.getWorld().spawn(location, TNTPrimed.class);
        tnt.setVelocity(vTnt);

        location.setYaw(location.getYaw() + 60);
        vTnt = location.getDirection().multiply(2);

        tnt = player.getWorld().spawn(location, TNTPrimed.class);
        tnt.setVelocity(vTnt);
    }

    private void step2Action(Player player) {
        Location location = player.getEyeLocation();

        Vector vTnt = location.getDirection().multiply(2);

        TNTPrimed tnt = player.getWorld().spawn(location, TNTPrimed.class);
        tnt.setVelocity(vTnt);

        location.setYaw(location.getYaw() + 180);
        vTnt = location.getDirection().multiply(2);

        tnt = player.getWorld().spawn(location, TNTPrimed.class);
        tnt.setVelocity(vTnt);
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