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

package org.orecruncher.dsurround.client.fx.particle.system;

import org.orecruncher.dsurround.client.fx.SteamJetEffect;
import org.orecruncher.dsurround.client.fx.particle.ParticleSteamCloud;
import org.orecruncher.lib.WorldUtils;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleSteamJet extends ParticleJet {

	public ParticleSteamJet(final int strength, final World world, final double x, final double y, final double z) {
		super(strength, world, x, y, z);
	}

	@Override
	public boolean shouldDie() {
		return !SteamJetEffect.isValidSpawnBlock(WorldUtils.getDefaultBlockStateProvider(), getPos());
	}

	@Override
	protected void spawnJetParticle() {
		final Particle particle = new ParticleSteamCloud(this.world, this.posX, this.posY, this.posZ, 0.1D);
		addParticle(particle);
	}

}
