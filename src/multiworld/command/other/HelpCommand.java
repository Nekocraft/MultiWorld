/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiworld.command.other;

import multiworld.Utils;
import multiworld.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Fernando
 */
public class HelpCommand extends Command
{

	public HelpCommand()
	{super("help");
	}

	@Override
	public void runCommand(CommandSender sender, String[] arguments)
	{
		String helpType = "main";
		if(arguments.length != 0)
		{
			String tmp = arguments[0].toLowerCase();
			if(tmp.equals("user") || tmp.equals("admin") || tmp.equals("advanced")|| tmp.equals("world"))
			{
				helpType = tmp;
			}
		}
		Utils.sendMessage(sender,ChatColor.BLUE+"这里是帮助信息喵: "+ helpType, 12);
		if(helpType.equals("main"))
		{
			this.sendMainHelp(sender);
		}
		else if(helpType.equals("user"))
		{
			this.sendUserHelp(sender);
		}
		else if(helpType.equals("admin"))
		{
			this.sendAdminHelp(sender);
		}
		else if(helpType.equals("advanced"))
		{
			this.sendAdvancedHelp(sender);
		}
		else if(helpType.equals("world"))
		{
			this.sendWorldHelp(sender);
		}
		
		
		
		
	}
	
	private void sendMainHelp(CommandSender sender)
	{
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw help" +                                  ChatColor.GREEN + " - 显示帮助文本",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw help user" +                             ChatColor.GREEN + " - 显示用户帮助文本",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw help admin" +                            ChatColor.GREEN + " - 显示管理帮助文本",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw help advanced" +                         ChatColor.GREEN + " - 显示高级管理帮助文本",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw help world" +                            ChatColor.GREEN + " - 显示世界管理帮助文本",12);
	}
	
	private void sendUserHelp(CommandSender sender)
	{
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw list" +                                  ChatColor.GREEN + " - 列出服务器中的所有世界",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw help" +                                  ChatColor.GREEN + " - 显示帮助文本",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw goto <世界名>" +                         ChatColor.GREEN + " - 传送到 <世界名>",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw debug" +                                 ChatColor.GREEN + " - 显示调试信息",12);
	}
	
	private void sendAdminHelp(CommandSender sender)
	{
		
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw load" +                                  ChatColor.GREEN + " - 重新加载多世界插件",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw save" +                                  ChatColor.GREEN + " - 保存多世界插件",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw move <玩家名> <世界名>" +                ChatColor.GREEN + " - 移动玩家到一个世界",12);
	}
	
	private void sendAdvancedHelp(CommandSender sender)
	{
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw getflag <世界名> <标志名>" +            ChatColor.GREEN + " - 获取一个世界的标志的值",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw setflag <世界名> <标志名> <新值>" +     ChatColor.GREEN + " - 设置一个世界的标志的值",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw flaglist" +                             ChatColor.GREEN + " - 列出可用的标志列表",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw link <源世界> [目标世界]" +             ChatColor.GREEN + " - 链接世界的传送门到一个世界",12);
	}

	private void sendWorldHelp(CommandSender sender)
	{
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw create <世界名> [类型] [种子]" +        ChatColor.GREEN + " - 创建一个 <类型> 的 <世界名> 世界 ",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw delete <世界名>" +                      ChatColor.GREEN + " - 从世界列表中删除 <世界名> ",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw unload <世界名>" +                      ChatColor.GREEN + " - 从世界列表中卸载 <世界名> ",12);
		Utils.sendMessage(sender, ChatColor.YELLOW + "/mw load <世界名>" +                        ChatColor.GREEN + " - 从世界列表中加载 <世界名> ",12);
	}
}
