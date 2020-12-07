package io.github.boogiemonster1o1.blockmodnames.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import io.github.boogiemonster1o1.blockmodnames.BlockModNames
import net.minecraft.server.command.{CommandManager, ServerCommandSource}

object BlockModNamesCommand {
	def register(dispatcher: CommandDispatcher[ServerCommandSource]): Unit = {
		dispatcher.register(CommandManager.literal("blockmodname")
			.`then`(CommandManager.literal("save")
				.executes((_: CommandContext[ServerCommandSource]) => {
					BlockModNames.save()
					1
				})
			)
			.`then`(CommandManager.literal("load")
				.executes((_: CommandContext[ServerCommandSource]) => {
					BlockModNames.load()
					1
				})
			)
		)
	}
}
