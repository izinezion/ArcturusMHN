package com.eu.habbo.messages.outgoing.unknown;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.catalog.CatalogItem;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

import java.util.Calendar;

public class ClubDataComposer extends MessageComposer
{
    private final int windowId;
    private final Habbo habbo;

    public ClubDataComposer(Habbo habbo, int windowId)
    {
        this.habbo = habbo;
        this.windowId = windowId;
    }

    @Override
    public ServerMessage compose()
    {
        this.response.init(Outgoing.ClubDataComposer);

        this.response.appendInt(Emulator.getGameEnvironment().getCatalogManager().clubItems.size());

        //TODO Change this to a seperate table.
        for(CatalogItem item : Emulator.getGameEnvironment().getCatalogManager().clubItems)
        {
            this.response.appendInt(item.getId());
            this.response.appendString(item.getName());
            this.response.appendBoolean(false); //unused
            this.response.appendInt(item.getCredits());
            this.response.appendInt(item.getPoints());
            this.response.appendInt(item.getPointsType());
            this.response.appendBoolean(item.getName().contains("_VIP_"));

            String[] data = item.getName().replace("_VIP_", "_").toLowerCase().split("_");

            long seconds = 0;

            if(data[3].toLowerCase().startsWith("day"))
            {
                seconds = 86400 * Integer.valueOf(data[2]);
            }
            else if(data[3].toLowerCase().startsWith("month"))
            {
                seconds = 86400 * 31 * Integer.valueOf(data[2]);
            }
            else if(data[3].toLowerCase().startsWith("year"))
            {
                seconds = 86400 * 31 * 12 * Integer.valueOf(data[2]);
            }

            long secondsTotal = seconds;

            int totalYears = (int)Math.floor((int)seconds / 86400 * 31 * 12);
            seconds -= totalYears * 86400 * 31 * 12;

            int totalMonths = (int)Math.floor((int)seconds / 86400 * 31);
            seconds -= totalMonths * 86400 * 31;

            int totalDays = (int)Math.floor((int)seconds / 86400);
            seconds -= totalDays * 86400;

            this.response.appendInt((int) secondsTotal / 86400 / 31);
            this.response.appendInt((int) seconds);
            this.response.appendBoolean(false); //giftable
            this.response.appendInt((int) seconds);

            int endTimestamp = habbo.getHabboStats().getClubExpireTimestamp();

            if (endTimestamp < Emulator.getIntUnixTimestamp())
            {
                endTimestamp = Emulator.getIntUnixTimestamp();
            }

            endTimestamp += secondsTotal;

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(endTimestamp * 1000l);
            this.response.appendInt(cal.get(Calendar.YEAR));
            this.response.appendInt(cal.get(Calendar.MONTH) + 1);
            this.response.appendInt(cal.get(Calendar.DAY_OF_MONTH));
        }

        this.response.appendInt(this.windowId);
        return this.response;
    }
}
