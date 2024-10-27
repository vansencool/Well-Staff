package dev.vansen.wellstaff.message;

import dev.vansen.configutils.Configer;
import dev.vansen.libs.fastapi.text.Replacer;
import dev.vansen.utility.debugging.Debug;
import dev.vansen.utility.source.SoundSource;
import dev.vansen.welldevelopment.Holder;
import dev.vansen.wellstaff.api.events.message.ActionBarSendEvent;
import dev.vansen.wellstaff.api.events.message.MessageSendEvent;
import dev.vansen.wellstaff.api.events.message.SoundSendEvent;
import dev.vansen.wellstaff.api.events.message.TitleSendEvent;
import dev.vansen.wellstaff.message.title.TitleSender;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Messager {
    private static CommandSender sender;

    private Messager() {
    }

    public static Messager sender() {
        return new Messager();
    }

    public Messager who(@NotNull CommandSender sender) {
        Messager.sender = sender;
        return this;
    }

    public void send(@NotNull String key, String... placeholders) {
        Debug.debug(Component.text(
                "Sending message: " + key
        ));
        Configer.loadAsync("messages.yml")
                .thenAcceptAsync(config -> {
                    if (!config.contains("Staff.messages." + key)) {
                        Debug.debug(Component.text("Message selection not found: " + key));
                        return;
                    }
                    String originalMessage = config.getString("Staff.messages." + key + ".msg");
                    if (originalMessage == null) {
                        Debug.debug(Component.text("Message not found: " + key));
                        return;
                    }
                    for (int i = 0; i < placeholders.length; i += 2) {
                        originalMessage = Replacer.builder()
                                .target(placeholders[i])
                                .replacement(placeholders[i + 1])
                                .all()
                                .build()
                                .replace(originalMessage);
                    }
                    Component message = MiniMessage.miniMessage().deserialize(originalMessage);
                    if (config.getBoolean("Staff.messages." + key + ".send", true)) {
                        List<String> types = config.getStringList("Staff.messages." + key + ".types");
                        for (String type : types) {
                            switch (type.toLowerCase()) {
                                case "action_bar", "action bar", "actionbar" -> {
                                    if (!(sender instanceof Entity)) return;
                                    Debug.debug(Component.text(
                                            "Sending action bar message: " + key + " - " + message
                                    ));
                                    ActionBarSendEvent send = new ActionBarSendEvent(message);
                                    Holder.getEventer().post(send);
                                    Debug.debug(Component.text(
                                            "Action bar event cancelled? - " + send.cancel()
                                    ));
                                    if (!send.cancel()) {
                                        sender.sendActionBar(send.message());
                                        Debug.debug(Component.text("Sent action bar message: " + key + " - " + message));
                                    }
                                }
                                case "title" -> {
                                    if (!(sender instanceof Entity)) return;
                                    Component title = MiniMessage.miniMessage().deserialize(config.getString("Staff.messages." + key + ".title.main"));
                                    Component subtitle = MiniMessage.miniMessage().deserialize(config.getString("Staff.messages." + key + ".title.subtitle"));
                                    long fadeIn = config.getInt("Staff.messages." + key + ".title.time.fade_in");
                                    long stay = config.getInt("Staff.messages." + key + ".title.time.stay");
                                    long fadeOut = config.getInt("Staff.messages." + key + ".title.time.fade_out");
                                    Debug.debug(Component.text(
                                            "Sending title message: " + key + " - " + title + " - " + subtitle + " - " + fadeIn + " - " + stay + " - " + fadeOut
                                    ));
                                    TitleSendEvent send = new TitleSendEvent(title, subtitle, fadeIn, stay, fadeOut);
                                    Holder.getEventer().post(send);
                                    Debug.debug(Component.text(
                                            "Title event cancelled? - " + send.cancel()
                                    ));
                                    if (!send.cancel()) {
                                        TitleSender.builder()
                                                .who((Entity) sender)
                                                .title(send.title())
                                                .subtitle(send.subtitle())
                                                .fadeIn(send.fadeIn())
                                                .stay(send.stay())
                                                .fadeOut(send.fadeOut())
                                                .send();
                                        Debug.debug(Component.text(
                                                "Sent title message: " + key + " - " + title + " - " + subtitle + " - " + fadeIn + " - " + stay + " - " + fadeOut
                                        ));
                                    }
                                }
                                case "message", "msg" -> {
                                    Debug.debug(Component.text(
                                            "Sending message: " + key + " - " + message
                                    ));
                                    MessageSendEvent event = new MessageSendEvent(message);
                                    Holder.getEventer().post(event);
                                    Debug.debug(Component.text(
                                            "Message event cancelled? - " + event.cancel()
                                    ));
                                    if (!event.cancel()) {
                                        sender.sendMessage(event.message());
                                        Debug.debug(Component.text(
                                                "Sent message: " + key + " - " + message
                                        ));
                                    }
                                }
                            }
                        }
                        if (config.getBoolean("Staff.messages." + key + ".sounds.send", false)) {
                            try {
                                Sound.sound(builder -> {
                                    if (!(sender instanceof Entity)) return;
                                    Sound.Builder sound = builder.type(org.bukkit.Sound.valueOf(config.getString("Staff.messages." + key + ".sounds.sound").toUpperCase()))
                                            .volume((float) config.getDouble("Staff.messages." + key + ".sounds.volume", 1.0))
                                            .pitch((float) config.getDouble("Staff.messages." + key + ".sounds.pitch", 1.0))
                                            .source(SoundSource.fromString(config.getString("Staff.messages." + key + ".sounds.source", "player")));
                                    Debug.debug(Component.text(
                                            "Sending sound: " + key
                                    ));
                                    SoundSendEvent send = new SoundSendEvent(sound);
                                    Holder.getEventer().post(send);
                                    Debug.debug(Component.text(
                                            "Sound event cancelled? - " + send.cancel()
                                    ));
                                    if (!send.cancel()) {
                                        sender.playSound(send.sound().build());
                                        Debug.debug(Component.text(
                                                "Sent sound: " + key
                                        ));
                                    }
                                });
                            } catch (@NotNull IllegalArgumentException e) {
                                Holder.get()
                                        .getComponentLogger()
                                        .error(Component.text("Invalid sound: " + config.getString("Staff.messages." + key + ".sounds.sound") + " (message: " + key + ")"));
                            }
                        }
                    }
                });
    }
}