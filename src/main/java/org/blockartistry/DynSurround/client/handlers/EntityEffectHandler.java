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
package org.blockartistry.DynSurround.client.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.blockartistry.DynSurround.ModOptions;
import org.blockartistry.DynSurround.client.handlers.EnvironStateHandler.EnvironState;
import org.blockartistry.DynSurround.client.handlers.effects.FrostBreathEffect;
import org.blockartistry.lib.effects.EffectHandler;
import org.blockartistry.lib.effects.EffectLibrary;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityEffectHandler extends EffectHandlerBase {

	private static final EffectLibrary library = new EffectLibrary();

	static {
		library.register(FrostBreathEffect.DEFAULT_FILTER, new FrostBreathEffect.Factory());
	}

	private final Map<UUID, EffectHandler> handlers = new HashMap<UUID, EffectHandler>();

	public EntityEffectHandler() {
		super("EntityEffectHandler");
	}

	@Override
	public void process(@Nonnull final EntityPlayer player) {
		final double distanceThreshold = ModOptions.specialEffectRange * ModOptions.specialEffectRange;
		final List<UUID> deadOnes = new ArrayList<UUID>();

		// Update our handlers
		for (final Entry<UUID, EffectHandler> entry : this.handlers.entrySet()) {
			final EffectHandler eh = entry.getValue();
			eh.update();
			if (!eh.isAlive() || eh.distanceSq(player) > distanceThreshold)
				deadOnes.add(entry.getKey());
		}

		// Remove the dead or distant ones
		for (final UUID id : deadOnes)
			this.handlers.remove(id);
	}

	@SubscribeEvent(receiveCanceled = true)
	public void onLivingUpdate(@Nonnull final LivingUpdateEvent event) {
		final Entity entity = event.getEntity();
		if (!entity.getEntityWorld().isRemote)
			return;

		if (this.handlers.containsKey(entity.getPersistentID()))
			return;

		final double distanceThreshold = ModOptions.specialEffectRange * ModOptions.specialEffectRange;
		if (entity.isEntityAlive() && entity.getDistanceSqToEntity(EnvironState.getPlayer()) <= distanceThreshold) {
			final Optional<EffectHandler> handler = library.create(entity);
			this.handlers.put(entity.getPersistentID(), handler.get());
		}
	}

	@Override
	public void onConnect() {
		this.handlers.clear();
	}

	@Override
	public void onDisconnect() {
		this.handlers.clear();
	}
}
