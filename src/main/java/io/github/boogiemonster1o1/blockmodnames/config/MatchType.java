package io.github.boogiemonster1o1.blockmodnames.config;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;

public enum MatchType implements StringIdentifiable {
	EXACT("EXACT"),
	REGEX("REGEX");

	private static final Map<String, MatchType> BY_NAME = Util.make(Maps.newHashMap(), (map) -> {
		for (MatchType type : values()) {
			map.put(type.name, type);
		}
	});
	public static final Codec<MatchType> CODEC = StringIdentifiable.createCodec(MatchType::values, MatchType::byName);
	private final String name;

	MatchType(String name) {
		this.name = name;
	}

	@Override
	public String asString() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.asString();
	}

	public static MatchType byName(String name) {
		return BY_NAME.get(name);
	}
}
