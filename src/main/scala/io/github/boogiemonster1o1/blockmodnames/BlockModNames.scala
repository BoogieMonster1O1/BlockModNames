package io.github.boogiemonster1o1.blockmodnames

import java.io.IOException
import java.nio.file.{Files, Path}
import java.util

import com.google.gson.{Gson, GsonBuilder, JsonObject}
import com.mojang.serialization.JsonOps
import io.github.boogiemonster1o1.blockmodnames.config.Config
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.{LogManager, Logger}

object BlockModNames extends ModInitializer {
	val logger: Logger = LogManager.getLogger("BlockModNames")
	val gson: Gson = new GsonBuilder().setPrettyPrinting().setLenient().create()
	val configPath: Path = FabricLoader.getInstance.getConfigDir.resolve("blockmodnames.json")
	val modId: String = "blockmodnames"
	val version: String = FabricLoader.getInstance.getModContainer(modId).get.getMetadata.getVersion.toString
	var config: Config = new Config(util.Arrays.asList())

	override def onInitialize(): Unit = {
		println("Loading BlockModNames")
		load()
	}

	def load(): Unit = {
		if (Files.exists(configPath)) {
			try {
				val json: JsonObject = gson.fromJson(Files.newBufferedReader(configPath), classOf[JsonObject])
				config = Config.codec.parse(JsonOps.INSTANCE, json).getOrThrow(false, (str: String) => logger.error(str))
			} catch {
				case e: IOException =>
					logger.error("I/O Error loading config. Not loading BlockModNames config.")
					e.printStackTrace()
				case e: RuntimeException =>
					logger.error("Error loading config. Not loading BlockModNames config.")
					e.printStackTrace()
			}
		}
		else save()
	}

	def save(): Unit = {
		try {
			if (!Files.exists(configPath)) {
				Files.createFile(configPath)
			}
			Files.write(configPath, gson.toJson(Config.codec.encodeStart(JsonOps.INSTANCE, config).getOrThrow(false, (str: String) => logger.error(str))).getBytes)
		} catch {
			case e: IOException =>
				logger.error("I/O Error saving config")
				e.printStackTrace()
			case e: RuntimeException =>
				logger.error("Error saving config")
				e.printStackTrace()
		}
	}
}
