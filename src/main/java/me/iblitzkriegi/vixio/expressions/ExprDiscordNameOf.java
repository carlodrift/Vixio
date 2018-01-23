package me.iblitzkriegi.vixio.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import me.iblitzkriegi.vixio.util.wrapper.ChannelBuilder;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.bukkit.event.Event;

/**
 * Created by Blitz on 7/25/2017.
 */
public class ExprDiscordNameOf extends SimplePropertyExpression<Object, String>{
    static {
        Vixio.getInstance().registerPropertyExpression(ExprDiscordNameOf.class, String.class, "name", "channel/guild/user/member/bot/role/audiotrack/category/channelbuilder")
                .setName("Name of")
                .setDesc("Get the name of something/someone. There is a set changer for channel, guild, and bot.")
                .setExample("name of event-user");
    }
    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        super.init(exprs, matchedPattern, isDelayed, parseResult);
        setExpr(exprs[0]);
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "name of";
    }

    @Override
    public String convert(Object o) {
        if (o instanceof User) {
            return ((User) o).getName();
        } else if (o instanceof Guild) {
            return ((Guild) o).getName();
        } else if (o instanceof Channel) {
            return ((Channel) o).getName();
        } else if (o instanceof Member) {
            return ((Member) o).getUser().getName();
        } else if (o instanceof Bot) {
            return ((Bot) o).getName();
        } else if (o instanceof Role) {
            return ((Role) o).getName();
        } else if (o instanceof ChannelBuilder) {
            return ((ChannelBuilder) o).getName();
        } else if (o instanceof Category) {
            return ((Category) o).getName();
        } else if (o instanceof AudioTrack) {
            return ((AudioTrack) o).getInfo().title;
        }
        return null;
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if ((mode == Changer.ChangeMode.SET && getExpr().isSingle())) {
            return new Class[]{String.class};
        }
        return super.acceptChange(mode);
    }

    @Override
    public void change(final Event e, final Object[] delta, final Changer.ChangeMode mode) {
        Object object = getExpr().getSingle(e);
        if (object instanceof Member || object instanceof User) return;
        String name = (String) delta[0];
        switch (mode) {
            case SET:
                if (object instanceof Bot) {
                    Bot bot = (Bot) object;
                    bot.getSelfUser().getManager().setName(name).queue();
                } else if (object instanceof Channel) {
                    Channel channel = (Channel) object;
                    try {
                        channel.getManager().setName(name).queue();
                    } catch (PermissionException x) {

                    }
                } else if (object instanceof Guild) {
                    Guild guild = (Guild) object;
                    try {
                        guild.getManager().setName(name).queue();
                    } catch (PermissionException x) {

                    }
                } else if (object instanceof ChannelBuilder) {
                    ((ChannelBuilder) object).setName(name);
                } else if (object instanceof Category) {
                    ((Category) object).getManager().setName(name).queue();
                }
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}

