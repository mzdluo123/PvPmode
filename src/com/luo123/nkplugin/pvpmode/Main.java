package com.luo123.nkplugin.pvpmode;


import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;



public class Main extends PluginBase implements Listener {


    private static HashMap<String, Boolean> pvpmode = new HashMap<>();

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);  //注册监听器
        this.getServer().getLogger().info(TextFormat.GOLD + "pvpmode 1.0 by:luo123已加载");   //输出消息

        for (Player player : getServer().getOnlinePlayers().values()) {
            pvpmode.put(player.getName(), false);
        }  //吧插件加载时服务器内的所有玩家pvp模式设置为false


    }


    public void onDisable() {

        this.getServer().getLogger().info(TextFormat.GOLD + "pvpmode 1.0 by:luo123已卸载");

    }



    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {    //伤害监听器

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {   //当攻击者和被攻击者都是玩家
            Player caseplayer, player;
            player = (Player) event.getEntity();
            caseplayer = (Player) event.getDamager();

            if (!pvpmode.get(caseplayer.getName())) {   //当攻击者没有打开pvp
                caseplayer.sendMessage("你没有打开pvp ，无法攻击别人");
                event.setCancelled();
                return;
            }
            if (!pvpmode.get(player.getName())) {  //当被攻击者没有打开pvp
                caseplayer.sendMessage("对方没有打开pvp，你无法攻击他");
                event.setCancelled();

            }

        }
    }


    @EventHandler
    public void onPlayerjoint(PlayerJoinEvent event) {    //进服监听器
        pvpmode.put(event.getPlayer().getName(), false);
        event.getPlayer().sendMessage("pvp已关闭，请输入/pvpm开启pvp");
        //进服吧玩家pvp模式默认设置为false
    }

    @EventHandler
    public void onPlayerleft(PlayerQuitEvent event) {    //退出监听器
        pvpmode.remove(event.getPlayer().getName());
        //退出时清除玩家pvp模式
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {   //命令监听

        switch (command.getName()) {
            case "pvpm":
                if (pvpmode.get(sender.getName())) {
                    pvpmode.replace(sender.getName(), false);
                    sender.sendMessage("成功关闭pvp");
                } else {
                    pvpmode.replace(sender.getName(), true);
                    sender.sendMessage("成功开启pvp");
                }
                return true;
        }
        return true;
    }


    public static boolean getPvpmode(String playername) {
        return pvpmode.get(playername);
    }

    public static void setPvpmode(String playername, boolean playerpvpmode) {
        pvpmode.replace(playername, playerpvpmode);
    }

    public static boolean isexist(String playername) {

        for (String key : pvpmode.keySet()) {
            if (playername.equals(key)) {
                return true;
            }
        }
        return false;
    }
}


