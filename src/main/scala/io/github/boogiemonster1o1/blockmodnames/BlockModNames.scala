package io.github.boogiemonster1o1.blockmodnames

import java.io.IOException
import java.nio.file.{Files, Path}
import java.util

import com.google.gson.{Gson, GsonBuilder, JsonObject}
import com.mojang.brigadier.CommandDispatcher
import com.mojang.serialization.JsonOps
import io.github.boogiemonster1o1.blockmodnames.command.BlockModNamesCommand
import io.github.boogiemonster1o1.blockmodnames.config.{BlockedEntry, Config}
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.network.{PacketContext, ServerSidePacketRegistry}
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
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
		ServerSidePacketRegistry.INSTANCE.register(CustomPayloadC2SPacket.BRAND, (ctx: PacketContext, buf: PacketByteBuf) => {
			val modName: String = buf.readString(32767)
			ctx.getTaskQueue.execute(() => {
				if (config.matches(modName)) {
					ctx.getPlayer.asInstanceOf[ServerPlayerEntity].networkHandler.disconnect(new LiteralText("Unsupported client! Please contact the server administrator if you think this was a mistake"))
				}
			})
		})
		CommandRegistrationCallback.EVENT.register((dispatcher: CommandDispatcher[ServerCommandSource], _ /*dedicated*/: Boolean) => {
			BlockModNamesCommand.register(dispatcher)
		})
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
