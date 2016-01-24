package io.github.CMD_BLOCK.RegExpChat;

import java.io.File;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public void onEnable(){
		if(!getDataFolder().exists()){
			getDataFolder().mkdir();
		}
		File file=new File(getDataFolder(),"config.yml");
		if(!(file.exists())){
			this.saveDefaultConfig();
		}
		this.reloadConfig();
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("RegExpChat插件成功加载！作者:CMD丶BLOCK");
	}
	public void onDisable(){
		getLogger().info("RegExpChat插件成功卸载！作者:CMD丶BLOCK");
	}
	public void reload(){
		if(!getDataFolder().exists()){
			getDataFolder().mkdir();
		}
		File file=new File(getDataFolder(),"config.yml");
		if(!(file.exists())){
			this.saveDefaultConfig();
		}
		this.reloadConfig();
	}
	public boolean onCommand(CommandSender sender,Command cmd, String lable, String[] args){
		if(lable.equalsIgnoreCase("regexp")){
			if(sender.isOp()||sender.hasPermission("regexpchat.use")){
				if(args.length==0){sender.sendMessage("§c/regexp reload  重载插件配置");return true;}
				if(args.length==1){
					if(args[0].equalsIgnoreCase("reload")){
						reload();
						sender.sendMessage("§a重载完成！");
						return true;
					}
				}
				sender.sendMessage("§c/regexp reload  重载插件配置");
				return true;
			}
			sender.sendMessage("§4你没有使用此命令的权限");
			return true;
		}
		return false;
	}
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Player player=event.getPlayer();
		boolean pd=false;
		if(!player.hasPermission("regexpchat.bypass")){
			List<String> list=this.getConfig().getStringList("RegExpList");
			String msg=event.getMessage().toLowerCase();
			for(int x=0;x<list.toArray().length;x++){
				String regexp=(String)list.toArray()[x];
				regexp=regexp.toLowerCase();
				if(msg.matches(regexp)){
					pd=true;
					break;
				}
			}
			if(pd){
				if(this.getConfig().getBoolean("sendToConsole")){
					getLogger().info("玩家 "+player+"说关键词，原话:"+event.getMessage());
				}
				if(this.getConfig().getBoolean("Cancel")){
					event.setCancelled(true);
				}else{
					event.setMessage(this.getConfig().getString("ReplaceChar"));
				}
				if(this.getConfig().getBoolean("sendMessage")){
					player.sendMessage(this.getConfig().getString("sendMessageChar"));
				}
			}
		}
	}
}
