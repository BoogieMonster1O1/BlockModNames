package io.github.boogiemonster1o1.blockmodnames

import java.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

object Config {
	def codec: Codec[Config] = RecordCodecBuilder.create((instance: RecordCodecBuilder.Instance[Config]) => {
		instance.group(
			BlockedEntry.codec.listOf.fieldOf("entries").forGetter((config: Config) => config.getEntries)
		).apply(instance, (list: util.List[BlockedEntry]) => new Config(list))
	})
}

class Config(private val entries: util.List[BlockedEntry]) {
	def matches(str: String): Boolean = {
		val itr: util.Iterator[BlockedEntry] = entries.iterator()
		while (itr.hasNext) {
			val entry: BlockedEntry = itr.next()
			if (entry.matches(str)) return true
		}
		false
	}

	def getEntries: util.List[BlockedEntry] = {
		entries
	}

	override def toString: String = {
		new StringBuilder()
			.append("Config{")
			.append("entries:'")
			.append(entries)
			.append("'}")
			.toString
	}
}
