package me.iblitzkriegi.vixio.registration;

import ch.njol.skript.registrations.Converters;
import me.iblitzkriegi.vixio.util.UpdatingMessage;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class TypeConverters {
    public static void register() {
        Converters.registerConverter(EmbedBuilder.class, MessageEmbed.class,
				e -> e.isEmpty() ? null : e.build()
        );
		Converters.registerConverter(MessageBuilder.class, UpdatingMessage.class,
				builder -> builder.isEmpty() ? null : UpdatingMessage.from(builder.build()));
        Converters.registerConverter(Member.class, User.class,
				Member::getUser);
		Converters.registerConverter(EmbedBuilder.class, UpdatingMessage.class,
				b -> b.isEmpty() ? null : UpdatingMessage.from(new MessageBuilder().setEmbed(b.build()).build()));
        Converters.registerConverter(Bot.class, User.class,
				Bot::getSelfUser);
    }
}
