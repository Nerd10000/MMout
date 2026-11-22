package net.minecraft.client.gui.screens.worldselection;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.gamerules.GameRule;

@FunctionalInterface
interface EntryFactory<T> {
  AbstractGameRulesScreen.RuleEntry create(Component paramComponent, List<FormattedCharSequence> paramList, String paramString, GameRule<T> paramGameRule);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\AbstractGameRulesScreen$EntryFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */