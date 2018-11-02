/*
 * This file is part of Dynamic Surroundings, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.orecruncher.dsurround.proxy;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.orecruncher.dsurround.ModBase;
import org.orecruncher.dsurround.ModOptions;
import org.orecruncher.dsurround.client.fx.ParticleCollections;
import org.orecruncher.dsurround.client.fx.particle.ParticleDripOverride;
import org.orecruncher.dsurround.client.gui.HumDinger;
import org.orecruncher.dsurround.client.handlers.EffectManager;
import org.orecruncher.dsurround.client.hud.GuiHUDHandler;
import org.orecruncher.dsurround.client.hud.InspectionHUD;
import org.orecruncher.dsurround.client.hud.LightLevelHUD;
import org.orecruncher.dsurround.client.keyboard.KeyHandler;
import org.orecruncher.dsurround.client.renderer.AnimaniaBadge;
import org.orecruncher.dsurround.client.sound.BackgroundMute;
import org.orecruncher.dsurround.client.sound.MusicTickerReplacement;
import org.orecruncher.dsurround.client.sound.SoundEngine;
import org.orecruncher.dsurround.client.weather.RenderWeather;
import org.orecruncher.dsurround.client.weather.Weather;
import org.orecruncher.dsurround.commands.CommandCalc;
import org.orecruncher.dsurround.data.PresetHandler;
import org.orecruncher.dsurround.event.ReloadEvent;
import org.orecruncher.dsurround.event.WorldEventDetector;
import org.orecruncher.lib.Localization;
import org.orecruncher.lib.compat.ModEnvironment;
import org.orecruncher.lib.task.Scheduler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ProxyClient extends Proxy implements ISelectiveResourceReloadListener {

	@Override
	protected void registerLanguage() {
		Localization.initialize(Side.CLIENT);
	}

	@Override
	protected void eventBusRegistrations() {
		super.eventBusRegistrations();

		register(HumDinger.class);
		register(InspectionHUD.class);
		register(LightLevelHUD.class);
		register(KeyHandler.class);
		register(BackgroundMute.class);
		register(RenderWeather.class);
		register(Weather.class);
		register(PresetHandler.class);
		register(WorldEventDetector.class);
		register(LightLevelHUD.class);
		register(ParticleCollections.class);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean isRunningAsServer() {
		return false;
	}

	@Override
	public Side effectiveSide() {
		return FMLCommonHandler.instance().getEffectiveSide();
	}

	@Override
	public void preInit(@Nonnull final FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(@Nonnull final FMLInitializationEvent event) {
		super.init(event);

		KeyHandler.init();
		ParticleDripOverride.register();

		ClientCommandHandler.instance.registerCommand(new CommandCalc());

		if (ModOptions.general.disableWaterSuspendParticle)
			Minecraft.getMinecraft().effectRenderer.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), null);

		if (ModEnvironment.AmbientSounds.isLoaded())
			SoundEngine.configureSound(null);
	}

	@Override
	public void postInit(@Nonnull final FMLPostInitializationEvent event) {
		MusicTickerReplacement.initialize();

		// Register for resource load events
		final IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
		((IReloadableResourceManager) resourceManager).registerReloadListener(this);

		if (ModEnvironment.Animania.isLoaded())
			AnimaniaBadge.intitialize();
	}

	@Override
	public void clientConnect(@Nonnull final ClientConnectedToServerEvent event) {
		Scheduler.schedule(Side.CLIENT, () -> {
			EffectManager.connect();
			GuiHUDHandler.register();
			Weather.register(ModBase.isInstalledOnServer());
			ProxyClient.this.connectionTime = System.currentTimeMillis();
		});
	}

	@Override
	public void clientDisconnect(@Nonnull final ClientDisconnectionFromServerEvent event) {
		Scheduler.schedule(Side.CLIENT, () -> {
			EffectManager.disconnect();
			GuiHUDHandler.unregister();
			Weather.unregister();
			ProxyClient.this.connectionTime = 0;
		});
	}

	@SubscribeEvent
	public void onConfigChanged(@Nonnull final OnConfigChangedEvent event) {
		if (event.getModID().equals(ModBase.MOD_ID)) {
			// The configuration file changed. Fire an appropriate
			// event so that various parts of the mod can reinitialize.
			MinecraftForge.EVENT_BUS.post(new ReloadEvent.Configuration());
		}

	}

	@Override
	public void onResourceManagerReload(@Nonnull final IResourceManager resourceManager,
			@Nonnull final Predicate<IResourceType> resourcePredicate) {
		if (resourcePredicate.test(VanillaResourceType.SOUNDS)) {
			MinecraftForge.EVENT_BUS.post(new ReloadEvent.Resources(resourceManager));
		}
	}
}