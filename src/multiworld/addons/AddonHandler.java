/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiworld.addons;

import multiworld.data.DataHandler;
import multiworld.data.VersionHandler;

/**
 *
 * @author Fernando
 */
public final class AddonHandler implements VersionHandler,SettingsListener
{
	private final DataHandler data;
	private final String version;

	private final AddonMap plugins;
	
	
	
	public AddonHandler(DataHandler d, String version)
	{
		this.version = version;
		this.data = d;
		this.plugins = new AddonMap(data);

	}
	@Override
	public String getVersion()
	{
		return this.version;
	}

	@Override
	public boolean isLoaded(String plugin)
	{
		return plugins.isLoaded(plugin);
	}

	@Override
	public boolean isEnabled(String plugin)
	{
		return plugins.isEnabled(plugin);
	}

	@Override
	public String[] getPlugins()
	{
		return plugins.getPlugins();
	}

	public AddonHolder<?> getPlugin(String plugin)
	{
		return plugins.getPlugin(plugin);
	}


	@Override
	public void onSettingsChance()
	{
		this.plugins.onSettingsChance();
	}

	@Override
	public void disableAll()
	{
		this.plugins.disableAll();
	}

	@Override
	public boolean enabledInsideConfig(String plugin)
	{
		return this.plugins.enabledInsideConfig(plugin);
	}

	
}
