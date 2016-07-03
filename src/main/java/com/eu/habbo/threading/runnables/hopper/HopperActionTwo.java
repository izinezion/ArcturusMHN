package com.eu.habbo.threading.runnables.hopper;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.outgoing.rooms.items.FloorItemUpdateComposer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class HopperActionTwo implements Runnable
{
    private final HabboItem teleportOne;
    private final Room room;
    private final GameClient client;

    public HopperActionTwo(HabboItem teleportOne, Room room, GameClient client)
    {
        this.teleportOne = teleportOne;
        this.room = room;
        this.client = client;
    }

    @Override
    public void run()
    {
        this.teleportOne.setExtradata("2");

        int targetRoomId = 0;
        int targetItemId = 0;

        try
        {
            //PreparedStatement statement = Emulator.getDatabase().prepare("SELECT items.room_id, items_hoppers.* FROM items_hoppers INNER JOIN items ON items_hoppers.item_id = items.id WHERE base_item = ? AND items_hoppers.item_id != ? AND items.room_id > 0 ORDER BY RAND() LIMIT 1");
            PreparedStatement statement = Emulator.getDatabase().prepare("SELECT items.room_id, items.id FROM items INNER JOIN items_base ON items.item_id = items_base.id WHERE items_base.id = ? AND items.id != ? AND items.room_id > 0 ORDER BY RAND() LIMIT 1");
            statement.setInt(1, this.teleportOne.getBaseItem().getId());
            statement.setInt(2, this.teleportOne.getId());

            ResultSet set = statement.executeQuery();

            if(set.next())
            {
                targetItemId = set.getInt("id");
                targetRoomId = set.getInt("room_id");
            }

            set.close();
            statement.close();
            statement.getConnection().close();
        }
        catch (SQLException e)
        {
            Emulator.getLogging().logSQLException(e);
        }

        if(targetRoomId != 0 && targetItemId != 0)
        {
            Emulator.getThreading().run(new HopperActionThree(this.teleportOne, this.room, this.client, targetRoomId, targetItemId), 500);
        }
        else
        {
            this.teleportOne.setExtradata("0");
            this.client.getHabbo().getRoomUnit().setCanWalk(true);
            this.client.getHabbo().getRoomUnit().isTeleporting = false;
            Emulator.getThreading().run(new HopperActionFour(this.teleportOne, this.room, this.client), 500);
        }

        this.room.updateItem(this.teleportOne);
    }
}
