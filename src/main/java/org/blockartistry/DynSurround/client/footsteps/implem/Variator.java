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

package org.blockartistry.DynSurround.client.footsteps.implem;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum Variator {

	DEFAULT() {
		{
			IMMOBILE_DURATION = 200;
			EVENT_ON_JUMP = true;
			LAND_HARD_DISTANCE_MIN = 0.9f;
			SPEED_TO_JUMP_AS_MULTIFOOT = 0.005f;
			SPEED_TO_RUN = 0.022f;
			STRIDE = 1.25F; // 0.95f;
			STRIDE_STAIR = STRIDE * 0.65f;
			STRIDE_LADDER = 0.5f;
			PLAY_WANDER = true;
			QUADRUPED = false;
			PLAY_JUMP = false;
			DISTANCE_TO_CENTER = 0.2F;
			HAS_FOOTPRINT = true;
			FOOTPRINT_SCALE = 1F;
			VOLUME_SCALE = 1F;
		}
	},
	PLAYER() {
		{
			IMMOBILE_DURATION = 200;
			EVENT_ON_JUMP = true;
			LAND_HARD_DISTANCE_MIN = 0.9f;
			SPEED_TO_JUMP_AS_MULTIFOOT = 0.005f;
			SPEED_TO_RUN = 0.022f;
			STRIDE = 1.25F; // 0.95f;
			STRIDE_STAIR = STRIDE * 0.65f;
			STRIDE_LADDER = 0.5f;
			PLAY_WANDER = true;
			QUADRUPED = false;
			PLAY_JUMP = true;
			DISTANCE_TO_CENTER = 0.2F;
			HAS_FOOTPRINT = true;
			FOOTPRINT_SCALE = 1F;
			VOLUME_SCALE = 1F;
		}
	},
	PLAYER_QUAD() {
		{
			IMMOBILE_DURATION = 200;
			EVENT_ON_JUMP = true;
			LAND_HARD_DISTANCE_MIN = 0.9f;
			SPEED_TO_JUMP_AS_MULTIFOOT = 0.005f;
			SPEED_TO_RUN = 0.022f;
			STRIDE = 1.25F; // 0.95f;
			STRIDE_STAIR = STRIDE * 0.65f;
			STRIDE_LADDER = 0.5f;
			PLAY_WANDER = true;
			QUADRUPED = true;
			PLAY_JUMP = true;
			DISTANCE_TO_CENTER = 0.2F;
			HAS_FOOTPRINT = true;
			FOOTPRINT_SCALE = 0.75F;
			VOLUME_SCALE = 1F;
		}
	},
	VILLAGER() {
		{
			IMMOBILE_DURATION = 200;
			EVENT_ON_JUMP = true;
			LAND_HARD_DISTANCE_MIN = 0.9f;
			SPEED_TO_JUMP_AS_MULTIFOOT = 0.005f;
			SPEED_TO_RUN = 0.022f;
			STRIDE = 1.25F; // 0.95f;
			STRIDE_STAIR = STRIDE * 0.65f;
			STRIDE_LADDER = 0.5f;
			PLAY_WANDER = true;
			QUADRUPED = false;
			PLAY_JUMP = false;
			DISTANCE_TO_CENTER = 0.2F;
			HAS_FOOTPRINT = true;
			FOOTPRINT_SCALE = 1F;
			VOLUME_SCALE = 1F;
		}
	},
	CHILD() {
		{
			IMMOBILE_DURATION = 200;
			EVENT_ON_JUMP = true;
			LAND_HARD_DISTANCE_MIN = 0.9f;
			SPEED_TO_JUMP_AS_MULTIFOOT = 0.005f;
			SPEED_TO_RUN = 0.022f;
			STRIDE = 0.625F; // 0.95f;
			STRIDE_STAIR = STRIDE * 0.65f;
			STRIDE_LADDER = 0.5f;
			PLAY_WANDER = true;
			QUADRUPED = false;
			PLAY_JUMP = false;
			DISTANCE_TO_CENTER = 0.1F;
			HAS_FOOTPRINT = true;
			FOOTPRINT_SCALE = 0.5F;
			VOLUME_SCALE = 0.25F;
		}
	},
	SKELETON() {
		{
			IMMOBILE_DURATION = 200;
			EVENT_ON_JUMP = true;
			LAND_HARD_DISTANCE_MIN = 0.9f;
			SPEED_TO_JUMP_AS_MULTIFOOT = 0.005f;
			SPEED_TO_RUN = 0.022f;
			STRIDE = 1.25F; // 0.95f;
			STRIDE_STAIR = STRIDE * 0.65f;
			STRIDE_LADDER = 0.5f;
			PLAY_WANDER = true;
			QUADRUPED = false;
			PLAY_JUMP = false;
			DISTANCE_TO_CENTER = 0.2F;
			HAS_FOOTPRINT = true;
			FOOTPRINT_SCALE = 0.5F;
			VOLUME_SCALE = 0.25F;
		}
	};

	public int IMMOBILE_DURATION;
	public boolean EVENT_ON_JUMP;
	public float LAND_HARD_DISTANCE_MIN;
	public float SPEED_TO_JUMP_AS_MULTIFOOT;
	public float SPEED_TO_RUN;
	public float STRIDE;
	public float STRIDE_STAIR;
	public float STRIDE_LADDER;
	public boolean PLAY_WANDER;
	public boolean QUADRUPED;
	public boolean PLAY_JUMP;
	public float DISTANCE_TO_CENTER;
	public boolean HAS_FOOTPRINT;
	public float FOOTPRINT_SCALE;
	public float VOLUME_SCALE;

	// public boolean FORCE_HUMANOID = false;
	// public boolean GALLOP_3STEP = true;
	// public float GALLOP_DISTANCE_1 = 0.80f;
	// public float GALLOP_DISTANCE_2 = 0.25f;
	// public float GALLOP_DISTANCE_3 = 0.25f;
	// public float GALLOP_DISTANCE_4 = 0.05f;
	// public float GALLOP_VOLUME = 1f;

	// public float GROUND_AIR_STATE_SPEED = 0.2f;
	// public float HUGEFALL_LANDING_DISTANCE_MAX = 3f + 9f;

	// public float HUGEFALL_LANDING_DISTANCE_MIN = 3f;

	// public float SLOW_DISTANCE = 0.75f;
	// public float SLOW_VOLUME = 1f;
	// public float SPEED_TO_GALLOP = 0.13f;
	// public float SPEED_TO_WALK = 0.08f;

	// public float STAIRCASE_ANTICHASE_DIFFERENCE = 1f;

	// public float WALK_CHASING_FACTOR = 1f / 7f;

	// public float WALK_DISTANCE = 0.65f;
	// public int WING_FAST = 550 - 350;
	// public int WING_IMMOBILE_FADE_DURATION = 20000;

	// public int WING_IMMOBILE_FADE_START = 20000;

	// public int WING_JUMPING_REST_TIME = 700;

	// public int WING_SLOW = 550;
	// public float WING_SPEED_MAX = 0.2f + 0.25f;
	// public float WING_SPEED_MIN = 0.2f;
}
