/*
 * Copyright (c) 2014,
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the {organization} nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 */

package net.doubledoordev.d3core;


import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.doubledoordev.d3core.util.DevPerks;
import net.doubledoordev.d3core.util.ID3Mod;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import static net.doubledoordev.d3core.util.CoreConstants.*;

/**
 * @author Dries007
 */
@Mod(modid = MODID, name = NAME, canBeDeactivated = false, guiFactory = MOD_GUI_FACTORY)
public class D3Core
{
    @Mod.Instance(MODID)
    public static D3Core instance;

    @Mod.Metadata
    private ModMetadata metadata;

    private Logger logger;
    private DevPerks devPerks;
    private Configuration configuration;

    private boolean debug = false;
    private boolean sillyness = true;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(this);

        logger = event.getModLog();
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
    {
        if (eventArgs.modID.equals(MODID)) syncConfig();
        for (ModContainer modContainer : Loader.instance().getActiveModList())
        {
            if (modContainer.getMod() instanceof ID3Mod)
            {
                ((ID3Mod) modContainer.getMod()).syncConfig();
            }
        }
    }

    public void syncConfig()
    {
        configuration.setCategoryLanguageKey(MODID, "d3.core.config.core").setCategoryComment(MODID, I18n.format("d3.core.config.core.tooltip"));

        debug = configuration.getBoolean("debug", MODID, debug, "Enable debug mode", "d3.core.config.debug");
        sillyness = configuration.getBoolean("sillyness", MODID, sillyness, "Enable sillyness", "d3.core.config.sillyness");

        if (sillyness) MinecraftForge.EVENT_BUS.register(getDevPerks());
        else MinecraftForge.EVENT_BUS.unregister(getDevPerks());

        if (configuration.hasChanged()) configuration.save();
    }

    public static Logger getLogger()
    {
        return instance.logger;
    }

    public static boolean debug()
    {
        return instance.debug;
    }

    public static Configuration getConfiguration()
    {
        return instance.configuration;
    }

    public static DevPerks getDevPerks()
    {
        if (instance.devPerks == null) instance.devPerks = new DevPerks();
        return instance.devPerks;
    }
}
