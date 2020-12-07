package io.github.boogiemonster1o1.blockmodnames

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader

object BlockModNames extends ModInitializer {
	val modId: String = "blockmodnames"
	val version: String = FabricLoader.getInstance.getModContainer(modId).get.getMetadata.getVersion.toString

	override def onInitialize(): Unit = {
		println("Loading BlockModNames")
	}
}
