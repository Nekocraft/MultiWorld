package multiworld;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import multiworld.addons.AddonHandler;
import multiworld.api.MultiWorldAPI;
import multiworld.command.CommandHandler;
import multiworld.data.DataHandler;
import multiworld.data.InternalWorld;
import multiworld.data.MyLogger;
import multiworld.data.PlayerHandler;
import multiworld.data.ReloadHandler;
import multiworld.data.WorldHandler;
import multiworld.worldgen.SimpleChunkGen;
import multiworld.worldgen.WorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the project
 * @author Fernando
 */
public class MultiWorldPlugin extends JavaPlugin
{
	/**
	 * The version of the plugin
	 */
	private String version;
	/**
	 * The plugin directory
	 */
	File pluginDir;
	/**
	 * The configuration
	 */
	DataHandler data = null;
	/**
	 * was there anny critical error?
	 */
	boolean errorStatus = false;
	/**
	 * The logger
	 */
	MyLogger log;
	private CommandHandler commandHandler;
	private PlayerHandler playerHandler;
	private WorldHandler worldHandler;
	private static MultiWorldPlugin instance;
	private AddonHandler pluginHandler;
	private ReloadHandler reloadHandler;

	/**
	 * this is called when the plugin is enabled
	 */
	@Override
	public void onEnable()
	{
		try
		{
			MultiWorldPlugin.instance = this;
			PluginDescriptionFile pdfFile = this.getDescription();
			this.version = pdfFile.getVersion();
			this.pluginDir = this.getDataFolder();
			this.pluginDir.mkdir();

			this.data = new DataHandler(this.getServer(), this.getConfig(), this); //NOI18N
			this.log = this.data.getLogger();
			this.playerHandler = new PlayerHandler(this.data);
			this.worldHandler = new WorldHandler(this.data);
			this.pluginHandler = new AddonHandler(this.data, this.version);
			this.reloadHandler = new ReloadHandler(this.data, this.getPluginHandler());
			this.commandHandler = new CommandHandler(this.data, this.playerHandler, this.worldHandler, this.reloadHandler, this.getPluginHandler(), this.getPluginHandler());
			this.pluginHandler.onSettingsChance();
			this.submitStats();
			this.log.info("v" + this.version + " 已启用."); //NOI18N

		}
		catch (ConfigException e)
		{
			this.getServer().getLogger().log(Level.SEVERE, "[MultiWorld] 启用失败:".concat(e.toString())); //NOI18N
			this.getServer().getLogger().severe("[MultiWorld] 配置文件加载失败 请检查配置文件:"); //NOI18N
			e.printStackTrace(System.err);
			this.errorStatus = true;
			this.setEnabled(false);
		}
		catch (RuntimeException e)
		{
			this.getServer().getLogger().log(Level.SEVERE, "[MultiWorld] 启用失败:".concat(e.toString())); //NOI18N
			this.getServer().getLogger().severe("[MultiWorld] 请将错误报告提供给作者:"); //NOI18N
			e.printStackTrace(System.err);
			this.errorStatus = true;
			this.setEnabled(false);
		}
	}

	/**
	 * this is called when the plugin is deactivated
	 */
	@Override
	public void onDisable()
	{
		if (!this.errorStatus)
		{
			this.log.info("已禁用."); //NOI18N
			this.getPluginHandler().disableAll();
		}
		else
		{
			this.getServer().getLogger().severe("[MultiWorld] !!!     CRITICAL MALL FUNCTION     !!!"); //NOI18N
			this.getServer().getLogger().severe("[MultiWorld] !!!          SHUTTING DOWN         !!!"); //NOI18N
			this.getServer().getLogger().severe("[MultiWorld] !!!               :(               !!!"); //NOI18N
		}
		this.commandHandler = null;
		this.data = null;
		Bukkit.getScheduler().cancelTasks(this);
		MultiWorldPlugin.instance = null;
	}

	public void log(String msg)
	{
		this.log.info(msg);
	}

	public void warning(String msg)
	{
		this.log.warning(msg);
	}

	/**
	 * Called when there a command for this plugin
	 * @param sender The sender of the command
	 * @param cmd The command itself
	 * @param cmdLine The raw command
	 * @param split The parameters for the command
	 * @return if the command was ecuted succesfiully
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLine, String[] split)
	{
		try
		{
			split = Utils.parseArguments(split);
			this.commandHandler.excute(sender, cmd.getName(), split);
		}
		catch (NotAPlayerException e)
		{
			sender.sendMessage(ChatColor.RED + this.data.getLang().getString("COMMAND ONLY PLAYER"));
		}
		catch (PermissionException e)
		{
			sender.sendMessage(ChatColor.RED + this.data.getLang().getString("NO PERMISSIONS"));
		}
		catch (InvalidFlagException e)
		{
			sender.sendMessage(ChatColor.RED + this.data.getLang().getString("FLAG UNKNOWN"));
		}
		catch (InvalidFlagValueException e)
		{
			sender.sendMessage(ChatColor.RED + this.data.getLang().getString("FLAG VALUE UNKNOWN"));
			sender.sendMessage(ChatColor.BLUE + this.data.getLang().getString("FLAG VALUE UNKNOWN 1"));
		}
		catch (UnknownWorldException e)
		{
			sender.sendMessage(ChatColor.RED + this.data.getLang().getString("WORLD UNKNOWN", new Object[]
				{
					e.getWrongWorld()
				}));
			sender.sendMessage(ChatColor.BLUE + this.data.getLang().getString("WORLD UNKNOWN 1"));
		}
		catch (NotEnabledException e)
		{
			sender.sendMessage(ChatColor.RED + this.data.getLang().getString("FUNCTION NOT ENABLED"));
		}
		catch (ArgumentException e)
		{
			sender.sendMessage(ChatColor.RED + this.data.getLang().getString("USAGE: ", new Object[]
				{
					e.correctUsage()
				}));
		}
		catch (CommandException ex)
		{
			Utils.sendMessage(sender, ChatColor.RED + "Error: " + ex.getMessage());
		}
		catch (RuntimeException ex)
		{
			Utils.sendMessage(sender, ChatColor.RED + "Unknown internal error: " + ex.toString());
			Utils.sendMessage(sender, ChatColor.RED + "Inform plugin author about this");
			if (ex instanceof RuntimeException)
			{
				this.log.throwing("MultiWorldPlugin", "onCommand", ex, "Error while excuting command");
			}
			Throwable cause = ex;
			while ((cause = cause.getCause()) != null)
			{
				Utils.sendMessage(sender, ChatColor.RED + "Caused by: " + cause.toString());
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] split)
	{
		split = Utils.parseArguments(split);		
		List<String> list = Arrays.asList(this.commandHandler.getOptionsForUnfinishedCommands(sender, command.getName(), split));
		return list;
	}

	protected InternalWorld getWorld(String name, boolean mustBeLoaded) throws UnknownWorldException
	{
		return Utils.getWorld(name, data, mustBeLoaded);
	}

	/**
	 * Gets a chunk gen by name
	 * @param worldName The name of the world
	 * @param id the id of the gen to get
	 * @return The chunk gen of succces, or null on error
	 */
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		ChunkGenerator gen = WorldGenerator.getGen(id);
		if (gen == null)
		{
			//this.getServer().getLogger().log(Level.SEVERE, "[ {0} ] illegal or unknown worldGenerator, use ''FLATLAND'' or ''PIXELARTROOM''", getDescription().getFullName());
			return null;
		}
		//this.getServer().getLogger().log(Level.INFO, "[ {0} ] using world gen {1} for world {2}", new Object[]
		//	{
		//		getDescription().getFullName(), id, worldName
		//	});
		return gen;
	}

	public void gc()
	{
		WorldGenerator[] list = WorldGenerator.values();
		for (WorldGenerator w : list)
		{
			ChunkGenerator gen = WorldGenerator.getGen(w.name());
			if (gen == null)
			{
				continue;
			}
			if (gen instanceof SimpleChunkGen)
			{
				((SimpleChunkGen) gen).gc();
			}
		}
	}

	public static MultiWorldPlugin getInstance()
	{
		return MultiWorldPlugin.instance;
	}

	/**
	 * Gets the multiworld api interface
	 * Notice that its better to cache the result of this
	 * @return the api interface
	 */
	public MultiWorldAPI getApi()
	{
		if (this.isEnabled())
		{
			return new MultiWorldAPI(this);
		}
		return null;
	}

	public DataHandler getDataManager()
	{
		return this.data;
	}

	/**
	 * @return the pluginHandler
	 */
	public AddonHandler getPluginHandler()
	{
		return pluginHandler;
	}

	private void submitStats()
	{
		//Hit Performance
	}
}
