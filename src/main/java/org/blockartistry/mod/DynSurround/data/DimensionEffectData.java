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

package org.blockartistry.mod.DynSurround.data;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.blockartistry.mod.DynSurround.ModOptions;
import org.blockartistry.mod.DynSurround.Module;
import org.blockartistry.mod.DynSurround.util.XorShiftRandom;

import com.google.common.collect.ImmutableSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants;

/**
 * Per world effect data for effects
 */
public final class DimensionEffectData extends WorldSavedData {

	private static final XorShiftRandom random = new XorShiftRandom();
	private static final DecimalFormat FORMATTER = new DecimalFormat("0");

	public final static float MIN_INTENSITY = 0.0F;
	public final static float MAX_INTENSITY = 1.0F;
	public final static float MIN_THUNDER_THRESHOLD = 0.0F;
	public final static float MAX_THUNDER_THRESHOLD = 1.0F;

	private final class NBT {
		public final static String DIMENSION = "d";
		public final static String INTENSITY = "s";
		public final static String MIN_INTENSITY = "min";
		public final static String MAX_INTENSITY = "max";
		public final static String AURORA_LIST = "al";
		public final static String THUNDER_THRESHOLD = "tt";
	};

	private int dimensionId = 0;
	private float intensity = 0.0F;
	private float minIntensity = ModOptions.defaultMinRainStrength;
	private float maxIntensity = ModOptions.defaultMaxRainStrength;
	private float thunderThreshold = ModOptions.defaultThunderThreshold;
	private final Set<AuroraData> auroras = new HashSet<AuroraData>();

	private DimensionEffectData(final int dimension) {
		this(Module.MOD_ID);
		this.dimensionId = dimension;
	}

	public DimensionEffectData(@Nonnull final String identifier) {
		super(identifier);
	}

	public int getDimensionId() {
		return this.dimensionId;
	}

	public float getRainIntensity() {
		return this.intensity;
	}

	public void setRainIntensity(final float intensity) {
		this.intensity = MathHelper.clamp_float(intensity, MIN_INTENSITY, MAX_INTENSITY);
		this.setDirty(true);
	}

	public float getMinRainIntensity() {
		return this.minIntensity;
	}

	public void setMinRainIntensity(final float intensity) {
		this.minIntensity = MathHelper.clamp_float(intensity, MIN_INTENSITY, this.maxIntensity);
		this.setDirty(true);
	}

	public float getMaxRainIntensity() {
		return this.maxIntensity;
	}

	public void setMaxRainIntensity(final float intensity) {
		this.maxIntensity = MathHelper.clamp_float(intensity, this.minIntensity, MAX_INTENSITY);
		this.setDirty(true);
	}

	public float getThunderThreshold() {
		return this.thunderThreshold;
	}

	public void setThunderThreshold(final float threshold) {
		this.thunderThreshold = MathHelper.clamp_float(threshold, MIN_THUNDER_THRESHOLD, MAX_THUNDER_THRESHOLD);
		this.setDirty(true);
	}

	@Nonnull
	public Set<AuroraData> getAuroraList() {
		return ImmutableSet.copyOf(this.auroras);
	}

	public void clearAuroraList() {
		if (this.auroras.size() > 0) {
			this.auroras.clear();
			this.setDirty(true);
		}
	}

	public boolean addAuroraData(@Nonnull final AuroraData aurora) {
		if (this.auroras.add(aurora)) {
			this.setDirty(true);
			return true;
		}
		return false;
	}

	public void removeAuroraData(@Nonnull final AuroraData aurora) {
		if (this.auroras.remove(aurora))
			this.setDirty(true);
	}

	public void randomizeRain() {
		float result = 0.0F;
		final float delta = this.maxIntensity - this.minIntensity;
		if (delta <= 0.0F) {
			result = (float) this.minIntensity;
		} else {
			final float mid = delta / 2.0F;
			result = random.nextFloat() * mid + random.nextFloat() * mid;
		}
		setRainIntensity(MathHelper.clamp_float(result, 0.01F, MAX_INTENSITY));
	}

	@Override
	public void readFromNBT(@Nonnull final NBTTagCompound nbt) {
		this.dimensionId = nbt.getInteger(NBT.DIMENSION);
		this.intensity = MathHelper.clamp_float(nbt.getFloat(NBT.INTENSITY), MIN_INTENSITY, MAX_INTENSITY);
		if (nbt.hasKey(NBT.MIN_INTENSITY))
			this.minIntensity = MathHelper.clamp_float(nbt.getFloat(NBT.MIN_INTENSITY), MIN_INTENSITY, MAX_INTENSITY);
		if (nbt.hasKey(NBT.MAX_INTENSITY))
			this.maxIntensity = MathHelper.clamp_float(nbt.getFloat(NBT.MAX_INTENSITY), this.minIntensity,
					MAX_INTENSITY);
		if (nbt.hasKey(NBT.THUNDER_THRESHOLD))
			this.thunderThreshold = MathHelper.clamp_float(nbt.getFloat(NBT.THUNDER_THRESHOLD), MIN_THUNDER_THRESHOLD,
					MAX_THUNDER_THRESHOLD);
		final NBTTagList list = nbt.getTagList(NBT.AURORA_LIST, Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++) {
			final NBTTagCompound tag = list.getCompoundTagAt(i);
			final AuroraData data = new AuroraData();
			data.readFromNBT(tag);
			this.auroras.add(data);
		}
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(@Nonnull final NBTTagCompound nbt) {
		nbt.setInteger(NBT.DIMENSION, this.dimensionId);
		nbt.setFloat(NBT.INTENSITY, this.intensity);
		nbt.setFloat(NBT.MIN_INTENSITY, this.minIntensity);
		nbt.setFloat(NBT.MAX_INTENSITY, this.maxIntensity);
		nbt.setFloat(NBT.THUNDER_THRESHOLD, this.thunderThreshold);
		final NBTTagList list = new NBTTagList();
		for (final AuroraData data : this.auroras) {
			final NBTTagCompound tag = new NBTTagCompound();
			data.writeToNBT(tag);
			list.appendTag(tag);
		}
		nbt.setTag(NBT.AURORA_LIST, list);
		return nbt;
	}

	@Nonnull
	public String configString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("dim ").append(this.dimensionId).append(": ");
		builder.append("rainIntensity [").append(FORMATTER.format(this.minIntensity * 100));
		builder.append(",").append(FORMATTER.format(this.maxIntensity * 100));
		builder.append("]");
		builder.append(", thunder threshold: ").append(FORMATTER.format(this.thunderThreshold * 100));
		return builder.toString();
	}

	@Override
	@Nonnull
	public String toString() {
		// Dump out some diagnostics for the current dimension
		final StringBuilder builder = new StringBuilder();
		builder.append("dim ").append(this.dimensionId).append(": ");
		builder.append("rainIntensity: ").append(FORMATTER.format(this.intensity * 100));
		builder.append(" [").append(FORMATTER.format(this.minIntensity * 100));
		builder.append(",").append(FORMATTER.format(this.maxIntensity * 100));
		builder.append("]");
		builder.append(", thunder: ").append(FORMATTER.format(this.thunderThreshold * 100));
		builder.append(", auroras: ").append(this.auroras.size());
		return builder.toString();
	}

	@Nonnull
	public static DimensionEffectData get(@Nonnull final World world) {
		final MapStorage storage = world.getPerWorldStorage();
		DimensionEffectData data = (DimensionEffectData) storage.getOrLoadData(DimensionEffectData.class,
				Module.MOD_ID);
		if (data == null) {
			data = new DimensionEffectData(world.provider.getDimension());
			storage.setData(Module.MOD_ID, data);
		}
		return data;
	}

}
