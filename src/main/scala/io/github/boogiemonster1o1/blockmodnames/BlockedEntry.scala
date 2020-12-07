package io.github.boogiemonster1o1.blockmodnames

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.github.boogiemonster1o1.blockmodnames.config.MatchType

object BlockedEntry {
	val codec: Codec[BlockedEntry] = RecordCodecBuilder.create((instance: RecordCodecBuilder.Instance[BlockedEntry]) => {
		instance.group(
			Codec.STRING.fieldOf("value").forGetter((entry: BlockedEntry) => entry.getValue),
			MatchType.CODEC.fieldOf("type").forGetter((entry: BlockedEntry) => entry.getType)
		).apply(instance, (str: String, `type`: MatchType) => new BlockedEntry(str, `type`))
	})
}

class BlockedEntry(private val value: String, private val `type`: MatchType) {
	def matches(str: String): Boolean = {
		if (`type`.equals(MatchType.EXACT)) str.equalsIgnoreCase(value)
		else if (`type`.equals(MatchType.REGEX)) str.matches(value)
		else false
	}

	def getValue: String = {
		value
	}

	def getType: MatchType = {
		`type`
	}

	override def toString: String = {
		new StringBuilder()
			.append("BlockedEntry{")
			.append("value:'")
			.append(value)
			.append("',type:")
			.append(`type`)
			.append(",}")
			.toString
	}
}
