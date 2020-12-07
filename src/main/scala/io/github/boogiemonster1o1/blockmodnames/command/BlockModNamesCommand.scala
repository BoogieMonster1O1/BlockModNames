package io.github.boogiemonster1o1.blockmodnames.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import io.github.boogiemonster1o1.blockmodnames.BlockModNames
import net.minecraft.server.command.{CommandManager, ServerCommandSource}
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting

object BlockModNamesCommand {
	def register(dispatcher: CommandDispatcher[ServerCommandSource]): Unit = {
		dispatcher.register(CommandManager.literal("blockmodnames")
			.`then`(CommandManager.literal("save")
				.executes((ctx: CommandContext[ServerCommandSource]) => {
					BlockModNames.save()
					ctx.getSource.sendFeedback(new LiteralText("Saved BlockModNames config").formatted(Formatting.YELLOW), true)
					1
				})
			)
			.`then`(CommandManager.literal("reload")
				.executes((ctx: CommandContext[ServerCommandSource]) => {
					BlockModNames.load()
					ctx.getSource.sendFeedback(new LiteralText("Reloaded BlockModNames config").formatted(Formatting.YELLOW), true)
					1
				})
			)
		)
	}
}
